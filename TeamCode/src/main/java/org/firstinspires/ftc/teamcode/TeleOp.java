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
    double clawspinpos;

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
                    if(robot.lift.liftLeft.getCurrentPosition() > 2850){
                        robot.claw.clawPivot.setPosition(robot.claw.pivotBACK);
                    }

            }
            if(gamestate == "SUBMERSIBLE"){
                if(robot.extension.extension.getCurrentPosition() > 1600){
                    robot.claw.clawPivot.setPosition(robot.claw.pivotDOWN);

                }
                    if(gamepad2.right_bumper){
                        robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                        robot.claw.clawPivot.setPosition(robot.claw.pivotSCORE);
                        robot.extension.extMode = Extension.ExtMode.NEAR;
                        gamestate = "NEUTRAL";
                    }
                    if(gamepad2.left_bumper){
                        robot.claw.clawSpin.setPosition(clawspinpos);
                        if(Math.abs(gamepad2.left_stick_x)>0.1) {
                            clawspinpos += 0.01;
                        }
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
                    robot.claw.clawOpen.setPosition(robot.claw.openMID);

               // robot.claw.clawSpin.setPosition(robot.claw.spinB);
                runtime.reset();
            }
            if(gamepad2.y){
                gamestate = "HIGHCHAM";
                robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                robot.extension.extMode = Extension.ExtMode.NEAR;
                robot.lift.liftMode = Lift.LiftMode.HIGH_CHAMBER;
            }
            if(gamepad2.dpad_down){
                gamestate = "SUBMERSIBLE";
                robot.lift.liftMode = Lift.LiftMode.GROUND;
                robot.extension.extMode = Extension.ExtMode.NEAR;
                runtime.reset();
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
            if(gamepad1.dpad_right){
                runtime.reset();
                robot.extension.extMode = Extension.ExtMode.HANG;
            }
            if(gamepad1.x){
                robot.claw.clawPivot.setPosition(robot.claw.pivotSCORE);
            }
            if(gamepad1.y){
                robot.claw.clawPivot.setPosition(robot.claw.pivotUP);

            }if(gamepad1.b){
                robot.claw.clawPivot.setPosition(robot.claw.pivotBACK);
            }
            if(gamepad1.a){
                robot.claw.clawPivot.setPosition(robot.claw.pivotDOWN);
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
