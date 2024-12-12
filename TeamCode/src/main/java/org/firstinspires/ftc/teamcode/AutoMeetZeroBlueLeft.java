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

public class AutoMeetZeroBlueLeft extends LinearOpMode {

    RobotHardware robot = new RobotHardware(this);
    private ElapsedTime runtime = new ElapsedTime();
    public void strafe(double power){
        robot.drivetrain.frontRight.setPower(power);
        robot.drivetrain.backLeft.setPower(power);
        robot.drivetrain.frontLeft.setPower(-power);
        robot.drivetrain.backRight.setPower(-power);
    }
    public void stoptherobot(){
        robot.drivetrain.frontRight.setPower(0);
        robot.drivetrain.backLeft.setPower(0);
        robot.drivetrain.frontLeft.setPower(0);
        robot.drivetrain.backRight.setPower(0);
    }


    public void runOpMode() {
        robot.init();

        runtime.reset();

        waitForStart();
        runtime.reset();
        while(runtime.seconds() <= 0.75){
            strafe(0.4);
        }
        stoptherobot();
        runtime.reset();
        while(runtime.seconds() <= 15){
            strafe(-0.6);
        }
        stoptherobot();
        stop();


    }

}