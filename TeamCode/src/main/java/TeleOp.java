
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.List;
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Teleop", group="Linear OpMode")

public class TeleOp extends LinearOpMode{
    ElapsedTime loopTimer;


    @Override
    public void runOpMode() {
        //calling constructor
        RobotHardware robot = new RobotHardware(this);
        loopTimer = new ElapsedTime();
        //calling init function
        robot.init();
        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Waiting for Start");
        telemetry.update();



        /*if(myOpMode.gamepad2.left_bumper){
            robot.drivetrain.side = Drivetrain.SideMode.BLUE;
        }else if(myOpMode.gamepad2.right_bumper){
            robot.drivetrain.side = Drivetrain.SideMode.RED;
        }*/

        waitForStart();




        //set bulk reads
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }


        loopTimer.reset();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("loop time", loopTimer.milliseconds());
            loopTimer.reset();
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
