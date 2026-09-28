package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ServoImplEx;


public class Lift {
    private OpMode myOpMode = null;

    //lift motors
    public DcMotor rightLift = null;
    public DcMotor leftLift = null;

    public static final int LIFT_HIGH_CHAMBER = 800;
    public static final int LIFT_LOW_CHAMBER = 400;
    public static final int LIFT_DOWN = 0;
    //public TouchSensor touch = null;

    public enum LiftMode {
        MANUAL,
        HIGH_CHAMBER,
        LOW_CHAMBER,
        GROUND
    }

    public LiftMode liftMode = LiftMode.MANUAL;
    PIDController liftLeftPID;
    PIDController liftRightPID;

    public static final double LIFT_KP = 0.005;
    public static final double LIFT_KI = 0;
    public static final double LIFT_KD = 0.0;

    public Lift(OpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {

        liftLeftPID = new PIDController(LIFT_KP, LIFT_KI, LIFT_KD, 0.9);
        liftRightPID = new PIDController(LIFT_KP, LIFT_KI, LIFT_KD, 0.9);

        rightLift = myOpMode.hardwareMap.get(DcMotor.class, "rightLift");
        leftLift = myOpMode.hardwareMap.get(DcMotor.class, "leftLift");

        leftLift.setDirection(DcMotor.Direction.REVERSE);
        rightLift.setDirection(DcMotor.Direction.FORWARD);

        // brake and encoders
        rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        myOpMode.telemetry.addData(">", "Lift Initialized");
    }

    public void teleOp() {
        update();

        if(myOpMode.gamepad1.dpad_up){
            liftMode = LiftMode.HIGH_CHAMBER;
        } else if (myOpMode.gamepad1.dpad_down){
            liftMode = LiftMode.GROUND;
        } else if (myOpMode.gamepad1.dpad_left){
            liftMode = LiftMode.LOW_CHAMBER;
        }

        //code defining behavior of lift in each state
        if (Math.abs(myOpMode.gamepad2.right_stick_y) > 0.2) {
            liftMode = LiftMode.MANUAL;
        }
        //if (touch)

        myOpMode.telemetry.addData("left_stick_y: ", -myOpMode.gamepad2.left_stick_y);
        myOpMode.telemetry.addData("Lift Mode ", liftMode);

    }

    public void update() {
        leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if (liftMode == LiftMode.HIGH_CHAMBER) {
            liftToPositionPIDClass(LIFT_HIGH_CHAMBER);
        } else if (liftMode == LiftMode.LOW_CHAMBER){
            liftToPositionPIDClass(LIFT_LOW_CHAMBER);
        } else if (liftMode == LiftMode.GROUND){
            liftToPositionPIDClass(LIFT_DOWN);
        } else if (liftMode == LiftMode.MANUAL){
            if (Math.abs(myOpMode.gamepad2.right_stick_y) > 0.1) {
                liftMode = LiftMode.MANUAL;
                leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                //robot.liftLeft.setPower(-0.8);
                //robot.liftRight.setPower(-0.8);
                rightLift.setPower(-myOpMode.gamepad2.right_stick_y); //
                leftLift.setPower(-myOpMode.gamepad2.right_stick_y); //
            } else {
                leftLift.setPower(0.07);
                rightLift.setPower(0.07);
            }
        }
        myOpMode.telemetry.addData("liftLeft", leftLift.getCurrentPosition());
        myOpMode.telemetry.addData("liftRight", rightLift.getCurrentPosition());
    }

    public void liftToPositionPIDClass(double targetPosition) {
        double outLeft = liftLeftPID.calculate(targetPosition, leftLift.getCurrentPosition());
        double outRight = liftRightPID.calculate(targetPosition, rightLift.getCurrentPosition());

        leftLift.setPower(outLeft);
        rightLift.setPower(outRight);

        myOpMode.telemetry.addData("lift", leftLift.getCurrentPosition());
        myOpMode.telemetry.addData("lift", rightLift.getCurrentPosition());

        myOpMode.telemetry.addData("LiftLeftPower: ", outLeft);
        myOpMode.telemetry.addData("LiftRightPower: ", outRight);
    }


}
