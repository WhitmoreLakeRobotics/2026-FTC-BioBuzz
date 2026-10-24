package org.firstinspires.ftc.teamcode.Comp.autons;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


public class ppAutonBase extends OpMode {


    public static Follower follower;
    // Pose factory that reads headings in DEGREES and converts them to radians for Pedro.
// Must be declared ABOVE the poses that use it.
    public static final PoseFactory Pf = PoseFactory.degrees();

    public static Pose Bstartpose = Pf.of(85, 130, 270);
    public static Pose Rstartpose = Pf.of(55, 11, 90);

    public static Pose RLscorePose = Pf.of(56, 16, 268);
    public static Pose RUscorePose = Pf.of(60, 120, 88);

    public static Pose BLscorepose = Pf.of(84, 15, 270);
    public static Pose BUscorepose = Pf.of(84, 117, 91);

    public static Pose Flowerp1 = Pf.of(14, 57, 250);
    public static Pose Flowerp2 = Pf.of(56, 128, 160);
    public static Pose Flowerp3 = Pf.of(130, 84, 73);
    public static Pose Flowerp4 = Pf.of(82, 112, -25);

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
