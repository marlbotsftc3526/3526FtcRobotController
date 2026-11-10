package org.firstinspires.ftc.teamcode.Subsystems;
import static java.lang.Math.PI;

import com.pedropathing.controllers.Controller;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.utility.PIDController;
public class Turret {
    private OpMode myOpMode = null;
    public CRServo turretRight = null;
    public CRServo turretLeft = null;
    public AnalogInput turretEncoder = null;
    public PIDController turretPID = null;
    Controller turretController;

    public double MIN_TURRET_SPEED = 0.06;
    public static double turretKP = 0.015;
    public double turretKI = 0;
    public double turretKD = 0;

    public double GEAR_RATIO = (double) 63 /127;
    public double ENCODER_MAX = 3.3;
    public double ENCODER_OFFSET = 1.825;
    public double WRAP_THRESHOLD = ENCODER_MAX/2;

    public double encoder_raw_value = ENCODER_OFFSET;
    public double previous_encoder_value = ENCODER_OFFSET;
    public int turn_count = 0;

    public enum TurretMode{
        MANUAL,
        AUTO,
        FIXED,
        CLOCKWISE,
        COUNTERCLOCKWISE
    }
    public TurretMode turretMode = TurretMode.FIXED;
    public Turret(OpMode opmode) {
        myOpMode = opmode;
    }

    public void init(){
        turretLeft = myOpMode.hardwareMap.get(CRServo.class, "turretLeft");
        turretRight = myOpMode.hardwareMap.get(CRServo.class, "turretRight");

        turretLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        turretRight.setDirection(DcMotorSimple.Direction.REVERSE);
        turretLeft.setPower(0);
        turretRight.setPower(0);

        turretEncoder = myOpMode.hardwareMap.get(AnalogInput.class, "turretEncoder");
        turretPID = new PIDController(turretKP, turretKI, turretKD, 0.8);
        turretController = Controller.pid(turretKP, turretKI, turretKD);

        encoder_raw_value = ENCODER_OFFSET;
        previous_encoder_value = ENCODER_OFFSET;
        turn_count = 0;
    }

    public void update(){
        encoder_raw_value = turretEncoder.getVoltage();
        double delta = encoder_raw_value - previous_encoder_value;
        if (delta > WRAP_THRESHOLD){
            turn_count--;
        } else if (delta < -WRAP_THRESHOLD){
            turn_count++;
        }
        previous_encoder_value = encoder_raw_value;

        double servoPosition = encoder_raw_value + (turn_count * ENCODER_MAX) - ENCODER_OFFSET;
        double servoAngle = 360/ENCODER_MAX * servoPosition;
        double turretAngle = servoAngle * GEAR_RATIO;
        myOpMode.telemetry.addData("turret encoder", turretEncoder.getVoltage());
        myOpMode.telemetry.addData("turn count", turn_count);
        myOpMode.telemetry.addData("servo position", servoPosition);
        myOpMode.telemetry.addData("servo angle", servoAngle);
        myOpMode.telemetry.addData("turd angle", turretAngle);
        myOpMode.telemetry.addData("turd mode", turretMode);


        if(turretMode == TurretMode.MANUAL){
            turretRight.setPower(-myOpMode.gamepad2.right_stick_x);
        }else if (turretMode == TurretMode.AUTO){

        }else if (turretMode == TurretMode.FIXED){
            turretLeft.setPower(0);
            turretRight.setPower(0);
        }else if (turretMode == TurretMode.CLOCKWISE){
            turretRight.setPower(0.25);
            turretLeft.setPower(0.25);
        }else if(turretMode == TurretMode.COUNTERCLOCKWISE){
            turretRight.setPower(-0.25);
            turretLeft.setPower(-0.25);
        }




    }

}
