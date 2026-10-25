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

import org.firstinspires.ftc.teamcode.SCRIM.Hardware.S_Robot;

import com.pedropathing.paths.Path;

@Autonomous(name = "SCRIM_ivytest1blue" , group = "")
public class S_ivytest1blue extends OpMode {

    S_Robot S_Robot = new S_Robot();
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
                S_Robot.S_DriveTrain.cmdAtFollow(basicpath()),
                S_Robot.S_DriveTrain.cmdAtFollow(Park())
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
    }

    @Override
    public void stop() {
    S_Robot.stop();

    }
}