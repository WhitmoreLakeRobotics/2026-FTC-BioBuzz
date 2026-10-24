package org.firstinspires.ftc.teamcode.Comp.Hardware;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Launcher flywheel motor (LM1).
 * Closed-loop RPM control (feedforward + PID). A cmd sets the target and the transition
 * state; loop() decides when we arrive.
 */
@Configurable
public class Launcher extends BaseHardware {

    private DcMotorEx launcherMotor;
    private VoltageSensor battery;

    // ---------------- TUNABLE CONSTANTS ----------------
    public static double LAUNCHER_RPM = 1700;
    public static int RPM_OFFSET = 0;                  // added to LAUNCHER_RPM (trim from Panels)
    public static double RPM_TOLERANCE = 50;           // "at speed" window around the target RPM
    public static double AT_SPEED_HOLD_SEC = 0.15;     // RPM must stay in tolerance this long
    public static double OFF_RPM_THRESHOLD = 50;       // |RPM| below this counts as stopped

    // ---------------- PID CONSTANTS ----------------
    public static double LAUNCHER_KP = 0.0002;
    public static double LAUNCHER_KI = 0.0;            // still unused
    public static double LAUNCHER_KD = 0.0;
    public static double LAUNCHER_KF = 1 / 6000.0;     // feedforward per RPM

    private static final double TICKS_PER_REV = 28;    // motor-shaft encoder; change if geared
    private static final double MIN_POWER = -1.0;
    private static final double MAX_POWER = 1.0;
    private static final double NOMINAL_VOLTAGE = 12.0;
    private static final String MOTOR_NAME = "LM1";    // must match the robot configuration

    // ---------------- STATE ----------------
    private LauncherState launcherState = LauncherState.OFF;
    private double targetRPM = 0;
    private final ElapsedTime atSpeedTimer = new ElapsedTime();

    // ---------------- PID WORKING VALUES ----------------
    private final ElapsedTime pidTimer = new ElapsedTime();
    private double integral = 0;
    private double lastError = 0;

    // ---------------- LIFECYCLE ----------------
    @Override
    public void init() {
        launcherMotor = hardwareMap.get(DcMotorEx.class, MOTOR_NAME);
        battery = hardwareMap.voltageSensor.iterator().next();

        launcherMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);   // our own PID; velocity still reads
        launcherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        resetState();
        pidTimer.reset();
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        pidTimer.reset();   // so the first dt isn't the whole time since init
    }

    @Override
    public void loop() {
        double rpm = getLauncherRPM();   // read the encoder once per loop

        runPID(rpm);
        updateLauncherState(rpm);

        telemetry.addData("Launcher State", launcherState);
        telemetry.addData("Launcher RPM", rpm);
        telemetry.addData("Launcher Target", targetRPM);
    }

    @Override
    public void stop() {
        resetState();

        if (launcherMotor != null) {
            launcherMotor.setPower(0);
        }
    }

    private void resetState() {
        targetRPM = 0;
        integral = 0;
        lastError = 0;
        launcherState = LauncherState.OFF;
    }

    // ---------------- COMMANDS ----------------
    // A cmd sets the target and the transition state; loop() decides when we arrive.

    public void cmdLauncherForward() {

        // Only one direction, so retargeting while spinning (or spinning down) is safe.
        targetRPM = LAUNCHER_RPM + RPM_OFFSET;

        if (launcherState == LauncherState.OFF || launcherState == LauncherState.SPINNING_DOWN) {
            atSpeedTimer.reset();
            launcherState = LauncherState.SPINNING_UP;
        }
        // SPINNING_UP / AT_SPEED: keep the state so a held button doesn't reset the debounce.
    }

    public void cmdLauncherStop() {

        targetRPM = 0;

        if (launcherState != LauncherState.OFF) {
            launcherState = LauncherState.SPINNING_DOWN;
        }
    }

    // ---------------- STATE MACHINE ----------------

    private void updateLauncherState(double rpm) {

        boolean inRange = Math.abs(rpm - targetRPM) <= RPM_TOLERANCE;

        switch (launcherState) {
            case SPINNING_UP:
                if (!inRange) {
                    atSpeedTimer.reset();
                } else if (atSpeedTimer.seconds() >= AT_SPEED_HOLD_SEC) {
                    launcherState = LauncherState.AT_SPEED;
                }
                break;

            case AT_SPEED:
                if (!inRange) {   // e.g. RPM dropped after a shot
                    atSpeedTimer.reset();
                    launcherState = LauncherState.SPINNING_UP;
                }
                break;

            case SPINNING_DOWN:
                if (Math.abs(rpm) < OFF_RPM_THRESHOLD) {
                    launcherState = LauncherState.OFF;
                }
                break;

            case OFF:
                break;
        }
    }

    // ---------------- PID ----------------

    private void runPID(double currentRPM) {

        double power;

        if (targetRPM == 0) {
            // no braking when stopping; reset so the PID doesn't spike later
            power = 0;
            integral = 0;
            lastError = 0;
            pidTimer.reset();
        } else {
            power = calculatePower(currentRPM);
        }

        launcherMotor.setPower(Math.max(MIN_POWER, Math.min(MAX_POWER, power)));

        telemetry.addData("Launcher Power", power);
    }

    private double calculatePower(double currentRPM) {
        double error = targetRPM - currentRPM;
        double dt = pidTimer.seconds();
        pidTimer.reset();

        if (dt <= 0) dt = 0.001;

        double p = LAUNCHER_KP * error;

        integral += error * dt;
        integral = Math.max(-5000, Math.min(5000, integral));
        double i = LAUNCHER_KI * integral;

        double derivative = (error - lastError) / dt;
        lastError = error;
        double d = LAUNCHER_KD * derivative;

        double voltage = Math.max(battery.getVoltage(), 1.0);
        double ff = (targetRPM * LAUNCHER_KF) * (NOMINAL_VOLTAGE / voltage);

        return ff + p + i + d;
    }

    // ---------------- STATUS GETTERS ----------------

    public LauncherState getLauncherState() {
        return launcherState;
    }

    public boolean isLauncherAtSpeed() {
        return launcherState == LauncherState.AT_SPEED;
    }

    public boolean isLauncherOff() {
        return launcherState == LauncherState.OFF;
    }

    public double getLauncherRPM() {
        return (launcherMotor.getVelocity() / TICKS_PER_REV) * 60.0;
    }

    public double getLauncherTargetRPM() {
        return targetRPM;
    }

    public double getBatteryVoltage() {
        return battery.getVoltage();
    }

    // ---------------- ENUMS (keep at the end of the class) ----------------

    /**
     * Launcher motor (LM1) status.
     * A cmd sets the transition state (SPINNING_UP / SPINNING_DOWN);
     * loop() moves to the final state (AT_SPEED / OFF) once the RPM arrives.
     */
    public enum LauncherState {
        OFF,
        SPINNING_UP,
        AT_SPEED,
        SPINNING_DOWN
    }
}