package org.firstinspires.ftc.teamcode.SCRIM.autons;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.SCRIM.Hardware.S_Robot;

//starts in corner and goes to upper side park area red
@Autonomous(name = "Scrim_RedUPark", group = "")
public class S_RedUPark extends OpMode {
    S_Robot S_Robot = new S_Robot();
    private TelemetryManager telemetryM;



        private final PoseFactory Pf = PoseFactory.degrees();


        private final Pose path1Start = Pf .of (8, 133, 0);
        private final Pose path1 = Pf.of(14, 113, 0);

        public Path cool_path() {
            return line(path1Start, path1).linear(path1Start, path1);
        }

    protected Command startToScore() {
        return sequential(
                S_Robot.S_DriveTrain.cmdAtFollow(cool_path())

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
        S_Robot.S_DriveTrain.cmdOpmodestartPose(path1Start);

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


}
