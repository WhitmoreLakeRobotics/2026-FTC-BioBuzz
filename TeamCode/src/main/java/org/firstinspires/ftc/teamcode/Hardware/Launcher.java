package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class Launcher extends BaseHardware {

// private DcMotor LM1;
// private DcMotor TransLM2;

    private DcMotorEx LM1;
    private DcMotorEx TransLM2;

    public Mode CurrentMode;

    public final double minPower = -1.0;
    public final double maxPower = 1.0;

    public double getBatteryVoltage() {return HIVEENERGY.getVoltage();
    }
    private static final double POLLENATING_VELOCITY = 4000;
    private static final double TRANSFER_VELOCITY = 4000;
    private VoltageSensor HIVEENERGY;
    private static final double NOMINAL_VOLTAGE = 12.0;

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

        HIVEENERGY= hardwareMap.voltageSensor.iterator().next();

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

    public void cmdTransferStop(){

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
}