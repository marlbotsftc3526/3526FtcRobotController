package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

//import org.firstinspires.ftc.teamcode.SimpleVisionProcessor;


@Autonomous(name = "multiCameraTest", group = "Linear Opmode")
public class MultiCameraTest extends LinearOpMode {

    OdoRobotHardware robot = new OdoRobotHardware(this);

    public void runOpMode() {
        robot.init();
        robot.camera.aprilTagProcessor.setDecimation(2);
        while (!isStarted()) {
            robot.camera.scanAprilTag(3);
            telemetry.addData("Position: ", robot.camera.returnSelection());
            telemetry.update();
        }
        waitForStart();

        //robot.camera.setActiveCamera(robot.camera.webcam2);
        while (opModeIsActive()) {
            telemetryAprilTag();
            /*//now you can move your robot based on the value of the 'selection' stored in the vision processor
            if(robot.camera.returnSelection() == SimpleVisionProcessor.Selected.LEFT){

            }else if(robot.camera.returnSelection() == SimpleVisionProcessor.Selected.RIGHT){
                //strafe right
                //drive straight
            }else{
                //drive straight ex;
                //robot.encoderDrive(0.5,24,5);
            }

            //You could also do other stuff afterwards...
            //turn right 90
            //robot.turnCW(0.5,90);*/
            telemetry.update();
        }
    }
    private void telemetryAprilTag() {

        List<AprilTagDetection> currentDetections = robot.camera.aprilTagProcessor.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }
    }
}