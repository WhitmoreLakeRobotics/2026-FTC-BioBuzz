package org.firstinspires.ftc.teamcode.Comp.Hardware;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Feeder motor (TransLM2): pushes balls from the roller into the launcher flywheel.
 * Closed-loop RPM control (feedforward + PID). A cmd sets the target and the transition
 * state; loop() decides when we arrive. Direction changes wait for a full stop first.
 */

@Configurable
public class Feeder extends BaseHardware {

    private DcMotorEx feederMotor;
    private VoltageSensor battery;

    // ---------------- TUNABLE CONSTANTS ----------------
    public static double FEEDER_FORWARD_RPM = 4000;
    public static double FEEDER_BACKWARD_RPM = -500;   // default until a real value is chosen
    public static double RPM_TOLERANCE = 50;           // "at speed" window around the target RPM
    public static double AT_SPEED_HOLD_SEC = 0.15;     // RPM must stay in tolerance this long
    public static double OFF_RPM_THRESHOLD = 50;       // |RPM| below this counts as stopped

    // ---------------- PID CONSTANTS ----------------
    public static double FEEDER_KP = 0.00070;
    public static double FEEDER_KI = 0.0;              // still unused
    public static double FEEDER_KD = 0.0000015;
    public static double FEEDER_KF = 1.24 / 6000.0;    // feedforward per RPM

    private static final double TICKS_PER_REV = 28;    // motor-shaft encoder; change if geared
    private static final double MIN_POWER = -1.0;
    private static final double MAX_POWER = 1.0;
    private static final double NOMINAL_VOLTAGE = 12.0;
    private static final String MOTOR_NAME = "TransLM2";   // must match the robot configuration

    // ---------------- STATE ----------------
    private FeederState feederState = FeederState.OFF;
    private double targetRPM = 0;
    private double pendingRPM = 0;      // direction change waiting for the motor to stop (0 = none)
    private double lastDirection = 0;   // sign of the last non-zero target we commanded
    private final ElapsedTime atSpeedTimer = new ElapsedTime();

    // ---------------- PID WORKING VALUES ----------------
    private final ElapsedTime pidTimer = new ElapsedTime();
    private double integral = 0;
    private double lastError = 0;

    // ---------------- LIFECYCLE ----------------
    @Override
    public void init() {
        feederMotor = hardwareMap.get(DcMotorEx.class, MOTOR_NAME);
        battery = hardwareMap.voltageSensor.iterator().next();

        feederMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);   // our own PID; velocity still reads
        feederMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

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
        double rpm = getFeederRPM();   // read the encoder once per loop

        runPID(rpm);
        updateFeederState(rpm);

