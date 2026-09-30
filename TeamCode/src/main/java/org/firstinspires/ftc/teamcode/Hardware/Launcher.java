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

    public Mode CurrentMode;

    public final double minPower = -1.0;
    public final double maxPower = 1.0;


    private static final double Pollenating  = 0.5;

    private static final double LaurenStop = 0.0;



    @Override
    public void init() {

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
    }
    public void cmdStop(){
        CurrentMode = Mode.LaurenStop;
        LM1.setPower (LaurenStop);
    }
    public enum Mode {
        LaurenStop,
        Pollenating
    }
}
