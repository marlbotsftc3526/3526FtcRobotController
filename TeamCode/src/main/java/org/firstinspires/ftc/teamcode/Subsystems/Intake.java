package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import static org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver.LayerHeight;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Prism.Color;
import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations;
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations.AnimationType;
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations.PoliceLights;
import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver.Artboard;
import org.firstinspires.ftc.teamcode.Prism.PrismConfigurator;

import java.util.concurrent.TimeUnit;
@Config
public class Intake {
    private OpMode myOpMode = null;   // gain access to methods in the calling OpMode.

    public DcMotor spin = null;
    public Servo kick = null;
    private DigitalChannel laserBottom;
    private DigitalChannel laserMiddle;
    private DigitalChannel laserTop;

    boolean toplaststate = false;
    boolean middlelaststate = false;
    boolean bottomlaststate = false;

    public ElapsedTime kickTimer;

    public boolean detectedTop;
    public boolean detectedMiddle;
    public boolean detectedBottom;

    GoBildaPrismDriver prism;
    PrismAnimations.Solid solidTop = new PrismAnimations.Solid(Color.PURPLE);
    PrismAnimations.Solid solidMiddle = new PrismAnimations.Solid(Color.GREEN);
    PrismAnimations.Solid solidBottom = new PrismAnimations.Solid(Color.BLUE);



    //TODO Adjust based on desired states
    public enum IntakeMode {
        UP,
        LAUNCH,
        DOWN,
        OFF,

    }

    public enum KickMode {
        OUT,
        IN,
        AUTO
    }

    public static final double WHEN_KICK_OPEN = 0;
    public static final double WHEN_KICK_CLOSED = .55;
    public Intake.KickMode kickMode = Intake.KickMode.IN;

    public static double INTAKE_SPEED = 1;
    public static double INTAKE_LAUNCHER_SPEED = 0.7;
    public static double OUTTAKE_SPEED = -.5;

    public Intake.IntakeMode intakeMode = Intake.IntakeMode.OFF;


    public Intake(OpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {
        kickTimer = new ElapsedTime();

        prism = myOpMode.hardwareMap.get(GoBildaPrismDriver.class,"prism");
        solidTop.setBrightness(0);
        solidTop.setStartIndex(0);
        solidTop.setStopIndex(3);

        solidMiddle.setBrightness(0);
        solidMiddle.setStartIndex(4);
        solidMiddle.setStopIndex(7);

        solidBottom.setBrightness(0);
        solidBottom.setStartIndex(8);
        solidBottom.setStopIndex(11);

        // Get the digital sensor from the hardware map
        laserTop = myOpMode.hardwareMap.get(DigitalChannel.class, "laserTop");

        // Set the channel as an input
        laserTop.setMode(DigitalChannel.Mode.INPUT);

        // Get the digital sensor from the hardware map
        laserMiddle = myOpMode.hardwareMap.get(DigitalChannel.class, "laserMiddle");

        // Set the channel as an input
        laserMiddle.setMode(DigitalChannel.Mode.INPUT);

        // Get the digital sensor from the hardware map
        laserBottom = myOpMode.hardwareMap.get(DigitalChannel.class, "laserBottom");

        // Set the channel as an input
        laserBottom.setMode(DigitalChannel.Mode.INPUT);

        spin = myOpMode.hardwareMap.get(DcMotor.class, "intake");
        spin.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spin.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        kick = myOpMode.hardwareMap.get(Servo.class, "kick");

        spin.setDirection(DcMotor.Direction.REVERSE);

        prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, solidTop);
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_1, solidMiddle);
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_2, solidBottom);

        myOpMode.telemetry.addData(">", "Intake Initialized");

    }

    public void update() {

        if (intakeMode == IntakeMode.UP) {
            // Send calculated power to wheels
            spin.setPower(INTAKE_SPEED);
        } else if(intakeMode == IntakeMode.LAUNCH){
            spin.setPower(INTAKE_LAUNCHER_SPEED);
        }else if (intakeMode == IntakeMode.DOWN) {
            spin.setPower(OUTTAKE_SPEED);
        } else if (intakeMode == IntakeMode.OFF) {
            spin.setPower(0);
        }
        // Read the sensor state (true = HIGH, false = LOW)
        detectedTop = laserTop.getState();
        detectedMiddle = laserMiddle.getState();
        detectedBottom = laserBottom.getState();


        // Display detection state
        if (detectedTop) {
            solidTop.setBrightness(50);
            //prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, solidTop);
            //myOpMode.telemetry.addLine("Top Object detected!");
        } else {
            solidTop.setBrightness(0);
            //prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, solidTop);
            //myOpMode.telemetry.addLine("No top object detected");
        }

        if (detectedMiddle) {
            solidMiddle.setBrightness(50);
            //prism.insertAndUpdateAnimation(LayerHeight.LAYER_1, solidMiddle);
            //myOpMode.telemetry.addLine("Middle Object detected!");
        } else {
            solidMiddle.setBrightness(0);
            //prism.insertAndUpdateAnimation(LayerHeight.LAYER_1, solidMiddle);
            //myOpMode.telemetry.addLine("No middle object detected");
        }

        if (detectedBottom) {
            solidBottom.setBrightness(50);
            //prism.insertAndUpdateAnimation(LayerHeight.LAYER_2, solidBottom);
            //myOpMode.telemetry.addLine("Bottom Object detected!");
        } else {
            solidBottom.setBrightness(0);
            //prism.insertAndUpdateAnimation(LayerHeight.LAYER_2, solidBottom);
            //myOpMode.telemetry.addLine("No bottom object detected");
        }

        if (detectedTop != toplaststate) {
            prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, solidTop);
            toplaststate = detectedTop;
        }

        if (detectedMiddle != middlelaststate) {
            prism.insertAndUpdateAnimation(LayerHeight.LAYER_1, solidMiddle);
            middlelaststate = detectedMiddle;
        }

        if (detectedBottom != bottomlaststate) {
            prism.insertAndUpdateAnimation(LayerHeight.LAYER_2, solidBottom);
            bottomlaststate = detectedBottom;
        }

        if (kickMode == Intake.KickMode.IN) {
            kick.setPosition(WHEN_KICK_CLOSED);
            //myOpMode.telemetry.addLine("Kick is in (closed)");
        } else if (kickMode == Intake.KickMode.OUT) {
            kick.setPosition(WHEN_KICK_OPEN);
            //myOpMode.telemetry.addLine("Kick is out (open)");
        }

        if (kickTimer.milliseconds()>=200){
            kickMode = KickMode.IN;
        }
    }
    public void teleOp() {
        update();

        //Set states based on gamepad presses
        //TODO Update based on desired control scheme
        if (myOpMode.gamepad1.y || myOpMode.gamepad2.y) {
            intakeMode = IntakeMode.UP;

        } else if (myOpMode.gamepad1.a || myOpMode.gamepad2.a) {
            intakeMode = IntakeMode.DOWN; //s=S

        }
        else if (myOpMode.gamepad1.x) {
            intakeMode = IntakeMode.OFF; //s=S
        }

        if (myOpMode.gamepad1.b) {
            kickTimer.reset();
            kickMode = KickMode.OUT;

        }

    }

    public void stop () {
        spin.setPower(0);
    }
}
