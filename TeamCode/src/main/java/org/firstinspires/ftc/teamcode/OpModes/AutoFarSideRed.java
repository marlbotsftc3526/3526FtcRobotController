package org.firstinspires.ftc.teamcode.OpModes;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Disabled
@Autonomous(name = "AutoFarSideRed", group = "AutoTemplates")
public class AutoFarSideRed extends LinearOpMode {
    //Declare Robot and Follower
    RobotHardware robot;
    private Follower follower;
    private Paths paths;
    private TelemetryManager panelsTelemetry;

    public static final String X_POS_KEY = "X Position";
    public static final String Y_POS_KEY = "Y Position";
    public static final String HEADING_KEY = "Heading";
    // Panels Telemetry instance

    //Declare timer for use in switch, could have multiple timers if useful
    ElapsedTime timer = new ElapsedTime();

    // This enum defines our "state"
    // This is essentially just defines the possible steps our program will take
    //TODO Update states to reflect flow of robot actions
    enum State {
        DRIVE_TO_LAUNCH_POSITION,
        LAUNCH_ARTIFACTS,
        ALIGN_ARTIFACTS,
        COLLECT_ARTIFACTS,
        DRIVE_TO_LAUNCH_POSITION2,
        LAUNCH_ARTIFACTS2,
        ALIGN_ARTIFACTS2,
        COLLECT_ARTIFACTS2,
        DRIVE_TO_LAUNCH_POSITION3,
        LAUNCH_ARTIFACTS3,
        ALIGN_ARTIFACTS3,
        COLLECT_ARTIFACTS3,
        DRIVE_TO_LAUNCH_POSITION4,
        LAUNCH_ARTIFACTS4,
        STRAIGHT4,
        COLLECT_ARTIFACTS5,
        END,
        DRIVE_TO_LAUNCH_POSITION5,
        LAUNCH_ARTIFACTS5,
        IDLE
    }

    // We define the current state we're on
    State currentState = State.DRIVE_TO_LAUNCH_POSITION;
    // Store the last state run to manage flow of path following in state machine
    State lastState = State.IDLE;

    @Override
    public void runOpMode() {
        robot = new RobotHardware(this);
        robot.init();
        robot.drivetrain.side = Drivetrain.SideMode.RED;

        follower = Constants.createFollower(hardwareMap);
        //TODO Set starting pose from path generation
        follower.setStartingPose(new Pose(87.5010060362173, 8.112676056338033, Math.toRadians(270)));
        //128.250297683,112.12560245311887
        paths = new Paths(follower); // Build paths

        //Update Panels dashboard telemetry
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);

        // Wait for the game to start (driver presses START)
        telemetry.addData(">", "Robot Initialized");
        telemetry.addData("Status", "Waiting for Start");
        telemetry.update();


        //  Object xPosition = blackboard.getOrDefault(X_POS_KEY, 0);
        //Object yPosition = blackboard.getOrDefault(Y_POS_KEY, 0);
        //Object heading = blackboard.getOrDefault(HEADING_KEY, 0);

