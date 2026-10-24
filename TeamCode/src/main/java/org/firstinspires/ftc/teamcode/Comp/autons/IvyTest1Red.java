package org.firstinspires.ftc.teamcode.Comp.autons;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Comp.Hardware.Robot;

@Autonomous(name = "IvyTest1R", group = "PP")
public class IvyTest1Red extends OpMode {


    Robot robot = new Robot();


    public static final PoseFactory P = PoseFactory.degrees();


    public static Pose startpose = P.of(40, 8, 90);
    public static Pose endscore = P.of(60, 25, 90);
    public static Pose Park = P.of(11, 90, -13);
    public static Pose Controlpoint1 = P.of(18, 50, 77);


    public Path basicpath() {
        return line(startpose, endscore).constant(startpose);

    }

    public Path Park() {
        return curve(endscore, Controlpoint1, Park).linear(endscore, Park);


    }


    private Command autoRoutine() {
        return sequential(
                robot.driveTrain.cmdAtFollow(basicpath()),
                robot.driveTrain.cmdAtFollow(Park())

        );
    }


    @Override
    public void init() {
        robot.hardwareMap = hardwareMap;
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
        schedule(autoRoutine());


    }

    @Override
    public void loop() {
        robot.loop();

        Scheduler.execute();

        telemetry.addData("auto running", autoRoutine().isScheduled());
        telemetry.update();

    }


    @Override
    public void stop() {
        robot.stop();


    }


}
