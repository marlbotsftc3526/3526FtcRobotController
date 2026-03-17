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
import org.firstinspires.ftc.teamcode.Subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.utility.PIDController;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;

@Autonomous(name = "Auto6RedSide", group = "AutoTemplates")
public class Auto6RedSide extends LinearOpMode {
    //Declare Robot and Follower
    RobotHardware robot;
    private Follower follower;
    private Paths paths;
    private TelemetryManager panelsTelemetry;

    public static final String X_POS_KEY = "X Position";
    public static final String Y_POS_KEY = "Y Position";
    public static final String HEADING_KEY = "Heading";
    // Panels Telemetry instance

    PIDController limelightTurnController;

    //Declare timer for use in switch, could have multiple timers if useful
    ElapsedTime timer = new ElapsedTime();

    // This enum defines our "state"
    // This is essentially just defines the possible steps our program will take
    //TODO Update states to reflect flow of robot actions
    enum State {
        DRIVE_TO_LAUNCH_POSITION,
        ADJUST,
        LAUNCH_ARTIFACTS,
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

            robot.limelight = hardwareMap.get(LimeLight.class, "LimeLight");
            robot.limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
            robot.limelight.start(); // This tells Limelight to start looking!

        robot.drivetrain.side = Drivetrain.SideMode.RED;

        follower = Constants.createFollower(hardwareMap);
        //TODO Set starting pose from path generation
        follower.setStartingPose(new Pose(87.581, 8.290, Math.toRadians(-90)));

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
                        follower.followPath(paths.launchartifacts1,true);
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
                        currentState = State.ADJUST;
                    }
                    break;

                case ADJUST:
                    if (onStateStart()){
                        timer.reset();
                        if(Math.abs(robot.drivetrain.offset) >= 1) {
                            robot.drivetrain.turn = -limelightTurnController.calculate(0, robot.drivetrain.offset);
                            robot.drivetrain.turn = Math.signum(robot.drivetrain.turn) * Math.max(Math.abs(robot.drivetrain.turn), robot.drivetrain.min_turn_speed);
                            robot.drivetrain.leftFrontDrive.setPower(robot.drivetrain.turn);
                            robot.drivetrain.rightFrontDrive.setPower(-robot.drivetrain.turn);
                            robot.drivetrain.leftBackDrive.setPower(robot.drivetrain.turn);
                            robot.drivetrain.rightBackDrive.setPower(-robot.drivetrain.turn);
                        }
                        else{
                            robot.drivetrain.leftFrontDrive.setPower(0);
                            robot.drivetrain.rightFrontDrive.setPower(0);
                            robot.drivetrain.leftBackDrive.setPower(0);
                            robot.drivetrain.rightBackDrive.setPower(0);
                        }
                    }
                    if(timer.seconds() > 2) {
                        currentState = State.LAUNCH_ARTIFACTS;
                        robot.drivetrain.leftFrontDrive.setPower(0);
                        robot.drivetrain.rightFrontDrive.setPower(0);
                        robot.drivetrain.leftBackDrive.setPower(0);
                        robot.drivetrain.rightBackDrive.setPower(0);
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
                    if(timer.seconds() > 2) {
                        currentState = State.COLLECT_ARTIFACTS;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTS:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.intakeartifacts1,true);
                    }
                    if(timer.seconds()> 1.5){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION2;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION2:
                    if(onStateStart()){
                        follower.followPath(paths.launchartifacts2, true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.ADJUST2;

                    }
                    break;

                case ADJUST2:
                    if (onStateStart()){
                        timer.reset();
                        if(Math.abs(robot.drivetrain.offset) >= 1) {
                            robot.drivetrain.turn = -limelightTurnController.calculate(0, robot.drivetrain.offset);
                            robot.drivetrain.turn = Math.signum(robot.drivetrain.turn) * Math.max(Math.abs(robot.drivetrain.turn), robot.drivetrain.min_turn_speed);
                            robot.drivetrain.leftFrontDrive.setPower(robot.drivetrain.turn);
                            robot.drivetrain.rightFrontDrive.setPower(-robot.drivetrain.turn);
                            robot.drivetrain.leftBackDrive.setPower(robot.drivetrain.turn);
                            robot.drivetrain.rightBackDrive.setPower(-robot.drivetrain.turn);
                        }
                        else{
                            robot.drivetrain.leftFrontDrive.setPower(0);
                            robot.drivetrain.rightFrontDrive.setPower(0);
                            robot.drivetrain.leftBackDrive.setPower(0);
                            robot.drivetrain.rightBackDrive.setPower(0);
                        }
                    }
                    if(timer.seconds() > 2) {
                        currentState = State.LAUNCH_ARTIFACTS2;
                        robot.drivetrain.leftFrontDrive.setPower(0);
                        robot.drivetrain.rightFrontDrive.setPower(0);
                        robot.drivetrain.leftBackDrive.setPower(0);
                        robot.drivetrain.rightBackDrive.setPower(0);
                    }
                    break;

                case LAUNCH_ARTIFACTS2:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;

                    if(timer.seconds() >1.5){
                        currentState = State.COLLECT_ARTIFACTS2;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;

                case COLLECT_ARTIFACTS2:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.intakeartifacts2, true);
                    }
                    if(!follower.isBusy() ||timer.seconds() >2){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION3;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION3:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.launchartifacts3, true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.ADJUST3;

                    }
                    break;

                case ADJUST3:
                    if (onStateStart()){
                        timer.reset();
                        if(Math.abs(robot.drivetrain.offset) >= 1) {
                            robot.drivetrain.turn = -limelightTurnController.calculate(0, robot.drivetrain.offset);
                            robot.drivetrain.turn = Math.signum(robot.drivetrain.turn) * Math.max(Math.abs(robot.drivetrain.turn), robot.drivetrain.min_turn_speed);
                            robot.drivetrain.leftFrontDrive.setPower(robot.drivetrain.turn);
                            robot.drivetrain.rightFrontDrive.setPower(-robot.drivetrain.turn);
                            robot.drivetrain.leftBackDrive.setPower(robot.drivetrain.turn);
                            robot.drivetrain.rightBackDrive.setPower(-robot.drivetrain.turn);
                        }
                        else{
                            robot.drivetrain.leftFrontDrive.setPower(0);
                            robot.drivetrain.rightFrontDrive.setPower(0);
                            robot.drivetrain.leftBackDrive.setPower(0);
                            robot.drivetrain.rightBackDrive.setPower(0);
                        }
                    }
                    if(timer.seconds() > 2) {
                        currentState = State.LAUNCH_ARTIFACTS3;
                        robot.drivetrain.leftFrontDrive.setPower(0);
                        robot.drivetrain.rightFrontDrive.setPower(0);
                        robot.drivetrain.leftBackDrive.setPower(0);
                        robot.drivetrain.rightBackDrive.setPower(0);
                    }
                    break;

                case LAUNCH_ARTIFACTS3:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;

                    if(timer.seconds() >1.5){
                        currentState = State.COLLECT_ARTIFACTS3;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTS3:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.intakeartifacts3, true);
                    }
                    if(!follower.isBusy() ||timer.seconds() >1.5){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION4;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;

                case DRIVE_TO_LAUNCH_POSITION4:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.launchartifacts4, true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.ADJUST4;

                    }
                    break;

