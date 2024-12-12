
package org.firstinspires.ftc.teamcode;


import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotHardware;

@Disabled
    @com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "testteleop", group = "Linear Opmode")
    public class TestTeleOp extends LinearOpMode {

        RobotHardware robot = new RobotHardware(this);
        private ElapsedTime runtime = new ElapsedTime();



        @Override
        public void runOpMode() {

            robot.init();

            telemetry.addData("Status", "Initialized");
            telemetry.update();

            waitForStart();
            runtime.reset();

            while (opModeIsActive()) {

                robot.teleOp();
                telemetry.update();
            }
    }
}
