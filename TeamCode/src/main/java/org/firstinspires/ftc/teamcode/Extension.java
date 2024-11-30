package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
public class Extension {
    private LinearOpMode myOpMode = null;
    public DcMotor extension = null;


    public Extension(LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init() {
        extension  = myOpMode.hardwareMap.get(DcMotor.class, "extension");
    }
    public void teleOp(){
        if(myOpMode.gamepad2.dpad_right){
            extension.setPower(0.5);
        }else if(myOpMode.gamepad2.dpad_left){
            extension.setPower(-0.5);
        }else{
            extension.setPower(0);
        }

    }
}
