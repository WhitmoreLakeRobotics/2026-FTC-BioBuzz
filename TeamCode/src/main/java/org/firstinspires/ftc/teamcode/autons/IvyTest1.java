package org.firstinspires.ftc.teamcode.autons;

import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Hardware.Robot;
import org.firstinspires.ftc.teamcode.OpModeStorage;

import static com.pedropathing.api.Paths.*;

import android.telecom.TelecomManager;

import com.pedropathing.paths.Path;

import kotlin.PublishedApi;
import kotlin.contracts.Returns;
@Autonomous(name = "IvyTest1", group = "Ivy")
public class IvyTest1 extends OpMode{


    Robot robot = new Robot();
    private TelecomManager telemetryM;

    public Follower follower;

    private stage Currentstage = stage ._00_unknown;

    public static final PoseFactory pf = PoseFactory.degrees();


    public static Pose startpose = pf.of (40,8,90);
    public static Pose endscore = pf.of (93,25, 90);
    public static Pose parkpose = pf.of (8,96,90);
    public static Pose CPparkpath = pf.of (24,39,90);

    public Path pthScore() {
        return Paths.line(startpose,endscore).constant(startpose);
    }
    public Path pthPark() {
        return Paths.curve(endscore,CPparkpath,parkpose).constant(startpose);
    }
protected Command startToScore() {
        return sequential(
                robot.driveTrain.cmdAtFollow(pthScore()),
                robot.driveTrain.cmdAtFollow(pthPark())
        );

    }

/*
    public Path basicpath(){
        return line(startpose,endscore).

    }
    
 */

    @Override
    public void init(){
        robot.hardwareMap = hardwareMap;
        robot.init();
        OpModeStorage.startPose = startpose;
Scheduler.reset();

    }


    @Override
    public void init_loop(){
robot.init_loop();

    }


    @Override
    public void start(){
        robot.start();
schedule(startToScore());

    }

    @Override
    public void loop(){
robot.loop();
Scheduler.execute();
    }


    @Override
    public void stop(){
robot.stop();


    }

    public enum stage{
        _00_unknown,

    }
}
