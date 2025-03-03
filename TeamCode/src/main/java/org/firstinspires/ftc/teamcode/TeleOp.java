package org.firstinspires.ftc.teamcode;


import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.util.

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotHardware;
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "teleop", group = "Linear Opmode")
public class TeleOp extends LinearOpMode {

    RobotHardware robot = new RobotHardware(this);
    private ElapsedTime runtime = new ElapsedTime();

    String gamestate = "";
    double clawspinpos = robot.claw.spinA;
    boolean subonetime = false;

    @Override
    public void runOpMode() {

        robot.initTeleOp();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            telemetry.addData("spin", clawspinpos);

            robot.teleOp();
            telemetry.update();
            runtime.reset();
            if(gamestate == "HIGHCHAM") {
                    if (gamepad2.right_bumper) {
                        robot.lift.liftMode = Lift.LiftMode.HIGH_CHAMBER_SCORE;
                    }
                    if(robot.lift.liftLeft.getCurrentPosition() > 500){

                        robot.claw.clawPivot.setPosition(robot.claw.pivotSCORE);

                    }

            }
            if(gamestate == "HIGHBUCK"){
                    if (gamepad2.right_bumper && !gamepad2.left_bumper) {
                        robot.lift.liftMode = Lift.LiftMode.HIGH_BUCKET;
                    }
                    if(robot.lift.liftLeft.getCurrentPosition() > robot.lift.highbucketpos - 40){
                        robot.claw.clawPivot.setPosition(robot.claw.pivotBACK);
                    }
                    if(robot.extension.extension.getCurrentPosition()<= 2000 && robot.lift.liftLeft.getCurrentPosition() <= 350){
                        robot.claw.clawPivot.setPosition(robot.claw.pivotSCORE);
                    }

            }
            if(gamestate == "SUBMERSIBLE"){
                if(robot.extension.extension.getCurrentPosition() > robot.extension.nearpos - 150){
                    robot.claw.clawPivot.setPosition(robot.claw.pivotDOWN);

                }
                if(robot.claw.clawruntime.seconds() > 0.3 && robot.claw.clawruntime.seconds() < 0.8){
                    robot.extension.extMode = Extension.ExtMode.NEAR;
                }
                if(robot.extension.extension.getCurrentPosition() > 150 && subonetime){
                    robot.lift.liftMode = Lift.LiftMode.SUBMERSIBLE;
                    robot.claw.clawOpen.setPosition(robot.claw.openOPEN);
                    subonetime = false;
                }
                if(gamepad2.left_bumper){

                    if(gamepad2.left_stick_x>0.1 && clawspinpos < 1) {
                        clawspinpos += 0.02;
                    }else if(gamepad2.left_stick_x < -0.1 && 0 < clawspinpos){
                        clawspinpos -= 0.02;
                    }
                    if(gamepad2.left_stick_button){
                        clawspinpos = robot.claw.spinA;
                    }
                    robot.claw.clawSpin.setPosition(clawspinpos);
                    telemetry.addData("spin", clawspinpos);

                    if(gamepad2.right_bumper){
                        robot.lift.liftMode = Lift.LiftMode.SUBMERSIBLE;
                    }
                }

                if(gamepad2.right_bumper && !gamepad2.left_bumper){
                    robot.lift.liftMode = Lift.LiftMode.GROUND;
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
                robot.extension.extMode = Extension.ExtMode.FARBACK;
                    robot.claw.clawOpen.setPosition(robot.claw.openOPEN);

                runtime.reset();
            }
            if(gamepad2.y){
                gamestate = "HIGHCHAM";
                robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                robot.extension.extMode = Extension.ExtMode.NEAR;
                robot.lift.liftMode = Lift.LiftMode.HIGH_CHAMBER;
            }
            if((gamestate == "HIGHBUCK" && (gamepad2.right_trigger > 0.1 && gamepad2.left_trigger > 0.1))){
                    gamestate = "SUBMERSIBLE";
                    subonetime = true;

                    runtime.reset();
            }
            if(gamepad2.dpad_down){
                gamestate = "SUBMERSIBLE";
                robot.extension.extMode = Extension.ExtMode.NEAR;
                subonetime = true;
            }
            if(gamepad2.dpad_up){
                gamestate = "HIGHBUCK";
                robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                robot.extension.extMode = Extension.ExtMode.FARBACK;
            }
            if(gamepad2.dpad_right && gamepad2.x){
                robot.lift.liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.lift.liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.lift.liftCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.extension.extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
            if(gamepad1.dpad_right){
                runtime.reset();
                robot.extension.extMode = Extension.ExtMode.HANG;
                robot.lift.isHang = true;
            }
            if(gamepad1.x){
                if(robot.extension.extension.getCurrentPosition() <= 2400) {
                    robot.claw.clawPivot.setPosition(robot.claw.pivotSCORE);
                }
            }
            if(gamepad1.y){
                if(robot.extension.extension.getCurrentPosition() <= 2400) {
                    robot.claw.clawPivot.setPosition(robot.claw.pivotUP);
                }

            }if(gamepad1.b){
                if(robot.extension.extension.getCurrentPosition() <= 2400) {
                    robot.claw.clawPivot.setPosition(robot.claw.pivotBACK);
                }
            }
            if(gamepad1.a){
                if(robot.extension.extension.getCurrentPosition() <= 2400) {
                    robot.claw.clawPivot.setPosition(robot.claw.pivotDOWN);
                }
            }
            if(gamepad1.dpad_up){
                robot.claw.clawSpin.setPosition(robot.claw.spinA);
            }
            if(gamepad1.dpad_down){
                robot.claw.clawSpin.setPosition(robot.claw.spinB);
            }

        }
    }
    }
