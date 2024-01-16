package org.firstinspires.ftc.teamcode;


import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Lift;
import org.firstinspires.ftc.teamcode.RobotHardware;
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "teleop", group = "Linear Opmode")
public class TeleOp extends LinearOpMode {

    RobotHardware robot = new RobotHardware(this);
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        robot.init();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            robot.teleOp();
            if(gamepad2.a){
                robot.lift.liftMode = Lift.LiftMode.GROUND;
                robot.lift.resetLiftPID();
                robot.lift.boxLeft.setPosition(robot.lift.BOX_intake);
                robot.lift.boxRight.setPosition(robot.lift.DIFF1-robot.lift.BOX_intake);
            }else if(gamepad2.b){
                robot.lift.liftMode = Lift.LiftMode.HIGH;
                robot.lift.resetLiftPID();
                robot.lift.boxLeft.setPosition(robot.lift.BOX_score);
                robot.lift.boxRight.setPosition(robot.lift.DIFF2-robot.lift.BOX_score);
            }else if(gamepad2.x){
                robot.lift.liftMode = Lift.LiftMode.MEDIUM;
                robot.lift.resetLiftPID();
                robot.lift.boxLeft.setPosition(robot.lift.BOX_score);
                robot.lift.boxRight.setPosition(robot.lift.DIFF2-robot.lift.BOX_score);
            }else if(gamepad2.y){
                robot.lift.liftMode = Lift.LiftMode.LOW;
                robot.lift.resetLiftPID();
                robot.lift.boxLeft.setPosition(robot.lift.BOX_score);
                robot.lift.boxRight.setPosition(robot.lift.DIFF2-robot.lift.BOX_score);
            }
            //if(robot.lift.touch.isPressed()){
            if(robot.intake.LeftPixel){
                robot.lift.gateLeft.setPosition(robot.lift.GATE_up);
            }
            if(robot.intake.RightPixel){
                robot.lift.gateRight.setPosition(robot.lift.GATE_down);
            }

            //}
            /*if(gamepad2.a){
                robot.lift.gateRight.setPosition(robot.lift.GATE_up);
            }else if(gamepad2.b){
                robot.lift.gateRight.setPosition(robot.lift.GATE_down);
            }
            else if(gamepad2.left_trigger > 0){
                robot.lift.boxLeft.setPosition(robot.lift.BOX_score);
                robot.lift.boxRight.setPosition(robot.lift.DIFF2-robot.lift.BOX_score);
            }else if(gamepad2.right_trigger > 0){
                robot.lift.boxLeft.setPosition(robot.lift.BOX_intake);
                robot.lift.boxRight.setPosition(robot.lift.DIFF1-robot.lift.BOX_intake);
            }else if(gamepad2.x){
                robot.lift.gateLeft.setPosition(robot.lift.GATE_down);

            }else if(gamepad2.y){
                robot.lift.gateLeft.setPosition(robot.lift.GATE_up);
            }*/
            telemetry.update();
        }
    }
}