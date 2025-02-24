package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="SpecimenAuto", group="Linear OpMode")
@Config
public class SpecimenAuto extends LinearOpMode {

    RobotHardware robot;

    ElapsedTime timer = new ElapsedTime();

    // This enum defines our "state"
    // This is essentially just defines the possible steps our program will take
    //TODO Update states to reflect flow of robot actions
    enum State {
        DRIVE_TO_SCORE,
        SCORING,
        DRIVE_TO_INTAKE_ONE,
        DRIVE_TO_INTAKE_TWO,
        INTAKING
    }

    // We define the current state we're on
    // Default to IDLE
    State currentState = State.DRIVE_TO_SCORE;

    // Define our start pose
    Pose2D startPose = new Pose2D(DistanceUnit.INCH, 0,0, AngleUnit.DEGREES,0);

    // Define our target
    public static double scoreX = 19;
    public static double scoreY = 0;
    public static double scoreT = 0;
    Pose2D scorePose = new Pose2D(DistanceUnit.INCH, scoreX, scoreY, AngleUnit.DEGREES, scoreT);

    public static double intakeX = 0;
    public static double intakeY = -40;
    public static double intakeT = 0;

    public static int cycle = 0;
    Pose2D intakePoseOne = new Pose2D(DistanceUnit.INCH, intakeX+8,intakeY, AngleUnit.DEGREES, intakeT);
    Pose2D intakePoseTwo = new Pose2D(DistanceUnit.INCH, intakeX,intakeY, AngleUnit.DEGREES, intakeT);

    @Override
    public void runOpMode() {
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
            switch (currentState) {
                case DRIVE_TO_SCORE:
                    //put condition for switch at the beginning, condition can be based on time or completion of a task
                    robot.extension.extMode = Extension.ExtMode.NEAR;
                    robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                    if(cycle >= 3) {
                        robot.claw.clawPivot.setPosition(robot.claw.pivotUP);
                        if (timer.seconds()>0.3) {
                            robot.claw.clawSpin.setPosition(robot.claw.spinA);

                        }
                    }
                    if ((robot.drivetrain.targetReached && timer.seconds() > 0.3)|| timer.seconds() > 3) {
                        currentState = SpecimenAuto.State.SCORING;
                        timer.reset();
                    }


                    break;
                case SCORING:

                    robot.lift.liftMode = Lift.LiftMode.HIGH_CHAMBER;
                    if (timer.seconds() > 1.5) {
                        robot.claw.clawPivot.setPosition(robot.claw.pivotSCORE);
                        if(timer.seconds() > 3){
                            robot.lift.liftMode = Lift.LiftMode.HIGH_CHAMBER;
                            if(timer.seconds() > 3.5) {
                                robot.claw.clawOpen.setPosition(robot.claw.openOPEN);
                                if (timer.seconds() > 4) {
                                    currentState = SpecimenAuto.State.DRIVE_TO_INTAKE_ONE;
                                    robot.drivetrain.setTargetPose(intakePoseOne);
                                    timer.reset();
                                }
                            }
                        }
                    }
                    break;
                case DRIVE_TO_INTAKE_ONE:
                    if(timer.seconds() < 0.5){
                        robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                        robot.claw.clawSpin.setPosition(robot.claw.spinB);
                    }
                    robot.lift.liftMode = Lift.LiftMode.GROUND;
                    robot.extension.extMode = Extension.ExtMode.FARBACK;
                    robot.claw.clawOpen.setPosition(robot.claw.openMID);
                    if (robot.drivetrain.targetReached|| timer.seconds() > 5) {
                        robot.claw.clawPivot.setPosition(robot.claw.pivotBACK);
                        currentState = SpecimenAuto.State.DRIVE_TO_INTAKE_TWO;
                        robot.drivetrain.setTargetPose(intakePoseTwo);
                        timer.reset();
                    }
                    break;
                case DRIVE_TO_INTAKE_TWO:
                    if (robot.drivetrain.targetReached || timer.seconds() > 2) {
                        if(timer.seconds() > 0.3) {
                            cycle += 3;
                            robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                            currentState = SpecimenAuto.State.INTAKING;
                            timer.reset();

                        }
                    }
                    break;
                case INTAKING:
                    scorePose = new Pose2D(DistanceUnit.INCH, scoreX+(cycle/5.5), scoreY+cycle, AngleUnit.DEGREES, scoreT);
                    robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                    if(cycle == 9){
                        stop();
                    }else {
                        if(timer.seconds() > 0.3) {
                            currentState = SpecimenAuto.State.DRIVE_TO_SCORE;
                            robot.drivetrain.setTargetPose(scorePose);
                            timer.reset();
                        }
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
