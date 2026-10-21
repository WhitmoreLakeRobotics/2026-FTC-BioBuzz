package org.firstinspires.ftc.teamcode.Hardware;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.Common.CommonLogic;

public class Feeder extends BaseHardware {

    private DcMotorEx TransLM2;

    private Launcher.FeederState feederState = Launcher.FeederState.OFF;

    public final double minPower = -1.0;
    public final double maxPower = 1.0;

    private static final double TRANSFER_RPM = 4000;

    private static final double TicksPerRev = 28;

    private final ElapsedTime transferAtSpeedTimer = new ElapsedTime();
    private ElapsedTime timer2 = new ElapsedTime();
    private double integral2 = 0;
    private double lastError2 = 0;

    private double pendingTransferRPM = 0;
    public int RPMoffset = 0;

    public static double FEEDER_BACKWARD_RPM = -500; // default until a real value is chosen
    public static double RPM_TOLERANCE = 50;         // "at speed" window around the target RPM
    public static double AT_SPEED_HOLD_SEC = 0.15;     // RPM must stay in tolerance this long
    public static double OFF_RPM_THRESHOLD = 50;    // |RPM| below this counts as stopped

    private VoltageSensor HIVEENERGY;
    public double getBatteryVoltage() {
        return HIVEENERGY.getVoltage();
    }

    // ---------------- PID CONSTANTS ----------------
    public static double bLkP = 0.00070;   // increased for faster recovery
    public static double bLkI = 0.0;       // still unused
    public static double bLkD = 0.0000015; // small D for damping
    public static double bkF = 1.24 / 6000.0; // feedforward per RPM
    private double targetRPM2 = 0;



    @Override
    public void init() {

        TransLM2 = hardwareMap.get(DcMotorEx.class, "TransLM2");

        TransLM2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        TransLM2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        feederState = Launcher.FeederState.OFF;
        pendingTransferRPM = 0;

    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {

        updateFeederState(getMotorRPM(TransLM2));

        telemetry.addData("Transfer State", feederState);

    }

    @Override
    public void stop() {

        feederState = Launcher.FeederState.OFF;
        pendingTransferRPM = 0;
        TransLM2.setPower(0);

    }

public void runPID() {
    double currentRPM2 = getMotorRPM(TransLM2);
    double power2 = pidMotor2(currentRPM2);
    power2 = Math.max(minPower,
            Math.min(maxPower, power2));

    if (targetRPM2 == 0) {
        power2 = 0;
        integral2 = 0;
        lastError2 = 0;
    }

    TransLM2.setPower(power2);

    telemetry.addData("Transfer RPM", currentRPM2);
    telemetry.addData("Transfer Target", targetRPM2);
    telemetry.addData("Transfer Power", power2);
}



    public double getMotorRPM(DcMotorEx motor) {

        double ticksPerSecond = motor.getVelocity();

        return
                (ticksPerSecond / TicksPerRev) * 60.0;

    }

    public void cmdTransferForward() {
        requestTransferRPM(TRANSFER_RPM);
    }
    public void cmdTransferBackward() {
        requestTransferRPM(FEEDER_BACKWARD_RPM);
    }

    public void cmdTransferStop() {

        targetRPM2 = 0;
        pendingTransferRPM = 0;

        if (feederState != Launcher.FeederState.OFF) {
            feederState = Launcher.FeederState.SPINNING_DOWN;
        }
    }

    private void requestTransferRPM(double rpm) {

        switch (feederState) {
            case OFF:
                targetRPM2 = rpm;
                transferAtSpeedTimer.reset();
                feederState = Launcher.FeederState.SPINNING_UP;
                break;

            case SPINNING_UP:
            case AT_SPEED_FORWARD:
            case AT_SPEED_BACKWARD:
                if (Math.signum(rpm) == Math.signum(targetRPM2)) {
                    targetRPM2 = rpm;   // same direction, keep state (no debounce reset)
                } else {
                    targetRPM2 = 0;
                    pendingTransferRPM = rpm;
                    feederState = Launcher.FeederState.SPINNING_DOWN;
                }
                break;

            case SPINNING_DOWN:
                pendingTransferRPM = rpm;   // wait for the stop, then loop() spins up
                break;
        }
    }

    private void updateFeederState(double rpm) {

        boolean inRange = CommonLogic.inRange(rpm, targetRPM2, RPM_TOLERANCE);

        switch (feederState) {
            case SPINNING_UP:
                if (!inRange) {
                    transferAtSpeedTimer.reset();
                } else if (transferAtSpeedTimer.seconds() >= AT_SPEED_HOLD_SEC) {
                    feederState = (targetRPM2 >= 0)
                            ? Launcher.FeederState.AT_SPEED_FORWARD
                            : Launcher.FeederState.AT_SPEED_BACKWARD;
                }
                break;

            case AT_SPEED_FORWARD:
            case AT_SPEED_BACKWARD:
                if (!inRange) {
                    transferAtSpeedTimer.reset();
                    feederState = Launcher.FeederState.SPINNING_UP;
                }
                break;

            case SPINNING_DOWN:
                if (Math.abs(rpm) < OFF_RPM_THRESHOLD) {
                    if (pendingTransferRPM != 0) {
                        // stopped, now safe to spin up (possibly the other direction)
                        targetRPM2 = pendingTransferRPM;
                        pendingTransferRPM = 0;
                        transferAtSpeedTimer.reset();
                        feederState = Launcher.FeederState.SPINNING_UP;
                    } else {
                        feederState = Launcher.FeederState.OFF;
                    }
                }
                break;

            case OFF:
                break;
        }
    }

    public Launcher.FeederState getTransferState() {
        return feederState;
    }

    public boolean isTransferAtSpeed() {
        return feederState == Launcher.FeederState.AT_SPEED_FORWARD
                || feederState == Launcher.FeederState.AT_SPEED_BACKWARD;
    }

    public double getTransferVelocity() {
        return TransLM2.getVelocity();
    }




    private double pidMotor2(double currentRPM) {
        double error = targetRPM2 - currentRPM;
        double dt = timer2.seconds();
        timer2.reset();

        if (dt <= 0) dt = 0.001;

        double p = bLkP * error;

        integral2 += error * dt;
        integral2 = Math.max(-5000, Math.min(5000, integral2));
        double i = bLkI * integral2;

        double derivative = (error - lastError2) / dt;
        lastError2 = error;
        double d = bLkD * derivative;

        double voltage = Math.max(HIVEENERGY.getVoltage(), 1.0);
        double ff = (targetRPM2 * bkF) * (12.0 / voltage);

        return ff + p + i + d;
    }

}
