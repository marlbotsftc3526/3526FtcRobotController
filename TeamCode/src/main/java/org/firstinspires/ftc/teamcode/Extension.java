package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
public class Extension {
    private LinearOpMode myOpMode = null;
    public DcMotor extension = null;
    PIDController extensionPID;
    TouchSensor extensionTouchSensor;
    public static final double EXT_KP = 0.01;
    public static final double EXT_KI = 0;
    public static final double EXT_KD = 0;
    public static final double MAX_OUT = 0.8;
    public ElapsedTime delay = null;
    public double delayTime = 0.5;

    public static final double farpos = 2600;
    public static final double midpos = 2000;
    public static final double nearpos = 1650;
    public static final double hangpos = 50;
    public static final double farbackpos= 0;

    public enum ExtMode {
        MANUAL,
        INTAKE,
        NEAR,
        MID,
        FAR,
        HANG,
        FARBACK
    }
    public Extension.ExtMode extMode = Extension.ExtMode.MANUAL;

    public Extension(LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init() {
        extension  = myOpMode.hardwareMap.get(DcMotor.class, "extension");
        extensionTouchSensor = myOpMode.hardwareMap.get(TouchSensor.class, "extensionLimitSwitch");
        extensionPID = new PIDController(EXT_KP, EXT_KI, EXT_KD, MAX_OUT);
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        extensionPID.reset();
    }
    public void teleOp(){
        myOpMode.telemetry.addData("extension: ", extension.getCurrentPosition());
        myOpMode.telemetry.addData("ext mode", extMode);
        if(extensionTouchSensor.isPressed()){
            extension.setPower(0);
            extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        if(Math.abs(myOpMode.gamepad1.right_stick_x) > 0.2 || (Math.abs(myOpMode.gamepad2.right_stick_y) > 0.1&& myOpMode.gamepad2.left_bumper)){
            extMode = extMode.MANUAL;
        }
        if (extMode == Extension.ExtMode.MANUAL) {
            extension.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            if (Math.abs(myOpMode.gamepad2.right_stick_y) > 0.1&& myOpMode.gamepad2.left_bumper) {
                if((myOpMode.gamepad2.right_stick_y >0.1 && extensionTouchSensor.isPressed()) ||
                        (myOpMode.gamepad2.right_stick_y < -0.1 && extension.getCurrentPosition() > farpos)){
                    extension.setPower(0);
                }
                else{
                    extension.setPower(-myOpMode.gamepad2.right_stick_y);
                }
            }else if(Math.abs(myOpMode.gamepad1.right_stick_x) > 0.1 && !myOpMode.gamepad1.left_bumper){
                if((myOpMode.gamepad1.right_stick_x > 0.1 && extensionTouchSensor.isPressed()) ||
                        (myOpMode.gamepad1.right_stick_x < -0.1 && extension.getCurrentPosition() > farpos)){
                    extension.setPower(0);
                }
                else{
                    extension.setPower(-myOpMode.gamepad1.right_stick_x);
                }
            }else {
                extension.setPower(0);
            }
        }else if (extMode == Extension.ExtMode.FAR ) {
            extToPositionPIDClass(farpos);
        }else if (extMode == Extension.ExtMode.MID ) {
            extToPositionPIDClass(midpos);
        } else if (extMode == Extension.ExtMode.NEAR) {
            extToPositionPIDClass(nearpos);
        } else if (extMode == Extension.ExtMode.HANG) {
            extToPositionPIDClass(hangpos);
        } else if (extMode == Extension.ExtMode.FARBACK) {
            extToPositionPIDClass(farbackpos);
        }
        /*if(myOpMode.gamepad2.dpad_right){
            extension.setPower(0.5);
        }else if(myOpMode.gamepad2.dpad_left){
            extension.setPower(-0.5);
        }else{
            extension.setPower(0);
        }*/

    }
    public void update(){
        if (extMode == Extension.ExtMode.FAR ) {
            extToPositionPIDClass(farpos);
        }else if (extMode == Extension.ExtMode.MID ) {
            extToPositionPIDClass(midpos);
        } else if (extMode == Extension.ExtMode.NEAR) {
            extToPositionPIDClass(nearpos);
        } else if (extMode == Extension.ExtMode.HANG) {
            extToPositionPIDClass(hangpos);
        } else if (extMode == Extension.ExtMode.FARBACK) {
            extToPositionPIDClass(farbackpos);
        }
    }
    public void extToPositionPIDClass(double targetPosition) {
        double outLeft = extensionPID.calculate(targetPosition, extension.getCurrentPosition());

        if(outLeft >= 0) {
            extension.setPower(Math.min(MAX_OUT, outLeft));
        }else{
            extension.setPower(Math.max(-MAX_OUT, outLeft));
        }

    }
}
