package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.MotionProfile.motionProfile;
import static org.firstinspires.ftc.teamcode.MotionProfile.motionProfileTime;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
@Config
public class OdoDrivetrain {
    private LinearOpMode myOpMode = null;   // gain access to methods in the calling OpMode.

    Localizer localizer;

    ElapsedTime time = new ElapsedTime();

    //drivetrain
    public DcMotor frontRight = null;
    public DcMotor frontLeft = null;
    public DcMotor backRight = null;
    public DcMotor backLeft = null;

    PIDController xPID;
    PIDController yPID;
    PIDController headingPID;

    public static double HEADING_KP = 0.7;
    public static double HEADING_KI = 0.0;
    public static double HEADING_KD = 0.0;
    public static double DRIVE_KP = 0.07;
    public static double DRIVE_KI = 0.0;
    public static double DRIVE_KD = 0;//0.0003;g
    public static double DRIVE_MAX_ACC = 2000;
    public static double DRIVE_MAX_VEL = 3500;
    public static double DRIVE_MAX_OUT = 0.8;
    public OdoDrivetrain(LinearOpMode opmode) {
        myOpMode = opmode;
    }

    public void init(){
        localizer = new Localizer(myOpMode);
        localizer.init();

        xPID = new PIDController(DRIVE_KP, DRIVE_KI, DRIVE_KD);
        yPID = new PIDController(DRIVE_KP, DRIVE_KI, DRIVE_KD);
        headingPID = new PIDController(HEADING_KP, HEADING_KI, HEADING_KD);

        xPID.maxOut = DRIVE_MAX_OUT;
        yPID.maxOut = DRIVE_MAX_OUT;
        headingPID.maxOut = DRIVE_MAX_OUT;

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

        double drive = -myOpMode.gamepad1.left_stick_y;
        double turn = myOpMode.gamepad1.right_stick_x;
        double strafe = -myOpMode.gamepad1.left_stick_x;

        double denominator = Math.max(Math.abs(drive) + Math.abs(strafe) + Math.abs(turn), 1.5);

        frontLeftPower = (-drive - turn + strafe) / denominator;
        frontRightPower = (-drive + turn - strafe) / denominator;
        backLeftPower = (-drive - turn - strafe) / denominator;
        backRightPower = (-drive + turn + strafe) / denominator;

        if (myOpMode.gamepad1.left_bumper) {
            frontLeft.setPower(frontLeftPower / 7);
            frontRight.setPower(frontRightPower / 7);
            backLeft.setPower(backLeftPower / 7);
            backRight.setPower(backRightPower / 7);
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

    public void driveToPose(double xTarget, double yTarget, double thetaTarget){
        //Use PIDs to calculate motor powers based on error to targets
        double xPower = xPID.calculate(xTarget, localizer.x);
        double yPower = yPID.calculate(yTarget, localizer.y);

        double wrappedAngle = angleWrap(Math.toRadians(thetaTarget)- localizer.heading);
        double tPower = headingPID.calculate(wrappedAngle);

        //rotate the motor powers based on robot heading
        double xPower_rotated = xPower * Math.cos(-localizer.heading) - yPower * Math.sin(-localizer.heading);
        double yPower_rotated = xPower * Math.sin(-localizer.heading) + yPower * Math.cos(-localizer.heading);

        // x, y, theta input mixing
        frontLeft.setPower(-xPower_rotated + yPower_rotated + tPower);
        backLeft.setPower(-xPower_rotated - yPower_rotated + tPower);
        frontRight.setPower(-xPower_rotated - yPower_rotated - tPower);
        backRight.setPower(-xPower_rotated + yPower_rotated - tPower);
    }

    // This function normalizes the angle so it returns a value between -180° and 180° instead of 0° to 360°.
    public double angleWrap(double radians) {

        while (radians > Math.PI) {
            radians -= 2 * Math.PI;
        }
        while (radians < -Math.PI) {
            radians += 2 * Math.PI;
        }

        // keep in mind that the result is in radians
        return radians;
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

    public void updateDriveConstants(){
        xPID.Kp = DRIVE_KP;
        xPID.Ki = DRIVE_KI;
        xPID.Kd = DRIVE_KD;

        yPID.Kp = DRIVE_KP;
        yPID.Ki = DRIVE_KI;
        yPID.Kd = DRIVE_KD;

        headingPID.Kp = HEADING_KP;
        headingPID.Ki = HEADING_KI;
        headingPID.Kd = HEADING_KD;

        xPID.maxOut = DRIVE_MAX_OUT;
        yPID.maxOut = DRIVE_MAX_OUT;
        headingPID.maxOut = DRIVE_MAX_OUT;
    }
}
