package org.firstinspires.ftc.teamcode.SCRIM.autons;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.api.Paths;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.SCRIM.Hardware.S_Robot;

import com.pedropathing.paths.Path;

// Starts next to on the right side of the upper red flower goes a short distance up and go to
// flower then parks on the upper red loading zone
@Autonomous(name = "Scrim_RedU_Flwr_Inpark", group = "PP")
public class S_RedU_Flwr_InPark extends OpMode {

    S_Robot S_Robot = new S_Robot();
    private TelemetryManager telemetryM;

    public static final PoseFactory pf = PoseFactory.degrees();
    public static Pose startPose = pf.of(60, 132, 270);
    public static Pose Scorepose = pf.of(60, 120, 275);
    public static Pose Parkpose = pf.of(17, 112, 295);
    public static Pose Flowerp2 = pf.of(56, 128, 160);

    public Path Score1() {
        return Paths.line(startPose, Scorepose).linear(startPose, Scorepose);

    }

    public Path FlowerP() {
        return Paths.line(Scorepose, Flowerp2).linear(Scorepose, Flowerp2);
    }

    public Path Score2() {
        return Paths.line(Flowerp2, Scorepose).linear(Flowerp2, Scorepose);
    }

    public Path Park() {
        return Paths.line(Scorepose, Parkpose).linear(Scorepose, Parkpose);

    }

    protected Command UBcycle1() {
        return sequential(
                S_Robot.S_DriveTrain.cmdAtFollow(Score1()),
                S_Robot.S_DriveTrain.cmdAtFollow(FlowerP()),
                S_Robot.S_DriveTrain.cmdAtFollow(Score2()),
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
        S_Robot.S_DriveTrain.cmdOpmodestartPose(startPose);

    }

    @Override
    public void init_loop() {
        S_Robot.init_loop();

    }

    @Override
    public void start() {
        S_Robot.start();
        auto = UBcycle1();
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
