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

//starts in corner and goes to upper side park area blue
@Autonomous(name = "BlueUPark", group = "Ivy")
public class BlueUPark extends OpMode  {
    Robot robot = new Robot();
    private TelemetryManager telemetryM;




    public static final PoseFactory pf = PoseFactory.degrees();


    public static Pose startpose = pf.of(133, 8, 180);
    public static Pose parkpose = pf.of(127, 25, 180);


    public Path pthpark() {
        return Paths.line(startpose, parkpose).constant(startpose);
    }



    protected Command startToScore() {
        return sequential(
                robot.driveTrain.cmdAtFollow(pthpark())
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
