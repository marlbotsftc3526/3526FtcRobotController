package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
public class Claw {
    private LinearOpMode myOpMode = null;
    public Servo clawOpen = null;
    public Servo clawPivot = null;
    public Claw(LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init() {
        clawOpen = myOpMode.hardwareMap.get(Servo.class, "clawOpen");
        clawPivot = myOpMode.hardwareMap.get(Servo.class, "clawPivot");
    }
    public void teleOp(){
        if(myOpMode.gamepad1.x){
            clawOpen.setPosition(0.3);
        }
        if(myOpMode.gamepad1.b){
            clawOpen.setPosition(0.6);
        }
        if(myOpMode.gamepad1.dpad_right){
            clawPivot.setPosition(0.05);
        }
        if (myOpMode.gamepad1.dpad_left) {
            clawPivot.setPosition(0.5);
        }
    }


}
