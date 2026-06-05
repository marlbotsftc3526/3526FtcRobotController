package org.firstinspires.ftc.teamcode.OpModes;

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

import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Auto9BlueLimelight", group = "AutoTemplates")
public class Auto9BlueLimelight extends LinearOpMode {
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
        ADJUST,
        LAUNCH_ARTIFACTS,
        COLLECT_ARTIFACTS_SPIKE,
        DRIVE_TO_LAUNCH_POSITION1,
        ADJUST1,
        LAUNCH_ARTIFACTS1,
        COLLECT_ARTIFACTS,
        DRIVE_TO_LAUNCH_POSITION2,
        ADJUST2,
        LAUNCH_ARTIFACTS2,
        COLLECT_ARTIFACTS2,
        DRIVE_TO_LAUNCH_POSITION3,
        ADJUST3,
        LAUNCH_ARTIFACTS3,
        COLLECT_ARTIFACTS3,
        DRIVE_TO_LAUNCH_POSITION4,
        ADJUST4,
        LAUNCH_ARTIFACTS4,
        COLLECT_ARTIFACTS4,
        DRIVE_TO_LAUNCH_POSITION5,
        ADJUST5,
        LAUNCH_ARTIFACTS5,
        COLLECT_ARTIFACTS5,
        DRIVE_TO_LAUNCH_POSITION6,
        ADJUST6,
        LAUNCH_ARTIFACTS6,
        END,
        IDLE,
    }

    // We define the current state we're on
    State currentState = State.DRIVE_TO_LAUNCH_POSITION;
    // Store the last state run to manage flow of path following in state machine
    State lastState = State.IDLE;

    @Override
    public void runOpMode() {
        robot = new RobotHardware(this);
        robot.init();
        robot.drivetrain.side = Drivetrain.SideMode.BLUE;
        robot.limelight.limelight.pipelineSwitch(1);

        follower = Constants.createFollower(hardwareMap);
        //TODO Set starting pose from path generation
        follower.setStartingPose(new Pose(55.62977867203219, 8.692152917505023, Math.toRadians(-90)));

        paths = new Paths(follower); // Build paths

        //Update Panels dashboard telemetry
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);

        // Wait for the game to start (driver presses START)
        telemetry.addData(">", "Robot Initialized");
        telemetry.addData("Status", "Waiting for Start");
        telemetry.update();

        robot.shooter.hoodMode = Shooter.HoodMode.FAR; //linear

        //  Object xPosition = blackboard.getOrDefault(X_POS_KEY, 0);
        //Object yPosition = blackboard.getOrDefault(Y_POS_KEY, 0);
        //Object heading = blackboard.getOrDefault(HEADING_KEY, 0);
        while(!isStarted() && !isStopRequested()){
            robot.limelight.update();
            telemetry.addData("targetVisible", robot.limelight.targetVisible);
            telemetry.addData("tx", robot.limelight.tx);
            telemetry.addData("Status", "Waiting for Start");
            telemetry.update();
        }
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
                        follower.followPath(paths.launchartifacts0);
                        //ex. turn shooter on
                        robot.shooter.shootMode = Shooter.ShootMode.BANGBANG;
                    }

                    //set the condition to advance to the next state
                    /* You could check for
                        - Follower State: "if(!follower.isBusy()) {}"
                        - Time: "if(pathTimer.getElapsedTimeSeconds() > 1) {}"
                        - Sensor Value: "if(touchDetected) {}" etc...
                        */
                    if(!follower.isBusy()) {
                        currentState = Auto9BlueLimelight.State.ADJUST;
                    }
                    break;

                case ADJUST:
                    if (onStateStart()){
                        timer.reset();
                    }
                    robot.drivetrain.adjustblue();
                    if(timer.seconds() > 1) {
                        currentState = Auto9BlueLimelight.State.LAUNCH_ARTIFACTS;
                        robot.drivetrain.stop();
                    }
                    break;

                case LAUNCH_ARTIFACTS:
                    //state start
                    if(onStateStart()){
                        //you could restart timers in here
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;
                    //state transition
                    if(timer.seconds() > 0.75) {
                        currentState = Auto9BlueLimelight.State.COLLECT_ARTIFACTS_SPIKE;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTS_SPIKE:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.collectartifactsfromspike,true);
                    }
                    if(timer.seconds()> 1.5){
                        currentState = Auto9BlueLimelight.State.DRIVE_TO_LAUNCH_POSITION1;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION1:
                    if(onStateStart()){
                        follower.followPath(paths.launchartifacts1);
                    }
                    if(!follower.isBusy()){
                        currentState = Auto9BlueLimelight.State.ADJUST1;

                    }
                    break;

                case ADJUST1:
                    if (onStateStart()){
                        timer.reset();
                    }
                    robot.drivetrain.adjustblue();
                    if(timer.seconds() > 1) {
                        currentState = Auto9BlueLimelight.State.LAUNCH_ARTIFACTS1;
                        robot.drivetrain.stop();
                    }
                    break;

                case LAUNCH_ARTIFACTS1:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;

                    if(timer.seconds() > 0.75){
                        currentState = Auto9BlueLimelight.State.COLLECT_ARTIFACTS;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTS:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.intakeartifacts1, true);
                    }
                    if(!follower.isBusy() ||timer.seconds() >2){
                        currentState = Auto9BlueLimelight.State.DRIVE_TO_LAUNCH_POSITION2;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION2:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.launchartifacts2);
                    }
                    if(!follower.isBusy()){
                        currentState = Auto9BlueLimelight.State.ADJUST2;

                    }
                    break;

                case ADJUST2:
                    if (onStateStart()){
                        timer.reset();
                    }
                    robot.drivetrain.adjustblue();
                    if(timer.seconds() > 1) {
                        currentState = Auto9BlueLimelight.State.LAUNCH_ARTIFACTS2;
                        robot.drivetrain.stop();
                    }
                    break;

                case LAUNCH_ARTIFACTS2:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;

                    if(timer.seconds() > 0.75){
                        currentState = Auto9BlueLimelight.State.COLLECT_ARTIFACTS2;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTS2:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.intakeartifacts2, true);
                    }
                    if(!follower.isBusy() ||timer.seconds() >2){
                        currentState = Auto9BlueLimelight.State.DRIVE_TO_LAUNCH_POSITION3;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION3:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.launchartifacts3);
                    }
                    if(!follower.isBusy()){
                        currentState = Auto9BlueLimelight.State.ADJUST3;

                    }
                    break;

                case ADJUST3:
                    if (onStateStart()){
                        timer.reset();
                    }
                    robot.drivetrain.adjustblue();
                    if(timer.seconds() > 1) {
                        currentState = Auto9BlueLimelight.State.LAUNCH_ARTIFACTS3;
                        robot.drivetrain.stop();
                    }
                    break;

                case LAUNCH_ARTIFACTS3:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;

                    if(timer.seconds() > 0.75){
                        currentState = Auto9BlueLimelight.State.COLLECT_ARTIFACTS3;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTS3:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.intakeartifacts3, true);
                    }
                    if(!follower.isBusy() ||timer.seconds() >1.5){
                        currentState = Auto9BlueLimelight.State.DRIVE_TO_LAUNCH_POSITION4;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;

                case DRIVE_TO_LAUNCH_POSITION4:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.launchartifacts4);
                    }
                    if(!follower.isBusy()){
                        currentState = Auto9BlueLimelight.State.ADJUST4;

                    }
                    break;

                case ADJUST4:
                    if (onStateStart()){
                        timer.reset();
                    }
                    robot.drivetrain.adjustblue();
                    if(timer.seconds() > 1) {
                        currentState = Auto9BlueLimelight.State.LAUNCH_ARTIFACTS4;
                        robot.drivetrain.stop();
                    }
                    break;

                case LAUNCH_ARTIFACTS4:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;

                    if(timer.seconds() > 0.75){
                        currentState = Auto9BlueLimelight.State.COLLECT_ARTIFACTS4;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTS4:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.intakeartifacts4, true);
                    }
                    if(!follower.isBusy() ||timer.seconds() >1.5 || robot.intake.detectedTop && robot.intake.detectedMiddle && robot.intake.detectedBottom){
                        currentState = Auto9BlueLimelight.State.DRIVE_TO_LAUNCH_POSITION5;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION5:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.launchartifacts5);
                    }
                    if(!follower.isBusy()){
                        currentState = Auto9BlueLimelight.State.ADJUST5;

                    }
                    break;

                case ADJUST5:
                    if (onStateStart()){
                        timer.reset();
                    }
                    robot.drivetrain.adjustblue();
                    if(timer.seconds() > 1) {
                        currentState = Auto9BlueLimelight.State.LAUNCH_ARTIFACTS5;
                        robot.drivetrain.stop();
                    }
                    break;

                case LAUNCH_ARTIFACTS5:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;

                    if(timer.seconds() > 0.5){
                        currentState = Auto9BlueLimelight.State.COLLECT_ARTIFACTS5;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTS5:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.intakeartifacts5, true);
                    }
                    if(!follower.isBusy() ||timer.seconds() >1.5 || robot.intake.detectedTop && robot.intake.detectedMiddle && robot.intake.detectedBottom){
                        currentState = Auto9BlueLimelight.State.DRIVE_TO_LAUNCH_POSITION6;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION6:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.launchartifacts6);
                    }
                    if(!follower.isBusy()){
                        currentState = Auto9BlueLimelight.State.ADJUST6;

                    }
                    break;

                case ADJUST6:
                    if (onStateStart()){
                        timer.reset();
                    }
                    robot.drivetrain.adjustblue();
                    if(timer.seconds() > 1) {
                        currentState = Auto9BlueLimelight.State.LAUNCH_ARTIFACTS6;
                        robot.drivetrain.stop();
                    }
                    break;

                case LAUNCH_ARTIFACTS6:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;

                    if(timer.seconds() > 0.75){
                        currentState = Auto9BlueLimelight.State.END;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case END:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.end, true);
                    }
                    if(!follower.isBusy()) {
                        currentState = Auto9BlueLimelight.State.IDLE;
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
            panelsTelemetry.debug("turn", robot.drivetrain.turn);
            panelsTelemetry.debug("offset", robot.drivetrain.offset);
            panelsTelemetry.debug("Tx", robot.limelight.tx);
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
        public PathChain launchartifacts0;
        public PathChain collectartifactsfromspike;
        public PathChain launchartifacts1;
        public PathChain intakeartifacts1;
        public PathChain launchartifacts2;
        public PathChain intakeartifacts2;
        public PathChain launchartifacts3;
        public PathChain intakeartifacts3;
        public PathChain launchartifacts4;
        public PathChain intakeartifacts4;
        public PathChain launchartifacts5;
        public PathChain intakeartifacts5;
        public PathChain launchartifacts6;
        public PathChain end;
        public Paths(Follower follower) {
               launchartifacts0 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(55.62977867203219, 8.692),

                                    new Pose(56.422, 18.379)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(291))

                    .build();
            collectartifactsfromspike = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(56.422, 18.379),
                                    new Pose(55.317, 37.786),
                                    new Pose(11.033, 34.384)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();
            launchartifacts1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(11.033, 34.384),

                                    new Pose(56.722, 18.379)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(289))

                    .build();

            intakeartifacts1 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(56.722, 18.379),
                                    new Pose(33.894, 10.696),
                                    new Pose(8.038, 13.297)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            launchartifacts2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(8.038, 13.297),

                                    new Pose(56.722, 18.673)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(289))

                    .build();

            intakeartifacts2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(56.722, 18.673),
                                    new Pose(32.010, 6.944),
                                    new Pose(5.841, 5.277)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            launchartifacts3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(5.841, 5.277),

                                    new Pose(56.722, 18.673)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(289))

                    .build();

            intakeartifacts3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(56.722, 18.673),

                                    new Pose(4.363, 1.007)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            launchartifacts4 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(4.363, 1.007),

                                    new Pose(56.722, 18.673)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(289))

                    .build();

            intakeartifacts4 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(56.722, 18.673),

                                    new Pose(6.573, 8.912)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            launchartifacts5 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(6.573, 8.912),

                                    new Pose(56.722, 18.673)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(289))

                    .build();

            intakeartifacts5 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(56.722, 18.673),

                                    new Pose(5.112, 4.824)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            launchartifacts6 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(5.112, 4.824),

                                    new Pose(56.428, 18.673)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(289))

                    .build();

            end = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(56.428, 18.673),

                                    new Pose(42.547, 14.191)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(289))

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


