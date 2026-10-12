package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


public class Launcher {
    private OpMode myOpMode = null;

    //lift motors
    public DcMotorEx rightLauncher = null;
    public DcMotorEx leftLauncher = null;

    static double revolutions_per_minute = 1000;
    static final double TICKS_PER_REVOLUTION = 28;
    double ticks_per_second;

    public enum LauncherMode {
        ON,
        OFF,
    }

    public LauncherMode launcherMode = LauncherMode.OFF;

    public Launcher(OpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {
        rightLauncher = myOpMode.hardwareMap.get(DcMotorEx.class, "rightLauncher");
        leftLauncher = myOpMode.hardwareMap.get(DcMotorEx.class, "leftLauncher");


        rightLauncher.setDirection(DcMotorSimple.Direction.FORWARD);
        leftLauncher.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void teleOp(){
        update();
    }

    public void update() {
    if(launcherMode == LauncherMode.ON) {
        ticks_per_second = revolutions_per_minute / 60 * TICKS_PER_REVOLUTION;
        rightLauncher.setVelocity(ticks_per_second);
        leftLauncher.setVelocity(ticks_per_second);
    }else if(launcherMode == LauncherMode.OFF){
        rightLauncher.setVelocity(0);
        leftLauncher.setVelocity(0);
    }
        double leftMeasuredRPM = leftLauncher.getVelocity() / TICKS_PER_REVOLUTION * 60;
        double rightMeasuredRPM = rightLauncher.getVelocity() / TICKS_PER_REVOLUTION * 60;

        myOpMode.telemetry.addData("Set RPM", revolutions_per_minute);
        myOpMode.telemetry.addData("leftMeasured RPM", leftMeasuredRPM);
        myOpMode.telemetry.addData("rightMeasured RPM", rightMeasuredRPM);
        myOpMode.telemetry.update();


    }


}
