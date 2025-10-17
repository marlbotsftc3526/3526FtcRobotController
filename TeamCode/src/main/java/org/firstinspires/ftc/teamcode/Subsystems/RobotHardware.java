package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class RobotHardware {
    private LinearOpMode myOpMode = null;

    public Drivetrain drivetrain;
    public Intake intake;
    public Shooter shooter;

    public RobotHardware(LinearOpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {
        drivetrain = new Drivetrain(myOpMode);
        intake = new Intake(myOpMode);
        shooter = new Shooter(myOpMode);

        drivetrain.init();
        intake.init();
        shooter.init();

        myOpMode.telemetry.addData(">", "Hardware Initialized");
    }

    public void teleOp() {
        drivetrain.teleOp();
        intake.teleOp();
        shooter.teleOp();
    }
/*
    public void update(){
        intake.update();
        shooter.update();
    }
*/
    public void stop(){
        drivetrain.stop();
        intake.stop();
        shooter.stop();
    }
}
