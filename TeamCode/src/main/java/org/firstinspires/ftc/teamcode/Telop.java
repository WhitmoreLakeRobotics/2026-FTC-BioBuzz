package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.Common.CommonLogic;
//import org.firstinspires.ftc.teamcode.Common.Settings;

import org.firstinspires.ftc.teamcode.Hardware.DriveTrain;
import org.firstinspires.ftc.teamcode.Hardware.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Launcher;
import org.firstinspires.ftc.teamcode.Hardware.TansitionR;
import org.firstinspires.ftc.teamcode.Hardware.Robot;



/*
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;

 */
import com.pedropathing.math.Pose;


import org.firstinspires.ftc.teamcode.Hardware.Robot;

@TeleOp (name = "TeleOp")
public class Telop extends OpMode {

    private static final String TAGTeleop = "8492-Teleop";
    //RobotTest robot = new RobotTest();
    Robot robot = new Robot();
    private boolean debug = true;  //insert if bebug then show telemetry stmt with values you're interested in
    //    // Declare OpMode members.
    private boolean gp1_prev_a = false;
    private boolean gp1_prev_b = false;
    private boolean gp1_prev_x = false;
    private boolean gp1_prev_y = false;
    private boolean gp1_prev_right_bumper = false;
    private boolean gp1_prev_left_bumper = false;
    private boolean gp1_prev_dpad_up = false;
    private boolean gp1_prev_dpad_down = false;
    private boolean gp1_prev_dpad_left = false;
    private boolean gp1_prev_dpad_right = false;
    private boolean gp1_prev_back = false;
    private boolean gp1_prev_start = false;

    private boolean gp2_prev_a = false;
    private boolean gp2_prev_b = false;
    private boolean gp2_prev_x = false;
    private boolean gp2_prev_y = false;
    private boolean gp2_prev_right_bumper = false;
    private boolean gp2_prev_left_bumper = false;
    private boolean gp2_prev_dpad_up = false;
    private boolean gp2_prev_dpad_down = false;
    private boolean gp2_prev_dpad_left = false;
    private boolean gp2_prev_dpad_right = false;
    private boolean gp2_prev_back = false;
    private double LeftMotorPower = 0;
    private double RightMotorPower = 0;
    private boolean gp2_prev_start = false;
    private int tHeading = 0;
    private boolean bAutoTurn = false;

    private Pose op_pose = new Pose(90,00, 80);

    @Override
    public void init() {
    robot.driveTrain.cmdOpmodestartPose(op_pose);

    }
    @Override
    public void init_loop(){


 }
    @Override
    public void start(){

}
    @Override
    public void loop(){

        if (bAutoTurn) {
        if (gamepad1.right_bumper) {
            robot.driveTrain.cmdTeleOp(CommonLogic.joyStickMath(gamepad1.left_stick_y * -1),
                    CommonLogic.joyStickMath(gamepad1.left_stick_x),
                    robot.driveTrain.autoTurn(tHeading), robot.driveTrain.DTrain_FASTSPEED);
        } else if (gamepad1.left_bumper) {
            robot.driveTrain.cmdTeleOp(CommonLogic.joyStickMath(gamepad1.left_stick_y * -1),
                    CommonLogic.joyStickMath(gamepad1.left_stick_x),
                    robot.driveTrain.autoTurn(tHeading), robot.driveTrain.DTrain_SLOWSPEED);


        } else {

            robot.driveTrain.cmdTeleOp(CommonLogic.joyStickMath(gamepad1.left_stick_y * -1),
                    CommonLogic.joyStickMath(gamepad1.left_stick_x),
                    robot.driveTrain.autoTurn(tHeading), robot.driveTrain.);
        }
    } else {
        if (gamepad1.right_bumper) {
            robot.driveTrain.cmdTeleOp(CommonLogic.joyStickMath(gamepad1.left_stick_y * -1),
                    CommonLogic.joyStickMath(gamepad1.left_stick_x),
                    CommonLogic.joyStickMath(gamepad1.right_stick_x), robot.driveTrain.DTrain_FASTSPEED);
        } else if (gamepad1.left_bumper) {
            robot.driveTrain.cmdTeleOp(CommonLogic.joyStickMath(gamepad1.left_stick_y * -1),
                    CommonLogic.joyStickMath(gamepad1.left_stick_x),
                    CommonLogic.joyStickMath(gamepad1.right_stick_x), robot.driveTrain.DTrain_SLOWSPEED);


        } else {

            robot.driveTrain.cmdTeleOp(CommonLogic.joyStickMath(gamepad1.left_stick_y * -1),
                    CommonLogic.joyStickMath(gamepad1.left_stick_x),
                    CommonLogic.joyStickMath(gamepad1.right_stick_x), robot.driveTrain.DTrain_NORMALSPEED);
        }

    }

        if (Math.abs(gamepad1.right_stick_y) > Settings.JOYSTICK_DEADBAND_STICK) {


    }

    }





    @Override
    public void stop(){

    }

}
