package org.firstinspires.ftc.teamcode.autons;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Hardware.Robot;

public class ppstarterAutonRed extends OpMode {

    Robot robot = new Robot();

    private stage currentStage = stage._00_unknown;

    //in pp v.3.x we use the pose factory were we can specify that all heading values are in
    //  degrees so no need to do a math to convert from degrees to radians that we did last year.
    public static final PoseFactory P = PoseFactory.degrees();

    public static Pose Bstartpose = P.of(85, 130, 270);
    public static Pose Park       = P.of(12, 91, 23);


    @Override
    public void init() {
        robot.init();
        robot.driveTrain.cmdOpmodestartPose(Bstartpose);


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
