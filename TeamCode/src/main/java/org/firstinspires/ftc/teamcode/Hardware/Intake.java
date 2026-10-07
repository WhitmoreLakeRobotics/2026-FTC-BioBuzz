package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Common.CommonLogic;


public class Intake extends BaseHardware {

    private DcMotorEx NTKM;

    public Mode CurrentMode;

    public final double minPower = -1.0;
    public final double maxPower = 1.0;

    private static final double NTK_VELOCITY = 4000;

    private static final double NTKspeed = -0.75;
    private static final double Hummingspeed = 0.45;
    private static final double Donespeed = 0;

    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime timerun = new ElapsedTime();


    @Override
    public void init() {
        // NTKM = hardwareMap.get(DcMotor.class, "NTKM");
        NTKM = hardwareMap.get(DcMotorEx.class, "NTKM");

        NTKM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        NTKM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);


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
        NTKM.setVelocity(0);

    }

    public void cmdIntakeForward() {
        NTKM.setPower(NTKspeed);
        CurrentMode = Mode.NTKforward;


    NTKM.setVelocity(NTK_VELOCITY);

    }

    public void cmdReverse() {
        CurrentMode = Mode.NTKbackward;
        NTKM.setPower(Hummingspeed);


    }

    public void cmdStop() {
        CurrentMode = Mode.NTKstop;
        NTKM.setPower(Donespeed);
        runtime.reset();
    }


    public enum Mode {
        NTKstop,
        NTKforward,
        NTKbackward
    }


}
