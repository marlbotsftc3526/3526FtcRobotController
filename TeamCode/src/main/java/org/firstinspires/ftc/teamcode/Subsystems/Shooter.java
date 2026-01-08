package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utility.PIDController;

@Config
public class Shooter {
    /* Declare OpMode members. */
    private OpMode myOpMode = null;   // gain access to methods in the calling OpMode.


    public Drivetrain drivetrain = null;

    public DcMotorEx shoot = null;
    public DcMotorEx shootLeft = null;
    public Servo transfer = null;
    public Servo hood = null;
    public PIDFCoefficients sPIDF = null;
    public PIDController shooterPID = null;
    public static double kp = 0.001;
    public static double ki = 0;
    public static double kd = 0;
    public static double sp = 0;
    public static double si = 0;
    public static double sd = 0;
    public static double sf = 0;

    //TODO Adjust based on desired states
    public enum ShootMode {
        ON,
        CUSTOMPID,
        OFF,
    }

    public enum HoodMode{
        CLOSE,
        AUTO,
        FAR,
        LINEAR,
    }

    public enum TransferMode {
        ON,
        OFF,
        AUTO
    }

    // Define Drive constants.  Make them public so they CAN be used by the calling OpMode
    //TODO Update values based on desired position
    public double REVOLUTIONS_PER_MINUTE = 3100;
    public double RPM_TUNING_CONSTANT = 1.1;
    public static final double CLOSE_RPM = 3440;
    public static double FAR_RPM_TESTING = 4995;//4500
    public static final double FAR_RPM = 5325;
    public static final double TICKS_PER_REVOLUTION = 28;
    public static final double TRANSFER_SPEED = -1;
    public static final double GATE_OPEN = 0.567;
    public static final double GATE_CLOSE=0.25;
    public static final double HOOD_CLOSE=.635;
    public static double HOOD_FAR_TESTING = .275;
    public static double HOOD_RETRACTED = 1;
   // 4790
   // public static final double HOOD_AUTO = .75;
    public static final double HOOD_FAR=0.34;
    double TICKS_PER_SECOND;
    public ShootMode shootMode = ShootMode.OFF;
    public TransferMode transferMode = TransferMode.OFF;
    public HoodMode hoodMode = HoodMode.LINEAR;
    //Constructor
    public Shooter(OpMode opmode, Drivetrain drive) {
        myOpMode = opmode;
        drivetrain = drive;
    }

    public void init() {
        shoot = myOpMode.hardwareMap.get(DcMotorEx.class, "shooter");
        shoot.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        shoot.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        shoot.setDirection(DcMotor.Direction.REVERSE);
        shootLeft = myOpMode.hardwareMap.get(DcMotorEx.class, "shooterLeft");
        shootLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        shootLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        shootLeft.setDirection(DcMotor.Direction.FORWARD);
        //sPIDF = new PIDFCoefficients(shoot.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
        shooterPID = new PIDController(kp,ki,kd,1);
        //sp = sPIDF.p;
        //si = sPIDF.i;
        //sd = sPIDF.d;
        //sf = sPIDF.f;

        transfer = myOpMode.hardwareMap.get(Servo.class, "transfer");

        hood = myOpMode.hardwareMap.get(Servo.class, "hood");

        hood.setPosition(HOOD_RETRACTED);
        myOpMode.telemetry.addData(">", "Shooter Initialized");
    }

