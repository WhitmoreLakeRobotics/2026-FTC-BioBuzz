package org.firstinspires.ftc.teamcode.autons;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Hardware.Robot;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;

//import java.nio.file.Path;


public class ppstarterAutonRed extends OpMode {

    Robot robot = new Robot();

    private stage currentStage = stage._00_unknown;

    //in pp v.3.x we use the pose factory were we can specify that all heading values are in
    //  degrees so no need to do a math to convert from degrees to radians that we did last year.
    public static final PoseFactory P = PoseFactory.degrees();

    public Follower follower;
    public static Pose Rstartpose = P.of(85, 130, 270);
    public static Pose CP1 = P.of(12,38, 150);
    public static Pose RscorePose = P.of(55,11, 90);
    public static Pose Park  = P.of(12, 91, 75);

    //private Path startPath, parkPath;


    public Path getstartPath() {
        return line(Rstartpose,CP1).linear(Rstartpose, CP1);

    }

    public Path getparkPath() {
        return line (CP1,Park).linear(CP1,Park);
    }





    @Override
    public void init() {
        robot.init();
        robot.driveTrain.cmdOpmodestartPose(Rstartpose);





    }

  public void init_loop() {

    }


    public void start() {



    }


    public void loop() {

        robot.loop();

        switch (currentStage) {
            case _00_unknown:
                currentStage = stage._10_Prestart;
               // follower.followPath(getstartPath(), true);

                break;

                case _10_Prestart:
                    currentStage = stage._20_Driveout;
                    break;

                    case _20_Driveout:
            currentStage = stage._30_Park;
            break;




        }

    }


    public void stop() {

    }

    public enum stage {

        _00_unknown,
        _10_Prestart,
        _20_Driveout,
        _30_Park,
        _40_End;



    }

}
