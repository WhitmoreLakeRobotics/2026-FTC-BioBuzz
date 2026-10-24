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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Comp.Hardware.Robot;

@Disabled
@Autonomous(name = "BlueL1cycle", group = "Comp")
public class BlueL1cycle extends OpMode {

    Robot robot = new Robot();
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
                robot.driveTrain.cmdAtFollow(Launch1()),
                robot.driveTrain.cmdAtFollow(Flower()),
                robot.driveTrain.cmdAtFollow(Backup()),
                robot.driveTrain.cmdAtFollow(Launch2()),
                robot.driveTrain.cmdAtFollow(Park())

                );
    }

    private Command auto;

    @Override
    public void init(){
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        robot.hardwareMap = hardwareMap;
        robot.telemetry = telemetry;
        robot.init();
        Scheduler.reset();
        robot.driveTrain.cmdOpmodestartPose(startpose);



    }

    @Override
    public void init_loop(){
        robot.init_loop();

    }

    @Override
    public void start(){
        robot.start();
        auto = TwocycleBlue();
        schedule(auto);

    }

    @Override
    public void loop(){
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
    public void stop(){
        robot.stop();


    }

    private enum stage{
        _00_unknown

    }
}
