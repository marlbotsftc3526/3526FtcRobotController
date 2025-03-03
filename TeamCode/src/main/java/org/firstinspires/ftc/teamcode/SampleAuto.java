package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="SampleAuto", group="Linear OpMode")
@Config
public class SampleAuto extends LinearOpMode {

    RobotHardware robot;

    ElapsedTime timer = new ElapsedTime();

    // This enum defines our "state"
    // This is essentially just defines the possible steps our program will take
    //TODO Update states to reflect flow of robot actions
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
    public static double scoreX = 8;//10
    public static double scoreY = 14;//12
    public static double scoreT = -45;
    Pose2D scorePose = new Pose2D(DistanceUnit.INCH, scoreX, scoreY, AngleUnit.DEGREES, scoreT);

    public static double intakeX = 26; // origionally 30
    public static double intakeY = 8; // orig 5,8
    public static double intakeT = 0;

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
        robot.init();

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
                    robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                    robot.claw.clawPivot.setPosition(robot.claw.pivotUP);
                    if ((robot.drivetrain.targetReached)|| timer.seconds() > 1.5) {
                        currentState = SampleAuto.State.SCORING;
                        timer.reset();
                    }
                    break;
                case SCORING:
                    robot.drivetrain.stop();
                    robot.lift.liftMode = Lift.LiftMode.HIGH_BUCKET;
                    //Original lift pos 2850
                    if(robot.lift.liftLeft.getCurrentPosition() > robot.lift.highbucketpos - 40){
                        robot.claw.clawPivot.setPosition(robot.claw.pivotBACK);
                        if(timer.seconds() > 2){
                            robot.claw.clawOpen.setPosition(robot.claw.openMID);
                            if(timer.seconds() > 3) {
                                if (counter<2) {
                                    currentState = SampleAuto.State.DRIVE_TO_INTAKE;
                                    intakePose = new Pose2D(DistanceUnit.INCH, intakeX+2*counter, intakeY + 8 * counter, AngleUnit.DEGREES, intakeT);
                                    robot.drivetrain.setTargetPose(intakePose);
                                    timer.reset();
                                }
                                else {
                                    currentState = SampleAuto.State.PARK;
                                    timer.reset();

                                }
                            }
                        }
                    }
                    break;
                case DRIVE_TO_INTAKE:
                    if(timer.seconds() < 1){
                        robot.claw.clawPivot.setPosition(robot.claw.pivotUP);
                    }
                    if(timer.seconds() > 0.3) {
                        robot.lift.liftMode = Lift.LiftMode.GROUND;
                        robot.extension.extMode = Extension.ExtMode.AUTO;
                        robot.claw.clawOpen.setPosition(robot.claw.openMID);
                        if(timer.seconds() > .8) {
                            if (robot.extension.extension.getCurrentPosition() > robot.extension.autopos - 100) {
                                robot.claw.clawPivot.setPosition(robot.claw.pivotDOWN);
                                robot.claw.clawOpen.setPosition(robot.claw.openOPEN);
                                if (robot.drivetrain.targetReached || timer.seconds() > 3) {
                                    currentState = SampleAuto.State.INTAKING;
                                    timer.reset();
                                }
                            }
                        }
                    }
                    break;
                case INTAKING:
                    if(timer.seconds() > 0.3) {
                        robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                        if (timer.seconds() > .5) {
                            robot.claw.clawPivot.setPosition(robot.claw.pivotUP);
                            robot.extension.extMode = Extension.ExtMode.FARBACK;
                            if (timer.seconds() > 1) {
                                currentState = SampleAuto.State.DRIVE_TO_SCORE;
                                robot.drivetrain.setTargetPose(scorePose);
                                counter++;
                                timer.reset();
                            }
                        }
                    }
                    break;
            case PARK:
                if(timer.seconds() < 0.5){
                    robot.claw.clawPivot.setPosition(robot.claw.pivotUP);
                    }
                    if(timer.seconds() > 0.3) {
                        robot.drivetrain.setTargetPose(parkPose);
                        robot.lift.liftMode = Lift.LiftMode.HIGH_CHAMBER;
                        robot.extension.extMode = Extension.ExtMode.NEAR;
                        robot.claw.clawPivot.setPosition(robot.claw.pivotBACK);
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
