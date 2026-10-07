package org.firstinspires.ftc.teamcode.autons;

import com.pedropathing.ivy.Command;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Hardware.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import static com.pedropathing.api.Paths.*;


import com.pedropathing.paths.Path;

@Autonomous(name = "IvyTest1R", group = "PP")
public class IvyTest1Red extends OpMode{


    Robot robot = new Robot();

    public Follower follower;

    private stage Currentstage = stage._00_unknown;

    public static final PoseFactory P = PoseFactory.degrees();


    public static Pose startpose = P.of (40,8,90);
    public static Pose endscore = P.of (60,25, 90);
    public static Pose Park = P.of (11,90,-13);
    public static Pose Controlpoint1 = P.of (18,50,77);


    public Path basicpath(){
        return line(startpose,endscore).constant(90);

    }

    public Path Park(){
       return curve(endscore,Controlpoint1,Park).linear(endscore,Park);


    }


    private Command autoRoutine() {
        return sequential(
                follow(follower, basicpath()),
                // Add mechanism commands here.
                follow(follower, Park())

        );
    }
    


    @Override
    public void init(){

        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startpose);



    }


    @Override
    public void init_loop(){


    }


    @Override
    public void start(){

        schedule(autoRoutine());


    }

    @Override
    public void loop(){


        Scheduler.execute();
        follower.update();

        robot.loop();

        switch (Currentstage) {
            case _00_unknown:
                Currentstage = stage._10_prestart;
                break;

            case _10_prestart:
                Currentstage = stage._20_Outdrive;
                break;


                case _20_Outdrive:
                if (follower.isBusy()) {
                    follower.follow(basicpath());
                    Currentstage = stage._30_end;
                }
                break;


                case _30_end:
                    if (!follower.isBusy()) {
                        stop();
                    }

        }

    }


    @Override
    public void stop(){



    }

    private enum stage{
        _00_unknown,
        _10_prestart,
        _20_Outdrive,
        _30_end;

    }
}
