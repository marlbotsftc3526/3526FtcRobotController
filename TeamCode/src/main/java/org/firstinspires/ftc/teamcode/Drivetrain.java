package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
public class Drivetrain {
    private LinearOpMode myOpMode = null;   // gain access to methods in the calling OpMode.

    ElapsedTime time = new ElapsedTime();

    //drivetrain
    public DcMotor frontRight = null;
    public DcMotor frontLeft = null;
    public DcMotor backRight = null;
    public DcMotor backLeft = null;

    public static final double DRIVE_KP = 0.01;
    public static final double DRIVE_KI = 0.0;
    public static final double DRIVE_KD = 0;//0.0003;
    public static final double DRIVE_MAX_ACC = 2000;
    public static final double DRIVE_MAX_VEL = 3500;
    public static final double DRIVE_MAX_OUT = 0.95;
    public Drivetrain(LinearOpMode opmode) {
        myOpMode = opmode;
    }

    public void init(){


        frontLeft = myOpMode.hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = myOpMode.hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = myOpMode.hardwareMap.get(DcMotor.class, "backLeft");
        backRight = myOpMode.hardwareMap.get(DcMotor.class, "backRight");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

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
    public void teleOp(){
        //drive train
        double frontLeftPower;
        double frontRightPower;
        double backLeftPower;
        double backRightPower;
        double max;

        double drive = -myOpMode.gamepad1.left_stick_y;
        double turn = myOpMode.gamepad1.right_stick_x;
        double strafe = -myOpMode.gamepad1.left_stick_x;

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

        if (myOpMode.gamepad1.left_bumper) {
            frontLeft.setPower(frontLeftPower / 4);
            frontRight.setPower(frontRightPower / 4);
            backLeft.setPower(backLeftPower / 4);
            backRight.setPower(backRightPower / 4);
        } else if (myOpMode.gamepad1.right_bumper) {
            frontLeft.setPower(2 * frontLeftPower);
            frontRight.setPower(2 * frontRightPower);
            backLeft.setPower(2 * backLeftPower);
            backRight.setPower(2 * backRightPower);
        } else {
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);
        }
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
        stopMotors();

        return;
    }

    public void stopMotors() {
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
}