package org.firstinspires.ftc.teamcode.autons;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Hardware.Robot;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.util.ElapsedTime;

public class ppRedparka extends OpMode {

    Robot robot = new Robot();


    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime timerun = new ElapsedTime();

    public stage Currentstage = stage ._00_unkown;

    private Follower follower;

    public static final PoseFactory P = PoseFactory.degrees();

    private static Pose StartPose = P.of (61, 131, 90);
    private static Pose Cp2 = P.of(27,70, 135);
    private static Pose Parkred = P.of (15,90, 180);


    private Path park() {
        return curve(StartPose, Cp2, Parkred).linear(StartPose, Parkred);
    }


    @Override
    public void init() {

    }

    public void init_loop(){


    }

    @Override
    public void start(){


    }

    @Override
    public void loop(){

        switch (Currentstage) {
            case _00_unkown:
                Currentstage = stage._10_prestart;
                break;

                case _10_prestart:
                Currentstage = stage._20_DrivetoPark;
                break;

                case _20_DrivetoPark:
                    if (follower.isBusy()) {
                        follower.follow(park());
                        Currentstage = stage._30_End;
                    }

                    case _30_End:
                        if (!follower.isBusy()) {
                            stop();
                            runtime.reset();
                        }
        }


    }

    public void stop(){


    }

    public enum stage {

        _00_unkown,
        _10_prestart,
        _20_DrivetoPark,
        _30_End;

    }
}
