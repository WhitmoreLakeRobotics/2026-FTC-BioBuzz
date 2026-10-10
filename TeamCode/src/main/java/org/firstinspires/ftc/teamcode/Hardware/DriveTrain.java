package org.firstinspires.ftc.teamcode.Hardware;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.ivy.pedro.PedroCommands;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;

//these are needed to draw the path on the field in Panels.
import com.bylazar.field.FieldManager;
import com.bylazar.field.PanelsField;
import java.util.ArrayList;
import java.util.List;


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
    private final TelemetryManager panels = PanelsTelemetry.INSTANCE.getTelemetry();

    //these are needed to draw the path on the field in Panels.
    private final FieldManager field = PanelsField.INSTANCE.getField();
    private final List<Pose> trail = new ArrayList<>();

    private static final double TRAIL_SPACING = 1.0;   // inches between recorded points
    private static final int    TRAIL_MAX     = 600;   // oldest points dropped after this
    private static final double ROBOT_RADIUS  = 9.0;   // inches, ~18" robot

    private double headingOffset = 0;


    @Override
    public void init() {
        // Build the Follower. This connects to the motors and the Pinpoint.
        // hardwareMap must already be set by Robot BEFORE this runs.
        follower = Constants.create(hardwareMap);
//these are needed to draw the path on the field in Panels.
        field.setOffsets(PanelsField.INSTANCE.getPresets().getPEDRO_PATHING());   // Pedro coordinates
        field.setBackground(PanelsField.INSTANCE.getImages().getBIOBUZZ().getDARK()); // or getLIGHT()
    }

    @Override
    public void init_loop() {
follower.update();
        panels.addData("DT pose", follower.pose());
    }

    @Override
    public void start() {
      

        follower.update();
    }

    @Override
    public void loop() {
        // update() reads the Pinpoint (where are we?) and sends the latest
        // powers to the motors. It MUST run every single loop.
        follower.update();
        panels.addData("DT mode", follower.mode());
        panels.addData("DT pathIndex", follower.pathIndex());
        panels.addData("DT pose", follower.pose());
        //these are needed to draw the path on the field in Panels.
        recordTrail();
        drawField();


    }


    @Override
    public void stop() {
        OpModeStorage.autonomousEndPose = follower.pose();
    }

    public void cmdTeleOp(double Left_Y, double Left_X, double Right_X, double Current_Speed) {
        //Current_Speed is to make adjustments to the speed if button pushed for slow or fast.
        LeftJoystick_x = Left_X * Current_Speed;
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
                follower.pose().heading() - headingOffset
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


//****************************************************************************
//these are needed to draw the path on the field in Panels.
    private void recordTrail() {
        Pose p = follower.pose();
        if (trail.isEmpty() || trail.get(trail.size() - 1).distance(p) >= TRAIL_SPACING) {
            trail.add(p);
            if (trail.size() > TRAIL_MAX) trail.remove(0);
        }
    }

    public void resetDriveHeading(){
        headingOffset = follower.pose().heading();


    }

    private void drawField() {
        // Panels only sends the field a few times per second, so skip the work on other loops
        if (!field.getShouldUpdateCanvas()) return;

        // 1. Planned path (the path segment currently being followed), blue
        if (follower.following()) {
            field.setStyle("blue", "blue", 0.5);
            Pose prev = follower.poseAt(0.0);
            for (int i = 1; i <= 20; i++) {
                Pose next = follower.poseAt(i / 20.0);
                field.moveCursor(prev.x(), prev.y());
                field.line(next.x(), next.y());
                prev = next;
            }
        }

        // 2. Where the robot has actually been, red
        field.setStyle("red", "red", 0.5);
        for (int i = 1; i < trail.size(); i++) {
            Pose a = trail.get(i - 1), b = trail.get(i);
            field.moveCursor(a.x(), a.y());
            field.line(b.x(), b.y());
        }

        // 3. The robot: circle plus a line showing which way it faces
        Pose p = follower.pose();
        field.setStyle("transparent", "white", 0.5);
        field.moveCursor(p.x(), p.y());
        field.circle(ROBOT_RADIUS);
        field.line(p.x() + ROBOT_RADIUS * Math.cos(p.heading()),
                p.y() + ROBOT_RADIUS * Math.sin(p.heading()));

        field.update();   // send to Panels
    }
    //************************************************************************
}
