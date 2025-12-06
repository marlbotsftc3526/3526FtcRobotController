package org.firstinspires.ftc.teamcode.OpModes;

import static org.firstinspires.ftc.teamcode.OpModes.PedroAutoBlueSide.HEADING_KEY;
import static org.firstinspires.ftc.teamcode.OpModes.PedroAutoBlueSide.X_POS_KEY;
import static org.firstinspires.ftc.teamcode.OpModes.PedroAutoBlueSide.Y_POS_KEY;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Meet0Teleop", group="Linear OpMode")

public class Meet0Teleop extends LinearOpMode{
    RobotHardware robot;

    // Get the singleton instance of the FtcDashboard
    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    @Override
    public void runOpMode() {
        //calling constructor
        robot = new RobotHardware(this);
        //calling init function
        robot.init();
        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Waiting for Start");
        telemetry.addData("x position", blackboard.get(X_POS_KEY));
        telemetry.addData("y position", blackboard.get(Y_POS_KEY));
        telemetry.addData("heading", blackboard.get(HEADING_KEY));
        telemetry.update();



        /*if(myOpMode.gamepad2.left_bumper){
            robot.drivetrain.side = Drivetrain.SideMode.BLUE;
        }else if(myOpMode.gamepad2.right_bumper){
            robot.drivetrain.side = Drivetrain.SideMode.RED;
        }*/

        waitForStart();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            robot.teleOp();
            telemetry.update();
            /*
            // 2. **Create a new TelemetryPacket**
            // The argument 'true' ensures the default field image is drawn.
            TelemetryPacket packet = new TelemetryPacket(true);

            // 3. **Get the Field Overlay Canvas**
            Canvas fieldOverlay = packet.fieldOverlay();

            // 4. **Set Drawing Styles (Optional but recommended)**
            fieldOverlay.setStroke("red"); // The color for lines/borders
            fieldOverlay.setStrokeWidth(2); // Line thickness in pixels/units

            // 5. **Draw the Robot Position (Circle)**
            // drawCircle(x, y, radius) - all in inches
            double robotX = robot.drivetrain.localizer.getPosX(DistanceUnit.INCH);
            double robotY = robot.drivetrain.localizer.getPosY(DistanceUnit.INCH);
            double robotHeading = robot.drivetrain.localizer.getHeading(AngleUnit.RADIANS);
            fieldOverlay.strokeCircle(robotX, robotY, 9);

            // 6. **Draw the Robot Heading (Line)**
            double x2 = robotX + 9 * Math.cos(robotHeading);
            double y2 = robotY + 9 * Math.sin(robotHeading);

            // drawLine(x1, y1, x2, y2)
            fieldOverlay.setStroke("blue"); // Change color for the line
            fieldOverlay.strokeLine(robotX, robotY, x2, y2);


            // 7. **Add Traditional Telemetry Data (Optional)**
            packet.put("Robot X (in)", String.format("%.2f", robotX));
            packet.put("Robot Y (in)", String.format("%.2f", robotY));

            // 8. **Send the Packet to the Dashboard**
            dashboard.sendTelemetryPacket(packet);

             */
        }
    }
}