    public void update() {



        if (transferMode == TransferMode.ON) {
            transfer.setPosition(GATE_OPEN);
        }
        else if (transferMode == TransferMode.OFF) {
            transfer.setPosition(GATE_CLOSE);
        }else if(transferMode == TransferMode.AUTO){
            myOpMode.telemetry.addData("diff:", Math.abs(shoot.getVelocity()/TICKS_PER_REVOLUTION*60 - REVOLUTIONS_PER_MINUTE));
            if(Math.abs(shoot.getVelocity()/TICKS_PER_REVOLUTION*60 - REVOLUTIONS_PER_MINUTE) <= Math.min(drivetrain.DISTANCE+20, 70)){
                transfer.setPosition(GATE_OPEN);
            }else{
                transfer.setPosition(GATE_CLOSE);
            }
        }

        if (hoodMode == HoodMode.CLOSE) {
            hood.setPosition(HOOD_CLOSE);
            REVOLUTIONS_PER_MINUTE = CLOSE_RPM;
        }
        else if (hoodMode == HoodMode.FAR) {
            hood.setPosition(HOOD_FAR_TESTING);
            REVOLUTIONS_PER_MINUTE = FAR_RPM_TESTING;
        } else if (hoodMode == HoodMode.AUTO) {
            if(drivetrain.DISTANCE  >= 110){
                hood.setPosition(1);
                REVOLUTIONS_PER_MINUTE = 3700+(drivetrain.DISTANCE-65)*14.3;
            }else if(drivetrain.DISTANCE < 36){
                hood.setPosition(0);
                REVOLUTIONS_PER_MINUTE = 3000+(drivetrain.DISTANCE-12)*8.33;
            }else if(drivetrain.DISTANCE >= 36 && drivetrain.DISTANCE <48){
                hood.setPosition((drivetrain.DISTANCE-36)/12*0.1);
                REVOLUTIONS_PER_MINUTE = 2900+(drivetrain.DISTANCE-24)*12.5;
            }else if(drivetrain.DISTANCE >= 48 && drivetrain.DISTANCE <60){
                hood.setPosition(0.9-(78-drivetrain.DISTANCE)/30*0.9);
                REVOLUTIONS_PER_MINUTE = 3000+(drivetrain.DISTANCE-48)*28.5;
            }else if(drivetrain.DISTANCE >= 60 && drivetrain.DISTANCE <72){
                hood.setPosition(1-(77-drivetrain.DISTANCE)/17*0.64);
                REVOLUTIONS_PER_MINUTE = 3342+(drivetrain.DISTANCE-60)*23;
            }else if(drivetrain.DISTANCE >= 72 && drivetrain.DISTANCE < 110){
                hood.setPosition(1);
                REVOLUTIONS_PER_MINUTE = 3700+(drivetrain.DISTANCE-65)*11;
            }
            //hood.setPosition(HOOD_FAR_TESTING);
            REVOLUTIONS_PER_MINUTE*=RPM_TUNING_CONSTANT;
            //REVOLUTIONS_PER_MINUTE = FAR_RPM_TESTING;
        }else if (hoodMode==HoodMode.LINEAR){
            REVOLUTIONS_PER_MINUTE=18.2*drivetrain.DISTANCE+2689;
            if (drivetrain.DISTANCE>=48){
                hood.setPosition(1);
            }else{
                hood.setPosition(2.79E-3*Math.exp(0.122*drivetrain.DISTANCE));
            }
        }
        if (shootMode == ShootMode.ON) {
            TICKS_PER_SECOND = REVOLUTIONS_PER_MINUTE / 60 * TICKS_PER_REVOLUTION;
            // Send calculated power to wheels
            shoot.setVelocity(TICKS_PER_SECOND);
            shootLeft.setVelocity(TICKS_PER_SECOND);
        } else if (shootMode == ShootMode.OFF) {
            shoot.setVelocity(0);
            shootLeft.setVelocity(0);
        }else if (shootMode == ShootMode.CUSTOMPID) {
            TICKS_PER_SECOND = REVOLUTIONS_PER_MINUTE / 60 * TICKS_PER_REVOLUTION;
            double output = shooterPID.calculate(TICKS_PER_SECOND, shoot.getVelocity());
            shoot.setPower(output);
            shootLeft.setPower(output);
            //calculate measured RPM from motors current degrees per second
            double measuredRPM = shoot.getVelocity()/TICKS_PER_REVOLUTION*60;
        }
        myOpMode.telemetry.addData("hoodMode: ", hoodMode);
        myOpMode.telemetry.addData("hoodPosition: ", hood.getPosition());
        myOpMode.telemetry.addData("rpm:", REVOLUTIONS_PER_MINUTE);
    }

