package org.firstinspires.ftc.teamcode.OpModes;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.nio.file.Paths;

@Autonomous(name = "PedroAutoTemplate", group = "AutoTemplates")
public class PedroAutoTemplate extends LinearOpMode {
    //Declare Robot and Follower
    RobotHardware robot;
    private Follower follower;
    private Paths paths;
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance

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
        follower.setStartingPose(new Pose(14.148048452220726, 112.21534320323015, Math.toRadians(0)));

        paths = new Paths(follower); // Build paths

        //Update Panels dashboard telemetry
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);

        // Wait for the game to start (driver presses START)
        telemetry.addData(">", "Robot Initialized");
        telemetry.addData("Status", "Waiting for Start");
        telemetry.update();

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
                        robot.shooter.transferMode = Shooter.TransferMode.ON;
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }

                    //state transition
                    if(timer.seconds() > 2) {
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
                        robot.shooter.transferMode = Shooter.TransferMode.ON;
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    if(timer.seconds() >2){
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
                        follower.followPath(paths.intakeballs2, true);
                    }
                    if(!follower.isBusy()){
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
                            robot.shooter.transferMode = Shooter.TransferMode.ON;
                            robot.intake.intakeMode = Intake.IntakeMode.UP;
                        }
                        if(timer.seconds() >2){
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
                        follower.followPath(paths.intakeballs3, true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.DRIVE_TO_LAUNCH_POSITION3;
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
                        robot.shooter.transferMode = Shooter.TransferMode.ON;
                        robot.intake.intakeMode = Intake.IntakeMode.UP;
                    }
                    if(timer.seconds() >2){
                        robot.shooter.transferMode = Shooter.TransferMode.OFF;
                        currentState = State.END;
                    }
                    break;
                case END:
                    if(onStateStart()){
                        follower.followPath(paths.ending, true);
                    }
                    if(!follower.isBusy()){
                        currentState = State.IDLE;
                    }
                    break;
                case IDLE:

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
        public PathChain ending;

        public Paths(Follower follower) {
            launchzone1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(16.225, 113.839), new Pose(47.807, 96.773))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-43))
                    .build();

            straight1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(47.807, 96.773), new Pose(46.648, 84.893))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-43), Math.toRadians(180))
                    .build();


            intakeballs1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(46.648, 84.893), new Pose(12.748, 83.734))
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();

            launchzone2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(12.748, 83.734), new Pose(46.938, 96.483))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(-41))
                    .build();
            straight2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(46.938, 96.483), new Pose(47.227, 60.266))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-41), Math.toRadians(180))
                    .build();

            intakeballs2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(47.227, 60.266), new Pose(10.431, 59.686))
                    )
                    .setTangentHeadingInterpolation()
                    .build();
            launchzone3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(14.487, 60.266), new Pose(47.807, 96.773))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(-41))
                    .build();
            straight3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(47.807, 96.773), new Pose(47.227, 35.928))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(-41), Math.toRadians(180))
                    .build();

            intakeballs3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(47.227, 35.928), new Pose(9.851, 35.348))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            launchzone4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(9.851, 35.348), new Pose(47.517, 95.903))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(-41))
                    .build();
            ending = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(47.517, 95.903), new Pose(50.414, 61.425))
                    )
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

