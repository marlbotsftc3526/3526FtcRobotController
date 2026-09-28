package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Intake {

    private OpMode myOpMode = null;

    //lift motors
    public DcMotor intake = null;

    public static final double INTAKE_SPEED= 1;
    public static final double OUTTAKE_SPEED = -0.5;

    public enum IntakeMode {
        OFF,
        INTAKE,
        OUTTAKE
    }

    public IntakeMode intakeMode = IntakeMode.OFF;

    public Intake(OpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {

        intake = myOpMode.hardwareMap.get(DcMotor.class, "intake");

        intake.setDirection(DcMotor.Direction.REVERSE);

        myOpMode.telemetry.addData(">", "Intake Initialized");
    }

    public void teleOp() {
        update();

        if(myOpMode.gamepad1.a){
            intakeMode = IntakeMode.INTAKE;
        } else if (myOpMode.gamepad1.b){
            intakeMode = IntakeMode.OFF;
        } else if (myOpMode.gamepad1.x){
           intakeMode = IntakeMode.OUTTAKE;
        }

        //code defining behavior of lift in each state

        myOpMode.telemetry.addData("left_stick_y: ", -myOpMode.gamepad2.left_stick_y);
        myOpMode.telemetry.addData("Lift Mode ", intakeMode);

    }

    public void update() {

        if (intakeMode == IntakeMode.OFF) {
            intake.setPower(0);
        } else if (intakeMode == IntakeMode.INTAKE){
            intake.setPower(INTAKE_SPEED);
        } else if (intakeMode == IntakeMode.OUTTAKE){
            intake.setPower(OUTTAKE_SPEED);
        }
    }
}
