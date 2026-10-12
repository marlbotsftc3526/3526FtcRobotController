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
        //Drivetrain driving
        double drive = -myOpMode.gamepad1.left_stick_y;
        double turn = myOpMode.gamepad1.right_stick_x;
        double strafe = -myOpMode.gamepad1.left_stick_x;

        double leftFrontPower = (drive + turn - strafe);
        double rightFrontPower = (drive - turn + strafe);
        double leftBackPower = (drive + turn + strafe);
        double rightBackPower = (drive - turn - strafe);

        drivetrain.leftFrontDrive.setPower(leftFrontPower);
        drivetrain.rightFrontDrive.setPower(rightFrontPower);
        drivetrain.leftBackDrive.setPower(leftBackPower);
        drivetrain.rightBackDrive.setPower(rightBackPower);

        //Intake controls
        if(myOpMode.gamepad1.y || myOpMode.gamepad2.y){
            intake.intakeMode = Intake.IntakeMode.IN;
        }else if(myOpMode.gamepad1.a || myOpMode.gamepad2.a) {
            intake.intakeMode = Intake.IntakeMode.OUT;
        }else if(myOpMode.gamepad1.b || myOpMode.gamepad2.b){
            intake.intakeMode = Intake.IntakeMode.LAUNCH;
        }else if(myOpMode.gamepad1.x || myOpMode.gamepad2.x){
            intake.intakeMode = Intake.IntakeMode.OFF;
        }

        //Shooter controls
        if(myOpMode.gamepad1.right_bumper){
            shooter.shooterMode = Shooter.ShooterMode.AUTO;
        }else if(myOpMode.gamepad1.left_bumper){
            shooter.shooterMode = Shooter.ShooterMode.OFF;
        }else if(myOpMode.gamepad2.right_bumper){
            shooter.shooterMode = Shooter.ShooterMode.FIXED;
        }

        //Transfer controls
        if(myOpMode.gamepad1.left_trigger > 0.5){
            transfer.transferMode = Transfer.TransferMode.DOWN;
        }else if(myOpMode.gamepad1.right_trigger > 0.5){
            transfer.transferMode = Transfer.TransferMode.UP;
        }else if(myOpMode.gamepad2.left_trigger > 0.5){
            transfer.transferMode = Transfer.TransferMode.OFF;
        }else if(myOpMode.gamepad2.right_trigger >0.5){
            transfer.transferMode = Transfer.TransferMode.LAUNCH;
        }

        //Turret controls
        if(myOpMode.gamepad1.dpad_up){
            turret.turretMode = Turret.TurretMode.MANUAL;
        }else if (myOpMode.gamepad1.dpad_down){
            turret.turretMode = Turret.TurretMode.AUTO;
        }else if (myOpMode.gamepad1.dpad_left){
            turret.turretMode = Turret.TurretMode.FIXED;
        }
    }

    public void update(){
        intake.update();
        shooter.update();
        drivetrain.update();
        transfer.update();
    }

    public void stop(){
        drivetrain.stop();
    }
}