package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
public class Lift {
    private LinearOpMode myOpMode = null;
    public DcMotor liftLeft = null;
    public DcMotor liftRight = null;
    PIDController liftLeftPID;
    PIDController liftRightPID;

    public static final double LIFT_KP = 0.01;
    public static final double LIFT_KI = 0;
    public static final double LIFT_KD = 0;
    public static final double MAX_OUT = 0.5;

    public ElapsedTime delay = null;
    public double delayTime = 0.5;


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
    public void init() {
        liftLeft  = myOpMode.hardwareMap.get(DcMotor.class, "liftLeft");
        liftRight  = myOpMode.hardwareMap.get(DcMotor.class, "liftRight");
        liftLeft.setDirection(DcMotor.Direction.REVERSE);
        liftRight.setDirection(DcMotor.Direction.FORWARD);
        liftLeftPID = new PIDController(LIFT_KP, LIFT_KI, LIFT_KD);
        liftRightPID = new PIDController(LIFT_KP, LIFT_KI, LIFT_KD);
        liftLeftPID.maxOut = 0.95;
        liftRightPID.maxOut = 0.95;
    }
    public void teleOp(){
        if (Math.abs(myOpMode.gamepad2.right_stick_y) > 0.3) {
            liftMode = LiftMode.MANUAL;
        }

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
        } else if (liftMode == LiftMode.HIGH && delay.seconds()>delayTime) {
            liftToPositionPIDClass(2000);
        } else if (liftMode == LiftMode.MEDIUM && delay.seconds()>delayTime) {
            liftToPositionPIDClass(1000);
        } else if (liftMode == LiftMode.LOW && delay.seconds()>delayTime) {
            liftToPositionPIDClass(700);
        } else if (liftMode == LiftMode.GROUND) {
            liftToPositionPIDClass(0);
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

    }

    public void resetLift(double speed) {
        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if (liftLeft.getCurrentPosition() >= 5) {
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
