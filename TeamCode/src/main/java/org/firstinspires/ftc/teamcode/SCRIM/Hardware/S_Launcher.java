package org.firstinspires.ftc.teamcode.SCRIM.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.Common.CommonLogic;

@Configurable
public class S_Launcher extends S_BaseHardware {

// private DcMotor LM1;
// private DcMotor TransLM2;

    private DcMotorEx LM1;


    private LauncherState launcherState = LauncherState.OFF;


    public final double minPower = -1.0;
    public final double maxPower = 1.0;

    public int RPMoffset = 0;

    public double getBatteryVoltage() {
        return HIVEENERGY.getVoltage();
    }

    private static  double POLLENATING_RPM = 1700;


    // ---------------- STATE MACHINE CONSTANTS ----------------
    public static double RPM_TOLERANCE = 50;           // "at speed" window around the target RPM
    public static double AT_SPEED_HOLD_SEC = 0.15;     // RPM must stay in tolerance this long
    public static double OFF_RPM_THRESHOLD = 50;       // |RPM| below this counts as stopped

    private VoltageSensor HIVEENERGY;
    private static final double NOMINAL_VOLTAGE = 12.0;

    private static final double TicksPerRev = 28;
    // private static final double GearRat = 1;
    // private double rpmToTicksPerSeconds(double rpm) {
    //return (rpm * TicksPerRev) / (60 * GearRat);

    // ---------------- PID CONSTANTS ----------------
    public static double LkP = 0.0002;   // increased for faster recovery
    public static double LkI = 0.0;       // still unused
    public static double LkD = 0.00000; // small D for damping
    public static double kF = 1 / 6000.0; // feedforward per RPM

    // ---------------- PID CONSTANTS ----------------
    public static double bLkP = 0.00070;   // increased for faster recovery
    public static double bLkI = 0.0;       // still unused
    public static double bLkD = 0.0000015; // small D for damping
    public static double bkF = 1.24 / 6000.0; // feedforward per RPM

    private double targetRPM1 = 0;

    // Transfer direction change waiting for the motor to stop first (0 = none pending).

    private final ElapsedTime launcherAtSpeedTimer = new ElapsedTime();



    private double lastError1 = 0;
    private double lastError2 = 0;

    private double integral1 = 0;
    private double integral2 = 0;

    private ElapsedTime timer1 = new ElapsedTime();



    /*private double getCompensatedVelocity(double targetVelocity) {

        double currentVoltage = HIVEENERGY.getVoltage();

        if (currentVoltage <= 0) {
            return targetVelocity;
        }

        return targetVelocity * (NOMINAL_VOLTAGE / currentVoltage);
    }*/

    // private static final double Pollenating = 1.0;
    // private static final double LaurenStop = 0.0;

    // private static final double Pushingspeed = 0.55;
    // private static final double naughtspeed = 0.0;

    @Override
    public void init() {

        // LM1 = hardwareMap.get(DcMotor.class, "LM1");
        // TransLM2 = hardwareMap.get(DcMotor.class, "TransLM2");

        LM1 = hardwareMap.get(DcMotorEx.class, "LM1");


        HIVEENERGY = hardwareMap.voltageSensor.iterator().next();

        LM1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        LM1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);


        // start from a known state
        targetRPM1 = 0;

        launcherState = LauncherState.OFF;


    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {

        runPID();

        updateLauncherState(getMotorRPM(LM1));

        telemetry.addData("RPM offset: ", RPMoffset);
        telemetry.addData("Launcher State", launcherState);


    }

    @Override
    public void stop() {

        targetRPM1 = 0;
        launcherState = LauncherState.OFF;
        LM1.setPower(0);

    }

    // ---------------- COMMANDS ----------------
    // A cmd sets the target and the transition state; loop() decides when we arrive.

