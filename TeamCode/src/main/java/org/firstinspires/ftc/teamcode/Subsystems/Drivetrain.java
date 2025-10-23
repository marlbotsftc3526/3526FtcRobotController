package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


import java.util.Locale;

public class Drivetrain {

        /* Declare OpMode members. */
        private OpMode myOpMode = null;   // gain access to methods in the calling OpMode.

        //TODO: Declare OpMode member for the Odometry System
        // If using GoBilda Pinpoint computer then use PinPointLocalizer class
        // If using Sparkfun OTOS then use SparfunLocalizer class


        ElapsedTime time = new ElapsedTime();

        //drivetrain motors
        public DcMotor rightFrontDrive = null;
        public DcMotor leftFrontDrive = null;
        public DcMotor rightBackDrive = null;
        public DcMotor leftBackDrive = null;


        public boolean targetReached = false;

        //Static Variables
        //TODO Adjust drive constants based on auto performance




        public Drivetrain(OpMode opmode) {
            myOpMode = opmode;
        }

        public void init() {
            //Initialize PID controllers
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
            //drive train
            double max;

            double leftFrontPower;
            double rightFrontPower;
            double leftBackPower;
            double rightBackPower;

            double drive = -myOpMode.gamepad1.left_stick_y;
            double turn = myOpMode.gamepad1.right_stick_x;
            double strafe = -myOpMode.gamepad1.left_stick_x;

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
                leftFrontDrive.setPower(leftFrontPower);
                rightFrontDrive.setPower(rightFrontPower);
                leftBackDrive.setPower(leftBackPower);
                rightBackDrive.setPower(rightBackPower);
            }
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

