package org.firstinspires.ftc.teamcode.Hardware;


/**
 * Holds all of Team 8492's hardware and passes each OpMode step
 * (init, init_loop, start, loop, stop) down to every part.
 *
 * CHANGED: removed the duplicate "telemetry" and "hardwareMap" fields.
 * Robot extends BaseHardware, which already has both, so declaring them
 * again here created a second, confusing copy. Telop's
 * "robot.hardwareMap = hardwareMap;" still works exactly the same.
 */
public class Robot extends BaseHardware {

    public DriveTrain driveTrain = new DriveTrain();
    public Intake intake = new Intake();
    public Launcher launcher = new Launcher();


    /**
     * Called once when the INIT button is pressed.
     */
    @Override
    public void init() {

        // drivetrain
        driveTrain.hardwareMap = this.hardwareMap;
        driveTrain.telemetry = this.telemetry;
        driveTrain.init();

        // Intake
        intake.hardwareMap = this.hardwareMap;
        intake.telemetry = this.telemetry;
        intake.init();

        //Launcher
        launcher.hardwareMap = this.hardwareMap;
        launcher.telemetry = this.telemetry;
        launcher.init();
    }

    /**
     * Called repeatedly after INIT is pressed, until PLAY.
     */
    @Override
    public void init_loop() {
        driveTrain.init_loop();
        intake.init_loop();
        launcher.init_loop();
    }

    /**
     * Called once when the PLAY button is pressed.
     */
    @Override
    public void start() {
        driveTrain.start();
        intake.start();
        launcher.start();
    }

    /**
     * Called repeatedly while the OpMode is running.
     */
    @Override
    public void loop() {
        driveTrain.loop();
        intake.loop();
        launcher.loop();
    }

    /**
     * Called once when STOP is pressed.
     * CHANGED: added "public" (was just "void stop()").
     */
    @Override
    public void stop() {
        driveTrain.stop();
        intake.stop();
        launcher.stop();
    }
}
