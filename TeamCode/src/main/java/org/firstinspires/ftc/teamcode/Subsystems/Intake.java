package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

public class Intake {
    private OpMode myOpMode = null;   // gain access to methods in the calling OpMode.

    public DcMotor spin = null;
    private DigitalChannel laserBottom;
    private DigitalChannel laserMiddle;
    private DigitalChannel laserTop;

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
        // Get the digital sensor from the hardware map
        laserTop = myOpMode.hardwareMap.get(DigitalChannel.class, "laserTop");

        // Set the channel as an input
        laserTop.setMode(DigitalChannel.Mode.INPUT);

        // Get the digital sensor from the hardware map
        laserMiddle = myOpMode.hardwareMap.get(DigitalChannel.class, "laserMiddle");

        // Set the channel as an input
        laserMiddle.setMode(DigitalChannel.Mode.INPUT);

        // Get the digital sensor from the hardware map
        laserBottom = myOpMode.hardwareMap.get(DigitalChannel.class, "laserDigitalInput");

        // Set the channel as an input
        laserBottom.setMode(DigitalChannel.Mode.INPUT);

        spin = myOpMode.hardwareMap.get(DcMotor.class, "intake");
        spin.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spin.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        spin.setDirection(DcMotor.Direction.REVERSE);

        myOpMode.telemetry.addData(">", "Intake Initialized");
    }
    public static final double INTAKE_SPEED = .7;
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
        boolean stateHighTop = laserTop.getState();
        boolean stateHighMiddle = laserMiddle.getState();
        boolean stateHighBottom = laserBottom.getState();

        // Active-HIGH: HIGH means an object is detected
        boolean detectedTop = stateHighTop;
        boolean detectedMiddle = stateHighMiddle;
        boolean detectedBottom = stateHighBottom;

        // Display detection state
        if (detectedTop) {
            myOpMode.telemetry.addLine("Object detected!");
        } else {
            myOpMode.telemetry.addLine("No object detected");
        }

        if (detectedMiddle) {
            myOpMode.telemetry.addLine("Object detected!");
        } else {
            myOpMode.telemetry.addLine("No object detected");
        }

        if (detectedBottom) {
            myOpMode.telemetry.addLine("Object detected!");
        } else {
            myOpMode.telemetry.addLine("No object detected");
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
        else if (myOpMode.gamepad1.x || myOpMode.gamepad2.x) {
            intakeMode = IntakeMode.OFF; //s=S
        }

    }

    public void stop () {
        spin.setPower(0);
    }
}
