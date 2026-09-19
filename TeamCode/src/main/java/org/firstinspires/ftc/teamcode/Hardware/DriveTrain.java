package org.firstinspires.ftc.teamcode.Hardware;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;


public class DriveTrain extends BaseHardware {

 private Follower follower;

 public final double Maxpower = 1.0;
 public final double Minpower = -1.0;

 public static final double MorningSpeed = 0.25;
 public static final double Normalspeed = 0.5;
 public static final double Fastspeed = 0.85;





    @Override
    public void init() {

        follower = Constants.create (hardwareMap);




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

    }
}
