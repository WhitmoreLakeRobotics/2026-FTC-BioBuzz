package org.firstinspires.ftc.teamcode.Comp.Hardware;


import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.Common.CommonLogic;

import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.*;

/**
 * Holds all of Team 8492's hardware and passes each OpMode step
 * (init, init_loop, start, loop, stop) down to every part.
 * <p>
 * CHANGED: removed the duplicate "telemetry" and "hardwareMap" fields.
 * Robot extends BaseHardware, which already has both, so declaring them
 * again here created a second, confusing copy. Telop's
 * "robot.hardwareMap = hardwareMap;" still works exactly the same.
 */
public class Robot extends BaseHardware {

    public DriveTrain driveTrain = new DriveTrain();
    public Intake intake = new Intake();
    public Launcher launcher = new Launcher();
    // CHANGED: the Comp bot has no TransitionR servo.
    //public TransitionR transitionR = new TransitionR();
    public PollenBlocker pollenBlocker = new PollenBlocker();
    public Feeder feeder = new Feeder();
    // CHANGED: runs the launch sequence. Declared AFTER the parts it uses.
    public TripleThreat tripleThreat = new TripleThreat(launcher, feeder, pollenBlocker);


    /**
     * Called once when the INIT button is pressed.
     */
    @Override
    public void init() {
//checking to make sure running the right code for this robot markerDevice is a device that is unique in each robot
        // CHANGED: "TransS" is the Scrimmage servo and doesn't exist on the Comp bot.
        // TODO: put a device name that only the Comp bot has here.
        //CommonLogic.WhichRobotCheck(this.hardwareMap,"TransS","Competition");
        // OPTIONAL: once CommonLogic has the NotRobotCheck method, this fails if the Scrimmage
        // servo is present (wrong robot). Uncomment it then.
        //CommonLogic.NotRobotCheck(this.hardwareMap, "TransS", "Competition");
        // drivetrain
        driveTrain.hardwareMap = this.hardwareMap;
        driveTrain.telemetry = this.telemetry;
        driveTrain.init();

        // Intake
        intake.hardwareMap = this.hardwareMap;
        intake.telemetry = this.telemetry;
        intake.init();

        //Launcher
        launcher.hardwareMap = this.hardwareMap;
        launcher.telemetry = this.telemetry;
        launcher.init();

        //Transition Roller
        //transitionR.hardwareMap = this.hardwareMap;
        //transitionR.telemetry = this.telemetry;
        //transitionR.init();

        //launcher blocker
        pollenBlocker.hardwareMap = this.hardwareMap;
        pollenBlocker.telemetry = this.telemetry;
        pollenBlocker.init();

        //Feeder
        feeder.hardwareMap = this.hardwareMap;
        feeder.telemetry = this.telemetry;
        feeder.init();

        //TripleThreat (launch sequence)
        tripleThreat.hardwareMap = this.hardwareMap;
        tripleThreat.telemetry = this.telemetry;
        tripleThreat.init();
    }

    @Override
    public void init_loop() {
        driveTrain.init_loop();
        intake.init_loop();
        launcher.init_loop();
        //transitionR.init_loop();
        pollenBlocker.init_loop();
        feeder.init_loop();
        tripleThreat.init_loop();
    }

    @Override
    public void start() {
        driveTrain.start();
        intake.start();
        launcher.start();
        //transitionR.start();
        pollenBlocker.start();
        feeder.start();
        tripleThreat.start();
    }

    @Override
    public void loop() {
        driveTrain.loop();
        intake.loop();
        // CHANGED: TripleThreat runs before the parts it commands, so its commands take effect this loop.
        tripleThreat.loop();
        launcher.loop();
        //transitionR.loop();
        pollenBlocker.loop();
        feeder.loop();
    }

    /**
     * Called once when STOP is pressed.
     * CHANGED: added "public" (was just "void stop()").
     * CHANGED: mechanisms stop first and driveTrain last (it saves the pose), so a failure in
     * driveTrain.stop() can't leave the launcher or feeder running.
     */
    @Override
    public void stop() {
        tripleThreat.stop();
        launcher.stop();
        feeder.stop();
        intake.stop();
        //transitionR.stop();
        pollenBlocker.stop();
        driveTrain.stop();
    }

    // ---------------- IVY COMMANDS (for Pedro auto paths) ----------------

    /**
     * Start the launch sequence and finish once TripleThreat is FIRING (or the shot aborted).
     * It keeps firing until cmdCeaseFireSequence() runs.
     * Auto must call robot.loop() every cycle, since that is what advances the states.
     */
    public Command cmdShootSequence() {
        return sequential(
                instant(() -> tripleThreat.cmdShoot()),
                waitUntil(() -> tripleThreat.isFiring() || tripleThreat.isIdle())
        ).requiring(this);
    }

    /** End the volley: stop the feeder and close the blocker (the launcher keeps spinning). */
    public Command cmdCeaseFireSequence() {
        return instant(() -> tripleThreat.cmdCeaseFire()).requiring(this);
    }
}