package org.firstinspires.ftc.teamcode.OpModes;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.List;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Meet0Teleop", group="Linear OpMode")
    public class Meet0Teleop extends LinearOpMode{
        RobotHardware robot;
        ElapsedTime loopTimer;
        private boolean BLUE;
        private double targetAngle = 0; //angle from the robot to the hive
        private double turretTargetAngle = 0; //angle from the robot to the turret
        private double targetX = 57;
        private double targetY = 54;


    private Follower follower;

        @Override
        public void runOpMode() {
            //calling constructor
            robot = new RobotHardware(this);
            follower = Constants.create(hardwareMap);
            loopTimer = new ElapsedTime();
            //calling init function
            robot.init();
            robot.drivetrain.pinpoint.recalibrateIMU();

            follower.setPose(OpModeStorage.autonomousEndPose);

            // Wait for the game to start (driver presses START)
            telemetry.addData("Status", "Waiting for Start");

        if(gamepad1.left_bumper){
            BLUE = false;
        }else if(gamepad1.right_bumper){
            BLUE = true;
        }
        telemetry.addData("blue", BLUE);
        telemetry.update();

            waitForStart();


            loopTimer.reset();


            // run until the end of the match (driver presses STOP)
            while (opModeIsActive()) {
                telemetry.addData("loop time", loopTimer.milliseconds());
                loopTimer.reset();

                targetAngle = Math.atan((targetY - follower.pose().y()) / (targetX - follower.pose().x()));
                turretTargetAngle = follower.pose().heading() - targetAngle;
                telemetry.addData("target angle", Math.toDegrees(targetAngle));
                telemetry.addData("turret target angle", Math.toDegrees(turretTargetAngle));
                telemetry.addData("pose", follower.pose());

                DrivePowers powers;
                if(BLUE==false) {
                    powers = ManualDrive.fieldCentric(
                            -gamepad1.left_stick_y,
                            -gamepad1.left_stick_x,
                            -gamepad1.right_stick_x,
                            follower.pose().heading()
                    );
                    follower.manual(powers);
                    follower.update();
                }
                else{
                    powers = ManualDrive.fieldCentric(
                            gamepad1.left_stick_y,
                            gamepad1.left_stick_x,
                            -gamepad1.right_stick_x,
                            follower.pose().heading()
                    );
                    follower.manual(powers);
                    follower.update();
                }

                if(gamepad2.b){
                    Pose reset = new Pose(0,0,0);
                    follower.setPose(reset);
                }

                if(gamepad1.dpad_up){
                    BLUE = false;
                }else if(gamepad1.dpad_down){
                    BLUE = true;
                }
                follower.manual(powers);
                follower.update();
                robot.teleOp();
                telemetry.update();

            }
           // robot.limelight.stop();
        }
}
