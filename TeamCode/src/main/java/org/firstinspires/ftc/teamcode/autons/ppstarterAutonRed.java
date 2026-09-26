package org.firstinspires.ftc.teamcode.autons;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Hardware.Robot;

public class ppstarterAutonRed extends OpMode {

    Robot robot = new Robot();

    private stage currentStage = stage._00_unknown;


    public static Pose Bstartpose = new Pose(85,130, 270);
    public static Pose Park = new Pose (12,91,23 );


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
