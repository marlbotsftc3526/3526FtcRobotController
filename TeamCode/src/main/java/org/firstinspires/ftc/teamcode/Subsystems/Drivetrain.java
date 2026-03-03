package org.firstinspires.ftc.teamcode.Subsystems;

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
import org.firstinspires.ftc.teamcode.OpModes.GobildaPinPoint;
import org.firstinspires.ftc.teamcode.PinpointLocalizer;
import org.firstinspires.ftc.teamcode.utility.PIDController;


import java.util.List;
import java.util.Locale;

@Config
public class Drivetrain {
    //HI Mr. Witman WAS HERE
    /* Declare OpMode members. */
    private OpMode myOpMode = null;   // gain access to methods in the calling OpMode.
    public GoBildaPinpointDriver pinpoint;
    private LimeLight limelight;
    //TODO: Declare OpMode member for the Odometry System
    // If using GoBilda Pinpoint computer then use PinPointLocalizer class
    // If using Sparkfun OTOS then use SparfunLocalizer class

    //TODO Adjust based on desired states
    public enum DrivetrainMode {
        FIELDCENTRIC,
        ROBOTCENTRIC,
    }
    public static enum SideMode {
        RED,
        BLUE
    }
    public enum KickstandMode{
        EXTENDED,
        RETRACTED
    }


    ElapsedTime time = new ElapsedTime();

    //drivetrain motors
    public DcMotor rightFrontDrive = null;
    public DcMotor leftFrontDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor leftBackDrive = null;
    public Servo kickstandRight = null;
    public Servo kickstandLeft = null;
    public DrivetrainMode drivetrainMode = DrivetrainMode.FIELDCENTRIC;
    public SideMode side = SideMode.BLUE;

    public static double LIMELIGHT_KP = 0.02;//0.015 2/19;
    public static double LIMELIGHT_KI = 0;//0.009 2/19; //0.003
    public static double LIMELIGHT_KD = 0; // 0.0003; 2/19// 0.0001
    public static double HEADING_KP = 0.01;
    public static double HEADING_KI = 0;
    public static double HEADING_KD = 0;
    public static double MAX_OUT = 0.8;
    public static double offset = 0;
    public static double min_turn_speed = 0.1;
    public static double TRANSITION_THRESHOLD = 15;
    public static double LKI_RANGE = 0.008;
    public static double LKI_BASE = 0.009;

    public static double DISTANCE = 0;
    public static double roboLocationX;
    public static double roboLocationY;
    public static boolean INZONE;

    public static double kickEXTENDED = 0.6;
    public static double kickRETRACTED = 0.3;
    public KickstandMode kickstand = KickstandMode.RETRACTED;

    PIDController headingController;
    PIDController limelightTurnController;
        //declare PID controller
        //create variable for PID constants (kP, kD...)
        //initialize PID controller with the constants
        //in teleOp loop
            //calculate the motor ouput with the PID controller based on the error between current heading and target heading

    public Drivetrain(OpMode opmode, LimeLight robotLime) {

        myOpMode = opmode;
        limelight = robotLime;
    }

    public boolean targetReached = false;

        //Static Variables
        //TODO Adjust drive constants based on auto performance


