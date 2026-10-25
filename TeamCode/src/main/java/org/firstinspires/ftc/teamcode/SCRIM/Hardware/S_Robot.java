package org.firstinspires.ftc.teamcode.SCRIM.Hardware;


import org.firstinspires.ftc.teamcode.Common.CommonLogic;

/**
 * Holds all of Team 8492's hardware and passes each OpMode step
 * (init, init_loop, start, loop, stop) down to every part.
 * <p>
 * CHANGED: removed the duplicate "telemetry" and "hardwareMap" fields.
 * Robot extends BaseHardware, which already has both, so declaring them
 * again here created a second, confusing copy. Telop's
 * "robot.hardwareMap = hardwareMap;" still works exactly the same.
 */
public class S_Robot extends S_BaseHardware {

    public S_DriveTrain S_DriveTrain = new S_DriveTrain();
    public S_Intake S_Intake = new S_Intake();
    public S_Launcher S_Launcher = new S_Launcher();
    public S_TransitionR S_TransitionR = new S_TransitionR();
    public S_Pollenblockerdisabled pollenBlocker = new S_Pollenblockerdisabled();
    public S_Feeder feeder = new S_Feeder();


    /**
     * Called once when the INIT button is pressed.
     */
    @Override
    public void init() {
//checking to make sure running the right code for this robot markerDevice is a device that is unique in each robot
        CommonLogic.WhichRobotCheck(this.hardwareMap,"TransS","Scrimmage");
        // drivetrain
        S_DriveTrain.hardwareMap = this.hardwareMap;
        S_DriveTrain.telemetry = this.telemetry;
        S_DriveTrain.init();

        // Intake
        S_Intake.hardwareMap = this.hardwareMap;
        S_Intake.telemetry = this.telemetry;
        S_Intake.init();

        //Launcher
        S_Launcher.hardwareMap = this.hardwareMap;
        S_Launcher.telemetry = this.telemetry;
        S_Launcher.init();

        //Transition Roller
        S_TransitionR.hardwareMap = this.hardwareMap;
        S_TransitionR.telemetry = this.telemetry;
        S_TransitionR.init();

        //launcher blocker
  //      pollenBlocker.hardwareMap = this.hardwareMap;
  //      pollenBlocker.telemetry = this.telemetry;
    //    pollenBlocker.init();

        //Feeder
        feeder.hardwareMap = this.hardwareMap;
        feeder.telemetry = this.telemetry;
        feeder.init();
    }

    /**
     * Called repeatedly after INIT is pressed, until PLAY.
     */
    @Override
    public void init_loop() {
        S_DriveTrain.init_loop();
        S_Intake.init_loop();
        S_Launcher.init_loop();
        S_TransitionR.init_loop();
      //  pollenBlocker.init_loop();
        feeder.init_loop();
    }

    /**
     * Called once when the PLAY button is pressed.
     */
    @Override
    public void start() {
        S_DriveTrain.start();
        S_Intake.start();
        S_Launcher.start();
        S_TransitionR.start();
 //       pollenBlocker.start();
        feeder.start();
    }

    /**
     * Called repeatedly while the OpMode is running.
     */
    @Override
    public void loop() {
        S_DriveTrain.loop();
        S_Intake.loop();
        S_Launcher.loop();
        S_TransitionR.loop();
   //     pollenBlocker.loop();
        feeder.loop();
    }

    /**
     * Called once when STOP is pressed.
     * CHANGED: added "public" (was just "void stop()").
     */
    @Override
    public void stop() {
        S_DriveTrain.stop();
        S_Intake.stop();
        S_Launcher.stop();
        S_TransitionR.stop();
   //     pollenBlocker.stop();
        feeder.stop();
    }
}
