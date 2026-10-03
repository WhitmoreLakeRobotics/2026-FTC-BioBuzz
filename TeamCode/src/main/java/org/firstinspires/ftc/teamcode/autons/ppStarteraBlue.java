package org.firstinspires.ftc.teamcode.autons;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.pedropathing.path gen.Path;
import org.firstinspires.ftc.teamcode.Hardware.Robot;

//import java.nio.file.Path;



public class ppStarteraBlue extends OpMode{
    Robot robot = new Robot();

    private ppStarteraBlue.stage currentStage = stage._00_unknown;

    //in pp v.3.x we use the pose factory were we can specify that all heading values are in
    //  degrees so no need to do a math to convert from degrees to radians that we did last year.
    public static PoseFactory P = PoseFactory.degrees();

    public static final Pose Rstartpose = P.of(81, 133, 90);
    public static final Pose CP1 = P.of(105, 103, 135);
    public static final Pose BscorePose = P.of (81, 99, 180);
    public static Pose park = P.of(131, 40, 90);





@Override
public void init() {






}

public void init_loop() {

}


public void start() {



}


public void loop() {






    }

}


public void stop() {

}

public enum stage {


}

}

