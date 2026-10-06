package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Common.CommonLogic;
import org.firstinspires.ftc.teamcode.Common.Settings;
import org.firstinspires.ftc.teamcode.Hardware.Robot;

import com.pedropathing.math.Pose;


@TeleOp(name = "TeleOp")
public class Telop extends OpMode {

    private static final String TAGTeleop = "8492-Teleop";
    Robot robot = new Robot();
    private boolean debug = true;  // if debug, show telemetry with values you're interested in

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
    private boolean gp2_prev_start = false;
    private double LeftMotorPower = 0;
    private double RightMotorPower = 0;
    private int tHeading = 0;
    private boolean bAutoTurn = false;

    // CHANGED: Pedro headings are in RADIANS.
    private Pose op_pose = new Pose(90, 0, Math.toRadians(80));

    @Override
    public void init() {
        robot.hardwareMap = hardwareMap;
        robot.telemetry = telemetry;
        robot.init();
        // CHANGED: removed "OpModeStorage.startPose = ..." — DriveTrain.start() handles it now.
    }

    @Override
    public void init_loop() {
        robot.init_loop();
    }

    @Override
    public void start() {
        robot.start();
    }

    @Override
    public void loop() {

        if (bAutoTurn) {
            // auto-turn code goes here later (use FASTSPEED / MORNINGSPEED,
            // DTrain_FASTSPEED and DTrain_SLOWSPEED don't exist).
        } else {
            robot.driveTrain.cmdTeleOp(
                    CommonLogic.joyStickMath(gamepad1.left_stick_y * -1),
                    CommonLogic.joyStickMath(gamepad1.left_stick_x),
                    CommonLogic.joyStickMath(gamepad1.right_stick_x),
                    robot.driveTrain.NORMALSPEED);
        }

        if (Math.abs(gamepad1.right_stick_y) > Settings.JOYSTICK_DEADBAND_STICK) {

        }

        //gamepad1

        if ((gamepad1.b)) {
            robot.intake.cmdForward();
            robot.transitionR.cmdSpinng();

        }

        if ((gamepad1.a)) {
            robot.intake.cmdStop();
            robot.transitionR.cmdStop();

        }

        if ((gamepad1.y)) {
            robot.intake.cmdReverse();
            robot.transitionR.cmdTumble();
        }

        if (gamepad1.right_bumper) {
            robot.launcher.cmdLauncherForward();
        }

        if (gamepad1.left_bumper) {
            robot.launcher.cmdTransferForward();
        }

        if (gamepad1.dpad_left) {
            robot.launcher.cmdLauncherStop();
        }

        if (gamepad1.dpad_right) {
            robot.launcher.cmdTransferStop();
        }

        if ((gamepad1.x)) {
            robot.launcher.cmdLauncherStop();
        }

        if ((gamepad1.x)) {
            robot.launcher.cmdLauncherStop();
        }


        //gamepad2

        if ((gamepad2.b)) {
            robot.intake.cmdForward();
            robot.transitionR.cmdSpinng();

        }

        if ((gamepad2.a)) {
            robot.intake.cmdStop();
            robot.transitionR.cmdStop();

        }

        if ((gamepad2.y)) {
            robot.intake.cmdReverse();
            robot.transitionR.cmdTumble();

        }


        if (gamepad2.right_bumper) {
            robot.launcher.cmdLauncherForward();
        }

        if (gamepad2.left_bumper) {
            robot.launcher.cmdTransferForward();
        }

        if (gamepad2.dpad_left) {
            robot.launcher.cmdLauncherStop();
        }

        if (gamepad2.dpad_right) {
            robot.launcher.cmdTransferStop();
        }

        if ((gamepad2.x)) {
            robot.launcher.cmdLauncherStop();
        }

        if ((gamepad2.x)) {
            robot.launcher.cmdLauncherStop();
        }

        // robot.loop() -> driveTrain.loop() -> follower.update(), which sends
        // the power to the motors. Must come AFTER cmdTeleOp.
        robot.loop();

        // ADDED: debug telemetry so you can see what's happening.
        if (debug) {
            Pose p = robot.driveTrain.getPose();
            telemetry.addData("Stick fwd/strafe/turn", "%.2f / %.2f / %.2f",
                    -gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            telemetry.addData("Robot X (in)", "%.1f", p.x());
            telemetry.addData("Robot Y (in)", "%.1f", p.y());
            telemetry.addData("Heading (deg)", "%.1f", Math.toDegrees(p.heading()));
            telemetry.addData("Launcher Velocity",robot.launcher.getLauncherVelocity());
            telemetry.addData("Transfer Velocity",robot.launcher.getTransferVelocity());
            telemetry.addData("Battery Voltage","%.2f", robot.launcher.getBatteryVoltage());
            telemetry.update();

        }
    }

    @Override
    public void stop() {
        // ADDED: without this, DriveTrain.stop() never saves the pose.
        robot.stop();
    }

}