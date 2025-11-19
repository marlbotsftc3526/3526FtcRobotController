package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.control.PIDFController;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.OpModes.GobildaPinPoint;
import org.firstinspires.ftc.teamcode.PinpointLocalizer;
import org.firstinspires.ftc.teamcode.utility.PIDController;


import java.util.Locale;

public class Drivetrain {

        /* Declare OpMode members. */
        private OpMode myOpMode = null;   // gain access to methods in the calling OpMode.
        GoBildaPinpointDriver pinpoint;
        //TODO: Declare OpMode member for the Odometry System
        // If using GoBilda Pinpoint computer then use PinPointLocalizer class
        // If using Sparkfun OTOS then use SparfunLocalizer class

    //TODO Adjust based on desired states
    public enum DrivetrainMode {
        FIELDCENTRIC,
        ROBOTCENTRIC,
    }

        ElapsedTime time = new ElapsedTime();

        //drivetrain motors
        public DcMotor rightFrontDrive = null;
        public DcMotor leftFrontDrive = null;
        public DcMotor rightBackDrive = null;
        public DcMotor leftBackDrive = null;
        public DrivetrainMode drivetrainMode = DrivetrainMode.FIELDCENTRIC;
        public GoBildaPinpointDriver localizer;

    public static double HEADING_KP = 0.05;//0.012 //0.0095
    public static double HEADING_KI = 0.0;
    public static double HEADING_KD = 0.0;
    public static double MAX_OUT = 0.8;


    PIDController headingController;
        //declare PID controller
        //create variable for PID constants (kP, kD...)
        //initialize PID controller with the constants
        //in teleOp loop
            //calculate the motor ouput with the PID controller based on the error between current heading and target heading


    public boolean targetReached = false;

        //Static Variables
        //TODO Adjust drive constants based on auto performance




        public Drivetrain(OpMode opmode) {
            myOpMode = opmode;
        }

        public void init() {
            //Initialize PID controllers
            headingController = new PIDController(HEADING_KP, HEADING_KI, HEADING_KD, MAX_OUT);
            //xController = new RampingController(MAX_SPEED, MIN_SPEED, RAMP_UP_RATE, RAMP_DOWN_RATE, THRESHOLD);
            //yController = new RampingController(MAX_SPEED, MIN_SPEED, RAMP_UP_RATE, RAMP_DOWN_RATE, THRESHOLD);
            //headingController = new RampingController(MAX_SPEED, MIN_SPEED, RAMP_UP_RATE, RAMP_DOWN_RATE, THRESHOLD);

            leftFrontDrive = myOpMode.hardwareMap.get(DcMotor.class, "leftFrontDrive");
            rightFrontDrive = myOpMode.hardwareMap.get(DcMotor.class, "rightFrontDrive");
            leftBackDrive = myOpMode.hardwareMap.get(DcMotor.class, "leftBackDrive");
            rightBackDrive = myOpMode.hardwareMap.get(DcMotor.class, "rightBackDrive");

            leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
            rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
            leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
            rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

            resetEncoders();
            useEncoders();

            pinpoint = myOpMode.hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
            pinpoint.setOffsets(80.0, -160.0, DistanceUnit.MM); //these are tuned for 3110-0002-0001 Product Insight #1

            pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

            pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                    GoBildaPinpointDriver.EncoderDirection.REVERSED);

            //pinpoint.resetPosAndIMU();

