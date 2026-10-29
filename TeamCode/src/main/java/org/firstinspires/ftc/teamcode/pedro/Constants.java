package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("leftFrontDrive");
        c.frontRightName.set("rightFrontDrive");
        c.backLeftName.set("leftBackDrive");
        c.backRightName.set("rightBackDrive");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });


    //manually measured
    //forward pod (x pod) is 1.5in right of center when viewed from top
    //strafe pod (y pod) is 7in below center when viewed from top
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(0.44164394769142934);
        c.yPodOffset.set(-8.298748346764272);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.3581728691207085);
                Controller secondaryTranslationalForward = Controller.proportional(0.13233530458477546);
                Controller primaryTranslationalLateral = Controller.proportional(0.730386497673605);
                Controller secondaryTranslationalLateral = Controller.proportional(0.2698582946037426);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.018753216053685625));
                c.brake.set(Controller.proportionalFeedforward(0.01594023364563278));

                c.headingFeedback.set(Controller.proportional(5.1301223937952445));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04607419321530579, 0.008465183946790682));

                c.linearBrakeCoefficients.set(Matrix.diag(0.1523767398543297, 0.013267939167269269));
                c.quadraticBrakeCoefficients.set(Matrix.diag(-0.0010605406056916733, 0.0030385941878697943));

                c.maxAchievableForwardVelocity.set(65.12137369042155);
                c.maxAchievableStrafeVelocity.set(1.3073101674443899);
                c.naturalForwardDeceleration.set(64.25812314842864);
                c.naturalStrafeDeceleration.set(77.2121143835228);
            }
    );

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }
}