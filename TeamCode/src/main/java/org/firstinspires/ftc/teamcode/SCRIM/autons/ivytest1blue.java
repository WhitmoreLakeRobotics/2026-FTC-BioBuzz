package org.firstinspires.ftc.teamcode.SCRIM.autons;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.SCRIM.Hardware.Robot;

import com.pedropathing.paths.Path;

@Autonomous(name = "ivytest1blue" , group = "")
public class ivytest1blue extends OpMode {

    Robot robot = new Robot();
    private TelemetryManager telemetryM;


    public static final PoseFactory pf = PoseFactory.degrees();


    public static Pose startpose = pf.of(86, 132, 90);
    public static Pose endscore = pf.of(86, 98, 90);
    public static Pose parkpose = pf.of(14, 91, -90);
    public static Pose CPparkpath = pf.of(14, 48, 0);

    public Path basicpath() {
        return line(startpose, endscore).constant(startpose);

    }

    public Path Park() {
        return curve(endscore, CPparkpath, parkpose).linear(endscore, parkpose);


    }

    protected Command startToScore() {
        return sequential(
                robot.driveTrain.cmdAtFollow(basicpath()),
                robot.driveTrain.cmdAtFollow(Park())
        );


    }
    private Command auto;

    @Override
    public void init() {
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        robot.hardwareMap = hardwareMap;
        robot.telemetry = telemetry;
        robot.init();
        Scheduler.reset();
        robot.driveTrain.cmdOpmodestartPose(startpose);

    }

    @Override
    public void init_loop() {
    robot.init_loop();


    }

    @Override
    public void start() {
    robot.start();
        auto = startToScore();
        schedule(auto);

    }

    @Override
    public void loop() {
    robot.loop();
    Scheduler.execute();
    telemetryM.addData("pose", robot.driveTrain.getPose());
    telemetryM.addData("auto running", auto.isScheduled());
    telemetryM.update();   // sends to Panels only
    }

    @Override
    public void stop() {
    robot.stop();

    }
}