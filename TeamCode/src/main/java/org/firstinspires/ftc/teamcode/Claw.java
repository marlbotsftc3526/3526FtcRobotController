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
    public static final double openOPEN = 0.4; //0

    public static final double openMID = 0.7;
    public static final double openCLOSE = 1;

    public static final double pivotDOWN = 1;
    public static final double pivotSCORE = 0.68;

    public static final double pivotUP = 0.45;

    public static final double pivotSCOREPOS = 0.55;

    public static final double pivotBACK = 0.1;

    public static final double spinA = 0.36;//0.5
    public static final double spinB = 0;//0

    public ElapsedTime clawruntime = new ElapsedTime();
    public Claw(LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init() {

        clawOpen = myOpMode.hardwareMap.get(Servo.class, "clawOpen");
        clawPivot = myOpMode.hardwareMap.get(Servo.class, "clawPivot");
        clawSpin = myOpMode.hardwareMap.get(Servo.class, "clawSpin");
        clawSpin.setPosition(spinA);
        clawOpen.setPosition(openOPEN);
        clawruntime= new ElapsedTime();
    }
    public void teleOp(){

        myOpMode.telemetry.addData("clawSpin: ", clawSpin.getPosition());
        if((myOpMode.gamepad2.right_trigger > 0.1 && myOpMode.gamepad2.left_trigger > 0.1) && !myOpMode.gamepad2.left_bumper){
            clawOpen.setPosition(openOPEN);
            clawruntime.reset();
        }
        if((myOpMode.gamepad2.right_trigger > 0.1 && myOpMode.gamepad2.left_trigger > 0.1) && myOpMode.gamepad2.left_bumper){
            clawOpen.setPosition(openMID);
        }
        if((myOpMode.gamepad2.right_trigger > 0.1 ^ myOpMode.gamepad2.left_trigger > 0.1) && !myOpMode.gamepad2.left_bumper){
            clawOpen.setPosition(openCLOSE);
        }
    }


}
