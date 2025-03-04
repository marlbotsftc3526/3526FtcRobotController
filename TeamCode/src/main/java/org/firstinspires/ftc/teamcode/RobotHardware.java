package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class RobotHardware {
    private LinearOpMode myOpMode = null;   // gain access to methods in the calling OpMode.

    ElapsedTime runtime = new ElapsedTime();

    public Drivetrain drivetrain;
    public Claw claw;
    public ActiveClaw aClaw;
    public Lift lift;
    public Extension extension;

    //public DualPortalCamera camera;

    public RobotHardware(LinearOpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {
        drivetrain = new Drivetrain(myOpMode);
        claw = new Claw(myOpMode);
        aClaw = new ActiveClaw(myOpMode);
        lift = new Lift(myOpMode);
        extension = new Extension(myOpMode);
        //camera = new DualPortalCamera(myOpMode);

        drivetrain.init();
        claw.init();
        //aClaw.init();
        lift.init();
        extension.init();
        //camera.init();

        myOpMode.telemetry.addData(">", "Hardware Initialized");
        myOpMode.telemetry.update();
    }
    public void initTeleOp() {
        drivetrain = new Drivetrain(myOpMode);
        claw = new Claw(myOpMode);
        aClaw = new ActiveClaw(myOpMode);
        lift = new Lift(myOpMode);
        extension = new Extension(myOpMode);
        //camera = new DualPortalCamera(myOpMode);

        drivetrain.init();
        claw.init();
        //aClaw.init();
        lift.initTeleOp();
        extension.initTeleOp();
        //camera.init();

        myOpMode.telemetry.addData(">", "Hardware Initialized");
        myOpMode.telemetry.update();
    }
    public void initTeleOpActive() {
        drivetrain = new Drivetrain(myOpMode);
        //claw = new Claw(myOpMode);
        aClaw = new ActiveClaw(myOpMode);
        lift = new Lift(myOpMode);
        extension = new Extension(myOpMode);
        //camera = new DualPortalCamera(myOpMode);

        drivetrain.init();
        aClaw.init();
        lift.initTeleOp();
        extension.initTeleOp();
        //camera.init();

        myOpMode.telemetry.addData(">", "Hardware Initialized");
        myOpMode.telemetry.update();
    }
    public void update(){
        drivetrain.update();
        lift.update();
        extension.update();
    }
    public void teleOp() {
        drivetrain.teleOp();
        claw.teleOp();
        lift.teleOp();
        extension.teleOp();
    }

    public void teleOpA() {
        drivetrain.teleOp();
        aClaw.teleOp();
        lift.teleOp();
        extension.teleOp();
    }

    public void stop(){
        drivetrain.stop();
    }


  }