package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class OdoRobotHardware {
    private LinearOpMode myOpMode = null;   // gain access to methods in the calling OpMode.

    ElapsedTime runtime = new ElapsedTime();

    public OdoDrivetrain drivetrain;
    public Intake intake;
    public Lift lift;
    public Drone drone;

    public SimpleCamera camera;

    //Variables for AprilTag Motion --> eventually reconcile these with drive constants in Drivetrain class
    final double SPEED_GAIN  =  0.02  ;   //  Forward Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double STRAFE_GAIN =  0.015 ;   //  Strafe Speed Control "Gain".  eg: Ramp up to 25% power at a 25 degree Yaw error.   (0.25 / 25.0)
    final double TURN_GAIN   =  0.01  ;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MAX_AUTO_SPEED = 0.5;   //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_STRAFE= 0.5;   //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_TURN  = 0.3;   //  Clip the turn speed to this max value (adjust for your robot)

    public OdoRobotHardware(LinearOpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {
        drivetrain = new OdoDrivetrain(myOpMode);
        intake = new Intake(myOpMode);
        lift = new Lift(myOpMode);
        drone = new Drone(myOpMode);
        camera = new SimpleCamera(myOpMode);

        drivetrain.init();
        intake.init();
        lift.init();
        drone.init();
        camera.init();

        myOpMode.telemetry.addData(">", "Hardware Initialized");
        myOpMode.telemetry.update();
    }
    public void teleOp() {
        drivetrain.teleOp();
        lift.teleOp();
        intake.teleOp();
        drone.teleOp();
        myOpMode.telemetry.update();
    }

    void driveToAprilTag(int targetTag, double targetDistance) {
        double rangeError = 100;
        double drive = 0;
        double turn = 0;
        double strafe = 0;

        while (rangeError > 1) {
            camera.scanAprilTag();
            if (camera.targetFound) {
                // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
                rangeError = (camera.desiredTag.ftcPose.range - targetDistance);
                double headingError = camera.desiredTag.ftcPose.bearing;
                double yawError = camera.desiredTag.ftcPose.yaw;

                // Use the speed and turn "gains" to calculate how we want the robot to move.
                drive = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
                turn = Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN);
                strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);

                // Calculate wheel powers.
                double leftFrontPower = drive - strafe - turn;
                double rightFrontPower = drive + strafe + turn;
                double leftBackPower = drive + strafe - turn;
                double rightBackPower = drive - strafe + turn;

                // Normalize wheel powers to be less than 1.0
                double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
                max = Math.max(max, Math.abs(leftBackPower));
                max = Math.max(max, Math.abs(rightBackPower));

                if (max > 1.0) {
                    leftFrontPower /= max;
                    rightFrontPower /= max;
                    leftBackPower /= max;
                    rightBackPower /= max;
                }

                // Send powers to the wheels.
                drivetrain.frontLeft.setPower(leftFrontPower);
                drivetrain.frontRight.setPower(rightFrontPower);
                drivetrain.backLeft.setPower(leftBackPower);
                drivetrain.backRight.setPower(rightBackPower);
            }
            drivetrain.localizer.update();
            drivetrain.localizer.updateDashboard();
            myOpMode.telemetry.update();
        }

        drivetrain.stopMotors();
    }

}
