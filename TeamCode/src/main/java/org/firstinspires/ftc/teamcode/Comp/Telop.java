package org.firstinspires.ftc.teamcode.Comp;

import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Common.CommonLogic;
import org.firstinspires.ftc.teamcode.Common.Settings;
import org.firstinspires.ftc.teamcode.Comp.Hardware.Robot;
import org.firstinspires.ftc.teamcode.OpModeStorage;


// CHANGED: renamed so it doesn't clash with the Scrimmage TeleOp (two OpModes can't share a name).
@TeleOp(name = "Comp TeleOp")
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
    // CHANGED: the shoot trigger moved to gamepad 2, so this flag is no longer used.
    //private boolean gp1_prev_right_trigger = false;

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
    // CHANGED: used to detect the moment the manual blocker trigger is released.
    private boolean gp2_prev_left_trigger = false;
    // CHANGED: used to detect the moment the shoot trigger is released (now on gamepad 2).
    private boolean gp2_prev_right_trigger = false;
    // CHANGED: true once the launcher has reached speed with the shoot trigger held. It latches,
    // so dips in RPM during a volley can't close the blocker. It clears when the trigger is released.
    private boolean volleyActive = false;
    private double LeftMotorPower = 0;
    private double RightMotorPower = 0;
    private int tHeading = 0;
    private boolean bAutoTurn = false;

    // CHANGED: the drive speed the sticks use. dpad left/up/down set it (it stays until changed).
    private double driveSpeed = robot.driveTrain.NORMALSPEED;

    // CHANGED: Pedro headings are in RADIANS.
    private Pose op_pose = new Pose(90, 0, Math.toRadians(80));

    @Override
    public void init() {
        robot.hardwareMap = hardwareMap;
        robot.telemetry = telemetry;
        robot.init();
        // CHANGED: autonomousEndPose is null if TeleOp runs without Auto first, so use a fallback.
        Pose startPose = OpModeStorage.autonomousEndPose;
        if (startPose == null) {
            startPose = new Pose(0, 0, 0);   // TODO: set a real default start pose
        }
        robot.driveTrain.cmdOpmodestartPose(startPose);
    }

    @Override
    public void init_loop() {
        robot.init_loop();
        PanelsTelemetry.INSTANCE.getTelemetry().update();
        telemetry.update();
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
                    CommonLogic.joyStickMath(gamepad1.left_stick_x * -1),
                    CommonLogic.joyStickMath(gamepad1.right_stick_x),
                    driveSpeed);   // CHANGED: was robot.driveTrain.NORMALSPEED
        }

        if (Math.abs(gamepad1.right_stick_y) > Settings.JOYSTICK_DEADBAND_STICK) {

        }

        if ((gamepad1.b)) {
            //robot.intake.cmdIntakeForward();
            //robot.transitionR.cmdSpinng();

        }

        if ((gamepad1.a)) {
            //robot.intake.cmdIntakeStop();
            //robot.transitionR.cmdStop();

        }

        if ((gamepad1.y)) {
            //robot.intake.cmdIntakeReverse();
            //robot.transitionR.cmdTumble();
        }

        // TODO (reminder): Launcher now has state tracking. Still to do here:
        //  1. Map a button to robot.feeder.cmdFeederBackward() (no button assigned yet).
        //  2. Replace the old commented-out "bAtSpeed" gate below with
        //     robot.launcher.isLauncherAtSpeed() / robot.feeder.isFeederAtSpeed() before feeding.
        //  3. Optionally show getLauncherState() / getFeederState() in debug telemetry.
        // DONE (1): gamepad2.dpad_left runs the feeder backward while held.
        // DONE (2): the shoot block (now on gamepad2.right_trigger, further down) gates the
        //           blocker and feeder on isLauncherAtSpeed().
        // DONE (3): the Launcher and Feeder classes print their own states; the debug block adds the at-speed flags.
        // CHANGED: the shoot block moved to gamepad 2. The old gamepad 1 version is kept here, commented out.
        /*
        boolean shootHeld = gamepad1.right_trigger_pressed;

        if (shootHeld) {
            //robot.launcher.cmdLauncherForward();
            robot.launcher.cmdLauncherForward();
          if(robot.launcher.isLauncherAtSpeed()){
              //robot.transitionR.cmdSpinng();
              robot.pollenBlocker.cmdOpen();
              robot.feeder.cmdFeederForward();


          }else{
              //robot.transitionR.cmdStop();
              robot.pollenBlocker.cmdBlock();
              robot.feeder.cmdFeederStop();

          }



        } else if (gp1_prev_right_trigger) {
            robot.pollenBlocker.cmdBlock();
            //robot.transitionR.cmdStop();
            robot.feeder.cmdFeederStop();
        }
        gp1_prev_right_trigger = shootHeld;
        */

        if (gamepad1.left_trigger_pressed) {

        }

        if (gamepad1.dpad_left) {
            driveSpeed = robot.driveTrain.NORMALSPEED;
        }

        if (gamepad1.dpad_right) {

        }

        if ((gamepad1.dpad_up)){
            driveSpeed = robot.driveTrain.FASTSPEED;

        }

        if ((gamepad1.dpad_down)){
            driveSpeed = robot.driveTrain.MORNINGSPEED;

        }

        if ((gamepad1.x)) {

        }



        if ((gamepad1.back)){
            robot.driveTrain.resetDriveHeading();

        }


        //gamepad2

        if ((gamepad2.b)) {
            robot.intake.cmdIntakeForward();

        }

        if ((gamepad2.a)) {
            robot.intake.cmdIntakeStop();

        }

        if ((gamepad2.y)) {
            robot.intake.cmdIntakeReverse();

        }

        if ((gamepad2.x)) {
            robot.launcher.cmdLauncherStop();
            robot.feeder.cmdFeederStop();
            volleyActive = false;
        }


        if (gamepad2.right_bumper) {
            robot.launcher.cmdLauncherForward();
        }

        if (gamepad2.left_bumper) {
            robot.feeder.cmdFeederForward();
        }

        if (gamepad2.dpad_left) {
            robot.feeder.cmdFeederBackward();
        } else if (gp2_prev_dpad_left) {
            robot.feeder.cmdFeederStop();
        }
        gp2_prev_dpad_left = gamepad2.dpad_left;

        if (gamepad2.dpad_right) {

        }

        if ((gamepad2.dpad_up)){

        }

        boolean blockerOverrideHeld = gamepad2.left_trigger > 0.6;

        if (blockerOverrideHeld){
            robot.pollenBlocker.cmdOpen();

        }else if (gp2_prev_left_trigger){
            robot.pollenBlocker.cmdBlock();
        }
        gp2_prev_left_trigger = blockerOverrideHeld;


        boolean shootHeld = gamepad2.right_trigger_pressed;

        if (shootHeld) {
            robot.launcher.cmdLauncherForward();

            // CHANGED: the at-speed check only STARTS the volley. After that, dips in RPM
            // (a ball loading the flywheel) no longer close the blocker.
            if (!volleyActive && robot.launcher.isLauncherAtSpeed()) {
                volleyActive = true;
            }

            if(volleyActive){
                robot.pollenBlocker.cmdOpen();
                robot.feeder.cmdFeederForward();


            }else{
                robot.pollenBlocker.cmdBlock();
                robot.feeder.cmdFeederStop();

            }



        } else if (gp2_prev_right_trigger) {
            volleyActive = false;
            robot.pollenBlocker.cmdBlock();
            robot.feeder.cmdFeederStop();
        }
        gp2_prev_right_trigger = shootHeld;






        // robot.loop() -> driveTrain.loop() -> follower.update(), which sends
        // the power to the motors. Must come AFTER cmdTeleOp.
        robot.loop();
        PanelsTelemetry.INSTANCE.getTelemetry().update();
        // ADDED: debug telemetry so you can see what's happening.
        if (debug) {
            Pose p = robot.driveTrain.getPose();
            telemetry.addData("Stick fwd/strafe/turn", "%.2f / %.2f / %.2f",
                    -gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            telemetry.addData("Robot X (in)", "%.1f", p.x());
            telemetry.addData("Robot Y (in)", "%.1f", p.y());
            telemetry.addData("Heading (deg)", "%.1f", Math.toDegrees(p.heading()));
            telemetry.addData("Battery Voltage","%.2f", robot.launcher.getBatteryVoltage());
            // CHANGED: at-speed flags and blocker state (the parts already print their own states).
            telemetry.addData("Launcher at speed", robot.launcher.isLauncherAtSpeed());
            telemetry.addData("Feeder at speed", robot.feeder.isFeederAtSpeed());
            telemetry.addData("Blocker open", robot.pollenBlocker.isOpen());
            telemetry.addData("Volley active", volleyActive);   // CHANGED: shows the latch


        }
        telemetry.update();
    }

    @Override
    public void stop() {
        // ADDED: without this, DriveTrain.stop() never saves the pose.
        robot.stop();
    }

}