                case ADJUST4:
                    if (onStateStart()){
                        timer.reset();
                        if(Math.abs(robot.drivetrain.offset) >= 1) {
                            robot.drivetrain.turn = -limelightTurnController.calculate(0, robot.drivetrain.offset);
                            robot.drivetrain.turn = Math.signum(robot.drivetrain.turn) * Math.max(Math.abs(robot.drivetrain.turn), robot.drivetrain.min_turn_speed);
                            robot.drivetrain.leftFrontDrive.setPower(robot.drivetrain.turn);
                            robot.drivetrain.rightFrontDrive.setPower(-robot.drivetrain.turn);
                            robot.drivetrain.leftBackDrive.setPower(robot.drivetrain.turn);
                            robot.drivetrain.rightBackDrive.setPower(-robot.drivetrain.turn);
                        }
                        else{
                            robot.drivetrain.leftFrontDrive.setPower(0);
                            robot.drivetrain.rightFrontDrive.setPower(0);
                            robot.drivetrain.leftBackDrive.setPower(0);
                            robot.drivetrain.rightBackDrive.setPower(0);
                        }
                    }
                    if(timer.seconds() > 2) {
                        currentState = State.LAUNCH_ARTIFACTS4;
                        robot.drivetrain.leftFrontDrive.setPower(0);
                        robot.drivetrain.rightFrontDrive.setPower(0);
                        robot.drivetrain.leftBackDrive.setPower(0);
                        robot.drivetrain.rightBackDrive.setPower(0);
                    }
                    break;

                case LAUNCH_ARTIFACTS4:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;

