package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import static org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver.LayerHeight;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Prism.Color;
import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations;
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations.AnimationType;
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations.PoliceLights;
import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver.Artboard;
import org.firstinspires.ftc.teamcode.Prism.PrismConfigurator;

import java.util.concurrent.TimeUnit;

public class Intake {
    private OpMode myOpMode = null;   // gain access to methods in the calling OpMode.

    public DcMotor spin = null;
    private DigitalChannel laserBottom;
    private DigitalChannel laserMiddle;
    private DigitalChannel laserTop;

    boolean toplaststate = false;
    boolean middlelaststate = false;
    boolean bottomlaststate = false;


    GoBildaPrismDriver prism;
    PrismAnimations.Solid solidTop = new PrismAnimations.Solid(Color.PURPLE);
    PrismAnimations.Solid solidMiddle = new PrismAnimations.Solid(Color.GREEN);
    PrismAnimations.Solid solidBottom = new PrismAnimations.Solid(Color.BLUE);

    //TODO Adjust based on desired states
    public enum IntakeMode {
        UP,
        DOWN,
        OFF,
    }

    public Intake(OpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {
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

        spin.setDirection(DcMotor.Direction.REVERSE);
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, solidTop);
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_1, solidMiddle);
        prism.insertAndUpdateAnimation(LayerHeight.LAYER_2, solidBottom);

        myOpMode.telemetry.addData(">", "Intake Initialized");

    }
    public static final double INTAKE_SPEED = 1;
    public static final double OUTTAKE_SPEED = -.5;

   public Intake.IntakeMode intakeMode = Intake.IntakeMode.OFF;

    public void update() {

        if (intakeMode == IntakeMode.UP) {
            // Send calculated power to wheels
            spin.setPower(INTAKE_SPEED);
        } else if (intakeMode == IntakeMode.DOWN) {
            spin.setPower(OUTTAKE_SPEED);
        }
        else if (intakeMode == IntakeMode.OFF){
            spin.setPower(0);
         }
        // Read the sensor state (true = HIGH, false = LOW)
        boolean detectedTop = laserTop.getState();
        boolean detectedMiddle = laserMiddle.getState();
        boolean detectedBottom = laserBottom.getState();


        // Display detection state
        if (detectedTop) {
            solidTop.setBrightness(50);
            //prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, solidTop);
            myOpMode.telemetry.addLine("Top Object detected!");
        } else {
            solidTop.setBrightness(0);
            //prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, solidTop);
            myOpMode.telemetry.addLine("No top object detected");
        }

        if (detectedMiddle) {
            solidMiddle.setBrightness(50);
            //prism.insertAndUpdateAnimation(LayerHeight.LAYER_1, solidMiddle);
            myOpMode.telemetry.addLine("Middle Object detected!");
        } else {
            solidMiddle.setBrightness(0);
            //prism.insertAndUpdateAnimation(LayerHeight.LAYER_1, solidMiddle);
            myOpMode.telemetry.addLine("No middle object detected");
        }

        if (detectedBottom) {
            solidBottom.setBrightness(50);
            //prism.insertAndUpdateAnimation(LayerHeight.LAYER_2, solidBottom);
            myOpMode.telemetry.addLine("Bottom Object detected!");
        } else {
            solidBottom.setBrightness(0);
            //prism.insertAndUpdateAnimation(LayerHeight.LAYER_2, solidBottom);
            myOpMode.telemetry.addLine("No bottom object detected");
        }

        if(detectedTop != toplaststate){
            prism.insertAndUpdateAnimation(LayerHeight.LAYER_0, solidTop);
            toplaststate = detectedTop;
        }

        if(detectedMiddle != middlelaststate){
            prism.insertAndUpdateAnimation(LayerHeight.LAYER_1, solidMiddle);
            middlelaststate = detectedMiddle;
        }

        if(detectedBottom != bottomlaststate){
            prism.insertAndUpdateAnimation(LayerHeight.LAYER_2, solidBottom);
            bottomlaststate = detectedBottom;
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

    }

    public void stop () {
        spin.setPower(0);
    }
}
