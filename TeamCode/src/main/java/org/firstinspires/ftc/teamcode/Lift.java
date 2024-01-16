package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.TouchSensor;
import static org.firstinspires.ftc.teamcode.MotionProfile.motionProfile;
import static org.firstinspires.ftc.teamcode.MotionProfile.motionProfileTime;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
@Config
public class Lift {
    private LinearOpMode myOpMode = null;

    public DcMotor liftRight = null;
    public DcMotor liftLeft = null;

    public Servo boxLeft = null;
    public Servo boxRight = null;

    public Servo gateLeft = null;
    public Servo gateRight = null;

    PIDController liftLeftPID;
    PIDController liftRightPID;

    public TouchSensor touch = null;

    public static final double LIFT_KP = 0.01;
    public static final double LIFT_KI = 0;
    public static final double LIFT_KD = 0;
    public static final double MAX_OUT = 0.5;
    public static double BOX_intake = 0.065;
    public static double BOX_score = 0.7;
    public static double DIFF1 = 0.93;
    public static double DIFF2 = 0.93;

    public static final double GATE_down = 0;
    public static final double GATE_up = 0.3;

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

        boxLeft = myOpMode.hardwareMap.get(Servo.class, "boxLeft");
        boxRight = myOpMode.hardwareMap.get(Servo.class, "boxRight");
        gateLeft = myOpMode.hardwareMap.get(Servo.class, "gateLeft");
        gateRight = myOpMode.hardwareMap.get(Servo.class, "gateRight");

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

        gateLeft.setPosition(GATE_down);
        gateRight.setPosition(GATE_up);

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
                if(touch.isPressed()){
                    liftLeft.setPower(Math.max(0, -myOpMode.gamepad2.right_stick_y));
                    liftRight.setPower(Math.max(0, -myOpMode.gamepad2.right_stick_y));
                }else{
                    liftLeft.setPower(-myOpMode.gamepad2.right_stick_y);
                    liftRight.setPower(-myOpMode.gamepad2.right_stick_y);
                }


            } else {
                liftLeft.setPower(0);
                liftRight.setPower(0);
            }
        } else if (liftMode == LiftMode.HIGH) {
            liftToPositionPIDClass(3000);
        } else if (liftMode == LiftMode.MEDIUM) {
            liftToPositionPIDClass(2000);
        } else if (liftMode == LiftMode.LOW) {
            liftToPositionPIDClass(1000);
        } else if (liftMode == LiftMode.GROUND) {
            resetLift(-0.8);
        }

    }

    public void liftToPositionPIDClass(double targetPosition) {
        double outLeft = liftLeftPID.calculate(targetPosition, liftLeft.getCurrentPosition());
        double outRight = liftRightPID.calculate(targetPosition, liftRight.getCurrentPosition());

        if(outLeft >= 0) {
            liftLeft.setPower(Math.min(MAX_OUT, outLeft));
            liftRight.setPower(Math.min(MAX_OUT, outRight));
        }else{
            liftLeft.setPower(Math.max(-MAX_OUT, outLeft));
            liftRight.setPower(Math.max(-MAX_OUT, outRight));
        }
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