                    if(timer.seconds() >1.5){
                        currentState = State.COLLECT_ARTIFACTS4;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTS4:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.intakeartifacts4, true);
                    }
                    if(!follower.isBusy() ||timer.seconds() >1.5 || robot.intake.detectedTop && robot.intake.detectedMiddle && robot.intake.detectedBottom){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION5;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION5:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.launchartifacts5, true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.ADJUST5;

                    }
                    break;

                case ADJUST5:
                    if (onStateStart()){
                        timer.reset();
                        if(Math.abs(robot.drivetrain.offset) >= 1) {
                            robot.drivetrain.turn = -limelightTurnController.calculate(0, robot.drivetrain.offset);
                            robot.drivetrain.turn = Math.signum(robot.drivetrain.turn) * Math.max(Math.abs(robot.drivetrain.turn), robot.drivetrain.min_turn_speed);
                            robot.drivetrain.leftFrontDrive.setPower(robot.drivetrain.turn);
                            robot.drivetrain.rightFrontDrive.setPower(-robot.drivetrain.turn);
                            robot.drivetrain.leftBackDrive.setPower(robot.drivetrain.turn);
                            robot.drivetrain.rightBackDrive.setPower(-robot.drivetrain.turn);
                        }
                        else{
                            robot.drivetrain.leftFrontDrive.setPower(0);
                            robot.drivetrain.rightFrontDrive.setPower(0);
                            robot.drivetrain.leftBackDrive.setPower(0);
                            robot.drivetrain.rightBackDrive.setPower(0);
                        }
                    }
                    if(timer.seconds() > 2) {
                        currentState = State.LAUNCH_ARTIFACTS5;
                        robot.drivetrain.leftFrontDrive.setPower(0);
                        robot.drivetrain.rightFrontDrive.setPower(0);
                        robot.drivetrain.leftBackDrive.setPower(0);
                        robot.drivetrain.rightBackDrive.setPower(0);
                    }
                    break;

                case LAUNCH_ARTIFACTS5:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;

                    if(timer.seconds() >1.5){
                        currentState = State.COLLECT_ARTIFACTS5;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTS5:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.intakeartifacts5, true);
                    }
                    if(!follower.isBusy() ||timer.seconds() >1.5 || robot.intake.detectedTop && robot.intake.detectedMiddle && robot.intake.detectedBottom){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION6;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION6:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.launchartifacts6, true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.ADJUST6;

                    }
                    break;

                case ADJUST6:
                    if (onStateStart()){
                        timer.reset();
                        if(Math.abs(robot.drivetrain.offset) >= 1) {
                            robot.drivetrain.turn = -limelightTurnController.calculate(0, robot.drivetrain.offset);
                            robot.drivetrain.turn = Math.signum(robot.drivetrain.turn) * Math.max(Math.abs(robot.drivetrain.turn), robot.drivetrain.min_turn_speed);
                            robot.drivetrain.leftFrontDrive.setPower(robot.drivetrain.turn);
                            robot.drivetrain.rightFrontDrive.setPower(-robot.drivetrain.turn);
                            robot.drivetrain.leftBackDrive.setPower(robot.drivetrain.turn);
                            robot.drivetrain.rightBackDrive.setPower(-robot.drivetrain.turn);
                        }
                        else{
                            robot.drivetrain.leftFrontDrive.setPower(0);
                            robot.drivetrain.rightFrontDrive.setPower(0);
                            robot.drivetrain.leftBackDrive.setPower(0);
                            robot.drivetrain.rightBackDrive.setPower(0);
                        }
                    }
                    if(timer.seconds() > 2) {
                        currentState = State.LAUNCH_ARTIFACTS6;
                        robot.drivetrain.leftFrontDrive.setPower(0);
                        robot.drivetrain.rightFrontDrive.setPower(0);
                        robot.drivetrain.leftBackDrive.setPower(0);
                        robot.drivetrain.rightBackDrive.setPower(0);
                    }
                    break;

                case LAUNCH_ARTIFACTS6:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.LAUNCH;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;

                    if(timer.seconds() >1.5){
                        currentState = State.END;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case END:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.end, true);
                    }
                    if(!follower.isBusy()) {
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
            launchartifacts1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(87.581, 8.290),

                                    new Pose(89.243, 17.390)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-90), Math.toRadians(252))

                    .build();

            intakeartifacts1 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(89.243, 17.390),
                                    new Pose(106.135, 10.333),
                                    new Pose(138.313, 13.003)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            launchartifacts2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(138.313, 13.003),

                                    new Pose(89.134, 17.607)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(252))

                    .build();

            intakeartifacts2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(89.134, 17.607),
                                    new Pose(103.150, 7.702),
                                    new Pose(138.313, 5.277)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            launchartifacts3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(138.313, 5.277),

                                    new Pose(89.236, 17.458)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(252))

                    .build();

            intakeartifacts3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(89.236, 17.458),

                                    new Pose(139.637, 1.007)

                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            launchartifacts4 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(139.637, 1.007),

                                    new Pose(89.236, 17.458)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(252))

                    .build();

            intakeartifacts4 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(89.236, 17.458),
                                    new Pose(106.619, 0.011),
                                    new Pose(139.199, 4.562)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            launchartifacts5 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(139.199, 4.562),

                                    new Pose(89.126, 17.487)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(252))

                    .build();

            intakeartifacts5 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(89.236, 17.458),
                                    new Pose(106.619, 0.011),
                                    new Pose(139.199, 4.562)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            launchartifacts6 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(139.199, 4.562),

                                    new Pose(89.126, 17.487)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(252))

                    .build();

            end = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(89.126, 17.487),

                                    new Pose(109.992, 9.935)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(252))

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

