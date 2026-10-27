package org.firstinspires.ftc.teamcode.SCRIM;

import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Common.CommonLogic;
import org.firstinspires.ftc.teamcode.Common.Settings;
import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.SCRIM.Hardware.S_Robot;

import com.pedropathing.math.Pose;


@TeleOp(name = "Scrim_TeleOp")
public class S_Telop extends OpMode {

    private static final String TAGTeleop = "8492-Teleop";
    S_Robot S_Robot = new S_Robot();
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
        S_Robot.hardwareMap = hardwareMap;
        S_Robot.telemetry = telemetry;
        S_Robot.init();
        S_Robot.S_DriveTrain.cmdOpmodestartPose(OpModeStorage.autonomousEndPose);
    }

    @Override
    public void init_loop() {
        S_Robot.init_loop();
        PanelsTelemetry.INSTANCE.getTelemetry().update();
        telemetry.update();
    }

    @Override
    public void start() {
        S_Robot.start();

    }

    @Override
    public void loop() {

        if (bAutoTurn) {
            // auto-turn code goes here later (use FASTSPEED / MORNINGSPEED,
            // DTrain_FASTSPEED and DTrain_SLOWSPEED don't exist).
        } else {
            S_Robot.S_DriveTrain.cmdTeleOp(
                    CommonLogic.joyStickMath(gamepad1.left_stick_y * -1),
                    CommonLogic.joyStickMath(gamepad1.left_stick_x * -1),
                    CommonLogic.joyStickMath(gamepad1.right_stick_x),
                    S_Robot.S_DriveTrain.NORMALSPEED);
        }

        if (Math.abs(gamepad1.right_stick_y) > Settings.JOYSTICK_DEADBAND_STICK) {

        }

        //gamepad1

        if ((gamepad1.b)) {
            S_Robot.S_Intake.cmdIntakeForward();
            S_Robot.S_TransitionR.cmdSpinng();

        }

        if ((gamepad1.a)) {
            S_Robot.S_Intake.cmdIntakeStop();
            S_Robot.S_TransitionR.cmdStop();

        }

        if ((gamepad1.y)) {
            S_Robot.S_Intake.cmdIntakeReverse();
            S_Robot.S_TransitionR.cmdTumble();
        }

        // TODO (reminder): Launcher now has state tracking. Still to do here:
        //  1. Map a button to robot.launcher.cmdTransferBackward() (no button assigned yet).
        //  2. Replace the old commented-out "bAtSpeed" gate below with
        //     robot.launcher.isLauncherAtSpeed() / isTransferAtSpeed() before feeding.
        //  3. Optionally show getLauncherState() / getTransferState() in debug telemetry.
        if (gamepad1.right_trigger_pressed) {
            //robot.launcher.cmdLauncherForward();
          if(S_Robot.S_Launcher.isLauncherAtSpeed()){
              S_Robot.S_TransitionR.cmdSpinng();
              S_Robot.pollenBlocker.cmdOpen();


          }else{
              S_Robot.S_TransitionR.cmdStop();
              S_Robot.pollenBlocker.cmdBlock();

          }



        }

        if (gamepad1.left_trigger_pressed) {

        }

        if (gamepad1.dpad_left) {
            S_Robot.S_DriveTrain.cmdTeleOp(0,0,0, S_Robot.S_DriveTrain.NORMALSPEED);
        }

        if (gamepad1.dpad_right) {

        }

        if ((gamepad1.dpad_up)){
            S_Robot.S_DriveTrain.cmdTeleOp(0, 0, 0, S_Robot.S_DriveTrain.FASTSPEED);

        }

        if ((gamepad1.dpad_down)){
            S_Robot.S_DriveTrain.cmdTeleOp(0, 0, 0, S_Robot.S_DriveTrain.MORNINGSPEED);

        }

        if ((gamepad1.x)) {

        }



        if ((gamepad1.back)){
            S_Robot.S_DriveTrain.resetDriveHeading();

        }


        //gamepad2

        if ((gamepad2.b)) {
            S_Robot.S_Intake.cmdIntakeForward();
            S_Robot.S_TransitionR.cmdSpinng();

        }

        if ((gamepad2.a)) {
            S_Robot.S_Intake.cmdIntakeStop();
            S_Robot.S_TransitionR.cmdStop();

        }

        if ((gamepad2.y)) {
            S_Robot.S_Intake.cmdIntakeReverse();
            S_Robot.S_TransitionR.cmdTumble();

        }

        if ((gamepad2.x)) {
            S_Robot.S_Launcher.cmdLauncherStop();
            S_Robot.feeder.cmdTransferStop();
        }


        if (gamepad2.right_bumper) {
            S_Robot.S_Launcher.cmdLauncherForward();
        }

        if (gamepad2.left_bumper) {
            S_Robot.feeder.cmdTransferForward();
        }

        if (gamepad2.dpad_left) {

        }

        if (gamepad2.dpad_right) {
           
        }

        if ((gamepad2.dpad_up)){

        }
        if ((gamepad2.left_trigger < 0.6)){
            S_Robot.pollenBlocker.cmdOpen();

        }else{
            S_Robot.pollenBlocker.cmdBlock();
        }






        // robot.loop() -> driveTrain.loop() -> follower.update(), which sends
        // the power to the motors. Must come AFTER cmdTeleOp.
        S_Robot.loop();
        PanelsTelemetry.INSTANCE.getTelemetry().update();
        // ADDED: debug telemetry so you can see what's happening.
        if (debug) {
            Pose p = S_Robot.S_DriveTrain.getPose();
            telemetry.addData("Stick fwd/strafe/turn", "%.2f / %.2f / %.2f",
                    -gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            telemetry.addData("Robot X (in)", "%.1f", p.x());
            telemetry.addData("Robot Y (in)", "%.1f", p.y());
            telemetry.addData("Heading (deg)", "%.1f", Math.toDegrees(p.heading()));
            telemetry.addData("Battery Voltage","%.2f", S_Robot.S_Launcher.getBatteryVoltage());


        }
        telemetry.update();
    }

    @Override
    public void stop() {
        // ADDED: without this, DriveTrain.stop() never saves the pose.
        S_Robot.stop();
    }

}