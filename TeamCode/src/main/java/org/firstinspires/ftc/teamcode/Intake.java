package org.firstinspires.ftc.teamcode;
import static org.firstinspires.ftc.teamcode.MotionProfile.motionProfile;
import static org.firstinspires.ftc.teamcode.MotionProfile.motionProfileTime;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
public class Intake {
    private LinearOpMode myOpMode = null;

    public DcMotor intake = null;
    public Intake(LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init(){
        intake = myOpMode.hardwareMap.get(DcMotor.class, "intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setDirection(DcMotor.Direction.FORWARD);
    }
    public void teleOp(){
        int power = 0;
        if(myOpMode.gamepad2.dpad_left){
            intake.setPower(0.7);
        }else if(myOpMode.gamepad2.dpad_right) {
            intake.setPower(-0.7);
        }else if(myOpMode.gamepad2.dpad_up){
            intake.setPower(0);
        }

    }
}
