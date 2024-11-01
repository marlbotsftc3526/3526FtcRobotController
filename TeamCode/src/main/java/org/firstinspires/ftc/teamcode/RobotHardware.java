package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class RobotHardware {
    private LinearOpMode myOpMode = null;   // gain access to methods in the calling OpMode.

    ElapsedTime runtime = new ElapsedTime();

    public Drivetrain drivetrain;
    public Intake intake;
    public Lift lift;
    public Drone drone;

    //public DualPortalCamera camera;

    public RobotHardware(LinearOpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {
        drivetrain = new Drivetrain(myOpMode);
        intake = new Intake(myOpMode);
        lift = new Lift(myOpMode);
        drone = new Drone(myOpMode);
        //camera = new DualPortalCamera(myOpMode);

        drivetrain.init();
        intake.init();        lift.init();
        drone.init();
        //camera.init();

        myOpMode.telemetry.addData(">", "Hardware Initialized");
        myOpMode.telemetry.update();
    }
    public void teleOp() {
        drivetrain.teleOp();
        lift.teleOp();
        intake.teleOp();
        drone.teleOp();
    }

}
