package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.TouchSensor;
import static org.firstinspires.ftc.teamcode.MotionProfile.motionProfile;
import static org.firstinspires.ftc.teamcode.MotionProfile.motionProfileTime;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

public class Lift {
    private LinearOpMode myOpMode = null;

    public DcMotor liftRight = null;
    public DcMotor liftLeft = null;

    public Servo box = null;

    PIDController liftLeftPID;
    PIDController liftRightPID;

    public TouchSensor touch = null;

    public static final double LIFT_KP = 0.01;
    public static final double LIFT_KI = 0;
    public static final double LIFT_KD = 0;

    public static final double BOX_intake = 0;
    public static final double BOX_score = 0.8;

    public double boxPosition;

    //if the subsystem has explicit states, it can be helpful to use an enum to define them
    public enum LiftMode {
        MANUAL,
        HIGH,
        MEDIUM,
        LOW,
        GROUND
    }
    public LiftMode liftMode = LiftMode.MANUAL;
    public Lift(LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init(){
        liftLeftPID = new PIDController(LIFT_KP, LIFT_KI, LIFT_KD);
        liftRightPID = new PIDController(LIFT_KP, LIFT_KI, LIFT_KD);

        box = myOpMode.hardwareMap.get(Servo.class, "box");

        touch = myOpMode.hardwareMap.get(TouchSensor.class, "touch");

        liftLeftPID.maxOut = 0.95;
        liftRightPID.maxOut = 0.95;

        boxPosition = BOX_intake;

        liftRight = myOpMode.hardwareMap.get(DcMotor.class, "liftRight");
        liftLeft = myOpMode.hardwareMap.get(DcMotor.class, "liftLeft");
        liftLeft.setDirection(DcMotor.Direction.FORWARD);
        liftRight.setDirection(DcMotor.Direction.REVERSE);

        // brake and encoders
        liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        myOpMode.telemetry.addData(">", "Lift Initialized");

        resetLiftPID();

    }
    public void teleOp(){

        if (Math.abs(myOpMode.gamepad2.right_stick_y) > 0.3) {
            liftMode = LiftMode.MANUAL;
        }

        myOpMode.telemetry.addData("touch", "Pressed: " + touch.isPressed());
        if (touch.isPressed()) {
            liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        //code defining behavior of lift in each state
        if (liftMode == LiftMode.MANUAL) {
            liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            if (Math.abs(myOpMode.gamepad2.right_stick_y) > 0.1) {
                liftLeft.setPower(-myOpMode.gamepad2.right_stick_y);
                liftRight.setPower(-myOpMode.gamepad2.right_stick_y);
            } else {
                liftLeft.setPower(0);
                liftRight.setPower(0);
            }
        } else if (liftMode == LiftMode.HIGH) {
            liftToPositionPIDClass(3700);
        } else if (liftMode == LiftMode.MEDIUM) {
            liftToPositionPIDClass(2284);
        } else if (liftMode == LiftMode.LOW) {
            liftToPositionPIDClass(1409);
        } else if (liftMode == LiftMode.GROUND) {
            resetLift(-0.8);
        }
    }

    public void liftToPositionPIDClass(double targetPosition) {
        double outLeft = liftLeftPID.calculate(targetPosition, liftLeft.getCurrentPosition());
        double outRight = liftRightPID.calculate(targetPosition, liftRight.getCurrentPosition());

        liftLeft.setPower(outLeft);
        liftRight.setPower(outRight);

        myOpMode.telemetry.addData("LiftLeftPower: ", outLeft);
        myOpMode.telemetry.addData("LiftRightPower: ", outRight);
    }

    public void resetLift(double speed) {
        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if (touch.isPressed() == false) {
            liftLeft.setPower(speed);
            liftRight.setPower(speed);
        } else {
            liftLeft.setPower(0);
            liftRight.setPower(0);
        }

    }

    public void resetLiftPID() {
        liftLeftPID.reset();
        liftRightPID.reset();
    }
}
