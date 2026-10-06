package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


public class TransitionR extends BaseHardware{

   // private DcMotor TRM;
    public CRServo TransS;

    public Mode CurrentMode;


    public static final double SquidSpeed = -0.95;
    public static final double BackSpeed = 0.4;
    public static final double EndSpeed = 0.0;

    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime timerun = new ElapsedTime();


    @Override
    public void init(){
        TransS = hardwareMap.get(CRServo.class, "TransS");

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
        TransS.setPower(SquidSpeed);

    }


    public void cmdTumble(){
        CurrentMode = Mode.BackSpeed;
        TransS.setPower(BackSpeed);
    }

    public void cmdStop(){
        CurrentMode = Mode.EndSpeed;
        TransS.setPower(EndSpeed);
        runtime.reset();
    }


    public enum Mode{

        SquidSpeed,
        BackSpeed,
        EndSpeed;



    }
}
