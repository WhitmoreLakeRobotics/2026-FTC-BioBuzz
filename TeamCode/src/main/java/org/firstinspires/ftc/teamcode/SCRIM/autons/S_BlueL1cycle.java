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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.SCRIM.Hardware.S_Robot;

import com.pedropathing.paths.Path;

@Disabled
@Autonomous(name = "SCRIM_BlueL1cycle", group = "PP")
public class S_BlueL1cycle extends OpMode {

    S_Robot S_Robot = new S_Robot();
    private TelemetryManager telemetryM;

    public stage currentstage = stage._00_unknown;

    private static final PoseFactory pf = PoseFactory.degrees();
    private static Pose startpose = pf.of(82,8,90);
    private static Pose scorePose = pf.of(80,15,87);
    private static Pose flowerp = pf.of(88,13,-60);
    private static Pose backup = pf.of(83,19,-33);
    private static Pose parkpose = pf.of(125,23,75);

    public Path Launch1(){
        return Paths.line(startpose,scorePose).linear(startpose,scorePose);
    }
    public Path Flower(){
        return Paths.line(scorePose,flowerp).linear(scorePose,flowerp);
    }
    public Path Backup(){
        return Paths.line(flowerp,backup).linear(flowerp,backup);
    }
    public Path Launch2(){
        return Paths.line(backup,scorePose).linear(backup,scorePose);
    }
    public Path Park(){
        return Paths.line(scorePose,parkpose).linear(scorePose,parkpose);
    }



    protected Command TwocycleBlue(){
        return sequential(
                S_Robot.S_DriveTrain.cmdAtFollow(Launch1()),
                S_Robot.S_DriveTrain.cmdAtFollow(Flower()),
                S_Robot.S_DriveTrain.cmdAtFollow(Backup()),
                S_Robot.S_DriveTrain.cmdAtFollow(Launch2()),
                S_Robot.S_DriveTrain.cmdAtFollow(Park())

                );
    }

    private Command auto;

    @Override
    public void init(){
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        S_Robot.hardwareMap = hardwareMap;
        S_Robot.telemetry = telemetry;
        S_Robot.init();
        Scheduler.reset();
        S_Robot.S_DriveTrain.cmdOpmodestartPose(startpose);



    }

    @Override
    public void init_loop(){
        S_Robot.init_loop();

    }

    @Override
    public void start(){
        S_Robot.start();
        auto = TwocycleBlue();
        schedule(auto);

    }

    @Override
    public void loop(){
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
    public void stop(){
        S_Robot.stop();


    }

    private enum stage{
        _00_unknown

    }
}
