package org.firstinspires.ftc.teamcode;

import com.pedropathing.math.Pose;

/**
 * Pedro Pathing doesn't persist the robot's pose between OpModes on its own.
 * ExampleAuto saves its ending pose here in stop(); ExampleTeleOp reads it in init()
 * so TeleOp can start from where autonomous left off.
 */
public class OpModeStorage {
    public static Pose autonomousEndPose = new Pose(0, 0, 0);
}
