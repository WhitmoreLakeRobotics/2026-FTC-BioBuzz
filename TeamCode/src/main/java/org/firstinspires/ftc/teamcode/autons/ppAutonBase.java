package org.firstinspires.ftc.teamcode.autons;

/*import com.bylazar.configurables.PanelsConfigurables;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

 */
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Common.CommonLogic;
import org.firstinspires.ftc.teamcode.Hardware.Robot;



public class ppAutonBase extends OpMode {


    public static Follower follower;
    public static Pose Bstartpose = new Pose(85,130, 270);
    public static Pose Rstartpose = new Pose (55,11, 90);

    public static Pose RLscorePose = new Pose (56,16, 268);
    public static Pose RUscorePose = new Pose (60,120,88);

    public static Pose BLscorepose = new Pose (84, 15, 270 );
    public static Pose BUscorepose = new Pose (84, 117, 91);

     public static Pose Flowerp1 = new Pose (14, 57, 250);
     public static Pose Flowerp2 = new Pose ( 56, 128, 160);
     public static Pose Flowerp3 = new Pose (130, 84, 73);
     public static Pose Flowerp4 = new Pose (82,  112, -25 );

    public void init(){


    }

    public void init_loop(){


    }

    public void start(){


    }

    public void loop(){


    }

    public void stop(){


    }



}
