package org.firstinspires.ftc.teamcode.Comp.Hardware;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Coordinates the launch sequence: launcher -> at speed -> feeder -> unblock.
 * It owns no hardware; it commands Launcher, Feeder and PollenBlocker, which
 * still run their own state machines.
 * <p>
 * cmdShoot() starts the sequence, cmdCeaseFire() ends the volley (launcher keeps
 * spinning for the next one), cmdTripleThreatStop() shuts everything off.
 * Straight-through volley: once firing, RPM dips between balls do NOT close the blocker.
 */
@Configurable
public class TripleThreat extends BaseHardware {

    private final Launcher launcher;
    private final Feeder feeder;
    private final PollenBlocker blocker;

    // ---------------- TUNABLE CONSTANTS ----------------
    public static double SPINUP_TIMEOUT_SEC = 3.0;     // give up waiting for launcher speed
    public static double FEEDER_TIMEOUT_SEC = 1.5;     // give up waiting for feeder speed
    public static double BLOCKER_OPEN_SEC = 0.30;      // servo travel time before we call it FIRING (tune!)
    public static boolean WAIT_FOR_FEEDER_SPEED = true; // false = open the blocker as soon as the feeder is commanded

    // ---------------- STATE ----------------
    private TripleThreatState tripleThreatState = TripleThreatState.IDLE;
    private final ElapsedTime stateTimer = new ElapsedTime();
    private boolean lastShotAborted = false;

    public TripleThreat(Launcher launcher, Feeder feeder, PollenBlocker blocker) {
        this.launcher = launcher;
        this.feeder = feeder;
        this.blocker = blocker;
    }

    // ---------------- LIFECYCLE ----------------
    @Override
    public void init() {
        tripleThreatState = TripleThreatState.IDLE;
        lastShotAborted = false;
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
        updateTripleThreatState();

        telemetry.addData("TripleThreat State", tripleThreatState);
        telemetry.addData("TripleThreat Aborted", lastShotAborted);
    }

    @Override
    public void stop() {
        // the parts stop themselves; just reset our own state
        tripleThreatState = TripleThreatState.IDLE;
    }

    // ---------------- COMMANDS ----------------

    /**
     * Start the launch sequence. Does nothing if a shot is already in progress.
     */
    public void cmdShoot() {
        if (tripleThreatState != TripleThreatState.IDLE) {
            return;   // a held button won't restart the sequence
        }

        lastShotAborted = false;
        launcher.cmdLauncherForward();
        stateTimer.reset();
        tripleThreatState = TripleThreatState.SPINNING_UP;
    }

    /**
     * End the volley: stop the feeder and close the blocker. The launcher keeps spinning.
     */
    public void cmdCeaseFire() {
        if (tripleThreatState == TripleThreatState.IDLE) {
            return;
        }

        feeder.cmdFeederStop();
        blocker.cmdBlock();
        tripleThreatState = TripleThreatState.IDLE;
    }

    /**
     * Shut everything down: feeder, blocker closed, launcher stopped.
     */
    public void cmdTripleThreatStop() {
        feeder.cmdFeederStop();
        blocker.cmdBlock();
        launcher.cmdLauncherStop();
        tripleThreatState = TripleThreatState.IDLE;
    }

    // ---------------- STATE MACHINE ----------------

    private void updateTripleThreatState() {

        switch (tripleThreatState) {
            case SPINNING_UP:
                if (launcher.isLauncherAtSpeed()) {
                    feeder.cmdFeederForward();
                    stateTimer.reset();
                    tripleThreatState = TripleThreatState.STARTING_FEEDER;
                } else if (stateTimer.seconds() > SPINUP_TIMEOUT_SEC) {
                    abort();
                }
                break;

            case STARTING_FEEDER:
                if (!WAIT_FOR_FEEDER_SPEED || feeder.isFeederAtSpeedForward()) {
                    blocker.cmdOpen();
                    stateTimer.reset();
                    tripleThreatState = TripleThreatState.OPENING;
                } else if (stateTimer.seconds() > FEEDER_TIMEOUT_SEC) {
                    abort();
                }
                break;

            case OPENING:
                // the blocker servo has no position feedback, so wait out its travel time
                if (stateTimer.seconds() >= BLOCKER_OPEN_SEC) {
                    tripleThreatState = TripleThreatState.FIRING;
                }
                break;

            case FIRING:
                // straight-through: nothing closes the blocker until cmdCeaseFire()
                break;

            case IDLE:
                break;
        }
    }

    /**
     * A step timed out: stop feeding and close the blocker, but leave the launcher spinning.
     */
    private void abort() {
        feeder.cmdFeederStop();
        blocker.cmdBlock();
        lastShotAborted = true;
        tripleThreatState = TripleThreatState.IDLE;
    }

    // ---------------- STATUS GETTERS ----------------

    public TripleThreatState getTripleThreatState() {
        return tripleThreatState;
    }

    public boolean isIdle() {
        return tripleThreatState == TripleThreatState.IDLE;
    }

    public boolean isFiring() {
        return tripleThreatState == TripleThreatState.FIRING;
    }

    public boolean didLastShotAbort() {
        return lastShotAborted;
    }

    // ---------------- ENUMS (keep at the end of the class) ----------------

    /**
     * IDLE: nothing in progress (the launcher may still be spinning).
     * SPINNING_UP: launcher on, waiting for speed.
     * STARTING_FEEDER: feeder on, waiting for feeder speed.
     * OPENING: blocker opening (servo travel time).
     * FIRING: everything running until cmdCeaseFire().
     */
    public enum TripleThreatState {
        IDLE,
        SPINNING_UP,
        STARTING_FEEDER,
        OPENING,
        FIRING
    }
}


/*Natalie use this command to call in auton

Command auto = sequential(
        robot.driveTrain.cmdAtFollow(shootPath),   // drive to the shooting spot
        robot.cmdShootSequence(),                  // spin up -> feeder -> unblock, ends once FIRING
        waitMs(5000),                              // keep firing; time how long your volley takes
        robot.cmdCeaseFireSequence(),              // feeder off, blocker closed (launcher keeps spinning)
        robot.driveTrain.cmdAtFollow(nextPath)
);


Add these to all auton using this command

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;

import static com.pedropathing.ivy.commands.Commands.*;   // instant, waitMs, waitUntil
import static com.pedropathing.ivy.groups.Groups.*;       // sequential, parallel
 */
