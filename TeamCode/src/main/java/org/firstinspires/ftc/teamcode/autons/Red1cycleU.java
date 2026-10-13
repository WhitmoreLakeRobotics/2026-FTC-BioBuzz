package org.firstinspires.ftc.teamcode.autons;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.api.Paths;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Hardware.Robot;

import com.pedropathing.paths.Path;

public class Red1cycleU extends OpMode {

    Robot robot = new Robot();
    private TelemetryManager telemetryM;

    public static final PoseFactory pf = PoseFactory.degrees();
    public static Pose startPose = pf.of (60, 132,270);
    public static Pose Scorepose = pf.of (60,120,275);


    @Override
    public void init() {

    }

    @Override
    public void init_loop() {
        super.init_loop();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {
        super.stop();
    }
}
