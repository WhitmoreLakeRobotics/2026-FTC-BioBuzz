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



    private static final double NTKspeed = 0.55;
     //private static final double

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

    public cmdForward (){
        CurrentMode = Mode.NTKforward;
        NTKM.setPower (NTKspeed);
    }





}