        telemetry.addData("Feeder State", feederState);
        telemetry.addData("Feeder RPM", rpm);
        telemetry.addData("Feeder Target", targetRPM);
    }

    @Override
    public void stop() {
        resetState();

        if (feederMotor != null) {
            feederMotor.setPower(0);
        }
    }

    private void resetState() {
        targetRPM = 0;
        pendingRPM = 0;
        lastDirection = 0;
        integral = 0;
        lastError = 0;
        feederState = FeederState.OFF;
    }

    // ---------------- COMMANDS ----------------
    // A cmd sets the target and the transition state; loop() decides when we arrive.

    public void cmdFeederForward() {
        requestRPM(FEEDER_FORWARD_RPM);
    }

    public void cmdFeederBackward() {
        requestRPM(FEEDER_BACKWARD_RPM);
    }

    public void cmdFeederStop() {
        targetRPM = 0;
        pendingRPM = 0;

        if (feederState != FeederState.OFF) {
            feederState = FeederState.SPINNING_DOWN;
        }
    }

    /**
     * Same direction: retarget directly. Opposite direction: drop the target to 0, remember
     * the request, and let loop() spin up the other way only after the motor has stopped,
     * so we never reverse a spinning motor.
     */
    private void requestRPM(double rpm) {

        switch (feederState) {
            case OFF:
                setTarget(rpm);
                atSpeedTimer.reset();
                feederState = FeederState.SPINNING_UP;
                break;

            case SPINNING_UP:
            case AT_SPEED_FORWARD:
            case AT_SPEED_BACKWARD:
                if (Math.signum(rpm) == Math.signum(targetRPM)) {
                    setTarget(rpm);          // same direction, keep state (no debounce reset)
                } else {
                    targetRPM = 0;
                    pendingRPM = rpm;
                    feederState = FeederState.SPINNING_DOWN;
                }
                break;

            case SPINNING_DOWN:
                if (Math.signum(rpm) == lastDirection) {
                    // still coasting the same way, so just re-power it
                    pendingRPM = 0;
                    setTarget(rpm);
                    atSpeedTimer.reset();
                    feederState = FeederState.SPINNING_UP;
                } else {
                    pendingRPM = rpm;        // wait for the stop, then loop() spins up
                }
                break;
        }
    }

    private void setTarget(double rpm) {
        targetRPM = rpm;
        if (rpm != 0) {
            lastDirection = Math.signum(rpm);
        }
    }

    // ---------------- STATE MACHINE ----------------

    private void updateFeederState(double rpm) {

        boolean inRange = Math.abs(rpm - targetRPM) <= RPM_TOLERANCE;

        switch (feederState) {
            case SPINNING_UP:
                if (!inRange) {
                    atSpeedTimer.reset();
                } else if (atSpeedTimer.seconds() >= AT_SPEED_HOLD_SEC) {
                    feederState = (targetRPM >= 0)
                            ? FeederState.AT_SPEED_FORWARD
                            : FeederState.AT_SPEED_BACKWARD;
                }
                break;

            case AT_SPEED_FORWARD:
            case AT_SPEED_BACKWARD:
                if (!inRange) {   // e.g. a ball loaded it down
                    atSpeedTimer.reset();
                    feederState = FeederState.SPINNING_UP;
                }
                break;

            case SPINNING_DOWN:
                if (Math.abs(rpm) < OFF_RPM_THRESHOLD) {
                    if (pendingRPM != 0) {
                        // stopped, now safe to spin up (possibly the other direction)
                        setTarget(pendingRPM);
                        pendingRPM = 0;
                        atSpeedTimer.reset();
                        feederState = FeederState.SPINNING_UP;
                    } else {
                        feederState = FeederState.OFF;
                    }
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

        feederMotor.setPower(Math.max(MIN_POWER, Math.min(MAX_POWER, power)));

        telemetry.addData("Feeder Power", power);
    }

    private double calculatePower(double currentRPM) {
        double error = targetRPM - currentRPM;
        double dt = pidTimer.seconds();
        pidTimer.reset();

        if (dt <= 0) dt = 0.001;

        double p = FEEDER_KP * error;

        integral += error * dt;
        integral = Math.max(-5000, Math.min(5000, integral));
        double i = FEEDER_KI * integral;

        double derivative = (error - lastError) / dt;
        lastError = error;
        double d = FEEDER_KD * derivative;

        double voltage = Math.max(battery.getVoltage(), 1.0);
        double ff = (targetRPM * FEEDER_KF) * (NOMINAL_VOLTAGE / voltage);

        return ff + p + i + d;
    }

    // ---------------- STATUS GETTERS ----------------

    public FeederState getFeederState() {
        return feederState;
    }

    public boolean isFeederAtSpeed() {
        return feederState == FeederState.AT_SPEED_FORWARD
                || feederState == FeederState.AT_SPEED_BACKWARD;
    }

    public boolean isFeederAtSpeedForward() {
        return feederState == FeederState.AT_SPEED_FORWARD;
    }

    public boolean isFeederOff() {
        return feederState == FeederState.OFF;
    }

    public double getFeederRPM() {
        return (feederMotor.getVelocity() / TICKS_PER_REV) * 60.0;
    }

    public double getFeederTargetRPM() {
        return targetRPM;
    }

    // ---------------- ENUMS (keep at the end of the class) ----------------

    /**
     * Feeder motor (TransLM2) status. A cmd sets the transition state
     * (SPINNING_UP / SPINNING_DOWN); loop() moves to the final state once the RPM arrives.
     * The at-speed states also say which direction the motor is running.
     */
    public enum FeederState {
        OFF,
        SPINNING_UP,
        AT_SPEED_FORWARD,
        AT_SPEED_BACKWARD,
        SPINNING_DOWN
    }
}