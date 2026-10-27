package org.firstinspires.ftc.teamcode.SCRIM.Hardware;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Disabled

public class S_Pollenblockerdisabled extends S_BaseHardware {

    private Servo blocker;



    public Mode CurrentMode = Mode.BLOCKING;

    public static double BLOCK_POSITION = 0.80;
    public static double OPEN_POSITION = 0.20;

    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime timerun = new ElapsedTime();

    @Override
    public void init() {

        blocker = hardwareMap.get(Servo.class, "LauncherBlocker");

        cmdBlock();
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {

        telemetry.addData("Blocker Position", blocker.getPosition());
        telemetry.addData("Blocker State", CurrentMode);

    }

    @Override
    public void stop() {

    }

    public void cmdBlock() {

        CurrentMode = Mode.BLOCKING;

        blocker.setPosition(BLOCK_POSITION);
    }

    public void cmdOpen() {

        CurrentMode = Mode.OPEN;

        blocker.setPosition(OPEN_POSITION);
    }

    public boolean isOpen() {

        return CurrentMode == Mode.OPEN;
    }

    public boolean isBlocking() {
        return CurrentMode == Mode.BLOCKING;
    }

    public double getPosition() {

        return blocker.getPosition();
    }
    public enum Mode {
        BLOCKING,
        OPEN
    }
}