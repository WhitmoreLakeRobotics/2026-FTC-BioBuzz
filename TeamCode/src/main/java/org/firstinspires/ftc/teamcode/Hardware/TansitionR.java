package org.firstinspires.ftc.teamcode.Hardware;

import android.transition.Transition;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Common.CommonLogic;



public class TansitionR extends BaseHardware{

    private DcMotor TRM;
    public Servo TansS;

    public Mode CurrentMode;


    public static final double SquidSpeed = 0.35;
    public static final double BackSpeed = -0.4;
    public static final double EndSpeed = 0.0;

    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime timerun = new ElapsedTime();


    @Override
    public void init(){

    }


    @Override
    public void init_loop(){


    }


    @Override
    public void start (){


    }


    @Override
    public void loop(){


    }


    @Override
    public void stop(){


    }

    public void cmdSpinng(){
        CurrentMode = Mode.SquidSpeed;
        TRM.setPower(SquidSpeed);

    }


    public void cmdTumble(){
        CurrentMode = Mode.BackSpeed;
        TRM.setPower(BackSpeed);
    }

    public void cmdStop(){
        CurrentMode = Mode.EndSpeed;
        TRM.setPower(EndSpeed);
        runtime.reset();
    }


    public enum Mode{

        SquidSpeed,
        BackSpeed,
        EndSpeed;



    }
}
