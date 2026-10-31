package org.firstinspires.ftc.teamcode.OpModes;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.Paths;
import com.pedropathing.math.Pose;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.Subsystems.Transfer;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name = "SimpleAuto", group = "Autonomous")
public class SimpleAuto extends LinearOpMode {

    //Declare Follower and Robot
    private Follower follower;
    RobotHardware robot;


    //Declare commands (these could be moved to robot hardware or subsystems)
    Command shooterOn = instant(() -> robot.shooter.shooterMode = Shooter.ShooterMode.FIXED);
    Command shooterOff = instant(() -> robot.shooter.shooterMode = Shooter.ShooterMode.OFF);
    Command transferOn = instant(() -> robot.transfer.transferMode = Transfer.TransferMode.UP);
    Command transferOff = instant(() -> robot.transfer.transferMode = Transfer.TransferMode.OFF);
    Command gateOpen = instant(() -> robot.transfer.gatePosition = Transfer.GATEOPEN);
    Command flowerUp = instant(() -> robot.intake.flowerServoPosition = Intake.FLOWERUP);


    // Build Autonomous routine using sequential or parallel commands
    public Command autoRoutine() {
        return sequential(
                flowerUp,
                shooterOn,
                follow(follower, driveToLaunch()),
                gateOpen,
                transferOn,
                waitMs(2000),
                shooterOff,
                transferOff,
                follow(follower, driveToPark())
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        robot = new RobotHardware(this);
        robot.init();

        waitForStart();
        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            robot.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            telemetry.update();
        }
        OpModeStorage.autonomousEndPose = follower.pose();
    }

    //Paste Code from Visualizer here
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(56, 8, 90);
    private final Pose driveToLaunch = poseFactory.of(55.9768, 19.8366, 90);
    private final Pose driveToPark = poseFactory.of(11.4929, 94.2904, 90);
    private final Pose driveToParkControl1 = poseFactory.of(17.0979, 25.9381, 0);

    public Path driveToLaunch() {
        return Paths.line(start, driveToLaunch).linear(start, driveToLaunch);
    }

    public Path driveToPark() {
        return Paths.curve(driveToLaunch, driveToParkControl1, driveToPark).constant(driveToPark);
    }
}
