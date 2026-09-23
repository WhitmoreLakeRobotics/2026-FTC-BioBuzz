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

    private DcMotor NTKM;
    public Mode CurrentMode;

    public final double minPower = -1.0;
    public final double maxPower = 1.0;

    private static final double NTKspeed = 0.55;
    private static final double Hummingspeed = 0.45;
    private static final double Donespeed = 0;

    @Override
    public void init(){


    }

    @Override
    public void init_loop(){


    }

    @Override
    public void start(){



    }

    @Override
    public void loop(){




    }

    @Override
    public void stop (){




    }

    public void cmdForward (){
        CurrentMode = Mode.NTKforward;
        NTKM.setPower (NTKspeed);
    }

    public void cmdReverse(){
        CurrentMode = Mode.NTKbackward;
        NTKM.setPower (Hummingspeed);


    }

    public void cmdStop(){
        CurrentMode = Mode.NTKstop;
        NTKM.setPower (Donespeed);

    }


    public enum Mode {
        NTKstop,
        NTKforward,
        NTKbackward
    }





}
