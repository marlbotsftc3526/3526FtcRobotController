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
    public static final double openOPEN = 0;
    public static final double openCLOSE = 0.45;

    public static final double pivotDOWN = 1;
    public static final double pivotSCORE = 0.7;

    public static final double pivotBACK = 1;

    public static final double spinA = 0;
    public static final double spinB = 0.7;
    public Claw(LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init() {
        clawOpen = myOpMode.hardwareMap.get(Servo.class, "clawOpen");
        clawPivot = myOpMode.hardwareMap.get(Servo.class, "clawPivot");
        clawSpin = myOpMode.hardwareMap.get(Servo.class, "clawSpin");
    }
    public void teleOp(){
        clawSpin.setPosition(spinA);
        if(myOpMode.gamepad2.right_trigger > 0.1 && myOpMode.gamepad2.left_trigger > 0.1){
            clawOpen.setPosition(openOPEN);
        }
        if(myOpMode.gamepad2.right_trigger > 0.1 ^ myOpMode.gamepad2.left_trigger > 0.1){
            clawOpen.setPosition(openCLOSE);
        }
        /*if(myOpMode.gamepad2.left_bumper){
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
        }*/
    }


}
