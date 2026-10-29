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

        transfer = new Transfer(myOpMode);
        transfer.init();

        intake = new Intake(myOpMode);
        intake.init();

        shooter = new Shooter(myOpMode);
        shooter.init();

        turret = new Turret (myOpMode);
        turret.init();

        myOpMode.telemetry.addData(">", "Hardware Initialized");
    }

    public void teleOp() {
        update();
        //Drivetrain driving
        /*
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
         */

        //Intake controls
        if(myOpMode.gamepad1.right_trigger > 0.1){
            intake.intakeMode = Intake.IntakeMode.OUT;
            transfer.transferMode = Transfer.TransferMode.DOWN;
        }else if(myOpMode.gamepad1.right_trigger < 0.1) {
            intake.intakeMode = Intake.IntakeMode.IN;
            transfer.transferMode = Transfer.TransferMode.UP;
        }else{
            intake.intakeMode = Intake.IntakeMode.OFF;
            transfer.transferMode = Transfer.TransferMode.OFF;
        }

        //Flower controls
        if(myOpMode.gamepad1.x){
            intake.flowerServoPosition = Intake.FLOWERDOWN;
        }
        else if(myOpMode.gamepad1.y){
            intake.flowerServoPosition = Intake.FLOWERUP;
        }

        //Gate controls
        if(myOpMode.gamepad1.right_bumper){
            transfer.gatePosition = Transfer.GATEOPEN;
        }
        else{
            transfer.gatePosition = Transfer.GATECLOSE;
        }

        //Shooter controls
        if(myOpMode.gamepad1.a){
            shooter.shooterMode = Shooter.ShooterMode.FIXED;
        }else if(myOpMode.gamepad1.b){
            shooter.shooterMode = Shooter.ShooterMode.OFF;
        }

        //Turret controls
        if(myOpMode.gamepad1.dpad_left){
            turret.turretMode = Turret.TurretMode.COUNTERCLOCKWISE;
        }else if (myOpMode.gamepad1.dpad_right){
            turret.turretMode = Turret.TurretMode.CLOCKWISE;
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