package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.teamcode.RobotHardware;

import java.util.List;

@Disabled
@Autonomous(name = "AutoMeetZeroBlueLeft", group = "Linear Opmode")

public class AutoMeetOneBlueRight extends LinearOpMode {

    RobotHardware robot = new RobotHardware(this);
    private ElapsedTime runtime = new ElapsedTime();
    public void strafe(double power){
        robot.drivetrain.frontRight.setPower(power);
        robot.drivetrain.backLeft.setPower(power);
        robot.drivetrain.frontLeft.setPower(-power);
        robot.drivetrain.backRight.setPower(-power);
    }
    public void drive(double power){
        robot.drivetrain.frontRight.setPower(power);
        robot.drivetrain.backLeft.setPower(power);
        robot.drivetrain.frontLeft.setPower(power);
        robot.drivetrain.backRight.setPower(power);
    }
    public void turn(double power){
        robot.drivetrain.frontRight.setPower(power);
        robot.drivetrain.backLeft.setPower(-power);
        robot.drivetrain.frontLeft.setPower(-power);
        robot.drivetrain.backRight.setPower(power);
    }


    public void runOpMode() {
        robot.init();

        runtime.reset();

        waitForStart();
        robot.claw.clawOpen.setPosition(0);
        runtime.reset();
        while(runtime.seconds() <= 1){
            strafe(0.4);
        }
        drive(0);
        runtime.reset();
        while(runtime.seconds() <= 2.6){
            robot.lift.liftRight.setPower(0.5);
            robot.lift.liftLeft.setPower(0.5);
        }
        runtime.reset();
        robot.lift.liftRight.setPower(0.3);
        robot.lift.liftLeft.setPower(0.3);
        while(runtime.seconds() <= 0.88){
            drive(0.4);
        }
        drive(0);
        robot.claw.clawPivot.setPosition(0.7);
        runtime.reset();
        while(runtime.seconds() <= 1){

        }
        runtime.reset();
        while(runtime.seconds() <= 1.5){
            robot.lift.liftRight.setPower(-0.2);
            robot.lift.liftLeft.setPower(-0.2);
        }
        robot.claw.clawPivot.setPosition(0.22);
        runtime.reset();
        while(runtime.seconds() <= 0.7) {
            robot.lift.liftRight.setPower(0.5);
            robot.lift.liftLeft.setPower(0.5);
        }
        robot.claw.clawOpen.setPosition(0.45);
        robot.lift.liftRight.setPower(0.3);
        robot.lift.liftLeft.setPower(0.3);
        runtime.reset();
        while(runtime.seconds() <= 0.2){
            drive(-0.5);
        }
        drive(0);
        runtime.reset();
        while(runtime.seconds() <= 1){
            turn(0.5);
        }
        drive(0);
        runtime.reset();
        robot.claw.clawPivot.setPosition(0.7);

        while(runtime.seconds() <= 4){
            strafe(0.4);
        }
        runtime.reset();
        robot.claw.clawOpen.setPosition(0.45);
        while(runtime.seconds() <= 0.8) {
            robot.lift.liftRight.setPower(-0.1);
            robot.lift.liftLeft.setPower(-0.1);
        }
        runtime.reset();
        while(runtime.seconds() <= 3){
            drive(0.2);
        }
        robot.claw.clawOpen.setPosition(0);
        robot.claw.clawPivot.setPosition(0.22);
        runtime.reset();
        while(runtime.seconds() <= 1){
            robot.lift.liftRight.setPower(0.5);
            robot.lift.liftLeft.setPower(0.5);
        }
        robot.lift.liftRight.setPower(0.3);
        robot.lift.liftLeft.setPower(0.3);
        robot.claw.clawOpen.setPosition(0);
        runtime.reset();
        while(runtime.seconds() <= 0.6){
            drive(-0.4);
        }
        runtime.reset();
        robot.claw.clawOpen.setPosition(0.22);
        robot.claw.clawOpen.setPosition(0);
        runtime.reset();
        while(runtime.seconds() <= 1){
            strafe(-0.4);
        }
        robot.claw.clawOpen.setPosition(0);
        runtime.reset();
        while(runtime.seconds() <= 1){
            turn(0.5);
        }
        runtime.reset();
        while(runtime.seconds() <= 1.1){
            strafe(0.6);
        }
        drive(0);
        runtime.reset();
        while(runtime.seconds() <= 2.2){
            robot.lift.liftRight.setPower(0.5);
            robot.lift.liftLeft.setPower(0.5);
        }
        robot.lift.liftRight.setPower(0.3);
        robot.lift.liftLeft.setPower(0.3);
        runtime.reset();
        while(runtime.seconds() <= 2) {
            drive(0.4);
        }
        runtime.reset();
        while(runtime.seconds() <= 0.13){
            drive(-0.1);
        }
        while(runtime.seconds() <= 1.3){

        }
        runtime.reset();
        robot.claw.clawPivot.setPosition(0.7);
        while(runtime.seconds() <= 2){
            robot.lift.liftRight.setPower(-0.1);
            robot.lift.liftLeft.setPower(-0.1);
        }
        while(runtime.seconds() <= 1){
            drive(-0.4);
        }
        while(runtime.seconds() <= 1.5){
            drive(1);
        }
    }

}