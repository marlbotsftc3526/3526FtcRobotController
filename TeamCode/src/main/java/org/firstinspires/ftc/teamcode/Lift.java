package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
public class Lift {
    private LinearOpMode myOpMode = null;
    public DcMotor liftLeft = null;
    public DcMotor liftRight = null;
    public DcMotor liftCenter = null;
    PIDController liftLeftPID;
    PIDController liftRightPID;
    PIDController liftCenterPID;
    TouchSensor liftTouchSensor;


    public static final double LIFT_KP = 0.01;
    public static final double LIFT_KI = 0;
    public static final double LIFT_KD = 0;
    public static final double MAX_OUT = 1;
    public static final double MAX_OUT_DOWN = 0.2;

    public static final double highchampos = 1100;
    public static final double highchamautopos = 900;
    public static final double highchamscorepos = 2100;
    public static final double highbucketpos = 2850; //1689 for low
    //change groundpos to 15 or 30 possibly
    public static final double groundpos = 15; //15
    public static final double submersiblepos = 250;
    public static boolean isHang = false;

    public ElapsedTime delay = null;
    public double delayTime = 0.5;


    public enum LiftMode {
        MANUAL,
        HIGH_CHAMBER,
        HIGH_CHAMBER_SCORE,
        HIGH_CHAMBER_AUTO,
        HIGH_BUCKET,
        GROUND,
        SUBMERSIBLE
    }
    public LiftMode liftMode = LiftMode.MANUAL;
    public Lift(LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init() {
        liftLeft  = myOpMode.hardwareMap.get(DcMotor.class, "liftLeft");
        liftRight  = myOpMode.hardwareMap.get(DcMotor.class, "liftRight");
        liftCenter = myOpMode.hardwareMap.get(DcMotor.class, "liftCenter");
        liftTouchSensor = myOpMode.hardwareMap.get(TouchSensor.class, "liftLimitSwitch");
        liftLeft.setDirection(DcMotor.Direction.REVERSE);
        liftRight.setDirection(DcMotor.Direction.FORWARD);
        liftCenter.setDirection(DcMotor.Direction.FORWARD);



        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftCenter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftLeftPID = new PIDController(LIFT_KP, LIFT_KI, LIFT_KD, MAX_OUT);
        liftRightPID = new PIDController(LIFT_KP, LIFT_KI, LIFT_KD, MAX_OUT);
        liftCenterPID = new PIDController(LIFT_KP, LIFT_KI, LIFT_KD, MAX_OUT);

        resetLiftPID();
    }
    public void initTeleOp() {
        liftLeft  = myOpMode.hardwareMap.get(DcMotor.class, "liftLeft");
        liftRight  = myOpMode.hardwareMap.get(DcMotor.class, "liftRight");
        liftCenter = myOpMode.hardwareMap.get(DcMotor.class, "liftCenter");
        liftTouchSensor = myOpMode.hardwareMap.get(TouchSensor.class, "liftLimitSwitch");
        liftLeft.setDirection(DcMotor.Direction.REVERSE);
        liftRight.setDirection(DcMotor.Direction.FORWARD);
        liftCenter.setDirection(DcMotor.Direction.FORWARD);
        liftLeftPID = new PIDController(LIFT_KP, LIFT_KI, LIFT_KD, MAX_OUT);
        liftRightPID = new PIDController(LIFT_KP, LIFT_KI, LIFT_KD, MAX_OUT);
        liftCenterPID = new PIDController(LIFT_KP, LIFT_KI, LIFT_KD, MAX_OUT);
    }
    public void teleOp(){
        myOpMode.telemetry.addData("liftLeft: ", liftLeft.getCurrentPosition());
        myOpMode.telemetry.addData("liftRight: ", liftRight.getCurrentPosition());
        myOpMode.telemetry.addData("liftCenter: ", liftCenter.getCurrentPosition());
        myOpMode.telemetry.addData("lift mode", liftMode);
        if (Math.abs(myOpMode.gamepad1.right_stick_y) > 0.3) {
            liftMode = LiftMode.MANUAL;
        }


        if (liftMode == LiftMode.MANUAL) {
            if(liftTouchSensor.isPressed()){
                liftLeft.setPower(0);
                liftRight.setPower(0);
                liftCenter.setPower(0);
                liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                liftCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                liftCenter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }


            if (Math.abs(myOpMode.gamepad1.right_stick_y) > 0.1 && liftLeft.getCurrentPosition() < 2900) {
                if(myOpMode.gamepad1.right_stick_y > 0 && liftTouchSensor.isPressed() ){
                    liftLeft.setPower(0);
                    liftRight.setPower(0);
                    liftCenter.setPower(0);
                }else if((myOpMode.gamepad1.right_stick_y > 0 && !liftTouchSensor.isPressed()) && !isHang){
                    //liftLeft.setPower(Math.max(-MAX_OUT_DOWN,-myOpMode.gamepad1.right_stick_y/3));
                    liftLeft.setPower(-MAX_OUT_DOWN);
                    //liftRight.setPower(Math.max(-MAX_OUT_DOWN,-myOpMode.gamepad1.right_stick_y/3));
                    liftRight.setPower(-MAX_OUT_DOWN);
                    //liftCenter.setPower(Math.max(-MAX_OUT_DOWN,-myOpMode.gamepad1.right_stick_y/3));
                    liftCenter.setPower(-MAX_OUT_DOWN);
                }else if(isHang){
                    liftLeft.setPower(-myOpMode.gamepad1.right_stick_y);
                    liftRight.setPower(-myOpMode.gamepad1.right_stick_y);
                    liftCenter.setPower(-myOpMode.gamepad1.right_stick_y);
                }else {
                    liftLeft.setPower(-myOpMode.gamepad1.right_stick_y/1.5);
                    liftRight.setPower(-myOpMode.gamepad1.right_stick_y/1.5);
                    liftCenter.setPower(-myOpMode.gamepad1.right_stick_y/1.5);
                }
            } else {
                if(liftLeft.getCurrentPosition() < 80 || liftRight.getCurrentPosition() < 80){
                    liftLeft.setPower(0);
                    liftRight.setPower(0);
                    liftCenter.setPower(0);

                }else{
                    liftLeft.setPower(0.1);
                    liftRight.setPower(0.1);
                    liftCenter.setPower(0.1);

                }
            }
        } else if (liftMode == LiftMode.HIGH_CHAMBER) {
            liftToPositionPIDClass(highchampos);
        }else if (liftMode == LiftMode.HIGH_CHAMBER_SCORE) {
            liftToPositionPIDClass(highchamscorepos);
        }else if (liftMode == LiftMode.HIGH_BUCKET) {
            //originally 2950
            liftToPositionPIDClass(highbucketpos);
        }else if (liftMode == LiftMode.GROUND) {
            liftToPositionPIDClass(groundpos);
            if(liftTouchSensor.isPressed()){
                liftLeft.setPower(0);
                liftRight.setPower(0);
                liftCenter.setPower(0);
                liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                liftCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                liftCenter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        }else if(liftMode == LiftMode.SUBMERSIBLE){
            liftToPositionPIDClass(submersiblepos);
        }else if(liftMode == LiftMode.HIGH_CHAMBER_AUTO){
            liftToPositionPIDClass(highchamautopos);
        }

    }
    public void update(){
        if (liftMode == LiftMode.HIGH_CHAMBER) {
            liftToPositionPIDClass(highchampos);
        }else if (liftMode == LiftMode.HIGH_CHAMBER_SCORE) {
            liftToPositionPIDClass(highchamscorepos);
        } else if (liftMode == LiftMode.HIGH_BUCKET) {
            liftToPositionPIDClass(highbucketpos);
        }else if (liftMode == LiftMode.GROUND) {
            liftToPositionPIDClass(groundpos);
            if(liftTouchSensor.isPressed()){
                liftLeft.setPower(0);
                liftRight.setPower(0);
                liftCenter.setPower(0);
                liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                liftCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                liftCenter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        }else if(liftMode == LiftMode.SUBMERSIBLE){
            liftToPositionPIDClass(submersiblepos);
        }
        else if(liftMode == LiftMode.HIGH_CHAMBER_AUTO){
            liftToPositionPIDClass(highchamautopos);
        }
    }

    public void liftToPositionPIDClass(double targetPosition) {
        double outCenter = liftCenterPID.calculate(targetPosition, liftCenter.getCurrentPosition());


        if(outCenter >= 0) {
            liftLeft.setPower(Math.min(MAX_OUT, outCenter));
            liftRight.setPower(Math.min(MAX_OUT, outCenter));
            liftCenter.setPower(Math.min(MAX_OUT, outCenter));

        }else{
            liftLeft.setPower(Math.max(-MAX_OUT_DOWN, outCenter));
            liftRight.setPower(Math.max(-MAX_OUT_DOWN, outCenter));
            liftCenter.setPower(Math.max(-MAX_OUT_DOWN, outCenter));

        }

    }

    public void resetLift(double speed) {
        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftCenter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if (liftLeft.getCurrentPosition() >= 5) {
            liftLeft.setPower(speed);
            liftRight.setPower(speed);
            liftCenter.setPower(speed);
        } else {
            liftLeft.setPower(0);
            liftRight.setPower(0);
            liftCenter.setPower(0);
        }

    }


    public void resetLiftPID() {
        liftLeftPID.reset();
        liftRightPID.reset();
        liftCenterPID.reset();
    }
}
