package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

public class ActiveClaw {
    private LinearOpMode myOpMode = null;
    public Servo clawActive = null;
    public Servo clawPivot = null;

    public Servo clawSpin = null;
    public static final double activeIn = 0;
    public static final double activeOut = 1;
    public static final double activeNo = 0.5;

    public static final double pivotDOWN = 1;
    public static final double pivotSCORE = 0.75;

    public static final double pivotUP = 0.45;

    public static final double pivotSCOREPOS = 0.55;

    public static final double pivotBACK = 0;

    public static final double spinA = 0;
    public static final double spinB = 0.5;

    public ActiveClaw(LinearOpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {

        clawActive = myOpMode.hardwareMap.get(Servo.class, "clawOpen");
        clawPivot = myOpMode.hardwareMap.get(Servo.class, "clawPivot");
        clawSpin = myOpMode.hardwareMap.get(Servo.class, "clawSpin");
        clawSpin.setPosition(spinA);
    }

    public void teleOp() {

        if (myOpMode.gamepad2.left_trigger > 0.1) {
            clawActive.setPosition(activeOut);
        } else if (myOpMode.gamepad2.right_trigger > 0.1) {
            clawActive.setPosition(activeIn);
        } else {
            clawActive.setPosition(activeNo);
        }
    }

}



