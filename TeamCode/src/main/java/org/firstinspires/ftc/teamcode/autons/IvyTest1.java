package org.firstinspires.ftc.teamcode.autons;

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
import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;

public class IvyTest1 extends OpMode{


    Robot robot = new Robot();

    public Follower follower;

    private stage Currentstage = stage ._00_unknown;

    public static final PoseFactory P = PoseFactory.degrees();


    public static Pose startpose = P.of (40,8,90);
    public static Pose endscore = P.of (93,25, 90);

/*
    public Path basicpath(){
        return line(startpose,endscore).

    }
    
 */

    @Override
    public void init(){


    }


    @Override
    public void init_loop(){


    }


    @Override
    public void start(){


    }

    @Override
    public void loop(){


    }


    @Override
    public void stop(){



    }

    public enum stage{
        _00_unknown,

    }
}
