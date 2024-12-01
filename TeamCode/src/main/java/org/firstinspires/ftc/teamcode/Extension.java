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
    public static final double MAX_OUT = 0.5;
    public ElapsedTime delay = null;
    public double delayTime = 0.5;

    public enum ExtMode {
        MANUAL,
        INTAKE,
        NEAR,
        MID,
        FAR,
    }
    public Extension.ExtMode extMode = Extension.ExtMode.MANUAL;

    public Extension(LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init() {
        extension  = myOpMode.hardwareMap.get(DcMotor.class, "extension");
        extensionPID = new PIDController(EXT_KP, EXT_KI, EXT_KD, MAX_OUT);
    }
    public void teleOp(){
        if(Math.abs(myOpMode.gamepad2.right_stick_x) > 0.3){
            extMode = extMode.MANUAL;
        }
        if (extMode == Extension.ExtMode.MANUAL) {
            extension.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            if (Math.abs(myOpMode.gamepad2.right_stick_x) > 0.1) {
                extension.setPower(-myOpMode.gamepad2.right_stick_x);
            } else {
                extension.setPower(0);
            }
        }else if (extMode == Extension.ExtMode.FAR && delay.seconds()>delayTime) {
            extToPositionPIDClass(1700);
        }else if (extMode == Extension.ExtMode.MID && delay.seconds()>delayTime) {
            extToPositionPIDClass(1200);
        } else if (extMode == Extension.ExtMode.NEAR && delay.seconds()>delayTime) {
            extToPositionPIDClass(500);
        } else if (extMode == Extension.ExtMode.INTAKE && delay.seconds()>delayTime) {
            extToPositionPIDClass(0);
        }
        /*if(myOpMode.gamepad2.dpad_right){
            extension.setPower(0.5);
        }else if(myOpMode.gamepad2.dpad_left){
            extension.setPower(-0.5);
        }else{
            extension.setPower(0);
        }*/

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