        waitForStart();
        timer.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive() && !isStopRequested()) {
            // Anything outside of the switch statement will run independent of the currentState
            // We update robot and follower continuously in the background, regardless of state
            robot.update();
            follower.update();

            //TODO Update switch to match desired states
            switch (currentState) {
                case DRIVE_TO_LAUNCH_POSITION:
                    //set events at the start of state
                    if(onStateStart()){
                        //ex. set path to follow
                        follower.followPath(paths.Shoot1,true);
                        //ex. turn shooter on
                        robot.shooter.shootMode = Shooter.ShootMode.ON;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;

                    }
                    robot.shooter.hoodMode = Shooter.HoodMode.AUTO;
                    //set the condition to advance to the next state
                    /* You could check for
                        - Follower State: "if(!follower.isBusy()) {}"
                        - Time: "if(pathTimer.getElapsedTimeSeconds() > 1) {}"
                        - Sensor Value: "if(touchDetected) {}" etc...
                        */
                    if(!follower.isBusy()) {
                        currentState = State.LAUNCH_ARTIFACTS;
                    }
                    break;
                case LAUNCH_ARTIFACTS:
                    //state start
                    if(onStateStart()){
                        //you could restart timers in here
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    robot.drivetrain.rightBackDrive.setPower(0);
                    robot.drivetrain.rightFrontDrive.setPower(0);
                    robot.drivetrain.leftBackDrive.setPower(0);
                    robot.drivetrain.leftFrontDrive.setPower(0);
                    robot.shooter.transferMode = Shooter.TransferMode.ON;
                    //state transition
                    if(timer.seconds() > 1.6) {
                        currentState = State.ALIGN_ARTIFACTS;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case ALIGN_ARTIFACTS:
                    if(onStateStart()){
                        follower.followPath(paths.Side, true);
                    }

                    if(!follower.isBusy()){
                        currentState = State.IDLE;
                    }
                    break;

                    /*
                case DRIVE_TO_LAUNCH_POSITION5:
                    if(onStateStart()){
                        follower.followPath(paths.launchzone4, true);
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;
                    robot.shooter.hoodMode = Shooter.HoodMode.AUTO;
                    if(!follower.isBusy() || timer.seconds() >1.5){
                        currentState = State.LAUNCH_ARTIFACTS;
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                        robot.shooter.shootMode = Shooter.ShootMode.ON;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                    case LAUNCH_ARTIFACTS5:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;
                    robot.shooter.hoodMode = Shooter.HoodMode.AUTO;
                    if(timer.seconds() >1.5){
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                        currentState = State.IDLE;
                    }
                    break;
                    */
                case IDLE:
                    blackboard.put(X_POS_KEY, follower.getPose().getX());
                    blackboard.put(Y_POS_KEY, follower.getPose().getY());
                    blackboard.put(HEADING_KEY, follower.getPose().getHeading());

                    //telemetry.addData("OpMode started times", blackboard.get(TIMES_STARTED_KEY));
                    break;
            }

            // Log values to Panels and Driver Station
            panelsTelemetry.debug("Current State", currentState);
            panelsTelemetry.debug("X", follower.getPose().getX());
            panelsTelemetry.debug("Y", follower.getPose().getY());
            panelsTelemetry.debug("Heading", follower.getPose().getHeading());
            panelsTelemetry.update(telemetry);

            //telemetry.addData("state", currentState);
            telemetry.update();
        }
    }

    //TODO Define All Paths. Use the Visualizer auto generated code from https://visualizer.pedropathing.com/
    public static class Paths {

        public PathChain Shoot1;
        public PathChain Side;

        public Paths(Follower follower) {
            Shoot1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(87.501, 8.113), new Pose(92.137, 17.674))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(250))
                    .build();

            Side = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(92.137, 17.674), new Pose(108.764, 17.334))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(250), Math.toRadians(0))
                    .build();
        }
        /*

        public PathChain launchzone1;
        public PathChain straight1;
        public PathChain intakeballs1;
        public PathChain launchzone2;
        public PathChain straight2;
        public PathChain intakeballs2;
        public PathChain launchzone3;
        public PathChain straight3;
        public PathChain intakeballs3;
        public PathChain launchzone4;
        public PathChain straight4;
        public PathChain intakeballs4;
        public PathChain launchzone5;
        public PathChain End;

        public Paths(Follower follower) {
            launchzone1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(128.250, 112.126), new Pose(106.126, 108.001))
                    )
                    .setLinearHeadingInterpolation(
                            Math.toRadians(180),
                            Math.toRadians(-141)
                    )
                    .build();

            straight1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(106.126, 108.001), new Pose(99.751, 84.001))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-141), Math.toRadians(0))
                    .build();

            intakeballs1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(99.751, 84.001), new Pose(130.500, 83.626))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            launchzone2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(130.500, 83.626), new Pose(105.751, 107.626))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-141))
                    .build();

            straight2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(105.751, 107.626), new Pose(95.034, 59.976))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-141), Math.toRadians(0))
                    .build();

            intakeballs2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(95.034, 59.976), new Pose(137.915, 59.107))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            launchzone3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(137.915, 59.107),
                                    new Pose(104.248, 56.252),
                                    new Pose(101.482, 100.657)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-141))
                    .build();

            straight3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(101.482, 100.657), new Pose(93.875, 35.928))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-141), Math.toRadians(0))
                    .build();

            intakeballs3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(93.875, 35.928), new Pose(137.336, 35.638))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            launchzone4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(137.336, 35.638),
                                    new Pose(85.183, 41.143),
                                    new Pose(101.332, 101.106)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-141))
                    .build();

            straight4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(101.332, 101.106), new Pose(100.585, 60.453))
                    )
                    .setLinearHeadingInterpolation(
                            Math.toRadians(-141),
                            Math.toRadians(270)
                    )
                    .build();

            intakeballs4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(100.585, 60.453), new Pose(136.875, -0.747))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(270))
                    .build();

            launchzone5 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(136.875, -0.747),
                                    new Pose(120.531, 45.199),
                                    new Pose(90.978, 16.515)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(240))
                    .build();

            End = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(90.978, 16.515), new Pose(100.000, 71.513))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(240), Math.toRadians(230))
                    .build();
        }

         */


            /*public PathChain straight1;
            public PathChain intakeballs1;
            public PathChain launchzone2;
            public PathChain straight2;
            public PathChain intakeballs2;
            public PathChain launchzone3;
            public PathChain straight3;
            public PathChain intakeballs3;
            public PathChain launchzone4;
            public PathChain straight4;
            public PathChain intakeballs4;
            public PathChain End;

  public Paths(Follower follower) {
                launchzone1 = follower
                        .pathBuilder()
                        .addPath(
                                new BezierLine(new Pose(128.250, 112.126), new Pose(106.126, 108.001))
                        )
                        .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(-138))
                        .build();

                straight1 = follower
                        .pathBuilder()
                        .addPath(
                                new BezierLine(new Pose(106.126, 108.001), new Pose(99.751, 84.001))
                        )
                        .setLinearHeadingInterpolation(Math.toRadians(-138), Math.toRadians(0))
                        .build();

                intakeballs1 = follower
                        .pathBuilder()
                        .addPath(
                                new BezierLine(new Pose(99.751, 84.001), new Pose(130.500, 83.626))
                        )
                        .setTangentHeadingInterpolation()
                        .build();

                launchzone2 = follower
                        .pathBuilder()
                        .addPath(
                                new BezierLine(new Pose(130.500, 83.626), new Pose(105.751, 107.626))
                        )
                        .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-138))
                        .build();

                straight2 = follower
                        .pathBuilder()
                        .addPath(
                                new BezierLine(new Pose(105.751, 107.626), new Pose(96.173, 60.005))
                        )
                        .setLinearHeadingInterpolation(Math.toRadians(-138), Math.toRadians(0))
                        .build();

                intakeballs2 = follower
                        .pathBuilder()
                        .addPath(
                                new BezierLine(new Pose(96.173, 60.005), new Pose(137.046, 58.817))
                        )
                        .setTangentHeadingInterpolation()
                        .build();

                launchzone3 = follower
                        .pathBuilder()
                        .addPath(
                                new BezierCurve(
                                        new Pose(137.046, 58.817),
                                        new Pose(104.248, 56.252),
                                        new Pose(87.751, 80.626)
                                )
                        )
                        .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-130))
                        .build();

                straight3 = follower
                        .pathBuilder()
                        .addPath(
                                new BezierLine(new Pose(87.751, 80.626), new Pose(96.773, 35.928))
                        )
                        .setLinearHeadingInterpolation(Math.toRadians(-130), Math.toRadians(0))
                        .build();

                intakeballs3 = follower
                        .pathBuilder()
                        .addPath(
                                new BezierLine(new Pose(96.773, 35.928), new Pose(137.046, 35.638))
                        )
                        .setTangentHeadingInterpolation()
                        .build();

                launchzone4 = follower
                        .pathBuilder()
                        .addPath(
                                new BezierCurve(
                                        new Pose(137.046, 35.638),
                                        new Pose(85.183, 41.143),
                                        new Pose(80.547, 71.855)
                                )
                        )
                        .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-127))
                        .build();

                straight4 = follower
                        .pathBuilder()
                        .addPath(
                                new BezierLine(new Pose(80.547, 71.855), new Pose(136.875, 31.877))
                        )
                        .setLinearHeadingInterpolation(Math.toRadians(-127), Math.toRadians(270))
                        .build();

                intakeballs4 = follower
                        .pathBuilder()
                        .addPath(
                                new BezierLine(new Pose(136.875, 31.877), new Pose(136.875, -0.747))
                        )
                        .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(270))
                        .build();

                End = follower
                        .pathBuilder()
                        .addPath(
                                new BezierLine(new Pose(136.875, -0.747), new Pose(109.873, 68.626))
                        )
                        .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(270))
                        .build();
            }



 */
        /*
        public PathChain launchzone1;
        public PathChain straight1;
        public PathChain intakeballs1;
        public PathChain launchzone2;
        public PathChain straight2;
        public PathChain intakeballs2;
        public PathChain launchzone3;
        public PathChain straight3;
        public PathChain intakeballs3;
        public PathChain launchzone4;
        public PathChain straight4;
        public PathChain intakeballs4;
        public PathChain End;

        public Paths(Follower follower) {
            launchzone1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(128.250, 112.126), new Pose(106.126, 108.001))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(-138))
                    .build();

            straight1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(106.126, 108.001), new Pose(99.751, 84.001))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-138), Math.toRadians(0))
                    .build();

            intakeballs1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(99.751, 84.001), new Pose(130.500, 83.626))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            launchzone2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(130.500, 83.626), new Pose(105.751, 107.626))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-138))
                    .build();

            straight2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(105.751, 107.626), new Pose(96.173, 60.005))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-138), Math.toRadians(0))
                    .build();

            intakeballs2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(96.173, 60.005), new Pose(137.046, 58.817))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            launchzone3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(137.046, 58.817),
                                    new Pose(104.248, 56.252),
                                    new Pose(105.748, 107.251)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-130))
                    .build();

            straight3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(105.748, 107.251), new Pose(96.773, 35.928))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-130), Math.toRadians(0))
                    .build();

            intakeballs3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(96.773, 35.928), new Pose(138.747, 36.377))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            launchzone4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(138.747, 36.377),
                                    new Pose(83.998, 43.877),
                                    new Pose(84.751, 16.877)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-112))
                    .build();

            straight4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(84.751, 16.877),
                                    new Pose(95.623, 54.377),
                                    new Pose(137.997, 36.002)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-112), Math.toRadians(270))
                    .build();

            intakeballs4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(137.997, 36.002), new Pose(137.247, 8.628))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(270))
                    .build();

            End = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(137.247, 8.628), new Pose(109.873, 68.626))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(270))
                    .build();
        }

         */

    }

    private boolean onStateStart() {
        if (currentState != lastState) {
            lastState = currentState;
            return true;  // first time
        }
        return false;     // already entered
    }
}

