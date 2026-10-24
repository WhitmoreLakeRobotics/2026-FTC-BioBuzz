package org.firstinspires.ftc.teamcode.Comp.Hardware;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Configurable
public class Intake extends BaseHardware {

    private DcMotorEx NTKM;

    public static double INTAKE_POWER = -1.0;
    public static double REVERSE_POWER = 0.45;
    public static double AT_SPEED_INTAKE_RPM = 3000;
    public static double AT_SPEED_REVERSE_RPM = 800;
    public static double AT_SPEED_HOLD_SEC = 0.15;
    public static double OFF_RPM_THRESHOLD = 50;

    private static final double TICKS_PER_REV = 28;
    private static final double MIN_POWER = -1.0;
    private static final double MAX_POWER = 1.0;

    private IntakeState intakeState = IntakeState.OFF;
    private double targetPower = 0;
    private double pendingPower = 0;
    private double lastDirection = 0;
    private final ElapsedTime atSpeedTimer = new ElapsedTime();

    @Override
    public void init() {
        NTKM = hardwareMap.get(DcMotorEx.class, "NTKM");

        NTKM.setDirection(DcMotorSimple.Direction.REVERSE);
        NTKM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        NTKM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        targetPower = 0;
        pendingPower = 0;
        lastDirection = 0;
        intakeState = IntakeState.OFF;
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
        double rpm = getMotorRPM();

        updateIntakeState(rpm);

        double power = Math.max(MIN_POWER, Math.min(MAX_POWER, targetPower));
        NTKM.setPower(power);

        telemetry.addData("Intake State", intakeState);
        telemetry.addData("Intake RPM", rpm);
        telemetry.addData("Intake Power", power);
    }

    @Override
    public void stop() {
        targetPower = 0;
        pendingPower = 0;
        intakeState = IntakeState.OFF;

        if (NTKM != null) {
            NTKM.setPower(0);
        }
    }

    public void cmdIntakeForward() {
        requestPower(INTAKE_POWER);
    }

    public void cmdIntakeReverse() {
        requestPower(REVERSE_POWER);
    }

    public void cmdIntakeStop() {
        targetPower = 0;
        pendingPower = 0;

        if (intakeState != IntakeState.OFF) {
            intakeState = IntakeState.SPINNING_DOWN;
        }
    }

    private void requestPower(double power) {

        switch (intakeState) {
            case OFF:
                setTarget(power);
                atSpeedTimer.reset();
                intakeState = IntakeState.SPINNING_UP;
                break;

            case SPINNING_UP:
            case AT_SPEED_INTAKE:
            case AT_SPEED_REVERSE:
                if (Math.signum(power) == Math.signum(targetPower)) {
                    setTarget(power);
                } else {
                    targetPower = 0;
                    pendingPower = power;
                    intakeState = IntakeState.SPINNING_DOWN;
                }
                break;

            case SPINNING_DOWN:
                if (Math.signum(power) == lastDirection) {
                    pendingPower = 0;
                    setTarget(power);
                    atSpeedTimer.reset();
                    intakeState = IntakeState.SPINNING_UP;
                } else {
                    pendingPower = power;
                }
                break;
        }
    }

    private void setTarget(double power) {
        targetPower = power;
        if (power != 0) {
            lastDirection = Math.signum(power);
        }
    }

    private void updateIntakeState(double rpm) {

        double absRpm = Math.abs(rpm);

        switch (intakeState) {
            case SPINNING_UP:
                double needed = (targetPower < 0) ? AT_SPEED_INTAKE_RPM : AT_SPEED_REVERSE_RPM;
                if (absRpm < needed) {
                    atSpeedTimer.reset();
                } else if (atSpeedTimer.seconds() >= AT_SPEED_HOLD_SEC) {
                    intakeState = (targetPower < 0)
                            ? IntakeState.AT_SPEED_INTAKE
                            : IntakeState.AT_SPEED_REVERSE;
                }
                break;

            case AT_SPEED_INTAKE:
                if (absRpm < AT_SPEED_INTAKE_RPM) {
                    atSpeedTimer.reset();
                    intakeState = IntakeState.SPINNING_UP;
                }
                break;

            case AT_SPEED_REVERSE:
                if (absRpm < AT_SPEED_REVERSE_RPM) {
                    atSpeedTimer.reset();
                    intakeState = IntakeState.SPINNING_UP;
                }
                break;

            case SPINNING_DOWN:
                if (absRpm < OFF_RPM_THRESHOLD) {
                    if (pendingPower != 0) {
                        setTarget(pendingPower);
                        pendingPower = 0;
                        atSpeedTimer.reset();
                        intakeState = IntakeState.SPINNING_UP;
                    } else {
                        intakeState = IntakeState.OFF;
                    }
                }
                break;

            case OFF:
                break;
        }
    }

    public IntakeState getIntakeState() {
        return intakeState;
    }

    public boolean isIntakeAtSpeed() {
        return intakeState == IntakeState.AT_SPEED_INTAKE;
    }

    public boolean isIntakeOff() {
        return intakeState == IntakeState.OFF;
    }

    public double getMotorRPM() {
        return (NTKM.getVelocity() / TICKS_PER_REV) * 60.0;
    }

    public enum IntakeState {
        OFF,
        SPINNING_UP,
        AT_SPEED_INTAKE,
        AT_SPEED_REVERSE,
        SPINNING_DOWN
    }
}