package org.firstinspires.ftc.teamcode.SCRIM.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.pedropathing.tuning.autotune.Procedure;
import com.pedropathing.tuning.autotune.Tuner;

import org.firstinspires.ftc.teamcode.pedro.procedures.ForesightTuner;
import org.firstinspires.ftc.teamcode.pedro.procedures.MecanumTuner;
import org.firstinspires.ftc.teamcode.pedro.procedures.PinpointTuner;
import org.firstinspires.ftc.teamcode.pedro.procedures.Tests;

public class Tuning {
  //  @Tuner
    public static Procedure pinpointTuner() {
        return new PinpointTuner();
    }
  //  @Tuner
    public static Procedure mecanumTuner() {
        return new MecanumTuner();
    }// Tuners go here
  //  @Tuner
    public static Procedure tests() {
        return new Tests(hardwareMap -> new Mecanum(hardwareMap, S_Constants.drivetrainConfig), (hardwareMap -> new PinpointLocalizer(hardwareMap, S_Constants.localizerConfig)), () -> new Foresight(S_Constants.foresightConfig));
    }

  //  @Tuner
    public static Procedure foresightTuner() {
        return new ForesightTuner((hardwareMap) -> new PinpointLocalizer(hardwareMap, S_Constants.localizerConfig), (hardwareMap) -> new Mecanum(hardwareMap, S_Constants.drivetrainConfig));
    }

}
