package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class RobotHardware {
    private OpMode myOpMode = null;

    public Drivetrain drivetrain;
    public Intake intake;
    public Shooter shooter;
    public Transfer transfer;
    public LimeLight limelight;
    public Thread cameraThread;
    public Turret turret;


    public RobotHardware(OpMode opmode) {
        myOpMode = opmode;
    }


    public void init() {
        limelight = new LimeLight(myOpMode);
        limelight.init();
        drivetrain = new Drivetrain(myOpMode, limelight);
        drivetrain.init();
        intake = new Intake(myOpMode);
        shooter = new Shooter(myOpMode);

        transfer.init();
        intake.init();
        shooter.init();
        turret.init();


        myOpMode.telemetry.addData(">", "Hardware Initialized");
    }

    public void teleOp() {
        drivetrain.teleOp();
        intake.teleOp();
        shooter.teleOp();
    }

    public void update(){
        intake.update();
        shooter.update();
        //drivetrain.update();
        transfer.update();
    }

    public void stop(){
        drivetrain.stop();
    }
}