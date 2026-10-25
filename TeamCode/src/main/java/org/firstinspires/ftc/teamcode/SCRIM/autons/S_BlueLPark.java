package org.firstinspires.ftc.teamcode.SCRIM.autons;

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

import org.firstinspires.ftc.teamcode.SCRIM.Hardware.S_Robot;

import com.pedropathing.paths.Path;
//starts in corner and goes to upper side park area blue
@Autonomous(name = "SCRIM_BluelPark", group = "Ivy")
public class S_BlueLPark extends OpMode  {
    S_Robot S_Robot = new S_Robot();
    private TelemetryManager telemetryM;




    public static final PoseFactory pf = PoseFactory.degrees();


    public static Pose startpose = pf.of(133, 8, 180);
    public static Pose parkpose = pf.of(127, 25, 180);


    public Path pthpark() {
        return Paths.line(startpose, parkpose).constant(startpose);
    }



    protected Command startToScore() {
        return sequential(
                S_Robot.S_DriveTrain.cmdAtFollow(pthpark())
        );

    }

    private Command auto;



    @Override
    public void init() {
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        S_Robot.hardwareMap = hardwareMap;
        S_Robot.telemetry = telemetry;
        S_Robot.init();
        Scheduler.reset();
        S_Robot.S_DriveTrain.cmdOpmodestartPose(startpose);

    }


    @Override
    public void init_loop() {
        S_Robot.init_loop();

    }


    @Override
    public void start() {
        S_Robot.start();
        auto = startToScore();
        schedule(auto);


    }

    @Override
    public void loop() {
        S_Robot.loop();
        Scheduler.execute();
        telemetryM.addData("pose", S_Robot.S_DriveTrain.getPose());
        telemetryM.addData("auto running", auto.isScheduled());
        telemetryM.update();   // sends to Panels only

        // Driver Station: only the few things the drive team needs
        telemetry.addData("auto running", auto.isScheduled());
        telemetry.update();
    }


    @Override
    public void stop() {
        S_Robot.stop();


    }

    public enum stage {
        _00_unknown,

    }
}