    public void init() {
        //Initialize PID controllers
        headingController = new PIDController(HEADING_KP, HEADING_KI, HEADING_KD, MAX_OUT);

        limelightTurnController = new PIDController(LIMELIGHT_KP,LIMELIGHT_KI,LIMELIGHT_KD, MAX_OUT);

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
        kickstandLeft = myOpMode.hardwareMap.get(Servo.class, "kickstandLeft");
        kickstandRight = myOpMode.hardwareMap.get(Servo.class, "kickstandRight");
        kickstandLeft.setPosition(0);
        kickstandRight.setPosition(0);

        pinpoint.recalibrateIMU();


        myOpMode.telemetry.addData(">", "Drivetrain Initialized");

        if(myOpMode.gamepad2.left_bumper || myOpMode.gamepad2.x){
            side = SideMode.BLUE;
            myOpMode.telemetry.addData(">", "BLUE");
        }else if(myOpMode.gamepad2.right_bumper || myOpMode.gamepad2.b){
            side = SideMode.RED;
            myOpMode.telemetry.addData(">", "RED");
        }

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
        //headingController = new PIDController(HEADING_KP, HEADING_KI, HEADING_KD, MAX_OUT);
        headingController.Kp = HEADING_KP;
        headingController.Ki = HEADING_KI;
        headingController.Kd = HEADING_KD;
        //limelightTurnController = new PIDController(LIMELIGHT_KP,LIMELIGHT_KI,LIMELIGHT_KD, MAX_OUT);
        limelightTurnController.Kp = LIMELIGHT_KP;
        limelightTurnController.Ki = LIMELIGHT_KI;
        limelightTurnController.Kd = LIMELIGHT_KD;

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
        double goalLocationX;
        double goalLocationY;
        double power = 1.6;
        double autoAimAngle = 0;
        roboLocationX = pinpoint.getPosX(DistanceUnit.INCH);
        roboLocationY = pinpoint.getPosY(DistanceUnit.INCH);

        if(side == Drivetrain.SideMode.BLUE){
            goalLocationX = 2;
            if(roboLocationX >= 107){
                goalLocationY = 132;
            }else{
                goalLocationY = 142;
            }



            /*if(roboLocationY >= 110 && roboLocationX >= 40 && roboLocationX < 90){
                goalLocationY = 138;
                myOpMode.telemetry.addData("ZONE 1: ", roboLocationY);
            }else if(roboLocationY >= 90 && roboLocationY < 110 && roboLocationX >= 90){
                goalLocationY = 138;
                myOpMode.telemetry.addData("ZONE 2: ", roboLocationY);
            }else if(roboLocationX >= 110 && roboLocationY >= 90){
                goalLocationY = 133;
                myOpMode.telemetry.addData("ZONE 3: ", roboLocationY);
            }else{
                goalLocationY = 142;

            }*/
            //myOpMode.telemetry.addData("goalLocationY: ", goalLocationY);
            autoAimAngle = angleWrap(Math.toDegrees(Math.atan2(roboLocationY - goalLocationY, roboLocationX-goalLocationX)));
            DISTANCE = Math.sqrt((roboLocationX-15)*(roboLocationX-15)+(128-roboLocationY)*(128-roboLocationY));
        }else if(side == Drivetrain.SideMode.RED){
            goalLocationX = 142;
            if(roboLocationX <= 37){
                goalLocationY = 132;
            }else{
                goalLocationY = 142;
            }

            autoAimAngle = angleWrap(Math.toDegrees(Math.atan2(roboLocationY - goalLocationY, roboLocationX - goalLocationX)));
            DISTANCE = Math.sqrt((128-roboLocationX)*(128-roboLocationX) + (128-roboLocationY)*(128-roboLocationY));
        }

        if(myOpMode.gamepad1.dpad_down && myOpMode.gamepad1.left_bumper){
            kickstand = KickstandMode.EXTENDED;
        }
        else if (myOpMode.gamepad1.dpad_up){
            kickstand = KickstandMode.RETRACTED;
        }

        if(kickstand == KickstandMode.EXTENDED){
            kickstandLeft.setPosition(kickEXTENDED);
            kickstandRight.setPosition(kickEXTENDED);
        }else if (kickstand == KickstandMode.RETRACTED){
            kickstandRight.setPosition(kickRETRACTED);
            kickstandLeft.setPosition(kickRETRACTED);
        }

        /*if(myOpMode.gamepad2.b){
            pinpoint.setHeading(-90,AngleUnit.DEGREES);
            pinpoint.setPosX(72,DistanceUnit.INCH);
            pinpoint.setPosY(72, DistanceUnit.INCH);
        }*/
        if(Math.abs(72-roboLocationX) <= -roboLocationY+36){
            INZONE = true;
        }else if(Math.abs(72-roboLocationX) - 12 <= roboLocationY - 72){
            INZONE = true;
        }else{
            INZONE = false;
        }
        if (drivetrainMode == Drivetrain.DrivetrainMode.ROBOTCENTRIC) {
            // Send calculated power to wheels
            drive = myOpMode.gamepad1.left_stick_y;
            turn = myOpMode.gamepad1.right_stick_x*0.8;
            strafe = myOpMode.gamepad1.left_stick_x;
        } else if (drivetrainMode == Drivetrain.DrivetrainMode.FIELDCENTRIC) {
            if(side == Drivetrain.SideMode.BLUE) {
                drive = -(Math.hypot(-(myOpMode.gamepad2.left_stick_x), -(myOpMode.gamepad2.left_stick_y)) * Math.sin(AngleUnit.normalizeRadians(Math.atan2(-(myOpMode.gamepad2.left_stick_y), -(myOpMode.gamepad2.left_stick_x)) +
                        pinpoint.getHeading(AngleUnit.RADIANS))));
                turn = (myOpMode.gamepad2.right_stick_x)*0.8;
                strafe = -(Math.hypot(-(myOpMode.gamepad2.left_stick_x), -(myOpMode.gamepad2.left_stick_y)) * Math.cos(AngleUnit.normalizeRadians(Math.atan2(-(myOpMode.gamepad2.left_stick_y), -(myOpMode.gamepad2.left_stick_x)) +
                        pinpoint.getHeading(AngleUnit.RADIANS))));
            }else if(side == Drivetrain.SideMode.RED){
                drive = (Math.hypot(-(myOpMode.gamepad2.left_stick_x), -(myOpMode.gamepad2.left_stick_y)) * Math.sin(AngleUnit.normalizeRadians(Math.atan2(-(myOpMode.gamepad2.left_stick_y), -(myOpMode.gamepad2.left_stick_x)) +
                        pinpoint.getHeading(AngleUnit.RADIANS))));
                turn = (myOpMode.gamepad2.right_stick_x)*0.8;
                strafe = (Math.hypot(-(myOpMode.gamepad2.left_stick_x), -(myOpMode.gamepad2.left_stick_y)) * Math.cos(AngleUnit.normalizeRadians(Math.atan2(-(myOpMode.gamepad2.left_stick_y), -(myOpMode.gamepad2.left_stick_x)) +
                        pinpoint.getHeading(AngleUnit.RADIANS))));
            }
        }

        //limelight.result = limelight.limelight.getLatestResult();
        //myOpMode.telemetry.addData("isValid", limelight.result.isValid());
        //MODIFIED FOR LIMELIGHT IN SEPERATE THREAD
        myOpMode.telemetry.addData("tagVisible", limelight.targetVisible);
        myOpMode.telemetry.addData("INZONE", INZONE);
        //limelight.result = limelight.limelight.getLatestResult();
        //myOpMode.telemetry.addData("isValid", limelight.result.isValid());
        /*

        if(limelight.result.isValid()){
            limelight.fiducials = limelight.result.getFiducialResults();
            LLResultTypes.FiducialResult tag = limelight.fiducials.get(0);
            myOpMode.telemetry.addData("tag Distance", tag.getCameraPoseTargetSpace().getPosition().z);
            Pose3D botpose = limelight.result.getBotpose();
            if (botpose != null) {
                double x = botpose.getPosition().x;
                double y = botpose.getPosition().y;
                myOpMode.telemetry.addData("bot pose", "(" + x + ", " + y + ")");
            }
        }

         */

        //myOpMode.telemetry.addData("LIMELIGHT KI", LIMELIGHT_KI);
        if (myOpMode.gamepad2.left_trigger > 0.5 || myOpMode.gamepad1.left_trigger > 0.5) {
            //if(limelight.result.isValid() && Math.abs(limelight.result.getTx()) <= 10 && (DISTANCE >= 75 || roboLocationY >= 115)){
            //if(limelight.targetVisible && Math.abs(limelight.tx) <= 10 && (DISTANCE >= 75 || roboLocationY >= 115)){
            if(roboLocationY <= 60) {
                if(side == Drivetrain.SideMode.RED) {
                    offset = limelight.tx- (-0.083*roboLocationX - 0.024*roboLocationY+5.7);
                }else if(side == Drivetrain.SideMode.BLUE){
                    offset = limelight.tx - (-13.06+0.283*roboLocationX+0.443*roboLocationY-0.0018*roboLocationX*roboLocationX
                            -0.0084*roboLocationY*roboLocationY-0.0012*roboLocationX*roboLocationY);
                }
            }else {
                if(side == Drivetrain.SideMode.RED) {
                    offset = limelight.tx-(33.2402 - 0.7239 * roboLocationX - 0.2277 * roboLocationY + 0.00208 * roboLocationX * roboLocationX
                            - 0.0002 * roboLocationY * roboLocationY + 0.00503 * roboLocationX * roboLocationY);
                }else if(side == Drivetrain.SideMode.BLUE) {
                    offset = limelight.tx-(71.1395-0.297*roboLocationX-0.9632*roboLocationY-0.002*roboLocationX*roboLocationX
                            +0.0054*roboLocationX*roboLocationY+0.002*roboLocationY*roboLocationY);
                }
            }

                /*if(roboLocationY <= 118){
                offset = limelight.tx-4;
            }else{
                offset = limelight.tx-7;
            }*/ //feb 25, trying ashlynne's new close curve
            if(limelight.targetVisible && Math.abs(offset) <= TRANSITION_THRESHOLD && (DISTANCE >= 75 || roboLocationY >= 115)){
                    //LIMELIGHT_KI = LKI_RANGE*Math.exp(-0.5*Math.abs(offset))+LKI_BASE;

                    /*if(Math.abs(limelight.tx)>=1) {
                        turn = -limelightTurnController.calculate(0, limelight.tx);
                        turn = Math.signum(turn) * Math.max(Math.abs(turn), min_turn_speed);
                    }*/
                    if(Math.abs(offset) >= 1) {
                        turn = -limelightTurnController.calculate(0, offset);
                        turn = Math.signum(turn) * Math.max(Math.abs(turn), min_turn_speed);
                    }
                }else{
                    double adjustedError = angleWrap(autoAimAngle - pinpoint.getHeading(AngleUnit.DEGREES));
                    turn = -headingController.calculate(adjustedError);
                }
            }

        if(myOpMode.gamepad2.right_bumper){
            if(side == Drivetrain.SideMode.RED){
                double adjustedError = angleWrap(35 - pinpoint.getHeading(AngleUnit.DEGREES));
                turn = -headingController.calculate(adjustedError);
            }else if(side == Drivetrain.SideMode.BLUE){
                double adjustedError = angleWrap(145 - pinpoint.getHeading(AngleUnit.DEGREES));
                turn = -headingController.calculate(adjustedError);

            }
        }
        myOpMode.telemetry.addData("tx offset", offset);
        myOpMode.telemetry.addData("tx", limelight.tx);
        myOpMode.telemetry.addData("turn", turn);
        myOpMode.telemetry.addData("min_turn_speed", min_turn_speed);

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
        if (myOpMode.gamepad2.left_bumper) {
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
            rightBackDrive.setPower(rightBackPower); ///1.5
        }

        if(Math.abs(myOpMode.gamepad2.left_stick_x) >= 0.2 || Math.abs(myOpMode.gamepad2.left_stick_y) >= 0.2) {
            drivetrainMode = DrivetrainMode.FIELDCENTRIC;
        } else if (Math.abs(myOpMode.gamepad1.left_stick_x) >= 0.2 || Math.abs(myOpMode.gamepad1.left_stick_y) >= 0.2){
            drivetrainMode = DrivetrainMode.ROBOTCENTRIC;
        }

        if(myOpMode.gamepad2.b){
            side = SideMode.RED;
            limelight.limelight.pipelineSwitch(0);//0 is 2d mode, 3 is 3d
            //myOpMode.telemetry.addData(">", "RED");
        }else if(myOpMode.gamepad2.x){
            side = SideMode.BLUE;
            limelight.limelight.pipelineSwitch(1);//1 2d version, 2 is 3d version
            //myOpMode.telemetry.addData(">", "BLUE");
        }
        //myOpMode.telemetry.addData("sideMode: ", side);

        //myOpMode.telemetry.addData("kickstandmode: ", kickstand);
        //myOpMode.telemetry.addData("drivetrainMode: ", drivetrainMode);
        myOpMode.telemetry.addData("heading: ", pinpoint.getHeading(AngleUnit.DEGREES));
        //myOpMode.telemetry.addData("AutoAim Angle ", autoAimAngle);
        myOpMode.telemetry.addData("roboX: ", roboLocationX);
        myOpMode.telemetry.addData("roboY: ", roboLocationY);
        //myOpMode.telemetry.addData("Distance: ", DISTANCE);



       // dashboardTelemetry.addData("AutoAim Angle", autoAimAngle);
       // dashboardTelemetry.addData("pinpoint Heading", pinpoint.getHeading(AngleUnit.DEGREES));
       // dashboardTelemetry.addData("Limelight Tx", limelight.result.getTx());
       // dashboardTelemetry.update();


        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();
        dashboardTelemetry.addData("tx", limelight.tx);
        dashboardTelemetry.update();
    }



    public void update(){
        double roboLocationX = pinpoint.getPosX(DistanceUnit.INCH);
        double roboLocationY = pinpoint.getPosY(DistanceUnit.INCH);
        if(side == SideMode.BLUE){
            DISTANCE = Math.sqrt(roboLocationX*roboLocationX + (144-roboLocationY)*(144-roboLocationY));
        }else if(side == SideMode.RED){
            DISTANCE = Math.sqrt((144-roboLocationX)*(144-roboLocationX) + (144-roboLocationY)*(144-roboLocationY));
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

