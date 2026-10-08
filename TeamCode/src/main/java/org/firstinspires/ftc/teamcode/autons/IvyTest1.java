package org.firstinspires.ftc.teamcode.autons;

import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Hardware.Robot;

import com.pedropathing.paths.Path;

@Autonomous(name = "IvyTest1", group = "Ivy")
public class IvyTest1 extends OpMode {


    Robot robot = new Robot();
    private TelemetryManager telemetryM;

    private stage Currentstage = stage._00_unknown;

    public static final PoseFactory pf = PoseFactory.degrees();


    public static Pose startpose = pf.of(56, 8, 90);
    public static Pose endscore = pf.of(56, 25, 90);
    public static Pose parkpose = pf.of(10, 90, 90);
    public static Pose CPparkpath = pf.of(24, 39, 0);

    public Path pth1Score() {
        return Paths.line(startpose, endscore).constant(startpose);
    }

    public Path pthPark() {
        return Paths.curve(endscore, CPparkpath, parkpose).constant(parkpose);
    }

    protected Command startToScore() {
        return sequential(
                robot.driveTrain.cmdAtFollow(pth1Score()),
                robot.driveTrain.cmdAtFollow(pthPark())
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

        // Driver Station: only the few things the drive team needs
        telemetry.addData("auto running", auto.isScheduled());
        telemetry.update();
    }


    @Override
    public void stop() {
        robot.stop();


    }

    public enum stage {
        _00_unknown,

    }
}
