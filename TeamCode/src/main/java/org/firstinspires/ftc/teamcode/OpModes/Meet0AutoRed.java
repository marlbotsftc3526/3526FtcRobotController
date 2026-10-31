//package org.firstinspires.ftc.teamcode.OpModes;
//
//import com.pedropathing.ivy.Command;
//import com.pedropathing.ivy.Scheduler;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//import static com.pedropathing.ivy.Scheduler.schedule;
//import static com.pedropathing.ivy.groups.Groups.sequential;
//import static com.pedropathing.ivy.pedro.PedroCommands.follow;
//
//import org.firstinspires.ftc.teamcode.OpModes.OpModeStorage;
//import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
//import org.firstinspires.ftc.teamcode.Subsystems.Transfer;
//import org.firstinspires.ftc.teamcode.pedro.Constants;
//import com.pedropathing.follower.Follower;
//
//import com.pedropathing.api.PoseFactory;
//import com.pedropathing.math.Pose;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;
//import static com.pedropathing.ivy.commands.Commands.*;
//import java.util.List;
//
//import static com.pedropathing.api.Paths.*;
//import com.pedropathing.api.Paths;
//
//import com.pedropathing.api.PoseFactory;
//import com.pedropathing.math.Pose;
//import com.pedropathing.paths.Path;
//
//@Autonomous(name = "Meet0AutoRed", group = "AutoTemplates")
//public class Meet0AutoRed extends LinearOpMode {
//    RobotHardware robot;
//    private Follower follower;
//    private final PoseFactory p = PoseFactory.degrees(); // add this
//    private final Pose startPose = p.of(56, 8, 90);
//    Shooter shootshoot;
//    Paths paths;
//
//    public void runOpMode (){
//        Scheduler.reset();
//        robot = new RobotHardware(this);
//        robot.init();
//
//        Scheduler.reset();
//        follower = Constants.create(hardwareMap);
//        follower.setPose(startPose);
//        follower.update();
//
//        paths = new Paths();
//
//        Command turnonshooter = Command.build()
//                .setStart(() -> robot.shooter.shooterMode = Shooter.ShooterMode.FIXED)
//                .setDone(() -> true)
//                .requiring(robot.shooter);
//
//        Command turnshooteroff = instant(() -> robot.shooter.shooterMode = Shooter.ShooterMode.OFF);
//        //motor on
//        //done is true
//
//        Command transferon = Command.build()
//                .setStart(() -> robot.transfer.transferMode = Transfer.TransferMode.UP)
//                .setDone(() -> true)
//                .requiring(robot.transfer);
//
//        Command transferoff = instant(() -> robot.transfer.transferMode = Transfer.TransferMode.OFF);
//
//        Command opengate = instant(() -> robot.transfer.gate.setPosition(robot.transfer.GATEOPEN));
//
//        Command gotoshoot = follow(follower, paths.shootPreloads);
//        Command gotopark = follow(follower, paths.park);
//
//        Command sequence = sequential(
//                gotoshoot,
//                turnonshooter,
//                transferon,
//                opengate,
//                gotopark
//        );
//
//        waitForStart();
//
//        while(opModeIsActive()) {
//            robot.update();
//            Scheduler.execute();
//        }
//
//    }
//
//
//    public static class Paths {
//
//        private final PoseFactory poseFactory = PoseFactory.degrees();
//
//        private final Pose start = poseFactory.of(56, 8, 90);
//        private final Pose shootPreloads = poseFactory.of(56.1958, 19.3529, 90);
//        private final Pose park = poseFactory.of(7.5236, 108.2357, 90);
//        private final Pose parkControl1 = poseFactory.of(14.5355, 51.3644, 0);
//
//        public Path shootPreloads() {
//            return Paths.line(start, shootPreloads).linear(start, shootPreloads);
//        }
//
//        public Path park() {
//            return Paths.curve(shootPreloads, parkControl1, park).constant(park);
//        }
//    }
//
//    //call robot.update
//
//
//
////        RobotHardware robot;
////        private Follower follower;
////        private Paths paths;
////
////    public void runOpMode() {
////        robot = new RobotHardware(this);
////        robot.init();
////        //Shooter for robot
////        Command runshooter = Command.build()
////                .setExecute(() -> robot.shooter.shooterMode = Shooter.ShooterMode.AUTO);
////        //Intake
////        Command intakeup = Command.build()
////                .setExecute(() -> robot.intake.intakeMode = Intake.IntakeMode.IN);
////        .setDone(() -> )
////
////    }
////        // if case shootpreloads
////        //shooter, intake, transfer
////            //follow path shootpreloads
////            //intake mode launch
////            //turret shootmode auto
////            //shooter shootmode auto
////            //transfermode launch
////
////
////        //if case intakefromflower1
////            //follow path intakefromflower
////            //intake up
////            //transfer up
////            //turret shootmode off
////
////        //if case backup
////            //follow path backup
////            //intake off
////            //shootmode off
////        //if case shootflower
////            //follow path shootflower
////            //intake mode launch
////            //turret shootmode auto
////            //shooter shootmode auto
////            //transfermode launch
////        //if case intakeflower2
////            //follow path intakefromflower2
////            //intake up
////            //transfer up
////            //turret shootmode off
////        //if case shootflower2
////            //follow path shootflower2
////            //intake mode launch
////            //turret shootmode auto
////            //shooter shootmode auto
////            //transfermode launch
////        //if case park
////            //follow path park
////            //intake mode off
////            //turrest shootmode off
////            //transfermode launch off
////
////
////
////        public class Paths {
////
////            private final PoseFactory poseFactory = PoseFactory.degrees();
////
////            private final Pose start = poseFactory.of(56, 8, 90);
////            private final Pose shootpreloads = poseFactory.of(55.0461, 34.7281, 90);
////            private final Pose intakefromflowerStart = poseFactory.of(55.0461, 34.7281, 90);
////            private final Pose intakefromflower = poseFactory.of(8.4596, 46.636, 180);
////            private final Pose backupStart = poseFactory.of(8.4596, 46.636, 180);
////            private final Pose backup = poseFactory.of(32.4697, 93.4292, 270);
////            private final Pose shootflower = poseFactory.of(57.3315, 110.6506, 270);
////            private final Pose shootflowerControl1 = poseFactory.of(31.5944, 116.2169, 0);
////            private final Pose intakefromflower2Start = poseFactory.of(57.3315, 110.6506, 270);
////            private final Pose intakefromflower2 = poseFactory.of(49.709, 131.6742, 120);
////            private final Pose shootflower2Start = poseFactory.of(49.709, 131.6742, 120);
////            private final Pose shootflower2 = poseFactory.of(57.5753, 111.609, 270);
////            private final Pose park = poseFactory.of(7.9494, 111.8486, 180);
////
////            public Path shootpreloads() {
////                return Paths.line(start, shootpreloads).linear(start, shootpreloads);
////            }
////
////            public Path intakefromflower() {
////                return Paths.line(intakefromflowerStart, intakefromflower).linear(intakefromflowerStart, intakefromflower);
////            }
////
////            public Path backup() {
////                return Paths.line(backupStart, backup).linear(backupStart, backup);
////            }
////
////            public Path shootflower() {
////                return Paths.curve(backup, shootflowerControl1, shootflower).constant(shootflower);
////            }
////
////            public Path intakefromflower2() {
////                return Paths.line(intakefromflower2Start, intakefromflower2).linear(intakefromflower2Start, intakefromflower2);
////            }
////
////            public Path shootflower2() {
////                return Paths.line(shootflower2Start, shootflower2).linear(shootflower2Start, shootflower2);
////            }
////
////            public Path park() {
////                return Paths.line(shootflower2, park).constant(park);
////            }
////        }
//}
