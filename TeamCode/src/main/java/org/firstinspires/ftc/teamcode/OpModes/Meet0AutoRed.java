//package org.firstinspires.ftc.teamcode.OpModes;
//
//import com.bylazar.telemetry.PanelsTelemetry;
//import com.bylazar.telemetry.TelemetryManager;
//import com.pedropathing.follower.Follower;
//import com.pedropathing.geometry.BezierCurve;
//import com.pedropathing.geometry.BezierLine;
//import com.pedropathing.geometry.Pose;
//import com.pedropathing.paths.PathChain;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//
//import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
//import org.firstinspires.ftc.teamcode.Subsystems.Intake;
//import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
//import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
//import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
//
//
//import static com.pedropathing.ivy.Scheduler.schedule;
//import static com.pedropathing.ivy.commands.Commands.*;
//import static com.pedropathing.ivy.groups.Groups.sequential;
//import static org.firstinspires.ftc.teamcode.math.PoseMirror.mirror;
//
//
//@Autonomous(name = "Meet0AutoRed", group = "AutoTemplates")
//public class Meet0AutoRed extends LinearOpMode{
//        RobotHardware robot;
//        private Follower follower;
//        private Paths paths;
//
//    public void runOpMode() {
//        robot = new RobotHardware(this);
//        robot.init();
//        //Shooter for robot
//        Command runshooter = Command.build()
//                .setExecute(() -> robot.shooter.shooterMode = Shooter.ShooterMode.AUTO);
//        //Intake
//        Command intakeup = Command.build()
//                .setExecute(() -> robot.intake.intakeMode = Intake.IntakeMode.IN);
//        .setDone(() -> )
//
//    }
//        // if case shootpreloads
//        //shooter, intake, transfer
//            //follow path shootpreloads
//            //intake mode launch
//            //turret shootmode auto
//            //shooter shootmode auto
//            //transfermode launch
//
//
//        //if case intakefromflower1
//            //follow path intakefromflower
//            //intake up
//            //transfer up
//            //turret shootmode off
//
//        //if case backup
//            //follow path backup
//            //intake off
//            //shootmode off
//        //if case shootflower
//            //follow path shootflower
//            //intake mode launch
//            //turret shootmode auto
//            //shooter shootmode auto
//            //transfermode launch
//        //if case intakeflower2
//            //follow path intakefromflower2
//            //intake up
//            //transfer up
//            //turret shootmode off
//        //if case shootflower2
//            //follow path shootflower2
//            //intake mode launch
//            //turret shootmode auto
//            //shooter shootmode auto
//            //transfermode launch
//        //if case park
//            //follow path park
//            //intake mode off
//            //turrest shootmode off
//            //transfermode launch off
//
//
//
//        public class Paths {
//
//            private final PoseFactory poseFactory = PoseFactory.degrees();
//
//            private final Pose start = poseFactory.of(56, 8, 90);
//            private final Pose shootpreloads = poseFactory.of(55.0461, 34.7281, 90);
//            private final Pose intakefromflowerStart = poseFactory.of(55.0461, 34.7281, 90);
//            private final Pose intakefromflower = poseFactory.of(8.4596, 46.636, 180);
//            private final Pose backupStart = poseFactory.of(8.4596, 46.636, 180);
//            private final Pose backup = poseFactory.of(32.4697, 93.4292, 270);
//            private final Pose shootflower = poseFactory.of(57.3315, 110.6506, 270);
//            private final Pose shootflowerControl1 = poseFactory.of(31.5944, 116.2169, 0);
//            private final Pose intakefromflower2Start = poseFactory.of(57.3315, 110.6506, 270);
//            private final Pose intakefromflower2 = poseFactory.of(49.709, 131.6742, 120);
//            private final Pose shootflower2Start = poseFactory.of(49.709, 131.6742, 120);
//            private final Pose shootflower2 = poseFactory.of(57.5753, 111.609, 270);
//            private final Pose park = poseFactory.of(7.9494, 111.8486, 180);
//
//            public Path shootpreloads() {
//                return Paths.line(start, shootpreloads).linear(start, shootpreloads);
//            }
//
//            public Path intakefromflower() {
//                return Paths.line(intakefromflowerStart, intakefromflower).linear(intakefromflowerStart, intakefromflower);
//            }
//
//            public Path backup() {
//                return Paths.line(backupStart, backup).linear(backupStart, backup);
//            }
//
//            public Path shootflower() {
//                return Paths.curve(backup, shootflowerControl1, shootflower).constant(shootflower);
//            }
//
//            public Path intakefromflower2() {
//                return Paths.line(intakefromflower2Start, intakefromflower2).linear(intakefromflower2Start, intakefromflower2);
//            }
//
//            public Path shootflower2() {
//                return Paths.line(shootflower2Start, shootflower2).linear(shootflower2Start, shootflower2);
//            }
//
//            public Path park() {
//                return Paths.line(shootflower2, park).constant(park);
//            }
//        }
//}
