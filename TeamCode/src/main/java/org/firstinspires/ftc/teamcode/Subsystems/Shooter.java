package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utility.PIDController;

@Config
public class Shooter {
    /* Declare OpMode members. */
    private OpMode myOpMode = null;   // gain access to methods in the calling OpMode.
    public DcMotorEx shootLeft = null;
    public DcMotorEx shoot = null;
    public Servo transfer = null;
    public Servo hood = null;
    public PIDFCoefficients sPIDF = null;
    public PIDController shooterPID = null;
    public static double kp = 0.01;
    public static double ki = 0;
    public static double kd = 0;
    public static double sp = 0;
    public static double si = 0;
    public static double sd = 0;
    public static double sf = 0;

    //TODO Adjust based on desired states
    public enum ShootMode {
        ON,
        OFF,
    }

    public enum HoodMode{
        CLOSE,
        AUTO,
        FAR,
    }

    public enum TransferMode {
        ON,
        OFF,
    }

    // Define Drive constants.  Make them public so they CAN be used by the calling OpMode
    //TODO Update values based on desired position
    public double REVOLUTIONS_PER_MINUTE = 3100;
    public static final double CLOSE_RPM = 3440;
    public static double FAR_RPM_TESTING = 4995;//4500
    public static final double FAR_RPM = 5325;
    public static final double TICKS_PER_REVOLUTION = 28;
    public static final double TRANSFER_SPEED = -1;
    public static final double GATE_OPEN = 0.48;
    public static final double GATE_CLOSE=0.25;
    public static final double HOOD_CLOSE=.635;
    public static double HOOD_FAR_TESTING = .275;
   // 4790
   // public static final double HOOD_AUTO = .75;
    public static final double HOOD_FAR=0.34;
    double TICKS_PER_SECOND;
    public ShootMode shootMode = ShootMode.OFF;
    public TransferMode transferMode = TransferMode.OFF;
    public HoodMode hoodMode = HoodMode.CLOSE;
    //Constructor
    public Shooter(OpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {
        shoot = myOpMode.hardwareMap.get(DcMotorEx.class, "shooter");
        shoot.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
       // shoot.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        shoot.setDirection(DcMotor.Direction.REVERSE);
        shootLeft = myOpMode.hardwareMap.get(DcMotorEx.class, "shooterLeft");
        shootLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        // shoot.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        shootLeft.setDirection(DcMotor.Direction.FORWARD);
        sPIDF = new PIDFCoefficients(shoot.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
        sp = sPIDF.p;
        si = sPIDF.i;
        sd = sPIDF.d;
        sf = sPIDF.f;

        transfer = myOpMode.hardwareMap.get(Servo.class, "transfer");

        hood = myOpMode.hardwareMap.get(Servo.class, "hood");

        hood.setPosition(1);
        myOpMode.telemetry.addData(">", "Shooter Initialized");
    }

    public void update() {

        if (shootMode == ShootMode.ON) {
            TICKS_PER_SECOND = REVOLUTIONS_PER_MINUTE / 60 * TICKS_PER_REVOLUTION;
            // Send calculated power to wheels
            shoot.setVelocity(TICKS_PER_SECOND);
        } else if (shootMode == ShootMode.OFF) {
            shoot.setVelocity(0);
        }

        if (transferMode == TransferMode.ON) {
            transfer.setPosition(GATE_OPEN);
        }
        else if (transferMode == TransferMode.OFF) {
            transfer.setPosition(GATE_CLOSE);
        }

        if (hoodMode == HoodMode.CLOSE) {
            hood.setPosition(HOOD_CLOSE);
            REVOLUTIONS_PER_MINUTE = CLOSE_RPM;
        }
        else if (hoodMode == HoodMode.FAR) {
            hood.setPosition(HOOD_FAR_TESTING);
            REVOLUTIONS_PER_MINUTE = FAR_RPM_TESTING;
        }
        myOpMode.telemetry.addData("hoodMode: ", hoodMode);
        myOpMode.telemetry.addData("hoodPosition: ", hood.getPosition());
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
        }
        if(myOpMode.gamepad1.dpad_up){
            FAR_RPM_TESTING += 5;
        }
        if(myOpMode.gamepad1.dpad_down){
            FAR_RPM_TESTING -= 5;
        }
        if(myOpMode.gamepad1.left_bumper){
            HOOD_FAR_TESTING -= 0.005;
        }
        if(myOpMode.gamepad1.right_bumper){
            HOOD_FAR_TESTING += 0.005;
        }
        if(myOpMode.gamepad2.right_bumper){
            sPIDF.p = sp;
            sPIDF.i = si;
            sPIDF.d = sd;
            sPIDF.f = sf;
            shoot.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, sPIDF);
            shoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        myOpMode.telemetry.addData("HOOD_FAR_TESTING: ", HOOD_FAR_TESTING);
        myOpMode.telemetry.addData("FAR_RPM_TESTING: ", FAR_RPM_TESTING);
        myOpMode.telemetry.addData("sPIDF.p: ", sPIDF.p);


        double measuredRPM = shoot.getVelocity()/TICKS_PER_REVOLUTION*60;

        // Show the elapsed game time and wheel power.
        myOpMode.telemetry.addData("Set RPM", REVOLUTIONS_PER_MINUTE);
        myOpMode.telemetry.addData("Measured RPM", measuredRPM);
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


