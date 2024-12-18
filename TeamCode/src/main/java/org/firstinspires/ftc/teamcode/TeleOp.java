package org.firstinspires.ftc.teamcode;


import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotHardware;
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "teleop", group = "Linear Opmode")
public class TeleOp extends LinearOpMode {

    RobotHardware robot = new RobotHardware(this);
    private ElapsedTime runtime = new ElapsedTime();

    String gamestate = "";

    @Override
    public void runOpMode() {

        robot.init();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            robot.teleOp();
            telemetry.update();
            runtime.reset();
            if(gamestate == "LOWCHAM"){
                    if (gamepad2.right_bumper & !gamepad2.left_bumper) {
                        robot.lift.liftMode = Lift.LiftMode.LOW_CHAMBER;
                    }
                    if(robot.lift.liftLeft.getCurrentPosition() > 680){
                        robot.claw.clawPivot.setPosition(robot.claw.pivotSCORE);
                    }
                    if (gamepad2.right_bumper && gamepad2.left_bumper) {

                        robot.lift.liftMode = Lift.LiftMode.LOW_CHAMBER_SCORE;
                    }
                }
            if(gamestate == "HIGHCHAM") {
                    if(robot.extension.extension.getCurrentPosition() > 1300 &&

                            robot.lift.liftLeft.getCurrentPosition() < 300){
                        robot.claw.clawPivot.setPosition(robot.claw.pivotUP);
                        if(robot.lift.liftLeft.getCurrentPosition() < 500) {
                            robot.claw.clawSpin.setPosition(robot.claw.spinA);
                        }
                    }
                    if (gamepad2.right_bumper & !gamepad2.left_bumper) {
                        robot.lift.liftMode = Lift.LiftMode.HIGH_CHAMBER;
                    }
                    if(robot.lift.liftLeft.getCurrentPosition() > 1480){

                        robot.claw.clawPivot.setPosition(robot.claw.pivotSCORE);

                    }
                    if (gamepad2.right_bumper && gamepad2.left_bumper) {
                        robot.lift.liftMode = Lift.LiftMode.HIGH_CHAMBER_SCORE;
                    }

                }
            if(gamestate == "LOWBUCK"){
                    if (gamepad2.right_bumper) {
                        robot.lift.liftMode = Lift.LiftMode.LOW_BUCKET;
                    }
                    if(robot.lift.liftLeft.getCurrentPosition() > 1580){
                        robot.claw.clawPivot.setPosition(robot.claw.pivotBACK);
                    }

                }
            if(gamestate == "HIGHBUCK"){
                    if (gamepad2.right_bumper && !gamepad2.left_bumper) {
                        robot.lift.liftMode = Lift.LiftMode.HIGH_BUCKET;
                    }
                    if(gamepad2.right_bumper && gamepad2.left_bumper){
                        robot.claw.clawPivot.setPosition(robot.claw.pivotBACK);
                    }

            }
            if(gamestate == "SUBMERSIBLE"){
                    if(gamepad2.right_bumper){
                        robot.claw.clawPivot.setPosition(robot.claw.pivotDOWN);
                    }
                }
            if(gamestate == "ASCEND"){
                    robot.extension.extMode = Extension.ExtMode.HANG;
                    if(runtime.seconds() > 2){
                        robot.lift.liftMode = Lift.LiftMode.GROUND;
                    }
                }
            if(gamestate == "HIGHCHAMTEST"){
                    if (gamepad2.right_bumper && !gamepad2.left_bumper) {
                        robot.lift.liftMode = Lift.LiftMode.HIGH_CHAMBER_TEST;
                    }
                }
            if(gamestate == "CHAMBERINTAKE"){
                    if(robot.extension.extension.getCurrentPosition() < 575){
                        robot.claw.clawPivot.setPosition(robot.claw.pivotBACK);
                    }
                }
            if(gamepad2.a){
                gamestate = "CHAMBERINTAKE";
                robot.lift.liftMode = Lift.LiftMode.GROUND;
                robot.extension.extMode = Extension.ExtMode.INTAKE;
                robot.claw.clawOpen.setPosition(robot.claw.openMID);
                robot.claw.clawSpin.setPosition(robot.claw.spinB);
                runtime.reset();
            }
            if(gamepad2.b){
                gamestate = "LOWCHAM";
                robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                robot.extension.extMode = Extension.ExtMode.NEAR;
            }
            if(gamepad2.y){
                gamestate = "HIGHCHAM";
                robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                robot.extension.extMode = Extension.ExtMode.NEAR;
            }
            if(gamepad2.dpad_down){
                robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                robot.lift.liftMode = Lift.LiftMode.GROUND;
                gamestate = "SUBMERSIBLE";
                runtime.reset();
            }
            if(gamepad2.dpad_left){
                gamestate = "LOWBUCK";
                robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                robot.claw.clawPivot.setPosition(robot.claw.pivotSCORE);
                robot.extension.extMode = Extension.ExtMode.FARBACK;
            }
            if(gamepad2.dpad_up){
                gamestate = "HIGHBUCK";
                robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                robot.claw.clawPivot.setPosition(robot.claw.pivotSCORE);
                robot.extension.extMode = Extension.ExtMode.FARBACK;
            }
            if(gamepad2.dpad_right && gamepad2.x){
                robot.lift.liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.lift.liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.extension.extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
            if(gamepad2.dpad_right && !gamepad2.x){
                runtime.reset();
                gamestate = "ASCEND";
            }


        }
    }
    }
