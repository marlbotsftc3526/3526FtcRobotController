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

import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Disabled
@Autonomous(name = "CyclingBlueSide", group = "AutoTemplates")
public class CyclingBlueSide extends LinearOpMode {
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
        ALIGN_ARTIFACTS2,
        COLLECT_ARTIFACTS2,
        DRIVE_TO_LAUNCH_POSITION3,
        LAUNCH_ARTIFACTS3,
        ALIGN_ARTIFACTS3,
        COLLECT_ARTIFACTS3,
        DRIVE_TO_LAUNCH_POSITION4,
        LAUNCH_ARTIFACTS4,
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

        follower = Constants.createFollower(hardwareMap);
        //TODO Set starting pose from path generation
        follower.setStartingPose(new Pose(64.49878091839476, 8.627558653834283, Math.toRadians(270)));

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
                        follower.followPath(paths.angleright,true);
                        robot.shooter.shootMode = Shooter.ShootMode.ON;
                        //ex. turn shooter on
                    }
                    robot.shooter.hoodMode = Shooter.HoodMode.AUTO;
                    robot.shooter.transferMode = Shooter.TransferMode.AUTO;

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
                    robot.shooter.transferMode = Shooter.TransferMode.AUTO;

                    //state transition
                    if(timer.seconds() > 2) {
                        currentState = State.COLLECT_ARTIFACTS;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTS:
                    if(onStateStart()){
                        follower.followPath(paths.Intakeballs1,true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION2;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION2:
                    if(onStateStart()){
                        follower.followPath(paths.launchzone1, true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.LAUNCH_ARTIFACTS2;

                    }
                    break;
                case LAUNCH_ARTIFACTS2:
                    if(onStateStart()){
                        timer.reset();
                        robot.shooter.transferMode = Shooter.TransferMode.AUTO;
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    if(timer.seconds() >2){
                        currentState = State.COLLECT_ARTIFACTS2;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTS2:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.Intakeballs2, true);
                    }
                    if(!follower.isBusy()|| timer.seconds()>2){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION3;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION3:
                    if(onStateStart()){
                        follower.followPath(paths.launchzone2, true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.LAUNCH_ARTIFACTS3;
                    }
                    break;
                case LAUNCH_ARTIFACTS3:
                        if(onStateStart()){
                            timer.reset();
                            robot.shooter.transferMode = Shooter.TransferMode.AUTO;
                            robot.intake.intakeMode = Intake.IntakeMode.UP;
                        }
                        if(timer.seconds() >2){
                            robot.shooter.transferMode = Shooter.TransferMode.OFF;
                            currentState = State.COLLECT_ARTIFACTS3;
                        }
                    break;
                case COLLECT_ARTIFACTS3:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.Intakeballs3, true);
                    }
                    if(!follower.isBusy()||timer.seconds()>2){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION4;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION4:
                    if(onStateStart()){
                        follower.followPath(paths.launchzone3, true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.LAUNCH_ARTIFACTS4;
                    }
                    break;
                case LAUNCH_ARTIFACTS4:
                    if(onStateStart()){
                        timer.reset();
                        robot.shooter.transferMode = Shooter.TransferMode.AUTO;
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    if(timer.seconds() >2){
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                        currentState = State.END;
                    }
                    break;
                case END:
                    if(onStateStart()){
                    }
                    if(!follower.isBusy()){
                        currentState = State.IDLE;
                        robot.intake.intakeMode = Intake.IntakeMode.DOWN;
                        robot.shooter.shootMode = Shooter.ShootMode.OFF;
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
            //telemetry.update();
        }
    }

    //TODO Define All Paths. Use the Visualizer auto generated code from https://visualizer.pedropathing.com/
    public static class Paths {

        public PathChain angleright;
        public PathChain Intakeballs1;
        public PathChain launchzone1;
        public PathChain Intakeballs2;
        public PathChain launchzone2;
        public PathChain Intakeballs3;
        public PathChain launchzone3;

        public Paths(Follower follower) {
            angleright = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(64.499, 8.628), new Pose(69.989, 11.627))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(-41))
                    .build();

            Intakeballs1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(69.989, 11.627), new Pose(8.200, 10.509))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-41), Math.toRadians(-180))
                    .build();

            launchzone1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(8.200, 10.509), new Pose(69.765, 11.627))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-180), Math.toRadians(-41))
                    .build();

            Intakeballs2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(69.765, 11.627), new Pose(8.200, 10.300))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-41), Math.toRadians(-180))
                    .build();

            launchzone2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(8.200, 10.300), new Pose(68.647, 12.075))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-180), Math.toRadians(-41))
                    .build();

            Intakeballs3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(68.647, 12.075), new Pose(8.200, 10.509))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-41), Math.toRadians(-180))
                    .build();

            launchzone3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(8.200, 10.509), new Pose(69.318, 12.075))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-180), Math.toRadians(-41))
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

