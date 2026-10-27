package org.firstinspires.ftc.teamcode.Comp.autons;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Comp.Hardware.Robot;
@Autonomous(name = "BlueLPark",group = "comp")
public class BlueLPark extends OpMode{
    Robot robot = new Robot();
    private final PoseFactory poseFactory = PoseFactory.degrees();
    private TelemetryManager telemetryM;






    private final PoseFactory Pf = PoseFactory.degrees();

    private final Pose start = poseFactory.of(78, 7, 90);
    private final Pose path1 = poseFactory.of(127, 23, 180);
    private final Pose path1Control1 = poseFactory.of(99, 37, 0);

    public Path path1() {
        return curve(start, path1Control1, path1).linear(start, path1);
    }

    private Command auto;



    @Override
    public void init() {
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        robot.hardwareMap = hardwareMap;
        robot.telemetry = telemetry;
        robot.init();
        Scheduler.reset();
        robot.driveTrain.cmdOpmodestartPose(start);

    }


    @Override
    public void init_loop() {
        robot.init_loop();

    }


    @Override
    public void start() {
        robot.start();
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
}
