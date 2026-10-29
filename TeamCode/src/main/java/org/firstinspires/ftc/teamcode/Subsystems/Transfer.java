package org.firstinspires.ftc.teamcode.Subsystems;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Transfer {
    private OpMode myOpMode = null;

    public DcMotor transfer = null;
    public Servo gate = null;

    public static final double GATEOPEN = 0.7;
    public static final double GATECLOSE = 0.4;
    public double gatePosition = GATECLOSE;
    public static final double TRANSFERUP = -1;
    public static final double TRANSFERDOWN = 1;
    public static final double TRANSFERLAUNCH = 20;
    public enum TransferMode{
        UP,
        DOWN,
        LAUNCH,
        OFF
    }

    public TransferMode transferMode = TransferMode.OFF;
    public Transfer(OpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {
        transfer = myOpMode.hardwareMap.get(DcMotor.class, "transfer");
        gate = myOpMode.hardwareMap.get(Servo.class, "gate");
    }

    public void update(){
        if (transferMode == TransferMode.UP){
            transfer.setPower(TRANSFERUP);
        }else if (transferMode == TransferMode.DOWN) {
            transfer.setPower(TRANSFERDOWN);
        }else if(transferMode == TransferMode.LAUNCH){
            transfer.setPower(TRANSFERLAUNCH);
        }else if (transferMode == TransferMode.OFF){
            transfer.setPower(0);
        }
        gate.setPosition(gatePosition);
    }
}
