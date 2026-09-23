package org.firstinspires.ftc.teamcode.Hardware;



import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.robotcore.external.Telemetry;


/**
 * Base class for FTC Team 8492 defined hardware
 */
public  class Robot extends BaseHardware{


        public DriveTrain driveTrain = new DriveTrain();
        public Intake intake = new Intake();
        public Launcher launcher = new Launcher();

    /**
     * The {@link #telemetry} field contains an object in which a user may accumulate data which
     * is to be transmitted to the driver station. This data is automatically transmitted to the
     * driver station on a regular, periodic basis.
     */



    public Telemetry telemetry = null;

    /**
     * Hardware Mappings
     */
    public HardwareMap hardwareMap = null; // will be set in Child class


    /**
     * BaseHardware constructor
     * <p>
     * The op mode name should be unique. It will be the name displayed on the driver station. If
     * multiple op modes have the same name, only one will be available.
     */




    /**
     * User defined init method
     * <p>
     * This method will be called once when the INIT button is pressed.
     */
     public void init(){

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


     };

    /**
     * User defined init_loop method
     * <p>
     * This method will be called repeatedly when the INIT button is pressed.
     * This method is optional. By default this method takes no action.
     */
     public void init_loop() {

         driveTrain.init_loop();
         intake.init_loop();
         launcher.init_loop();



     };

    /**
     * User defined start method.
     * <p>
     * This method will be called once when the PLAY button is first pressed.
     * This method is optional. By default this method takes not action.
     * Example usage: Starting another thread.
     */
     public void start() {

         driveTrain.start();
         intake.start();
         launcher.start();


     };

    /**
     * User defined loop method
     * <p>
     * This method will be called repeatedly in a loop while this op mode is running
     */
     public void loop() {

         driveTrain.loop();
         intake.loop();
         launcher.loop();


     };

    /**
     * User defined stop method
     * <p>
     * This method will be called when this op mode is first disabled
     * <p>
     * The stop method is optional. By default this method takes no action.
     */
     void stop() {

         driveTrain.stop();
         intake.stop();
         launcher.stop();


     };
}

