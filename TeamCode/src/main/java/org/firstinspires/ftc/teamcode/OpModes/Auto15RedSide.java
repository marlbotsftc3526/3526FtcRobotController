package org.firstinspires.ftc.teamcode.OpModes;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;


@Autonomous(name = "Auto15RedSide", group = "AutoTemplates")
public class Auto15RedSide extends LinearOpMode {
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
        COLLECT_ARTIFACTS,
        DRIVE_TO_LAUNCH_POSITION2,
        LAUNCH_ARTIFACTS2,
        COLLECT_ARTIFACTSFROMGATE,
        DRIVE_TO_LAUNCH_POSITION3,
        LAUNCH_ARTIFACTS3,
        COLLECT_ARTIFACTS3,
        DRIVE_TO_LAUNCH_POSITION4,
        LAUNCH_ARTIFACTS4,
        COLLECT_ARTIFACTS4,
        DRIVE_TO_LAUNCH_POSITION5,
        LAUNCH_ARTIFACTS5,
        END,
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
        follower.setStartingPose(new Pose(112.132, 132.7541, Math.toRadians(270)));

        paths = new Paths(follower); // Build paths

        //Update Panels dashboard telemetry
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);

        // Wait for the game to start (driver presses START)
        telemetry.addData(">", "Robot Initialized");
        telemetry.addData("Status", "Waiting for Start");
        telemetry.update();

        robot.shooter.hoodMode = Shooter.HoodMode.CLOSE; //linear

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
                    if (onStateStart()) {
                        //ex. set path to follow
                        follower.followPath(paths.launchzone1, true);
                        //ex. turn shooter on
                        robot.shooter.shootMode = Shooter.ShootMode.ON;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }

                    //set the condition to advance to the next state
                    /* You could check for
                        - Follower State: "if(!follower.isBusy()) {}"
                        - Time: "if(pathTimer.getElapsedTimeSeconds() > 1) {}"
                        - Sensor Value: "if(touchDetected) {}" etc...
                        */
                    if (!follower.isBusy()) {
                        currentState = State.LAUNCH_ARTIFACTS;
                    }
                    break;
                case LAUNCH_ARTIFACTS:
                    //state start
                    if (onStateStart()) {
                        //you could restart timers in here
                        timer.reset();

                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;
                    //state transition
                    if (timer.seconds() > 1 ) {
                        currentState = State.COLLECT_ARTIFACTS;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTS:
                    if (onStateStart()) {
                        follower.followPath(paths.intakeballs1, true);
                    }
                    if (!follower.isBusy()) {
                        currentState = State.DRIVE_TO_LAUNCH_POSITION2;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION2:
                    if (onStateStart()) {
                        follower.followPath(paths.launchzone2, true);
                    }
                    if (!follower.isBusy()) {
                        currentState = State.LAUNCH_ARTIFACTS2;
                    }
                    break;
                case LAUNCH_ARTIFACTS2:
                    if (onStateStart()) {
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;

                    if (timer.seconds() > 1 ) {
                        currentState = State.COLLECT_ARTIFACTSFROMGATE;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTSFROMGATE:
                    if (onStateStart()) {
                        timer.reset();
                        follower.followPath(paths.opengate, true);
                    }
                    if (timer.seconds() > 5) {
                        currentState = State.DRIVE_TO_LAUNCH_POSITION3;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION3:
                    if (onStateStart()) {
                        follower.followPath(paths.launchzone3, true);
                    }
                    if (!follower.isBusy()) {
                        currentState = State.LAUNCH_ARTIFACTS3;
                    }
                    break;
                case LAUNCH_ARTIFACTS3:
                    if (onStateStart()) {
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;
                    if (timer.seconds() > 1 ) {
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                        currentState = State.COLLECT_ARTIFACTS3;
                    }
                    break;
                case COLLECT_ARTIFACTS3:
                    if (onStateStart()) {
                        timer.reset();
                        follower.followPath(paths.intakeballs2, true);
                    }
                    if (!follower.isBusy() || timer.seconds() > 2) {
                        currentState = State.DRIVE_TO_LAUNCH_POSITION4;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION4:
                    if (onStateStart()) {
                        follower.followPath(paths.launchzone4, true);
                    }
                    if (!follower.isBusy()) {
                        currentState = State.LAUNCH_ARTIFACTS4;
                    }
                    break;
                case LAUNCH_ARTIFACTS4:
                    if (onStateStart()) {
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;
                    robot.shooter.hoodMode = Shooter.HoodMode.CLOSE;
                    if (timer.seconds() > 1 ) {
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                        currentState = State.COLLECT_ARTIFACTS4;
                    }
                    break;
                case COLLECT_ARTIFACTS4:
                    if (onStateStart()) {
                        timer.reset();
                        follower.followPath(paths.intakeballs3, true);
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    if (!follower.isBusy()) {
                        currentState = State.DRIVE_TO_LAUNCH_POSITION5;
                    }
                    break;

                case DRIVE_TO_LAUNCH_POSITION5:
                    if (onStateStart()) {
                        follower.followPath(paths.launchzone5, true);
                    }
                    if (!follower.isBusy()) {
                        currentState = State.LAUNCH_ARTIFACTS5;
                    }
                    break;
                case LAUNCH_ARTIFACTS5:
                    if (onStateStart()) {
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;

                    if (timer.seconds() > 1 ) {
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                        currentState = State.END;
                    }
                    break;
                case END:
                    if (onStateStart()) {
                        follower.followPath(paths.ending, true);
                    }
                    if (!follower.isBusy()) {
                        currentState = State.IDLE;
                    }
                    break;
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
        public PathChain launchzone1;
        public PathChain intakeballs1;
        public PathChain launchzone2;
        public PathChain opengate;
        public PathChain launchzone3;
        public PathChain intakeballs2;
        public PathChain launchzone4;
        public PathChain intakeballs3;
        public PathChain launchzone5;
        public PathChain ending;

        public Paths(Follower follower) {
            launchzone1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(112.132, 132.7541),

                                    new Pose(99.091, 97.352)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(-139))

                    .build();

            intakeballs1 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(99.091, 97.352),
                                    new Pose(65.895, 60.445),
                                    new Pose(136.757, 58.817)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            launchzone2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(136.757, 58.817),
                                    new Pose(78.199, 55.655),
                                    new Pose(99.091, 97.642)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(225.5))

                    .build();

            opengate = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(99.091, 97.642),
                                    new Pose(77.368, 56.326),
                                    new Pose(134.384, 59.461)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(40))

                    .build();

            launchzone3 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(134.384, 59.461),
                                    new Pose(73.832, 52.900),
                                    new Pose(99.358, 97.213)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(225.5))

                    .build();

            intakeballs2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(99.358, 97.213),
                                    new Pose(80.837, 84.604),
                                    new Pose(135.308, 82.865)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            launchzone4 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(135.308, 82.865),

                                    new Pose(99.091, 97.352)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(225.5))

                    .build();

            intakeballs3 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(99.091, 97.352),
                                    new Pose(76.599, 25.461),
                                    new Pose(138.495, 36.217)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            launchzone5 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(138.495, 36.217),
                                    new Pose(104.217, 61.686),
                                    new Pose(99.380, 97.062)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(225.5))

                    .build();

            ending = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(99.380, 97.062),

                                    new Pose(98.295, 63.050)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(225.5))

                    .build();
        }
    }




    private boolean onStateStart() {
        if (currentState != lastState) {
            lastState = currentState;
            return true;  // first time
        }
        return false;     // already entered
    }
}