    public void teleOp() {
        update();
        //Set states based on gamepad presses
        //TODO Update based on desired control scheme
        if (myOpMode.gamepad2.right_bumper) { // || myOpMode.gamepad2.right_bumper
            shootMode = ShootMode.ON;
        } else if (myOpMode.gamepad2.left_bumper) {//myOpMode.gamepad1.left_bumper ||
            shootMode = ShootMode.OFF; //s=S
        }

        if (myOpMode.gamepad2.right_trigger>.5) {//(myOpMode.gamepad1.right_trigger>.5||
            transferMode = TransferMode.ON;
        } else {
            transferMode = TransferMode.OFF;
        }

        if (myOpMode.gamepad2.dpad_up){
            hoodMode = HoodMode.CLOSE;
        }
        else if (myOpMode.gamepad2.dpad_down) { //myOpMode.gamepad1.dpad_down ||
            hoodMode = HoodMode.FAR;
            shooterPID = new PIDController(kp,ki,kd,1);
        }else if (myOpMode.gamepad2.dpad_right) {
            hoodMode = HoodMode.AUTO;
        }


        if(myOpMode.gamepad1.dpad_up){
            FAR_RPM_TESTING += 5;
        }
        if(myOpMode.gamepad1.dpad_down){
            FAR_RPM_TESTING -= 5;
        }
        if(myOpMode.gamepad1.dpad_left){
            HOOD_FAR_TESTING -= 0.005;
        }
        if(myOpMode.gamepad1.dpad_right){
            HOOD_FAR_TESTING += 0.005;
        }
        /*
        if(myOpMode.gamepad2.right_bumper){
            sPIDF.p = sp;
            sPIDF.i = si;
            sPIDF.d = sd;
            sPIDF.f = sf;
            shoot.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, sPIDF);
            shoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

         */

        myOpMode.telemetry.addData("HOOD_FAR_TESTING: ", HOOD_FAR_TESTING);
        myOpMode.telemetry.addData("FAR_RPM_TESTING: ", FAR_RPM_TESTING);
        //myOpMode.telemetry.addData("sPIDF.p: ", sPIDF.p);


        double measuredRPM = shoot.getVelocity()/TICKS_PER_REVOLUTION*60;
        double measuredRPMLeft = shootLeft.getVelocity()/TICKS_PER_REVOLUTION*60;

        // Show the elapsed game time and wheel power.
        myOpMode.telemetry.addData("Set RPM", REVOLUTIONS_PER_MINUTE);
        myOpMode.telemetry.addData("Measured RPM", measuredRPM);
        myOpMode.telemetry.addData("Measured RPM Left", measuredRPMLeft);
        myOpMode.telemetry.addData("ShooterMode", shootMode);
        myOpMode.telemetry.addData("diff:", Math.abs(shoot.getVelocity()/TICKS_PER_REVOLUTION*60 - REVOLUTIONS_PER_MINUTE));
        myOpMode.telemetry.update();

        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        dashboardTelemetry.addData("Set RPM", REVOLUTIONS_PER_MINUTE);
        dashboardTelemetry.addData("Measured RPM", measuredRPM);
        dashboardTelemetry.addData("Measured Ticks Per Second", shoot.getVelocity());
        dashboardTelemetry.addData("Ticks Per Second", TICKS_PER_SECOND);

        dashboardTelemetry.addData("sp", sp);
        dashboardTelemetry.addData("si", si);
        dashboardTelemetry.addData("sd", sd);
        dashboardTelemetry.addData("sf", sf);
        dashboardTelemetry.update();

    }

        public void stop () {
            shoot.setPower(0);
            transfer.setPosition(GATE_CLOSE);
        }
    }


