package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class RobotHardware {
    private LinearOpMode myOpMode = null;   // gain access to methods in the calling OpMode.

    ElapsedTime runtime = new ElapsedTime();

    public Drivetrain drivetrain;
    public Claw claw;
    public Lift lift;
    public Extension extension;

    //public DualPortalCamera camera;

    public RobotHardware(LinearOpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {
        drivetrain = new Drivetrain(myOpMode);
        claw = new Claw(myOpMode);
        lift = new Lift(myOpMode);
        extension = new Extension(myOpMode);
        //camera = new DualPortalCamera(myOpMode);

        drivetrain.init();
        claw.init();
        lift.init();
        extension.init();
        //camera.init();

        myOpMode.telemetry.addData(">", "Hardware Initialized");
        myOpMode.telemetry.update();
    }
    public void teleOp() {
        drivetrain.teleOp();
        claw.teleOp();
        lift.teleOp();
        extension.teleOp();
    }

}