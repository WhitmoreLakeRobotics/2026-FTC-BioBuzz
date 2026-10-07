package org.firstinspires.ftc.teamcode.pedro;

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
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    // ---------------------------------------------------------------
    // LOCALIZER: the Pinpoint odometry computer that tracks where the
    // robot is on the field. "pinpoint" must match the Driver Hub config.
    //
    // CHECK THESE OFFSETS: 0.19" and 0.50" means both pods are almost
    // exactly in the center of the robot. If that's not true, re-run the
    // Pinpoint AutoTuner. Wrong offsets make field-centric drive feel wrong.
    // ---------------------------------------------------------------
  /*  public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(0.09681786139180343);
        c.yPodOffset.set(0.9472080290786863);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });
*/
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(0.1630282026576245);
        c.yPodOffset.set(1.352237190787248);
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
    public static ForesightConfig foresightConfig = new ForesightConfig(c -> {
        Controller primaryTranslationalForward = Controller.proportional(0.3);
        Controller secondaryTranslationalForward = Controller.proportional(0.1);
        Controller primaryTranslationalLateral = Controller.proportional(0.3);
        Controller secondaryTranslationalLateral = Controller.proportional(0.1);

        c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
        c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

        c.coast.set(Controller.proportionalFeedforward(0.010978350889324107));
        c.brake.set(Controller.proportionalFeedforward(0.008731598255925491));

        c.headingFeedback.set(Controller.proportional(5.258721785960744));
        c.headingBrakeCoefficients.set(Vector2D.cartesian(0.05642143125655298, 0.0063829525363003695));

        c.linearBrakeCoefficients.set(Matrix.diag(0.10605894992901523, 0.08719146175596092));
        c.quadraticBrakeCoefficients.set(Matrix.diag(0.0014663966976606565, 0.0013837064502458813));

        c.maxAchievableForwardVelocity.set(72.72923108818539);
        c.maxAchievableStrafeVelocity.set(52.34323936525474);
        c.naturalForwardDeceleration.set(85.01144677379789);
        c.naturalStrafeDeceleration.set(104.49787535782846);
    });

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
