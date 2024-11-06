package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
public class Lift {
    private LinearOpMode myOpMode = null;
    public DcMotor liftLeft = null;
    public DcMotor liftRight = null;


    public Lift(LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init() {
        liftLeft  = myOpMode.hardwareMap.get(DcMotor.class, "liftLeft");
        liftRight  = myOpMode.hardwareMap.get(DcMotor.class, "liftRight");
        liftLeft.setDirection(DcMotor.Direction.REVERSE);
        liftRight.setDirection(DcMotor.Direction.FORWARD);
    }
    public void teleOp(){
        if(myOpMode.gamepad1.dpad_up){
            liftLeft.setPower(0.4);
            liftRight.setPower(0.4);
        }else if(myOpMode.gamepad1.dpad_down){
            liftLeft.setPower(-0.4);
            liftRight.setPower(-0.4);
        }else{
            liftLeft.setPower(0);
            liftRight.setPower(0);
        }

    }
}
