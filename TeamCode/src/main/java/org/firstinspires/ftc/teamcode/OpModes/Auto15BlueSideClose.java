package org.firstinspires.ftc.teamcode.OpModes;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
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

@Autonomous(name = "Auto15BlueSide", group = "AutoTemplates")
public class Auto15BlueSideClose extends LinearOpMode {
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

        follower = Constants.createFollower(hardwareMap);
        //TODO Set starting pose from path generation
        follower.setStartingPose(new Pose(11.299798792756539, 109.23138832997988, Math.toRadians(0)));

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
                        follower.followPath(paths.launchzone1,true);
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
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.AUTO;
                    //state transition
                    if(timer.seconds() > 3) {
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
                        follower.followPath(paths.launchzone2, true);
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
                    robot.shooter.hoodMode = Shooter.HoodMode.AUTO;
                    if(timer.seconds() >3){
                        currentState = State.ALIGN_ARTIFACTS2;
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                    }
                    break;
                case ALIGN_ARTIFACTS2:
                    if(onStateStart()){
                        follower.followPath(paths.straight2, true);
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
                        follower.followPath(paths.launchzone3, true);
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
                    robot.shooter.hoodMode = Shooter.HoodMode.AUTO;
                        if(timer.seconds() >3){
                            robot.shooter.transferMode = Shooter.TransferMode.OFF;
                            currentState = State.ALIGN_ARTIFACTS3;
                        }
                    break;
                case ALIGN_ARTIFACTS3:
                    if(onStateStart()){
                        follower.followPath(paths.straight3, true);
                    }
                    if(!follower.isBusy())
                    {
                        currentState = State.COLLECT_ARTIFACTS3;
                    }
                    break;
                case COLLECT_ARTIFACTS3:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.intakeballs3, true);
                    }
                    if(!follower.isBusy()||timer.seconds()>2){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION4;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION4:
                    if(onStateStart()){
                        follower.followPath(paths.launchzone4, true);
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
                    robot.shooter.hoodMode = Shooter.HoodMode.AUTO;
                    if(timer.seconds() >3){
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                        currentState = State.STRAIGHT4;
                    }
                    break;
                case STRAIGHT4:
                    if(onStateStart()){
                        follower.followPath(paths.straight4, true);
                    }
                    if(!follower.isBusy())
                    {
                        currentState = State.COLLECT_ARTIFACTS5;
                    }
                    break;
                case COLLECT_ARTIFACTS5:
                    if(onStateStart()){
                        timer.reset();
                        follower.followPath(paths.intakeballs4, true);
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    if(!follower.isBusy()||timer.seconds()>2){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION5;
                    }
                    break;
                case DRIVE_TO_LAUNCH_POSITION5:
                    if(onStateStart()){
                        follower.followPath(paths.launchzone5, true);
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.ON;
                    robot.shooter.hoodMode = Shooter.HoodMode.AUTO;
                    if(!follower.isBusy() || timer.seconds() >3){
                        currentState = State.LAUNCH_ARTIFACTS;
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                        robot.shooter.shootMode = Shooter.ShootMode.ON;
                        robot.shooter.transferMode = Shooter.TransferMode.ON;
                    }
                    break;
                    case LAUNCH_ARTIFACTS5:
                    if(onStateStart()){
                        timer.reset();
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    robot.shooter.transferMode = Shooter.TransferMode.AUTO;
                    robot.shooter.hoodMode = Shooter.HoodMode.AUTO;
                    if(timer.seconds() >3.5){
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
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

        public Paths(Follower follower) {
            launchzone1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(14.647, 114.258), new Pose(52.556, 85.362))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-41))
                    .build();

            straight1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(52.556, 85.362), new Pose(43.365, 80.767))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-41), Math.toRadians(180))
                    .build();

            intakeballs1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(43.365, 80.767), new Pose(13.304, 80.076))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            launchzone2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(13.304, 80.076), new Pose(53.040, 80.940))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(-41))
                    .build();

            straight2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(53.040, 80.940), new Pose(47.827, 60.005))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-41), Math.toRadians(180))
                    .build();

            intakeballs2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(47.827, 60.005), new Pose(6.954, 58.817))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            launchzone3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(6.954, 58.817), new Pose(53.904, 76.276))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(-41))
                    .build();

            straight3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(53.904, 76.276), new Pose(47.227, 35.928))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-41), Math.toRadians(180))
                    .build();

            intakeballs3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(47.227, 35.928), new Pose(6.954, 35.638))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            launchzone4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(6.954, 35.638), new Pose(51.485, 77.139))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(-41))
                    .build();

            straight4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(51.485, 77.139), new Pose(9.059, 28.915))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            intakeballs4 = follower
                    .pathBuilder()
                    .addPath(new BezierLine(new Pose(9.059, 28.915), new Pose(8.692, 4.056)))
                    .setTangentHeadingInterpolation()
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

