package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

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
                robot.lift.box.setPosition(robot.lift.BOX_intake);
            }else if(gamepad2.b){
                robot.lift.liftMode = Lift.LiftMode.HIGH;
                robot.lift.resetLiftPID();
                robot.lift.box.setPosition(robot.lift.BOX_score);
            }else if(gamepad2.x){
                robot.lift.liftMode = Lift.LiftMode.MEDIUM;
                robot.lift.resetLiftPID();
                robot.lift.box.setPosition(robot.lift.BOX_score);
            }else if(gamepad2.y){
                robot.lift.liftMode = Lift.LiftMode.LOW;
                robot.lift.resetLiftPID();
                robot.lift.box.setPosition(robot.lift.BOX_score);
            }
        }
    }
}