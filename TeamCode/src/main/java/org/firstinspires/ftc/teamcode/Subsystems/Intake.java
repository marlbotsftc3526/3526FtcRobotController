package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;
public class Intake {
    private OpMode myOpMode = null;   // gain access to methods in the calling OpMode.

    public DcMotor intake = null;
    public Servo flower = null;
    private DigitalChannel distOne;
    private DigitalChannel distTwo;

    boolean distonelaststate = false;
    boolean disttwolaststate = false;

    public boolean detectedOne;
    public boolean detectedTwo;

    public static double FLOWERUP = 0.07;
    public static double FLOWERDOWN = 0.54;
    public double flowerServoPosition = FLOWERUP;

    //TODO Adjust based on desired states
    public enum IntakeMode {
        IN,
        OUT,
        LAUNCH,
        OFF,

    }

    //COME BACK WRONG VALUES PROBABLY
    public static double INTAKE_SPEED = 1;
    public static double INTAKE_LAUNCHER_SPEED = 0.7;
    public static double OUTTAKE_SPEED = -.7;

    public Intake.IntakeMode intakeMode = Intake.IntakeMode.OFF;


    public Intake(OpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {

        // Get the digital sensor from the hardware map
        distOne = myOpMode.hardwareMap.get(DigitalChannel.class, "distOne");

        // Set the channel as an input
        distOne.setMode(DigitalChannel.Mode.INPUT);

        // Get the digital sensor from the hardware map
        distTwo = myOpMode.hardwareMap.get(DigitalChannel.class, "distTwo");

        // Set the channel as an input
        distTwo.setMode(DigitalChannel.Mode.INPUT);

        intake = myOpMode.hardwareMap.get(DcMotor.class, "intake");
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake.setDirection(DcMotor.Direction.REVERSE);

        myOpMode.telemetry.addData(">", "Intake Initialized");

        flower = myOpMode.hardwareMap.get(Servo.class, "intakeBar");
        flower.setPosition(flowerServoPosition);

    }

    public void update() {

        if (intakeMode == IntakeMode.IN) {
            // Send calculated power to wheels
            intake.setPower(INTAKE_SPEED);
        } else if (intakeMode == IntakeMode.LAUNCH) {
            intake.setPower(INTAKE_LAUNCHER_SPEED);
        } else if (intakeMode == IntakeMode.OUT) {
            intake.setPower(OUTTAKE_SPEED);
        } else if (intakeMode == IntakeMode.OFF) {
            intake.setPower(0);
        }
        // Read the sensor state (true = HIGH, false = LOW)
        detectedOne = distOne.getState();
        detectedTwo = distTwo.getState();

        flower.setPosition(flowerServoPosition);
    }

    public void teleOp(){

    }

}
