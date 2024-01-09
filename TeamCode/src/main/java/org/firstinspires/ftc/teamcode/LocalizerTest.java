package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Config
@Autonomous (name="LocalizerTest", group = "Concept")
public class LocalizerTest extends LinearOpMode {
    private RobotHardware robot;

    @Override public void runOpMode() {
        robot = new RobotHardware(this);
        robot.init();
        waitForStart();
        while (opModeIsActive())
        {

            robot.localizer.telemetry();

            robot.localizer.update();

            TelemetryPacket packet = new TelemetryPacket();
            FtcDashboard dashboard = FtcDashboard.getInstance();

            robot.localizer.drawRobot(packet.fieldOverlay());
            dashboard.sendTelemetryPacket(packet);
        }
    }
}