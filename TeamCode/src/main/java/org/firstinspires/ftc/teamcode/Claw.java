package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
public class Claw {
    private LinearOpMode myOpMode = null;
    public Servo clawOpen = null;
    public Servo clawPivot = null;

    public Servo clawSpin = null;
    public Claw(LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init() {
        clawOpen = myOpMode.hardwareMap.get(Servo.class, "clawOpen");
        clawPivot = myOpMode.hardwareMap.get(Servo.class, "clawPivot");
        clawSpin = myOpMode.hardwareMap.get(Servo.class, "clawSpin");
    }
    public void teleOp(){
        clawSpin.setPosition(0);
        if(myOpMode.gamepad2.left_bumper){
            clawOpen.setPosition(0);
        }
        if(myOpMode.gamepad2.right_bumper){
            clawOpen.setPosition(0.45);
        }
        if(myOpMode.gamepad2.x){
            clawPivot.setPosition(0.22);
        }
        if(myOpMode.gamepad2.y){
            clawPivot.setPosition(0.7);
        }
        if (myOpMode.gamepad2.b) {
            clawPivot.setPosition(1);
        }
    }


}
