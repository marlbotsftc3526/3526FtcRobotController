package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
public class Extension {
    private LinearOpMode myOpMode = null;
    public DcMotor extension = null;
    PIDController extensionPID;
    public static final double EXT_KP = 0.01;
    public static final double EXT_KI = 0;
    public static final double EXT_KD = 0;
    public static final double MAX_OUT = 0.8;
    public ElapsedTime delay = null;
    public double delayTime = 0.5;

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
        extensionPID = new PIDController(EXT_KP, EXT_KI, EXT_KD, MAX_OUT);
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        extensionPID.reset();
    }
    public void teleOp(){
        myOpMode.telemetry.addData("extension: ", extension.getCurrentPosition());
        myOpMode.telemetry.addData("ext mode", extMode);
        if(Math.abs(myOpMode.gamepad2.right_stick_x) > 0.3 && myOpMode.gamepad2.left_bumper){
            extMode = extMode.MANUAL;
        }
        if (extMode == Extension.ExtMode.MANUAL) {
            extension.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            if (Math.abs(myOpMode.gamepad2.right_stick_x) > 0.1&& myOpMode.gamepad2.left_bumper) {
                extension.setPower(myOpMode.gamepad2.right_stick_x);
            } else {
                extension.setPower(0);
            }
        }else if (extMode == Extension.ExtMode.FAR ) {
            extToPositionPIDClass(4800);
        }else if (extMode == Extension.ExtMode.MID ) {
            extToPositionPIDClass(2600);
        } else if (extMode == Extension.ExtMode.NEAR) {
            extToPositionPIDClass(1350);
        } else if (extMode == Extension.ExtMode.FARBACK) {
            extToPositionPIDClass(0);
        } else if (extMode == Extension.ExtMode.HANG) {
            extToPositionPIDClass(530);
        } else if(extMode == Extension.ExtMode.INTAKE){
            extToPositionPIDClass(550);
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
            extToPositionPIDClass(4800);
        }else if (extMode == Extension.ExtMode.MID ) {
            extToPositionPIDClass(2600);
        } else if (extMode == Extension.ExtMode.NEAR) {
            extToPositionPIDClass(1350);
        } else if (extMode == Extension.ExtMode.INTAKE) {
            extToPositionPIDClass(0);
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
