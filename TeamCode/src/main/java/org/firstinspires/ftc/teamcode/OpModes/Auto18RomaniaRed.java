package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;


@Autonomous(name = "Auto18RomaniaRed", group = "AutoTemplates")
public class Auto18RomaniaRed extends LinearOpMode {
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
        COLLECT_ARTIFACTS2,
        OPEN_GATE,
        DRIVE_TO_LAUNCH_POSITION3,
        LAUNCH_ARTIFACTS3,
        COLLECT_ARTIFACTSHP,
        OPEN_GATE2,
        DRIVE_TO_LAUNCH_POSITION4,
        LAUNCH_ARTIFACTS4,
        OPEN_GATE3,
        COLLECT_ARTIFACTSHP2,
        DRIVE_TO_LAUNCH_POSITION5,
        LAUNCH_ARTIFACTS5,
        OPEN_GATE4,
        COLLECT_ARTIFACTSHP3,
        DRIVE_TO_LAUNCH_POSITION6,
        LAUNCH_ARTIFACTS6,
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

        robot.shooter.hoodMode = Shooter.HoodMode.AUTOCLOSE; //linear

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

                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;
                    //state transition
                    if (timer.seconds() > .45 ) {
                        currentState = State.COLLECT_ARTIFACTS;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTS:
                    if (onStateStart()) {
                        follower.followPath(paths.intakeballs1, true);
                    }
                    if (!follower.isBusy() || (robot.intake.detectedTop && robot.intake.detectedMiddle && robot.intake.detectedBottom)) {
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
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;

                    if (timer.seconds() > .45 ) {
                        currentState = State.COLLECT_ARTIFACTS2;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTS2:
                    if (onStateStart()) {
                        timer.reset();
                        follower.followPath(paths.intakeballs2, true);
                    }
                    if (!follower.isBusy()) { // if (timer.seconds() > 3.5 || (robot.intake.detectedTop && robot.intake.detectedMiddle && robot.intake.detectedBottom)) { //3.5
                        currentState = State.OPEN_GATE;
                    }
                    break;
                case OPEN_GATE:
                    if (onStateStart()) {
                        timer.reset();
                        follower.followPath(paths.opengate1, true);
                    }
                    if (timer.seconds() > 3 || (robot.intake.detectedTop && robot.intake.detectedMiddle && robot.intake.detectedBottom)) { //3.5
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
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;
                    if (timer.seconds() > .45 ) {
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                        currentState = State.COLLECT_ARTIFACTSHP;
                    }
                    break;

                case COLLECT_ARTIFACTSHP:
                    if (onStateStart()) {
                        timer.reset();
                        follower.followPath(paths.hpzone1, true);
                    }
                    if (!follower.isBusy()) { // if (timer.seconds() > 3.5 || (robot.intake.detectedTop && robot.intake.detectedMiddle && robot.intake.detectedBottom)) 3.5
                        currentState = State.OPEN_GATE2;
                    }
                    break;
                case OPEN_GATE2:
                    if (onStateStart()) {
                        timer.reset();
                        follower.followPath(paths.opengate2, true);
                    }
                    if (timer.seconds() > 2.5 || (robot.intake.detectedTop && robot.intake.detectedMiddle && robot.intake.detectedBottom)) { // 3.5
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
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;
                    if (timer.seconds() > .45 ) {
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                        currentState = State.OPEN_GATE3;
                    }
                    break;

                case OPEN_GATE3:
                    if (onStateStart()) {
                        timer.reset();
                        follower.followPath(paths.opengate3, true);
                    }
                    if (!follower.isBusy() || timer.seconds() > 1.2 || (robot.intake.detectedTop && robot.intake.detectedMiddle && robot.intake.detectedBottom)) {
                        currentState = State.COLLECT_ARTIFACTSHP2;
                    }
                    break;

                case COLLECT_ARTIFACTSHP2:
                    if (onStateStart()) {
                        timer.reset();
                        follower.followPath(paths.hpzone2, true);
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    if (!follower.isBusy() || (robot.intake.detectedTop && robot.intake.detectedMiddle && robot.intake.detectedBottom)) {
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
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;
                    if (timer.seconds() > .45 ) {
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                        currentState = State.OPEN_GATE4;
                    }
                    break;

                case OPEN_GATE4:
                    if (onStateStart()) {
                        timer.reset();
                        follower.followPath(paths.opengate4, true);
                    }
                    if (!follower.isBusy() || timer.seconds() > 1.2 || (robot.intake.detectedTop && robot.intake.detectedMiddle && robot.intake.detectedBottom)) {
                        currentState = State.COLLECT_ARTIFACTSHP3;
                    }
                    break;

                case COLLECT_ARTIFACTSHP3:
                    if (onStateStart()) {
                        timer.reset();
                        follower.followPath(paths.hpzone3, true);
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    if (!follower.isBusy() || (robot.intake.detectedTop && robot.intake.detectedMiddle && robot.intake.detectedBottom)) {
                        currentState = State.DRIVE_TO_LAUNCH_POSITION6;
                    }
                    break;


                case DRIVE_TO_LAUNCH_POSITION6:
                    if (onStateStart()) {
                        follower.followPath(paths.launchzone6, true);
                    }
                    if (!follower.isBusy()) {
                        currentState = State.LAUNCH_ARTIFACTS6;
                    }
                    break;
                case LAUNCH_ARTIFACTS6:
                    if (onStateStart()) {
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;

                    if (timer.seconds() > .5 ) {
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
            FtcDashboard dashboard = FtcDashboard.getInstance();
            Telemetry dashboardTelemetry = dashboard.getTelemetry();
            dashboardTelemetry.addData("Set RPM", robot.shooter.REVOLUTIONS_PER_MINUTE);
            dashboardTelemetry.addData("Measured RPM", robot.shooter.measuredRPM);
            dashboardTelemetry.addData("motor power", robot.shooter.shoot.getPower());
            dashboardTelemetry.update();

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
        public PathChain intakeballs2;
        public PathChain opengate1;
        public PathChain launchzone3;
        public PathChain hpzone1;
        public PathChain opengate2;
        public PathChain launchzone4;
        public PathChain opengate3;
        public PathChain hpzone2;
        public PathChain launchzone5;
        public PathChain opengate4;
        public PathChain hpzone3;
        public PathChain launchzone6;
        public PathChain ending;

        public Paths(Follower follower) {
            launchzone1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(112.132, 132.754),

                                    new Pose(92.716, 97.361)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(-129))

                    .build();

            intakeballs1 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(92.716, 97.361),
                                    new Pose(95.388, 79.189),
                                    new Pose(130.382, 84.079)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            launchzone2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(130.382, 84.079),

                                    new Pose(92.716, 97.361)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-129))

                    .build();

            intakeballs2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(92.716, 97.361),
                                    new Pose(92.669, 54.428),
                                    new Pose(128.154, 59.620)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            opengate1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(128.154, 59.620),

                                    new Pose(129.451, 65.891)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(270))

                    .build();

            launchzone3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(129.451, 65.891),

                                    new Pose(92.716, 97.361)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(-129))

                    .build();

            hpzone1 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(92.716, 97.361),
                                    new Pose(136.068, 55.447),
                                    new Pose(138.437, 20.703)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            opengate2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(138.437, 20.703),
                                    new Pose(123.654, 45.457),
                                    new Pose(129.451, 65.891)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(270))

                    .build();

            launchzone4 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(129.451, 65.891),

                                    new Pose(92.716, 97.361)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(-129))

                    .build();

            opengate3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(92.716, 97.361),

                                    new Pose(129.451, 65.891)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(270))

                    .build();

            hpzone2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(129.451, 65.891),
                                    new Pose(136.860, 53.665),
                                    new Pose(138.437, 20.703)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            launchzone5 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(138.437, 20.703),

                                    new Pose(92.716, 97.361)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(-129))

                    .build();

            opengate4 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(92.716, 97.361),

                                    new Pose(129.451, 65.891)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(270))

                    .build();

            hpzone3 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(129.451, 65.891),
                                    new Pose(136.860, 53.665),
                                    new Pose(138.437, 20.703)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            launchzone6 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(138.437, 20.703),

                                    new Pose(92.716, 97.361)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(-129))

                    .build();

            ending = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(92.716, 97.361),

                                    new Pose(98.535, 74.686)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(-129))

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






