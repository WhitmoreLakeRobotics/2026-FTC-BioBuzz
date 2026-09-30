package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;


/**
 * Base class for FTC Team 8492 defined hardware
 */
public abstract class BaseHardware {


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
     */
    public BaseHardware() {

    }

    /**
     * User defined init method
     * <p>
     * This method will be called once when the INIT button is pressed.
     */
    abstract public void init();

    /**
     * User defined init_loop method
     * <p>
     * This method will be called repeatedly when the INIT button is pressed.
     */
    abstract public void init_loop();

    /**
     * User defined start method.
     * <p>
     * This method will be called once when the PLAY button is first pressed.
     */
    abstract public void start();

    /**
     * User defined loop method
     * <p>
     * This method will be called repeatedly in a loop while this op mode is running
     */
    abstract public void loop();

    /**
     * User defined stop method
     * <p>
     * This method will be called when this op mode is first disabled
     * <p>
     * CHANGED: added "public" so Telop (in a different package) can call robot.stop().
     * Every class that extends BaseHardware (Robot, DriveTrain, Intake, Launcher)
     * must now write its stop() as "public void stop()".
     */
    abstract public void stop();
}