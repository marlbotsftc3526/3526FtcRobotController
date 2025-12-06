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

import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Auto9RedSide", group = "AutoTemplates")
public class Auto9RedSide extends LinearOpMode {
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
        GOSTRAIGHT,
        ALIGN_ARTIFACTS2,
        COLLECT_ARTIFACTS2,
        DRIVE_TO_LAUNCH_POSITION3,
        LAUNCH_ARTIFACTS3,
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
        follower.setStartingPose(new Pose(80.53120900998199, 7.642669164706678, Math.toRadians(90)));

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
                        follower.followPath(paths.TurnToShoot,true);
                        //ex. turn shooter on
                        robot.shooter.shootMode = Shooter.ShootMode.ON;
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
                        robot.shooter.transferMode = Shooter.TransferMode.ON;
                    }
                    robot.shooter.hoodMode = Shooter.HoodMode.AUTO;
                    //state transition
                    if(timer.seconds() > 1.5) {
                        currentState = State.ALIGN_ARTIFACTS;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case ALIGN_ARTIFACTS:
                    if(onStateStart()){
                        follower.followPath(paths.straight1, true);
                    }

                    if(!follower.isBusy()){
                        currentState = State.COLLECT_ARTIFACTS;
                    }
                    break;
                case COLLECT_ARTIFACTS:
                    if(onStateStart()){
                        follower.followPath(paths.intakeballs1,true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION2;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION2:
                    if(onStateStart()){
                        follower.followPath(paths.launchballs1, true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.LAUNCH_ARTIFACTS2;

                    }
                    break;
                case LAUNCH_ARTIFACTS2:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                        robot.shooter.transferMode = Shooter.TransferMode.ON;
                    }
                    robot.shooter.hoodMode = Shooter.HoodMode.AUTO;
                    if(timer.seconds() >1.5){
                        currentState = State.ALIGN_ARTIFACTS2;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case GOSTRAIGHT:
                    if(onStateStart()){
                        follower.followPath(paths.straight2, true);
                    }
                    if(!follower.isBusy())
                    {
                        currentState = State.ALIGN_ARTIFACTS2;
                    }
                    break;
                case ALIGN_ARTIFACTS2:
                    if(onStateStart()){
                        follower.followPath(paths.goforward, true);
                    }
                    if(!follower.isBusy())
                    {
                        currentState = State.COLLECT_ARTIFACTS2;
                    }
                    break;
                case COLLECT_ARTIFACTS2:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.intakeballs2, true);
                    }
                    if(!follower.isBusy()|| timer.seconds()>2){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION3;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION3:
                    if(onStateStart()){
                        follower.followPath(paths.launchballs2, true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.LAUNCH_ARTIFACTS3;
                    }
                    break;
                case LAUNCH_ARTIFACTS3:
                        if(onStateStart()){
                            timer.reset();
                            robot.intake.intakeMode = Intake.IntakeMode.UP;
                            robot.shooter.transferMode = Shooter.TransferMode.ON;
                        }
                    robot.shooter.hoodMode = Shooter.HoodMode.AUTO;
                        if(timer.seconds() >1.5){
                            robot.shooter.transferMode = Shooter.TransferMode.OFF;
                            currentState = State.END;
                        }
                    break;
                case END:
                    if(onStateStart()){
                        follower.followPath(paths.End, true);
                        robot.intake.intakeMode = Intake.IntakeMode.OFF;
                    }
                    if(!follower.isBusy()){
                        robot.shooter.shootMode = Shooter.ShootMode.OFF;
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

        public PathChain TurnToShoot;
        public PathChain straight1;
        public PathChain intakeballs1;
        public PathChain launchballs1;
        public PathChain straight2;
        public PathChain goforward;
        public PathChain intakeballs2;
        public PathChain launchballs2;
        public PathChain End;

        public Paths(Follower follower) {
            TurnToShoot = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(80.531, 7.643), new Pose(83.139, 14.163))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(-109))
                    .build();

            straight1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(83.139, 14.163), new Pose(83.948, 34.138))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-109), Math.toRadians(0))
                    .build();

            intakeballs1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(83.948, 34.138), new Pose(131.186, 35.758))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            launchballs1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(131.186, 35.758), new Pose(83.139, 14.163))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-116))
                    .build();

            straight2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(83.139, 14.163), new Pose(83.678, 35.758))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-116), Math.toRadians(0))
                    .build();

            goforward = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(83.678, 35.758), new Pose(137.900, 36.838))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-90))
                    .build();

            intakeballs2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(137.900, 36.838), new Pose(137.900, 3.000))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            launchballs2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(137.900, 3.000),
                                    new Pose(118.230, 35.758),
                                    new Pose(83.139, 14.163)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-90), Math.toRadians(-122))
                    .build();

            End = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(83.139, 14.163), new Pose(84.218, 35.488))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-122), Math.toRadians(-90))
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

