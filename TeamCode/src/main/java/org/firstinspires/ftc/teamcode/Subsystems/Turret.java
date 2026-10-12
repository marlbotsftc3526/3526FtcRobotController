package org.firstinspires.ftc.teamcode.Subsystems;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Subsystems.PIDController;
public class Turret {
    private OpMode myOpMode = null;
    public CRServo turretRight = null;
    public CRServo turretLeft = null;
    public AnalogInput turretEncoder = null;
    public PIDController turretPID = null;

    public double MIN_TURRET_SPEED = 0.06;
    public static double turretKP = 0.015;
    public double turretKI = 0;
    public double turretKD = 0;

    public enum TurretMode{
        MANUAL,
        AUTO,
        FIXED
    }
    public TurretMode turretMode = TurretMode.FIXED;

    public void init(){
        turretLeft = myOpMode.hardwareMap.get(CRServo.class, "turretLeft");
        turretRight = myOpMode.hardwareMap.get(CRServo.class, "turretRight");

        turretLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        turretRight.setDirection(DcMotorSimple.Direction.REVERSE);
        turretLeft.setPower(0);
        turretRight.setPower(0);

        turretEncoder = myOpMode.hardwareMap.get(AnalogInput.class, "turretEncoder");
        turretPID = new PIDController(turretKP, turretKI, turretKD, 0.8);

    }

    public void update(){
        if(turretMode == TurretMode.MANUAL){
            turretRight.setPower(-myOpMode.gamepad2.right_stick_x);
        }else if (turretMode == TurretMode.MANUAL){

        }else if (turretMode == TurretMode.FIXED){
            turretLeft.setPower(0);
        }
    }

}
