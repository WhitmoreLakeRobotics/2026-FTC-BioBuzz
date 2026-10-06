package org.firstinspires.ftc.teamcode.autons;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Hardware.Robot;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "ppStarteraBlue", group = "PP")
public class ppStarteraBlue extends OpMode{

    Robot robot = new Robot();

    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime timerun = new ElapsedTime();

     private Follower follower;
    private stage currentStage = stage._00_unkown;

    public static final PoseFactory P = PoseFactory.degrees();

    public static Pose BLscorepose = P.of (110, 11, 90 );
    public static Pose Bpark = P.of (128,20, 5);

    public Path BluePark(){
        return line (BLscorepose, Bpark).linear(BLscorepose, Bpark);
    }

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
        switch (currentStage){

            case _00_unkown:
                currentStage = stage._10_Prestart;
                break;

                case _10_Prestart:
                    currentStage = stage._30_Park;
                    break;

             case _30_Park:
                        if (follower.isBusy()) {
                            follower.follow(BluePark());
                            currentStage = stage._30_Park;
                        }
                        break;

                        case _40_End:
                            if (!follower.isBusy()) {
                                stop();
                                runtime.reset();
                            }



        }


    }

    @Override
    public void stop(){


    }

    public enum stage{
        _00_unkown,
        _10_Prestart,
        _30_Park,
        _40_End;

    }
}
