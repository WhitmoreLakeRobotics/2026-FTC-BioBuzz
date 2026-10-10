package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Common.CommonLogic;


public class Launcher extends BaseHardware {

// private DcMotor LM1;
// private DcMotor TransLM2;

    private DcMotorEx LM1;
    private DcMotorEx TransLM2;

    public Mode CurrentMode;

    public final double minPower = -1.0;
    public final double maxPower = 1.0;

    public int RPMoffset = 0;

    public double getBatteryVoltage() {
        return HIVEENERGY.getVoltage();
    }

    private static final double POLLENATING_VELOCITY = 4000;
    private static final double TRANSFER_VELOCITY = 4000;
    private VoltageSensor HIVEENERGY;
    private static final double NOMINAL_VOLTAGE = 12.0;

    // ---------------- PID CONSTANTS ----------------
    public static double LkP = 0.00060;   // increased for faster recovery
    public static double LkI = 0.0;       // still unused
    public static double LkD = 0.0000015; // small D for damping
    public static double kF = 1.24 / 6000.0; // feedforward per RPM

    // ---------------- PID CONSTANTS ----------------
    public static double bLkP = 0.00070;   // increased for faster recovery
    public static double bLkI = 0.0;       // still unused
    public static double bLkD = 0.0000015; // small D for damping
    public static double bkF = 1.01 / 6000.0; // feedforward per RPM

    private double targetRPM1 = 0;
    private double targetRPM2 = 0;

    private double targetRPM1Tol = 50;
    private double targetRPM2Tol = 50;
    public boolean bAtSpeed = false;


    private double lastError1 = 0;
    private double lastError2 = 0;

    private double integral1 = 0;
    private double integral2 = 0;

    private ElapsedTime timer1 = new ElapsedTime();
    private ElapsedTime timer2 = new ElapsedTime();


    private double getCompensatedVelocity(double targetVelocity) {

        double currentVoltage = HIVEENERGY.getVoltage();

        if (currentVoltage <= 0) {
            return targetVelocity;
        }

        return targetVelocity * (NOMINAL_VOLTAGE / currentVoltage);
    }

// private static final double Pollenating = 1.0;
// private static final double LaurenStop = 0.0;

    // private static final double Pushingspeed = 0.55;
// private static final double naughtspeed = 0.0;

    @Override
    public void init() {

// LM1 = hardwareMap.get(DcMotor.class, "LM1");
// TransLM2 = hardwareMap.get(DcMotor.class, "TransLM2");

        LM1 = hardwareMap.get(DcMotorEx.class, "LM1");
        TransLM2 = hardwareMap.get(DcMotorEx.class, "TransLM2");

        HIVEENERGY = hardwareMap.voltageSensor.iterator().next();

        LM1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        TransLM2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        LM1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        TransLM2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {

        runPID();

        if ((CommonLogic.inRange(getMotorRPM(LM1), targetRPM1, targetRPM1Tol)) &&
                (CommonLogic.inRange(getMotorRPM(TransLM2), targetRPM2, targetRPM2Tol))
        ) {
            bAtSpeed = true;
        } else {
            bAtSpeed = false;
        }
        telemetry.addData("RPM offset: ", RPMoffset);

    }

    @Override
    public void stop() {

        LM1.setVelocity(0);
        TransLM2.setVelocity(0);
    }

    public void cmdLauncherForward() {

        CurrentMode = Mode.Pollenating;

        LM1.setVelocity(getCompensatedVelocity(POLLENATING_VELOCITY));

    }

    public void cmdTransferForward() {

        CurrentMode = Mode.Pollenating;

        TransLM2.setVelocity(getCompensatedVelocity(TRANSFER_VELOCITY));
    }

    public void cmdLauncherStop() {

        CurrentMode = Mode.LaurenStop;

        LM1.setVelocity(0);

    }

    public void cmdTransferStop() {

        TransLM2.setVelocity(0);
    }

    public double getLauncherVelocity() {
        return LM1.getVelocity();
    }

    public double getTransferVelocity() {
        return TransLM2.getVelocity();
    }

    public enum Mode {
        LaurenStop,
        Pollenating,
    }


    public double getMotorRPM(DcMotorEx motor) {
        double ticksPerRevolution = 28; //update and double check
        double gearRatio = 1.0; //update and double check
        double ticksPerSecond = motor.getVelocity();
        return (ticksPerSecond / ticksPerRevolution) * 60 * gearRatio;
    }

    private double pidMotor1(double currentRPM) {
        double error = targetRPM1 - currentRPM;
        double dt = timer1.seconds();
        timer1.reset();

        if (dt <= 0) dt = 0.001;

        double p = LkP * error;

        integral1 += error * dt;
        double i = LkI * integral1;

        double derivative = (error - lastError1) / dt;
        lastError1 = error;
        double d = LkD * derivative;

        double voltage = HIVEENERGY.getVoltage();
        double ff = (targetRPM1 * kF) * (12.0 / voltage);

        return ff + p + i + d;
    }


    private double pidMotor2(double currentRPM) {
        double error = targetRPM2 - currentRPM;
        double dt = timer2.seconds();
        timer2.reset();

        if (dt <= 0) dt = 0.001;

        double p = bLkP * error;

        integral2 += error * dt;
        double i = bLkI * integral2;

        double derivative = (error - lastError2) / dt;
        lastError2 = error;
        double d = bLkD * derivative;

        double voltage = HIVEENERGY.getVoltage();
        double ff = (targetRPM2 * bkF) * (12.0 / voltage);

        return ff + p + i + d;
    }


    public void runPID() {
        double currentRPM1 = getMotorRPM(LM1);
        double power1 = pidMotor1(currentRPM1);

        double currentRPM2 = getMotorRPM(TransLM2);
        double power2 = pidMotor2(currentRPM2);

        // Prevent braking when stopping (test) MJD
        if (targetRPM1 == 0) {
            power1 = 0;
            integral1 = 0;   // reset PID so it doesn't spike later
            lastError1 = 0;
        }

        if (targetRPM2 == 0) {
            power2 = 0;
            integral2 = 0;
            lastError2 = 0;
        }


    }
}