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

        robot.drivetrain.localizer.setCoordinates(initx, inity, initheading);
        elapsedTime = new ElapsedTime();
        waitForStart();

        robot.camera.stopStreaming();

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
                tX = 34;
                tY = 28;
                tHeading = Math.PI;
                robot.drivetrain.driveToPose(tX, tY, tHeading);
                robot.drivetrain.stopMotors();
                elapsedTime.reset();

                while(elapsedTime.seconds() < 3) {
                    robot.intake.setPowerPower(-0.3);
                }
                robot.intake.setPowerPower(0);
                tX = 57 ;
                tY = 35;
                tHeading = Math.PI;
                robot.drivetrain.driveToPose(tX, tY, tHeading);
                robot.drivetrain.stopMotors();
                robot.lift.delay.reset();
                robot.lift.boxLeft.setPosition(robot.lift.BOX_score);
                robot.lift.boxRight.setPosition(robot.lift.DIFF1-robot.lift.BOX_score);
                elapsedTime.reset();

                while(elapsedTime.seconds() < 1.5) {
                    robot.lift.liftLeft.setPower(0.3);
                    robot.lift.liftRight.setPower(0.3);
                }
                robot.lift.liftLeft.setPower(0);
                robot.lift.liftRight.setPower(0);
                sleep(1000);
                robot.lift.gateLeft.setPosition(robot.lift.GATE_down);
                robot.lift.gateRight.setPosition(robot.lift.GATE_up);
                sleep(2000);
            }else if(robot.camera.returnSelection() == SimpleVisionProcessor.Selected.RIGHT){

                robot.lift.gateLeft.setPosition(robot.lift.GATE_up);
                robot.lift.gateRight.setPosition(robot.lift.GATE_down);
                tX = 10;
                tY = 26;
                tHeading = Math.PI;
                robot.drivetrain.driveToPose(tX, tY, tHeading);
                robot.drivetrain.stopMotors();
                elapsedTime.reset();

                while(elapsedTime.seconds() < 2) {
                    robot.intake.setPowerPower(-0.3);
                }
                robot.intake.setPowerPower(0);
                tX = 57;
                tY = 20;
                tHeading = Math.PI;
                robot.drivetrain.driveToPose(tX, tY, tHeading);
                robot.drivetrain.stopMotors();
                robot.lift.delay.reset();
                robot.lift.boxLeft.setPosition(robot.lift.BOX_score);
                robot.lift.boxRight.setPosition(robot.lift.DIFF1-robot.lift.BOX_score);
                elapsedTime.reset();

                while(elapsedTime.seconds() < 1.5) {
                    robot.lift.liftLeft.setPower(0.3);
                    robot.lift.liftRight.setPower(0.3);
                }
                robot.lift.liftLeft.setPower(0);
                robot.lift.liftRight.setPower(0);
                sleep(1000);
                robot.lift.gateLeft.setPosition(robot.lift.GATE_down);
                robot.lift.gateRight.setPosition(robot.lift.GATE_up);
                sleep(2000);
            }else{
                robot.lift.gateLeft.setPosition(robot.lift.GATE_up);
                robot.lift.gateRight.setPosition(robot.lift.GATE_down);
                tX = 10;
                tY = 42;
                tHeading = 3*Math.PI/2;
                robot.drivetrain.driveToPose(tX, tY, tHeading);
                robot.drivetrain.stopMotors();
                elapsedTime.reset();

                while(elapsedTime.seconds() < 2) {
                    robot.intake.setPowerPower(-0.3);
                }
                robot.intake.setPowerPower(0);
                tX = 57;
                tY = 28;
                tHeading = Math.PI;
                robot.drivetrain.driveToPose(tX, tY, tHeading);
                robot.drivetrain.stopMotors();
                robot.lift.delay.reset();
                robot.lift.boxLeft.setPosition(robot.lift.BOX_score);
                robot.lift.boxRight.setPosition(robot.lift.DIFF1-robot.lift.BOX_score);
                elapsedTime.reset();

                while(elapsedTime.seconds() < 1.5) {
                    robot.lift.liftLeft.setPower(0.3);
                    robot.lift.liftRight.setPower(0.3);
                }
                robot.lift.liftLeft.setPower(0);
                robot.lift.liftRight.setPower(0);
                sleep(1000);
                robot.lift.gateLeft.setPosition(robot.lift.GATE_down);
                robot.lift.gateRight.setPosition(robot.lift.GATE_up);
                sleep(2000);

            }
            if(gamepad1.b){
                robot.drivetrain.driveToPose(oX, oY, oHeading);
            }
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