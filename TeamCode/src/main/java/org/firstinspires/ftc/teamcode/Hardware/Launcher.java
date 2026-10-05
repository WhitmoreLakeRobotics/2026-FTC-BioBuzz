package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Common.CommonLogic;



public class Launcher extends BaseHardware{

    private DcMotor LM1;
    private DcMotor TransLM2;

    public Mode CurrentMode;

    public final double minPower = -1.0;
    public final double maxPower = 1.0;

    //launching speeds
    private static final double Pollenating  = 1.0;
    private static final double LaurenStop = 0.0;

    //Transition speeds
    private static final double Pushingspeed = 0.55;
    private static final double naughtspeed = 0.0;


    @Override
    public void init() {
        LM1 = hardwareMap.get(DcMotor.class, "LM1");
        TransLM2 = hardwareMap.get(DcMotor.class, "TransLM2");
    }

    @Override
    public void init_loop() {


    }

    @Override
    public void start() {


    }

    @Override
    public void loop(){

    }

    @Override
    public void stop(){


    }
    public void cmdForward (){
        CurrentMode = Mode.Pollenating;
        LM1.setPower (Pollenating);
        TransLM2.setPower(Pushingspeed);

    }
    public void cmdStop(){
        CurrentMode = Mode.LaurenStop;
        LM1.setPower (LaurenStop);
        TransLM2.setPower(naughtspeed);
    }
    public enum Mode {
        LaurenStop,
        Pollenating,

    }
}
