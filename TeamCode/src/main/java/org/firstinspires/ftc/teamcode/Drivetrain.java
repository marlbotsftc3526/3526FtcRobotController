package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;
@Config
public class Drivetrain {
    private LinearOpMode myOpMode = null;   // gain access to methods in the calling OpMode.

    ElapsedTime time = new ElapsedTime();

    //drivetrain
    public DcMotor frontRight = null;
    public DcMotor frontLeft = null;
    public DcMotor backRight = null;
    public DcMotor backLeft = null;

    public SparkfunLocalizer localizer;

    public static double HEADING_KP = 0.012;//0.01
    public static double HEADING_KI = 0.0;
    public static double HEADING_KD = 0.0;
    public static double DRIVE_KP = 0.02; //0.02
    public static double DRIVE_KI = 0.0;
    public static double DRIVE_KD = 0;//0.03;
    public static double DRIVE_MAX_OUT = 0.5;
    public static double S_CNT = 3.5;//4

    PIDController xController;
    PIDController yController;
    PIDController headingController;

    public boolean targetReached = false;
    Pose2D targetPose;
    public Drivetrain(LinearOpMode opmode) {
        myOpMode = opmode;
    }

    public void init(){


        frontLeft = myOpMode.hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = myOpMode.hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = myOpMode.hardwareMap.get(DcMotor.class, "backLeft");
        backRight = myOpMode.hardwareMap.get(DcMotor.class, "backRight");

        xController = new PIDController(DRIVE_KP,DRIVE_KI,DRIVE_KD, DRIVE_MAX_OUT);
        yController = new PIDController(DRIVE_KP,DRIVE_KI,DRIVE_KD,DRIVE_MAX_OUT);
        headingController = new PIDController(HEADING_KP,HEADING_KI,HEADING_KD,DRIVE_MAX_OUT);

        localizer = new SparkfunLocalizer(myOpMode);

        localizer.init();

        //TODO Set the offset of the localizer sensor
        SparkFunOTOS.Pose2D offset = new SparkFunOTOS.Pose2D(7, 0, 0);
        localizer.myOtos.setOffset(offset);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        resetEncoders();

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        myOpMode.telemetry.addData(">", "Drivetrain Initialized");
    }
    public void resetEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void useEncoders() {
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void teleOp() {
        localizer.update();
        useEncoders();
        //drive train
        double frontLeftPower;
        double frontRightPower;
        double backLeftPower;
        double backRightPower;
        double max;
        double drive;
        double turn;
        double strafe;

        if(!myOpMode.gamepad2.left_bumper){
            turn = myOpMode.gamepad2.right_stick_x;
        }else{
            turn = myOpMode.gamepad2.right_stick_x/6.5;
        }

        if(!myOpMode.gamepad2.left_bumper) {
            strafe = -myOpMode.gamepad2.left_stick_x;
            drive = -myOpMode.gamepad2.left_stick_y;
        }else{
            strafe = 0;
            drive = 0;
        }
        frontLeftPower = (drive + turn - strafe);

        frontRightPower = (drive - turn + strafe);
        backLeftPower = (drive + turn + strafe) ;
        backRightPower = (drive - turn - strafe);
        max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        if (max > 1.0) {
            frontLeftPower  /= max;
            frontRightPower /= max;
            backLeftPower   /= max;
            backRightPower  /= max;
        }
        frontLeft.setPower(frontLeftPower*0.9);
        frontRight.setPower(frontRightPower*0.9);
        backLeft.setPower(backLeftPower*0.9);
        backRight.setPower(backRightPower*0.9);

        myOpMode.telemetry.addData("frontLeftPower", frontLeftPower);

        /*if (myOpMode.gamepad2.left_bumper) {
            frontLeft.setPower(frontLeftPower / 4);
            frontRight.setPower(frontRightPower / 4);
            backLeft.setPower(backLeftPower / 4);
            backRight.setPower(backRightPower / 4);
        } else if (myOpMode.gamepad2.right_bumper) {
            frontLeft.setPower(2 * frontLeftPower);
            frontRight.setPower(2 * frontRightPower);
            backLeft.setPower(2 * backLeftPower);
            backRight.setPower(2 * backRightPower);
        } else {
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);
        }*/
    }
    public void encoderTurn(float inches, double power) {
        final double WHEEL_DIAMETER = 4;
        final double COUNTS_PER_INCH = 537.6 / (Math.PI * WHEEL_DIAMETER);
        final int STRAIGHT_COUNTS = (int) (COUNTS_PER_INCH * inches * -1);
        while (Math.abs(frontRight.getCurrentPosition()) < Math.abs(STRAIGHT_COUNTS)) {
            turn(power);
            myOpMode.telemetry.addData("STRAIGHT_COUNTS", STRAIGHT_COUNTS);
            myOpMode.telemetry.addData("POSITION", frontRight.getCurrentPosition());
            myOpMode.telemetry.update();
        }
        stop();

        return;
    }

    public void update(){

        //Use PIDs to calculate motor powers based on error to targets
        double xPower = xController.calculate(targetPose.getX(DistanceUnit.INCH), localizer.getX());
        double yPower = yController.calculate(targetPose.getY(DistanceUnit.INCH), localizer.getY());

        double wrappedAngleError = angleWrap(targetPose.getHeading(AngleUnit.DEGREES) - localizer.getHeading());
        double tPower = headingController.calculate(wrappedAngleError);

        double radianHeading = Math.toRadians(localizer.getHeading());

        //rotate the motor powers based on robot heading
        double xPower_rotated = xPower * Math.cos(-radianHeading) - yPower * Math.sin(-radianHeading);
        double yPower_rotated = xPower * Math.sin(-radianHeading) + yPower * Math.cos(-radianHeading);

        // x, y, theta input mixing to deliver motor powers
        frontLeft.setPower(xPower_rotated - S_CNT*yPower_rotated - tPower);
        backLeft.setPower(xPower_rotated + S_CNT*yPower_rotated - tPower);
        frontRight.setPower(xPower_rotated + S_CNT*yPower_rotated + tPower);
        backRight.setPower(xPower_rotated - S_CNT*yPower_rotated + tPower);

        //check if drivetrain is still working towards target
        targetReached = (xController.targetReached && yController.targetReached && headingController.targetReached);
        String data = String.format(Locale.US, "{tX: %.3f, tY: %.3f, tH: %.3f}", targetPose.getX(DistanceUnit.INCH), targetPose.getY(DistanceUnit.INCH), targetPose.getHeading(AngleUnit.DEGREES));

        myOpMode.telemetry.addData("Target Position", data);
        myOpMode.telemetry.addData("XReached", xController.targetReached);
        myOpMode.telemetry.addData("YReached", yController.targetReached);
        myOpMode.telemetry.addData("HReached", headingController.targetReached);
        myOpMode.telemetry.addData("targetReached", targetReached);
        myOpMode.telemetry.addData("xPower", xPower);
        myOpMode.telemetry.addData("xPowerRotated", xPower_rotated);
        localizer.update();
    }

    public void setTargetPose(Pose2D newTarget){
        targetPose = newTarget;
        targetReached = false;
    }

    public void setTargetPoseNoReset(Pose2D newTarget){
        targetPose = newTarget;
    }

    public void driveToPose(double xTarget, double yTarget, double degreeTarget) {
        //check if drivetrain is still working towards target
        targetReached = xController.targetReached && yController.targetReached && headingController.targetReached;
        //double thetaTarget = Math.toRadians(degreeTarget);
        //Use PIDs to calculate motor powers based on error to targets
        double xPower = xController.calculate(xTarget, localizer.getX());
        double yPower = yController.calculate(yTarget, localizer.getY());

        double wrappedAngleError = angleWrap(degreeTarget - localizer.getHeading());
        double tPower = headingController.calculate(wrappedAngleError);

        double radianHeading = Math.toRadians(localizer.getHeading());

        //rotate the motor powers based on robot heading
        double xPower_rotated = xPower * Math.cos(-radianHeading) - yPower * Math.sin(-radianHeading);
        double yPower_rotated = xPower * Math.sin(-radianHeading) + yPower * Math.cos(-radianHeading);

        // x, y, theta input mixing to deliver motor powers
        frontLeft.setPower(xPower_rotated - yPower_rotated - tPower);
        backLeft.setPower(xPower_rotated + yPower_rotated - tPower);
        frontRight.setPower(xPower_rotated + yPower_rotated + tPower);
        backRight.setPower(xPower_rotated - yPower_rotated + tPower);
    }




    public void stop() {
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
    }
    public void turn(double power) {
        frontRight.setPower(power);
        frontLeft.setPower(-power);
        backRight.setPower(power);
        backLeft.setPower(-power);
    }
    public double angleWrap(double degrees) {

        while (degrees > 180) {
            degrees -= 360;
        }
        while (degrees < -180) {
            degrees += 360;
        }

        // keep in mind that the result is in degrees
        return degrees;
    }
}