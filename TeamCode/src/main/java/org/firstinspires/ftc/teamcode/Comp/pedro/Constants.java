package org.firstinspires.ftc.teamcode.Comp.pedro;

import com.pedropathing.algorithm.Foresight;             // ADDED
import com.pedropathing.algorithm.ForesightConfig;       // ADDED
import com.pedropathing.controllers.Controller;          // ADDED
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;                     // ADDED
import com.pedropathing.math.Vector2D;                   // ADDED
import com.pedropathing.revhub.drivetrains.Mecanum;      // ADDED
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer; // ADDED
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    // ---------------------------------------------------------------
    // DRIVETRAIN: which motors are which, and which way they spin.
    // The names ("LDM1", etc.) MUST match the names in the robot
    // configuration on your Driver Hub EXACTLY (capital letters count).
    // ---------------------------------------------------------------
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("LDM1");
        c.frontRightName.set("RDM1");
        c.backLeftName.set("LDM2");
        c.backRightName.set("RDM2");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);
    });

    // ---------------------------------------------------------------
    // LOCALIZER: the Pinpoint odometry computer that tracks where the
    // robot is on the field. "pinpoint" must match the Driver Hub config.
    //
    // CHECK THESE OFFSETS: 0.19" and 0.50" means both pods are almost
    // exactly in the center of the robot. If that's not true, re-run the
    // Pinpoint AutoTuner. Wrong offsets make field-centric drive feel wrong.
    // ---------------------------------------------------------------

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(-1.3966823187400037);
        c.yPodOffset.set(0.11193760736720769);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });
    // ---------------------------------------------------------------
    // ADDED: FORESIGHT is Pedro's path-following "brain". The Follower
    // cannot be built without it, even if you only drive manually.
    //
    // !!! TEMPORARY VALUES !!! These numbers are the example values from
    // the Pedro Pathing docs, NOT your robot. They are only here so the
    // code runs for TeleOp testing. Run the Foresight AutoTuner and paste
    // your own values here before you use Pedro for autonomous paths.
    // ---------------------------------------------------------------

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.3756106647117264);
                Controller secondaryTranslationalForward = Controller.proportional(0.1387781041091769);
                Controller primaryTranslationalLateral = Controller.proportional(0.7082725041248348);
                Controller secondaryTranslationalLateral = Controller.proportional(0.2616877648842624);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.014056781295862273));
                c.brake.set(Controller.proportionalFeedforward(0.011948264101482932));

                c.headingFeedback.set(Controller.proportional(6.8145338806730456));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(-0.012989764307967082, 0.022951060334438885));

                c.linearBrakeCoefficients.set(Matrix.diag(0.110871617833743, 0.07003593470018442));
                c.quadraticBrakeCoefficients.set(Matrix.diag(9.806866373336875E-4, 0.0015279170050780109));

                c.maxAchievableForwardVelocity.set(71.56738972977463);
                c.maxAchievableStrafeVelocity.set(54.3746260863919);
                c.naturalForwardDeceleration.set(33.63473758632306);
                c.naturalStrafeDeceleration.set(72.24037100782935);
            }
    );



    // ---------------------------------------------------------------
    // CHANGED: this used to "return null", which crashed the robot.
    // Now it actually builds the Follower out of the three parts above.
    // ---------------------------------------------------------------
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }


}
