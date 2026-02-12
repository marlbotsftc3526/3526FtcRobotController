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
        COLLECT_ARTIFACTS,
        DRIVE_TO_LAUNCH_POSITION2,
        LAUNCH_ARTIFACTS2,
        COLLECT_ARTIFACTS2,
        DRIVE_TO_LAUNCH_POSITION3,
        LAUNCH_ARTIFACTS3,
        COLLECT_ARTIFACTS3,
        DRIVE_TO_LAUNCH_POSITION4,
        LAUNCH_ARTIFACTS4,
        STRAIGHT4,
        COLLECT_ARTIFACTS5,
        DRIVE_TO_LAUNCH_POSITION5,
        LAUNCH_ARTIFACTS5,
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

        follower = Constants.createFollower(hardwareMap);
        //TODO Set starting pose from path generation
        follower.setStartingPose(new Pose(87.823, 7.865, Math.toRadians(-90)));

        paths = new Paths(follower); // Build paths

        //Update Panels dashboard telemetry
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);

        // Wait for the game to start (driver presses START)
        telemetry.addData(">", "Robot Initialized");
        telemetry.addData("Status", "Waiting for Start");
        telemetry.update();

        robot.shooter.hoodMode = Shooter.HoodMode.LINEAR;

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
                        robot.shooter.shootMode = Shooter.ShootMode.ON;
                    }

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
                    if(timer.seconds() > 1.5) {
                        currentState = State.COLLECT_ARTIFACTS;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case COLLECT_ARTIFACTS:
                    if(onStateStart()){
                        follower.followPath(paths.intakeartifacts1,true);
                    }
                    if(!follower.isBusy() || timer.seconds()> 1.5 || robot.intake.detectedTop && robot.intake.detectedMiddle && robot.intake.detectedBottom){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION2;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION2:
                    if(onStateStart()){
                        follower.followPath(paths.launchartifacts2, true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.LAUNCH_ARTIFACTS2;

                    }
                    break;
                case LAUNCH_ARTIFACTS2:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.AUTO;

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
                    if(!follower.isBusy()|| timer.seconds()>1.5){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION3;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION3:
                    if(onStateStart()){
                        follower.followPath(paths.launchartifacts3, true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.LAUNCH_ARTIFACTS3;
                    }
                    break;
                case LAUNCH_ARTIFACTS3:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.AUTO;

                    if(timer.seconds() >1.7){
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                        currentState = State.COLLECT_ARTIFACTS3;
                    }
                    break;
                case COLLECT_ARTIFACTS3:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.intakeartifacts3, true);
                    }
                    if(!follower.isBusy()|| timer.seconds()>1.5){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION4;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION4:
                    if(onStateStart()){
                        follower.followPath(paths.launchartifacts4, true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.LAUNCH_ARTIFACTS4;
                    }
                    break;
                case LAUNCH_ARTIFACTS4:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.AUTO;

                    if(timer.seconds() >1.7){
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                        currentState = State.STRAIGHT4;
                    }
                    break;
                case STRAIGHT4:
                    if(onStateStart()){
                        follower.followPath(paths.turntointake, true);
                    }

                    if(!follower.isBusy()){
                        currentState = State.COLLECT_ARTIFACTS5;
                    }
                    break;
                case COLLECT_ARTIFACTS5:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.intakeartifacts4, true);
                    }
                    if(!follower.isBusy()|| timer.seconds()>1.5){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION5;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION5:
                    if(onStateStart()){
                        follower.followPath(paths.launchartifacts5, true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.LAUNCH_ARTIFACTS5;
                    }
                    break;
                case LAUNCH_ARTIFACTS5:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.AUTO;

                    if(timer.seconds() >1.7){
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                        currentState = State.END;
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
        public PathChain turntointake;
        public PathChain intakeartifacts4;
        public PathChain launchartifacts5;
        public PathChain end;

        public Paths(Follower follower) {
            launchartifacts1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(87.823, 7.865),

                                    new Pose(88.583, 14.466)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(244))

                    .build();

            intakeartifacts1 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(88.583, 14.466),
                                    new Pose(85.878, 39.622),
                                    new Pose(135.666, 39.558)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(244), Math.toRadians(0))

                    .build();

            launchartifacts2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(135.666, 39.558),

                                    new Pose(90.627, 13.640)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(244))

                    .build();

            intakeartifacts2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(90.627, 13.640),
                                    new Pose(110.106, 10.696),
                                    new Pose(133.713, 13.363)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(244), Math.toRadians(0))

                    .build();

            launchartifacts3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(133.713, 13.363),

                                    new Pose(90.871, 13.581)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(244))

                    .build();

            intakeartifacts3 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(90.871, 13.581),
                                    new Pose(111.990, 6.944),
                                    new Pose(136.697, 8.988)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(244), Math.toRadians(0))

                    .build();

            launchartifacts4 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(136.697, 8.988),

                                    new Pose(90.997, 13.635)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(244))

                    .build();

            turntointake = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(90.997, 13.635),

                                    new Pose(110.520, 9.421)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(244), Math.toRadians(0))

                    .build();

            intakeartifacts4 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(110.520, 9.421),

                                    new Pose(138.866, 4.096)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                    .build();

            launchartifacts5 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(138.866, 4.096),

                                    new Pose(90.584, 13.411)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(244))

                    .build();

            end = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(90.584, 13.411),

                                    new Pose(105.273, 13.309)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(244), Math.toRadians(0))

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

