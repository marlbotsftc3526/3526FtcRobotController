package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Config
@Autonomous (name="DriveFunctionTest", group = "Concept")
public class DriveFunctionTest extends LinearOpMode {
    private OdoRobotHardware robot;

    enum State{
        TARGET,
        ORIGIN,
        MANUAL
    }

    @Override public void runOpMode() {
        robot = new OdoRobotHardware(this);
        robot.init();



        robot.drivetrain.localizer.setCoordinates(0, 0, 0);

        while (!isStarted()) {
            robot.camera.scanAprilTag(3);
            telemetry.addData("Position: ", robot.camera.returnSelection());
            telemetry.update();
        }

       // robot.camera.stopColorProcessor();

        waitForStart();

        robot.driveToAprilTag(3,8);
        //try drive to pose
        //try drive to april tag
    }

}