    public void cmdLauncherForward() {

        // Same direction as before, so retargeting while spinning (or spinning down) is safe.
        targetRPM1 = POLLENATING_RPM + RPMoffset;

        if (launcherState == LauncherState.OFF || launcherState == LauncherState.SPINNING_DOWN) {
            launcherAtSpeedTimer.reset();
            launcherState = LauncherState.SPINNING_UP;
        }
        // SPINNING_UP / AT_SPEED: keep the state so a held button doesn't reset the debounce.
    }

    public void cmdLauncherStop() {

        targetRPM1 = 0;

        if (launcherState != LauncherState.OFF) {
            launcherState = LauncherState.SPINNING_DOWN;
        }
    }




    /**
     * Sets a new transfer target. Same direction: retarget directly. Opposite direction (or
     * still spinning down): drop the target to 0, remember the request, and let loop() spin up
     * the other way only after the motor has stopped, so we never reverse a spinning motor.
     */


    // ---------------- STATE MACHINE ----------------

    private void updateLauncherState(double rpm) {

        boolean inRange = CommonLogic.inRange(rpm, targetRPM1, RPM_TOLERANCE);

        switch (launcherState) {
            case SPINNING_UP:
                if (!inRange) {
                    launcherAtSpeedTimer.reset();
                } else if (launcherAtSpeedTimer.seconds() >= AT_SPEED_HOLD_SEC) {
                    launcherState = LauncherState.AT_SPEED;
                }
                break;

            case AT_SPEED:
                if (!inRange) {   // e.g. RPM dropped after a shot
                    launcherAtSpeedTimer.reset();
                    launcherState = LauncherState.SPINNING_UP;
                }
                break;

            case SPINNING_DOWN:
                if (Math.abs(rpm) < OFF_RPM_THRESHOLD) {
                    launcherState = LauncherState.OFF;
                }
                break;

            case OFF:
                break;
        }
    }


    // ---------------- STATUS GETTERS ----------------

    public LauncherState getLauncherState() {
        return launcherState;
    }


    public boolean isLauncherAtSpeed() {
        return launcherState == LauncherState.AT_SPEED;
    }

    public double getLauncherVelocity() {
        return LM1.getVelocity();
    }



    public double getMotorRPM(DcMotorEx motor) {

        double ticksPerSecond = motor.getVelocity();

        return
                (ticksPerSecond / TicksPerRev) * 60.0;

    }

    private double pidMotor1(double currentRPM) {
        double error = targetRPM1 - currentRPM;
        double dt = timer1.seconds();
        timer1.reset();

        if (dt <= 0) dt = 0.001;

        double p = LkP * error;

        integral1 += error * dt;
        integral1 = Math.max(-5000, Math.min(5000, integral1));
        double i = LkI * integral1;

        double derivative = (error - lastError1) / dt;
        lastError1 = error;
        double d = LkD * derivative;

        double voltage = Math.max(HIVEENERGY.getVoltage(), 1.0);
        double ff = (targetRPM1 * kF) * (12.0 / voltage);

        return ff + p + i + d;
    }




    public void runPID() {
        double currentRPM1 = getMotorRPM(LM1);
        double power1 = pidMotor1(currentRPM1);
        power1 = Math.max(minPower, Math.min(maxPower, power1));



        // Prevent braking when stopping (test) MJD
        if (targetRPM1 == 0) {
            power1 = 0;
            integral1 = 0;   // reset PID so it doesn't spike later
            lastError1 = 0;
        }


        LM1.setPower(power1);

        telemetry.addData("Launcher RPM", currentRPM1);
        telemetry.addData("Launcher Target", targetRPM1);
        telemetry.addData("Launcher Power", power1);



    }

    // ---------------- ENUMS (keep at the end of the class) ----------------

    /**
     * Launcher motor (LM1) status.
     * A cmd sets the transition state (SPINNING_UP / SPINNING_DOWN);
     * loop() moves to the final state (AT_SPEED / OFF) once the RPM arrives.
     */
    public enum LauncherState {
        OFF,
        SPINNING_UP,
        AT_SPEED,
        SPINNING_DOWN
    }

    /**
     * Transfer motor (TransLM2) status. Same pattern as LauncherState, but the
     * final state also says which direction the motor is running.
     */


}