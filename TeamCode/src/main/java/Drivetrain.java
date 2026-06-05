

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.control.PIDFController;
import com.qualcomm.hardware.limelightvision.LLFieldMap;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
//import org.firstinspires.ftc.teamcode.OpModes.GobildaPinPoint;
//import org.firstinspires.ftc.teamcode.PinpointLocalizer;
//import org.firstinspires.ftc.teamcode.utility.PIDController;


import java.util.List;
import java.util.Locale;
public class Drivetrain {
    ElapsedTime time = new ElapsedTime();
    private OpMode myOpMode = null;   // gain access to methods in the calling OpMode.
    private LimeLight limelight;

    public DcMotor rightFrontDrive = null;
    public DcMotor leftFrontDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor leftBackDrive = null;

    public static double LIMELIGHT_KP = 0.02;//0.015 2/19;
    public static double LIMELIGHT_KI = 0;//0.009 2/19; //0.003
    public static double LIMELIGHT_KD = 0; // 0.0003; 2/19// 0.0001
    public static double MAX_OUT = 0.8;
    public static double drive = 0;
    public static double turn = 0;
    public static double strafe = 0;
    public static double roboLocationX;
    public static double roboLocationY;

    public PIDController limelightController;
    public enum DrivetrainMode{
        MANUAL,
        LIMELIGHT
    }

    public DrivetrainMode drivetrainMode = DrivetrainMode.MANUAL;

    public Drivetrain(OpMode opmode, LimeLight robotLime) {

        myOpMode = opmode;
        limelight = robotLime;
    }
    public boolean targetReached = false;

    public void init(){
        limelightController = new PIDController(LIMELIGHT_KP,LIMELIGHT_KI,LIMELIGHT_KD, MAX_OUT);

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
    }
    public void teleOp(){
        double max;

        double leftFrontPower;
        double rightFrontPower;
        double leftBackPower;
        double rightBackPower;

        drive = myOpMode.gamepad1.left_stick_y;
        turn = myOpMode.gamepad1.right_stick_x*0.8;
        if(drivetrainMode == DrivetrainMode.LIMELIGHT){
            strafe = limelightController.calculate(0, limelight.tx);
        }else{
            strafe = myOpMode.gamepad1.left_stick_x;
        }
        leftFrontPower = (drive + turn - strafe);
        rightFrontPower = (drive - turn + strafe);
        leftBackPower = (drive + turn + strafe);
        rightBackPower = (drive - turn - strafe);

        max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);

        myOpMode.telemetry.addData("tx", limelight.tx);
        myOpMode.telemetry.addData("tagVisible", limelight.targetVisible);
        myOpMode.telemetry.addData("strafe", strafe);

    }
    public void stop(){
        leftFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightFrontDrive.setPower(0);
        rightBackDrive.setPower(0);
    }
}
