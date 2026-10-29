package org.firstinspires.ftc.teamcode.Subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


public class Shooter{
    private OpMode myOpMode = null;

    //lift motors
    public DcMotorEx rightShooter = null;
    public DcMotorEx leftShooter = null;

    public double revolutions_per_minute = 1000;
    static final double TICKS_PER_REVOLUTION = 28;
    double ticks_per_second;

    public enum ShooterMode {
        AUTO,
        FIXED,
        OFF,
    }

    public ShooterMode shooterMode = ShooterMode.OFF;

    public Shooter(OpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {
        rightShooter = myOpMode.hardwareMap.get(DcMotorEx.class, "rightLauncher");
        leftShooter = myOpMode.hardwareMap.get(DcMotorEx.class, "leftLauncher");


        rightShooter.setDirection(DcMotorSimple.Direction.FORWARD);
        leftShooter.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void teleOp(){
        update();
    }

    public void update() {
        if(shooterMode == shooterMode.FIXED){
            ticks_per_second = revolutions_per_minute / 60 * TICKS_PER_REVOLUTION;
            rightShooter.setVelocity(ticks_per_second);
            leftShooter.setVelocity(ticks_per_second);
        }else if(shooterMode == shooterMode.OFF){
            rightShooter.setVelocity(0);
            leftShooter.setVelocity(0);
        }
        double leftMeasuredRPM = leftShooter.getVelocity() / TICKS_PER_REVOLUTION * 60;
        double rightMeasuredRPM = rightShooter.getVelocity() / TICKS_PER_REVOLUTION * 60;

        myOpMode.telemetry.addData("Set RPM", revolutions_per_minute);
        myOpMode.telemetry.addData("leftMeasured RPM", leftMeasuredRPM);
        myOpMode.telemetry.addData("rightMeasured RPM", rightMeasuredRPM);
        myOpMode.telemetry.update();


    }


}