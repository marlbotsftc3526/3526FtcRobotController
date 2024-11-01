package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

//import org.firstinspires.ftc.teamcode.SimpleVisionProcessor;


@Config
@Autonomous(name = "AutoLeftBlueNew", group = "Linear Opmode")
public class AutoLeftBlueNew extends LinearOpMode {

    private OdoRobotHardware robot;

    public static double tX, tY, tHeading;
    public double oX,oY,oHeading;
    boolean following = false;

    ElapsedTime elapsedTime;

    public static double initx = 10, inity = 65, initheading = 3*Math.PI/2;

    State state;


    enum State{
        TARGET,
        ORIGIN,
        MANUAL
    }

    public void runOpMode() {
        robot = new OdoRobotHardware(this);
        robot.init();

        oX = initx;
        oY = inity;
        oHeading = initheading;
        while (!isStarted()) {
            telemetry.addData("Position: ", robot.camera.returnSelection());
            telemetry.update();
        }

        robot.camera.visionPortalFront.stopStreaming();
        robot.drivetrain.localizer.setCoordinates(initx, inity, initheading);
        elapsedTime = new ElapsedTime();
        waitForStart();

        //robot.camera.stopStreaming();

        if (opModeIsActive()) {

            robot.drivetrain.localizer.telemetry();

            robot.drivetrain.localizer.update();

            TelemetryPacket packet = new TelemetryPacket();
            FtcDashboard dashboard = FtcDashboard.getInstance();

            robot.drivetrain.localizer.drawRobot(packet.fieldOverlay());
            dashboard.sendTelemetryPacket(packet);

            telemetry.update();

            robot.drivetrain.updateDriveConstants();

            //now you can move your robot based on the value of the 'selection' stored in the vision processor
            if(robot.camera.returnSelection() == SimpleVisionProcessor.Selected.LEFT){
                robot.lift.gateLeft.setPosition(robot.lift.GATE_up);
                robot.lift.gateRight.setPosition(robot.lift.GATE_down);
                tX = 35;
                tY = 30;
                tHeading = Math.PI;
                robot.drivetrain.driveToPose(tX, tY, tHeading, 3);
                robot.drivetrain.stopMotors();
                elapsedTime.reset();

                while(elapsedTime.seconds() < 2) {
                    robot.intake.setPowerPower(-0.3);
                }
                robot.intake.setPowerPower(0);
                elapsedTime.reset();
                tX = 36;
                tY = 33.5;
                tHeading = Math.PI+Math.PI/13;
                robot.drivetrain.driveToPose(tX, tY, tHeading, 2);
                robot.driveToAprilTag(1, 0.1);
                robot.drivetrain.stopMotors();
                robot.lift.delay.reset();

            }else if(robot.camera.returnSelection() == SimpleVisionProcessor.Selected.RIGHT){

                robot.lift.gateLeft.setPosition(robot.lift.GATE_up);
                robot.lift.gateRight.setPosition(robot.lift.GATE_down);
                tX = 9.5;
                tY = 30;
                tHeading = Math.PI;
                robot.drivetrain.driveToPose(tX, tY, tHeading, 3);
                robot.drivetrain.stopMotors();
                elapsedTime.reset();

                while(elapsedTime.seconds() < 2) {
                    robot.intake.setPowerPower(-0.3);
                }
                robot.intake.setPowerPower(0);
                tX = 30;
                tY = 28;
                tHeading = Math.PI-Math.PI/13;
                robot.drivetrain.driveToPose(tX, tY, tHeading, 2);
                elapsedTime.reset();
                robot.driveToAprilTag(3, 0.1);
                robot.drivetrain.stopMotors();
                robot.lift.delay.reset();
            }else{
                robot.lift.gateLeft.setPosition(robot.lift.GATE_up);
                robot.lift.gateRight.setPosition(robot.lift.GATE_down);
                tX = 10;
                tY = 39;
                tHeading = 3*Math.PI/2;
                robot.drivetrain.driveToPose(tX, tY, tHeading, 3);
                robot.drivetrain.stopMotors();
                elapsedTime.reset();

                while(elapsedTime.seconds() < 2) {
                    robot.intake.setPowerPower(-0.3);
                }
                robot.intake.setPowerPower(0);
                tX = 30;
                tY = 28;
                tHeading = Math.PI+Math.PI/17;
                robot.drivetrain.driveToPose(tX, tY, tHeading, 2);
                robot.driveToAprilTag(2, 0.1);
                robot.drivetrain.stopMotors();
                robot.lift.delay.reset();

            }
            robot.lift.boxLeft.setPosition(robot.lift.BOX_score);
            robot.lift.boxRight.setPosition(robot.lift.DIFF1-robot.lift.BOX_score);
            elapsedTime.reset();

            while(elapsedTime.seconds() < 0.8) {
                robot.lift.liftLeft.setPower(0.3);
                robot.lift.liftRight.setPower(0.3);
            }
            elapsedTime.reset();
            while(elapsedTime.seconds() < 1.5){
                robot.drivetrain.frontLeft.setPower(0.2);
                robot.drivetrain.frontRight.setPower(0.2);
                robot.drivetrain.backLeft.setPower(0.2);
                robot.drivetrain.backRight.setPower(0.2);
            }
            robot.lift.liftLeft.setPower(0);
            robot.lift.liftRight.setPower(0);
            sleep(1000);
            robot.lift.gateLeft.setPosition(robot.lift.GATE_down);
            robot.lift.gateRight.setPosition(robot.lift.GATE_up);
            sleep(1000);
            robot.lift.boxLeft.setPosition(robot.lift.BOX_intake);
            robot.lift.boxRight.setPosition(robot.lift.DIFF1-robot.lift.BOX_intake);
            sleep(2000);
            elapsedTime.reset();
            while(elapsedTime.seconds() < 0.2 && opModeIsActive()){
                robot.drivetrain.frontLeft.setPower(-0.5);
                robot.drivetrain.frontRight.setPower(-0.5);
                robot.drivetrain.backLeft.setPower(-0.5);
                robot.drivetrain.backRight.setPower(-0.5);
            }

            elapsedTime.reset();
            while(elapsedTime.seconds() < 0.9 && opModeIsActive()){
                robot.drivetrain.frontLeft.setPower(-0.7);
                robot.drivetrain.frontRight.setPower(0.7);
                robot.drivetrain.backLeft.setPower(0.7);
                robot.drivetrain.backRight.setPower(-0.7);
            }
            robot.drivetrain.stopMotors();
            sleep(2000);

            /*if(state == State.TARGET) {
                robot.drivetrain.driveToPose(tX, tY, tHeading);
            }else if(state == State.MANUAL){
                robot.drivetrain.teleOp();
            }*/



            //You could also do other stuff afterwards...
            //turn right 90
            //robot.turnCW(0.5,90);

        }
    }
}