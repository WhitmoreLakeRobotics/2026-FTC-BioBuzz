package org.firstinspires.ftc.teamcode.autons;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Hardware.Robot;

import java.nio.file.Path;


public class ppStarteraBlue {
    Robot robot = new Robot();

    private ppstarterAutonRed.stage currentStage = ppstarterAutonRed.stage._00_unknown;

    //in pp v.3.x we use the pose factory were we can specify that all heading values are in
    //  degrees so no need to do a math to convert from degrees to radians that we did last year.
    public static final PoseFactory P = PoseFactory.degrees();

    public static Pose Bstartpose = P.of(81, 133, 90);
    public static Pose CP1 = P.of();
    public static Pose BscorePose = P.of(81, 99, 180);
    public static Pose Park  = P.of(81, 133, 90);

    private Path startPath, parkPath;

}
