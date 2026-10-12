package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class RobotHardware {
    private OpMode myOpMode = null;

    public Drivetrain drivetrain;
    public LimeLight limelight;
    public Intake intake;
    public Launcher launcher;

    public RobotHardware(OpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {
        limelight = new LimeLight(myOpMode);
        drivetrain = new Drivetrain(myOpMode, limelight);
        intake = new Intake(myOpMode);
        launcher = new Launcher(myOpMode);

        limelight.init();
        drivetrain.init();
        intake.init();
        launcher.init();

        myOpMode.telemetry.addData(">", "Hardware Initialized");
    }

    public void teleOp() {
        limelight.teleOp();
        drivetrain.teleOp();
        intake.teleOp();
        launcher.teleOp();

        if(myOpMode.gamepad1.a){
            drivetrain.drivetrainMode = Drivetrain.DrivetrainMode.LIMELIGHT;
        }else if(myOpMode.gamepad1.b){
            drivetrain.drivetrainMode = Drivetrain.DrivetrainMode.MANUAL;
        }
    }


    public void stop(){
        drivetrain.stop();
    }
}