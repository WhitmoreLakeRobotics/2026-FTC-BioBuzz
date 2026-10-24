package org.firstinspires.ftc.teamcode.Comp.autons;

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

//starts lower facing hive then Tries to launch then drives by garden to park on the lower
//side of the red lower loading zone
@Autonomous(name = "RedL1cycle_Park", group = "")
public class RedL1cycle_Park extends OpMode {

    Robot robot = new Robot();
    private TelemetryManager telemetryM;
    public static final PoseFactory pf = PoseFactory.degrees();

    public static Pose Startpose = pf.of(56, 10, 90);
    public static Pose scorePose = pf.of(60, 20, 95);
    public static Pose Pickupgarden = pf.of(9, 10, 270);
    public static Pose Park = pf.of(13, 90, 260);

    public Path Pickup() {
        return Paths.line(Startpose, Pickupgarden).linear(Startpose, Pickupgarden);
    }

    public Path Launchp2() {
        return Paths.line(Pickupgarden, scorePose).linear(Pickupgarden, scorePose);
    }

    public Path DrivetoPark() {
        return Paths.line(scorePose, Park).linear(scorePose, Park);
    }

    protected Command Onecycle() {
        return sequential(
                robot.driveTrain.cmdAtFollow(Pickup()),
                robot.driveTrain.cmdAtFollow(Launchp2()),
                robot.driveTrain.cmdAtFollow(DrivetoPark())
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
        robot.driveTrain.cmdOpmodestartPose(Startpose);

    }


    @Override
    public void init_loop() {
        robot.init_loop();

    }

    @Override
    public void start() {
        robot.start();
        auto = Onecycle();
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