            myOpMode.telemetry.addData(">", "Drivetrain Initialized");
        }


        public void resetEncoders() {
            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        public void useEncoders() {
            leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        public void teleOp() {
            pinpoint.update();
            //drive train
            double max;

            double leftFrontPower;
            double rightFrontPower;
            double leftBackPower;
            double rightBackPower;

            double drive = 0;
            double turn = 0;
            double strafe = 0;

            double goalLocationX = 2;
            double goalLocationY = 142;
            double roboLocationX = pinpoint.getPosX(DistanceUnit.INCH);
            double roboLocationY = pinpoint.getPosY(DistanceUnit.INCH);
            double autoAimAngle = 180*Math.atan((goalLocationY - roboLocationY)/(roboLocationX - goalLocationX))/Math.PI;

            if (drivetrainMode == Drivetrain.DrivetrainMode.ROBOTCENTRIC) {
                // Send calculated power to wheels
                drive = -myOpMode.gamepad1.left_stick_y;
                turn = myOpMode.gamepad1.right_stick_x;
                strafe = -myOpMode.gamepad1.left_stick_x;
            } else if (drivetrainMode == Drivetrain.DrivetrainMode.FIELDCENTRIC) {
                drive = -(Math.hypot(-myOpMode.gamepad1.left_stick_x, -myOpMode.gamepad1.left_stick_y) * Math.sin(AngleUnit.normalizeRadians(Math.atan2(-myOpMode.gamepad1.left_stick_y, -myOpMode.gamepad1.left_stick_x) +
                        pinpoint.getHeading(AngleUnit.RADIANS))));
                turn = myOpMode.gamepad1.right_stick_x;
                strafe = -(Math.hypot(-myOpMode.gamepad1.left_stick_x, -myOpMode.gamepad1.left_stick_y) * Math.cos(AngleUnit.normalizeRadians(Math.atan2(-myOpMode.gamepad1.left_stick_y, -myOpMode.gamepad1.left_stick_x) +
                        pinpoint.getHeading(AngleUnit.RADIANS))));
            }
            if(myOpMode.gamepad2.b){
                turn = -headingController.calculate(-autoAimAngle, pinpoint.getHeading(AngleUnit.DEGREES));
            }

            leftFrontPower = (drive + turn - strafe);
            rightFrontPower = (drive - turn + strafe);
            leftBackPower = (drive + turn + strafe);
            rightBackPower = (drive - turn - strafe);

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower /= max;
                rightFrontPower /= max;
                leftBackPower /= max;
                rightBackPower /= max;
            }

            //Slow and Turbo Buttons
            //TODO Adjust factors affecting slow and turbo buttons
            //turbo button (full power)
            if (myOpMode.gamepad1.right_bumper) {
                leftFrontDrive.setPower(leftFrontPower);
                rightFrontDrive.setPower(rightFrontPower);
                leftBackDrive.setPower(leftBackPower);
                rightBackDrive.setPower(rightBackPower);
            }
            //slow button (fraction of full power)
            else if (myOpMode.gamepad1.left_bumper) {
                leftFrontDrive.setPower(leftFrontPower / 4);
                rightFrontDrive.setPower(rightFrontPower / 4);
                leftBackDrive.setPower(leftBackPower / 4);
                rightBackDrive.setPower(rightBackPower / 4);
            }
            //default power
            else {
                leftFrontDrive.setPower(leftFrontPower/1.2);
                rightFrontDrive.setPower(rightFrontPower/1.2);
                leftBackDrive.setPower(leftBackPower/1.2);
                rightBackDrive.setPower(rightBackPower/1.2); ///1.5
            }

            if(myOpMode.gamepad1.dpad_left || myOpMode.gamepad2.dpad_left) {
                drivetrainMode = DrivetrainMode.FIELDCENTRIC;
            } else if (myOpMode.gamepad1.dpad_right || myOpMode.gamepad2.dpad_right){
                drivetrainMode = DrivetrainMode.ROBOTCENTRIC;
            }

            if(myOpMode.gamepad2.dpad_up){

            }
            myOpMode.telemetry.addData("drivetrainMode: ", drivetrainMode);
            myOpMode.telemetry.addData("heading: ", pinpoint.getHeading(AngleUnit.DEGREES));
            myOpMode.telemetry.addData("AutoAim Angle ", autoAimAngle);
            myOpMode.telemetry.addData("roboX: ", roboLocationX);
            myOpMode.telemetry.addData("roboY: ", roboLocationY);
            myOpMode.telemetry.addData("turnPower: ", turn);
        }


        public void stop(){
            leftFrontDrive.setPower(0);
            leftBackDrive.setPower(0);
            rightFrontDrive.setPower(0);
            rightBackDrive.setPower(0);
        }

        // This function normalizes the angle so it returns a value between -180° and 180° instead of 0° to 360°.
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

