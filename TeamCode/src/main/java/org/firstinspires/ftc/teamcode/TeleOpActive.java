package org.firstinspires.ftc.teamcode;


import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.util.

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotHardware;
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "teleopActive", group = "Linear Opmode")
public class TeleOpActive extends LinearOpMode {

    RobotHardware robot = new RobotHardware(this);
    private ElapsedTime runtime = new ElapsedTime();

    String gamestate = "";
    double clawspinpos = robot.aClaw.spinA;
    boolean subonetime = false;

    @Override
    public void runOpMode() {

        robot.initTeleOpActive();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            telemetry.addData("spin", clawspinpos);

            robot.teleOpA();
            telemetry.update();
            runtime.reset();
            if(gamestate == "HIGHCHAM") {
                if (gamepad2.right_bumper) {
                    robot.lift.liftMode = Lift.LiftMode.HIGH_CHAMBER_SCORE;
                }
                if(robot.lift.liftLeft.getCurrentPosition() > 500){

                    robot.aClaw.clawPivot.setPosition(robot.aClaw.pivotSCORE);

                }

            }
            if(gamestate == "HIGHBUCK"){
                if (gamepad2.right_bumper && !gamepad2.left_bumper) {
                    robot.lift.liftMode = Lift.LiftMode.HIGH_BUCKET;
                }
                if(robot.lift.liftLeft.getCurrentPosition() > robot.lift.highbucketpos - 40){
                    robot.aClaw.clawPivot.setPosition(robot.aClaw.pivotBACK);
                }
                if(robot.extension.extension.getCurrentPosition()<= 2200 && robot.lift.liftLeft.getCurrentPosition() <= 350){
                    robot.aClaw.clawPivot.setPosition(robot.aClaw.pivotSCORE);
                }

            }
            if(gamestate == "SUBMERSIBLE"){
                robot.aClaw.clawOpen.setPosition(robot.aClaw.activeIn);
                if(robot.extension.extension.getCurrentPosition() > robot.extension.nearpos - 150){
                    robot.aClaw.clawPivot.setPosition(robot.aClaw.pivotDOWN);

                }
                if(robot.aClaw.clawruntime.seconds() > 0.3 && robot.aClaw.clawruntime.seconds() < 0.8){
                    robot.extension.extMode = Extension.ExtMode.NEAR;
                }
                if(robot.extension.extension.getCurrentPosition() > 150 && subonetime){
                    robot.lift.liftMode = Lift.LiftMode.SUBMERSIBLEACTIVE;
                    subonetime = false;
                }
                if(gamepad2.left_bumper){

                    if(gamepad2.left_stick_x>0.1 && clawspinpos < 1) {
                        clawspinpos += 0.02;
                    }else if(gamepad2.left_stick_x < -0.1 && 0 < clawspinpos){
                        clawspinpos -= 0.02;
                    }
                    if(gamepad2.left_stick_button){
                        clawspinpos = robot.aClaw.spinA;
                    }
                    robot.aClaw.clawSpin.setPosition(clawspinpos);
                    telemetry.addData("spin", clawspinpos);

                    if(gamepad2.right_bumper){
                        robot.lift.liftMode = Lift.LiftMode.SUBMERSIBLEACTIVE;
                    }
                }

                if(gamepad2.right_bumper && !gamepad2.left_bumper){
                    robot.lift.liftMode = Lift.LiftMode.GROUNDACTIVE;
                }
            }
            if(gamestate == "CHAMBERINTAKE"){
                if(robot.extension.extension.getCurrentPosition() < 575){
                    robot.aClaw.clawPivot.setPosition(robot.aClaw.pivotBACK);
                }
            }
            if(gamepad2.a){
                gamestate = "CHAMBERINTAKE";
                robot.lift.liftMode = Lift.LiftMode.GROUNDACTIVE;
                robot.extension.extMode = Extension.ExtMode.FARBACK;
                robot.aClaw.clawOpen.setPosition(robot.aClaw.activeOut);

                runtime.reset();
            }
            if(gamepad2.y){
                gamestate = "HIGHCHAM";
                robot.aClaw.clawOpen.setPosition(robot.aClaw.activeIn);
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
                robot.aClaw.clawOpen.setPosition(robot.aClaw.activeIn);
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
                    robot.aClaw.clawPivot.setPosition(robot.aClaw.pivotSCORE);
                }
            }
            if(gamepad1.y){
                if(robot.extension.extension.getCurrentPosition() <= 2400) {
                    robot.aClaw.clawPivot.setPosition(robot.aClaw.pivotUP);
                }

            }if(gamepad1.b){
                if(robot.extension.extension.getCurrentPosition() <= 2400) {
                    robot.aClaw.clawPivot.setPosition(robot.aClaw.pivotBACK);
                }
            }
            if(gamepad1.a){
                if(robot.extension.extension.getCurrentPosition() <= 2400) {
                    robot.aClaw.clawPivot.setPosition(robot.aClaw.pivotDOWN);
                }
            }
            if(gamepad1.dpad_up){
                robot.aClaw.clawSpin.setPosition(robot.aClaw.spinA);
            }
            if(gamepad1.dpad_down){
                robot.aClaw.clawSpin.setPosition(robot.aClaw.spinB);
            }

        }
    }
}
