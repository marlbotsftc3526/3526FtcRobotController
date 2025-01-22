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

    public static final double openMID = 0.45;
    public static final double openCLOSE = 0.7;

    public static final double pivotDOWN = 1;
    public static final double pivotSCORE = 0.75;

    public static final double pivotUP = 0.45;

    public static final double pivotSCOREPOS = 0.55;

    public static final double pivotBACK = 0;

    public static final double spinA = 0;
    public static final double spinB = 0.6;
    public Claw(LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init() {

        clawOpen = myOpMode.hardwareMap.get(Servo.class, "clawOpen");
        clawPivot = myOpMode.hardwareMap.get(Servo.class, "clawPivot");
        clawSpin = myOpMode.hardwareMap.get(Servo.class, "clawSpin");
        clawSpin.setPosition(spinA);
        clawOpen.setPosition(openCLOSE);
    }
    public void teleOp(){


        if(myOpMode.gamepad2.right_trigger > 0.1 && myOpMode.gamepad2.left_trigger > 0.1){
            clawOpen.setPosition(openOPEN);
        }
        if(myOpMode.gamepad2.right_trigger > 0.1 ^ myOpMode.gamepad2.left_trigger > 0.1){
            clawOpen.setPosition(openCLOSE);
        }
    }


}
