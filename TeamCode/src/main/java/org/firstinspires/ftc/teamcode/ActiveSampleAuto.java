package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="ActiveSampleAuto", group="Linear OpMode")
@Config
public class ActiveSampleAuto extends LinearOpMode {

    RobotHardware robot;

    ElapsedTime timer = new ElapsedTime();

    // This enum defines our "state"
    // This is essentially just defines the possible steps our program will take
    //TODO Update states to reflect                                                                            flow of robot actions
    enum State {
        DRIVE_TO_SCORE,
        SCORING,
        DRIVE_TO_INTAKE,
        INTAKING,
        PARK,
    }

    // We define the current state we're on
    // Default to IDLE
    State currentState = State.DRIVE_TO_SCORE;

    // Define our start pose
    Pose2D startPose = new Pose2D(DistanceUnit.INCH, 0,0, AngleUnit.DEGREES,0);

    // Define our target
    public static double scoreX = 9;//10
    public static double scoreY = 13;//12
    public static double scoreT = -50;
    Pose2D scorePose = new Pose2D(DistanceUnit.INCH, scoreX, scoreY, AngleUnit.DEGREES, scoreT);

    public static double intakeX = 34.5; // origionally 30
    public static double intakeY = -5; //-2.5 orig 5,8 //-3.5
    public static double intakeT = 90;

    public static double parkX = 48; // origionally 30
    public static double parkY = -17; // orig 5
    public static double parkT = -90;
    Pose2D parkPose = new Pose2D(DistanceUnit.INCH, parkX, parkY, AngleUnit.DEGREES, parkT);

    public static int counter = 0;
    Pose2D intakePose = new Pose2D(DistanceUnit.INCH, intakeX,intakeY, AngleUnit.DEGREES, intakeT);

    @Override
    public void runOpMode() {
        counter = 0;
        //calling constructor
        robot = new RobotHardware(this);


        //calling init function
        robot.activeInit();

        //TODO Pass starting pose to localizer
        robot.drivetrain.localizer.myOtos.setPosition(startPose);

        //Set the drivetrain's first target
        robot.drivetrain.setTargetPose(scorePose);

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Waiting for Start");
        telemetry.update();
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            telemetry.addData("counter", counter);
            switch (currentState) {
                case DRIVE_TO_SCORE:
                    //put condition for switch at the beginning, condition can be based on time or completion of a task
                    robot.extension.extMode = Extension.ExtMode.FARBACK;
                    robot.aClaw.clawOpen.setPosition(robot.aClaw.activeIn);
                    robot.aClaw.clawPivot.setPosition(robot.aClaw.pivotUP);
                    if ((robot.drivetrain.targetReached)|| timer.seconds() > 1.5) { //1.5
                        currentState = ActiveSampleAuto.State.SCORING;
                        timer.reset();
                    }
                    break;
                case SCORING:
                    robot.drivetrain.stop();
                    robot.lift.liftMode = Lift.LiftMode.HIGH_BUCKET;
                    //Original lift pos 2850
                    if(robot.lift.liftLeft.getCurrentPosition() > robot.lift.highbucketpos - 40){
                        robot.aClaw.clawPivot.setPosition(robot.aClaw.pivotBACK);
                        if(timer.seconds() > 2){ //1.5
                            robot.aClaw.clawOpen.setPosition(robot.aClaw.activeOut);
                            if(timer.seconds() > 3) {
                                if (counter<3) {
                                    currentState = ActiveSampleAuto.State.DRIVE_TO_INTAKE;
                                    intakePose = new Pose2D(DistanceUnit.INCH, intakeX, intakeY + 8 * counter, AngleUnit.DEGREES, intakeT);
                                    robot.drivetrain.setTargetPose(intakePose);
                                    timer.reset();
                                }
                                else {
                                    currentState = ActiveSampleAuto.State.PARK;
                                    timer.reset();

                                }
                            }
                        }
                    }
                    break;
                case DRIVE_TO_INTAKE:
                    if(timer.seconds() < 1){ //1
                        robot.aClaw.clawPivot.setPosition(robot.aClaw.pivotUP);
                        robot.lift.liftMode = Lift.LiftMode.GROUNDACTIVE;
                        robot.extension.extMode = Extension.ExtMode.AUTO;
                        robot.aClaw.clawOpen.setPosition(robot.aClaw.activeIn);
                    }
                    if(timer.seconds() < 1.5) { //1.5
                        robot.aClaw.clawPivot.setPosition(robot.aClaw.pivotDOWN);
                    }

                    if(timer.seconds() > 1.6){ //1.6
                        intakePose = new Pose2D(DistanceUnit.INCH, intakeX, intakeY + 8 * counter+5.6, AngleUnit.DEGREES, intakeT);
                        robot.drivetrain.setTargetPose(intakePose);                    }
                    if (robot.drivetrain.targetReached || timer.seconds() > 2.1) {
                        currentState = ActiveSampleAuto.State.INTAKING;
                        timer.reset();
                    }
                    break;
                case INTAKING:
                    if(timer.seconds() > 0.3) { // 0.3
                        robot.aClaw.clawOpen.setPosition(robot.aClaw.activeIn);
                        if (timer.seconds() > .5) { //.5
                            robot.aClaw.clawPivot.setPosition(robot.aClaw.pivotUP);
                            robot.extension.extMode = Extension.ExtMode.FARBACK;
                            if (timer.seconds() > 1) { //1
                                currentState = ActiveSampleAuto.State.DRIVE_TO_SCORE;
                                robot.drivetrain.setTargetPose(scorePose);
                                counter++;
                                timer.reset();
                            }
                        }
                    }
                    break;
                case PARK:
                    if(timer.seconds() < 0.5){
                        robot.aClaw.clawPivot.setPosition(robot.aClaw.pivotUP);
                    }
                    if(timer.seconds() > 0.3) {
                        robot.drivetrain.setTargetPose(parkPose);
                        robot.lift.liftMode = Lift.LiftMode.HIGH_CHAMBER;
                        robot.extension.extMode = Extension.ExtMode.NEAR;
                        robot.aClaw.clawPivot.setPosition(robot.aClaw.pivotBACK);
                    }
                    break;
            }
            // Anything outside of the switch statement will run independent of the currentState
            // We update robot continuously in the background, regardless of state
            robot.update();

            telemetry.addData("state", currentState);
            telemetry.update();

        }
    }
}
