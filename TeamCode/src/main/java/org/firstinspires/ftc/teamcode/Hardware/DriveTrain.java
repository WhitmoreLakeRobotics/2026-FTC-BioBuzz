package org.firstinspires.ftc.teamcode.Hardware;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Common.CommonLogic;


import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;


public class DriveTrain extends BaseHardware {

 private Follower follower;

 public final double MAXPOWER = 1.0;
 public final double MINPOWER = -1.0;

 public final double MORNINGSPEED = 0.25;
 public final double NORMALSPEED = 0.5;
 public final double FASTSPEED = 0.85;


 //* for accepting joystick values from teleop and using them elsewhere
 private double RightJoystick_x;
 private double LeftJoystick_x;
 private double LeftJoystick_y;



    @Override
    public void init() {

        follower = Constants.create (hardwareMap);
        follower.setPose(OpModeStorage.startPose);




    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {



    }

    @Override
    public void loop() {

        follower.update();

    }


    @Override
     public void stop() {
        OpModeStorage.autonomousEndPose = follower.pose();
    }

    public void cmdTeleOp(double Left_Y, double Left_X, double Right_X, double Current_Speed) {
//Current_Speed is  to make adjustments to the speed if button pushed for slow or fast.
        LeftJoystick_x = Left_X * Current_Speed;
        LeftJoystick_y = Left_Y * Current_Speed;
        RightJoystick_x = Right_X * Current_Speed;


    }

    private void doTeleop(){


        DrivePowers powers = ManualDrive.fieldCentric(
                LeftJoystick_y,
                LeftJoystick_x,
                RightJoystick_x,
                follower.pose().heading()
        );
        follower.manual(powers);

    }


    public void cmdOpmodestartPose (Pose op_start){
        follower.setPose(op_start);

    }

}
