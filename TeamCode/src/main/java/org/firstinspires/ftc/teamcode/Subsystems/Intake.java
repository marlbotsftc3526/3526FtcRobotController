package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Intake {
    private OpMode myOpMode = null;   // gain access to methods in the calling OpMode.

    public DcMotor spin = null;

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
        spin = myOpMode.hardwareMap.get(DcMotor.class, "intake");
        spin.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spin.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        myOpMode.telemetry.addData(">", "Intake Initialized");
    }
    public static final double INTAKE_SPEED = .7;
    public static final double OUTTAKE_SPEED = -.5;

    Intake.IntakeMode intakeMode = Intake.IntakeMode.OFF;

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
    }
    public void teleOp() {
        update();
        //Set states based on gamepad presses
        //TODO Update based on desired control scheme
        if (myOpMode.gamepad1.y) {
            intakeMode = IntakeMode.UP;
        } else if (myOpMode.gamepad1.a) {
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
