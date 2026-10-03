package org.firstinspires.ftc.teamcode.autons;


import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Hardware.Robot;
import static com.pedropathing.api.Paths.*;
import static org.firstinspires.ftc.teamcode.autons.ppUstarterBlueA.stage._00_unknown;

import com.pedropathing.paths.Path;

public class ppUstarterBlueA extends OpMode {

    Robot robot = new Robot();

    private stage currentStage = _00_unknown;

    public Follower follower;
    //in pp v.3.x we use the pose factory were we can specify that all heading values are in
    //  degrees so no need to do a math to convert from degrees to radians that we did last year.
    public static PoseFactory P = PoseFactory.degrees();

    public static final Pose Rstartpose = P.of(81, 133, 90);
    public static final Pose CP1 = P.of(105, 103, 135);
    public static final Pose BscorePose = P.of (81, 99, 180);
    public static Pose park = P.of(131, 40, 90);

    public Path DriveOff(){
        return line (Rstartpose, CP1).linear(CP1,park);
    }

    @Override
    public void init() {

    }

    public void init_loop() {

    }

    public void start() {

    }

    public void loop() {
        robot.loop();

    switch(currentStage) {
            case _00_unknown:
                if (follower.isBusy()) {


                }
                currentStage = stage._10_Prestart;
                break;

        }

    }

    public void stop() {

    }

    public enum stage {
        _00_unknown,
        _10_Prestart,
        _20_MoveAway,
        _30_Park,
        _40_End;
    }
}
