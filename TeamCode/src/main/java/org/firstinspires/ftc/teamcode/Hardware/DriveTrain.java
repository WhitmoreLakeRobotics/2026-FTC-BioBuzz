package org.firstinspires.ftc.teamcode.Hardware;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.ivy.pedro.PedroCommands;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;


public class DriveTrain extends BaseHardware {

    private Follower follower;

    public final double MAXPOWER = 1.0;
    public final double MINPOWER = -1.0;

    public final double MORNINGSPEED = 0.25;
    public final double NORMALSPEED = 0.95;
    public final double FASTSPEED = 0.85;

    private double RightJoystick_x;
    private double LeftJoystick_x;
    private double LeftJoystick_y;


    @Override
    public void init() {
        // Build the Follower. This connects to the motors and the Pinpoint.
        // hardwareMap must already be set by Robot BEFORE this runs.
        follower = Constants.create(hardwareMap);

    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {
        // CHANGED: set the starting position here, when the driver presses PLAY.
        // By now the autonomous has definitely finished and saved its pose.
        // If there is no saved pose (auto never ran), start at (0, 0, 0)
        Pose savedPose = OpModeStorage.autonomousEndPose;
        if (savedPose == null) {
            savedPose = new Pose(0, 0, 0);
        }
        follower.setPose(savedPose);
        follower.update();
    }

    @Override
    public void loop() {
        // update() reads the Pinpoint (where are we?) and sends the latest
        // powers to the motors. It MUST run every single loop.
        follower.update();
    }


    @Override
    public void stop() {
        OpModeStorage.autonomousEndPose = follower.pose();
    }

    public void cmdTeleOp(double Left_Y, double Left_X, double Right_X, double Current_Speed) {
        //Current_Speed is to make adjustments to the speed if button pushed for slow or fast.
        LeftJoystick_x = -Left_X * Current_Speed;
        LeftJoystick_y = Left_Y * Current_Speed;
        RightJoystick_x = Right_X * Current_Speed;

        doTeleop();
    }

    private void doTeleop() {
        // Field-centric: "stick forward" always means "away from the driver",
        // no matter which way the robot is facing. Uses the Pinpoint heading.
        DrivePowers powers = ManualDrive.fieldCentric(
                LeftJoystick_y,
                LeftJoystick_x,
                RightJoystick_x,
                follower.pose().heading()
        );
        follower.manual(powers);
        // follower.manual(LeftJoystick_y, LeftJoystick_x, RightJoystick_x);
        // TESTING TIP: if field-centric acts strange, comment out the lines
        // above and use robot-centric instead (forward = robot's front):
        // follower.manual(LeftJoystick_y, LeftJoystick_x, RightJoystick_x);
    }

    public Pose getPose() {
        return follower.pose();
    }

    public void cmdOpmodestartPose(Pose op_start) {
        follower.setPose(op_start);
    }

    // use this to test for the path being done in auton's
    // CHANGED: isBusy() is TRUE while still driving, so "done" is NOT busy.
    public boolean cmdPathIsDone() {
        return !follower.isBusy();
    }

    public Command cmdAtFollow(Path atPath) {
        return PedroCommands.follow(follower, atPath).requiring(this);
    }
private void doFollow(Path PathIn) {
        follower.follow(PathIn);
    }

}
