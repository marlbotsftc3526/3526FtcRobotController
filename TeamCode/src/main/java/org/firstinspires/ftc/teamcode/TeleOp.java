package org.firstinspires.ftc.teamcode;


import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
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
            telemetry.update();
            if(gamepad2.cross){
                robot.lift.liftMode = Lift.LiftMode.GROUND;
                robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                robot.claw.clawPivot.setPosition(robot.claw.pivotBACK);
                robot.extension.extMode = Extension.ExtMode.INTAKE;
                robot.claw.clawOpen.setPosition(robot.claw.openOPEN);
            }
            if(gamepad2.circle){
                robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                robot.claw.clawPivot.setPosition(robot.claw.pivotSCORE);
                robot.claw.clawSpin.setPosition(robot.claw.spinB);
                robot.extension.extMode = Extension.ExtMode.NEAR;
                while(!gamepad2.right_bumper){
                    if(gamepad2.right_bumper){
                        robot.lift.liftMode = Lift.LiftMode.LOW_CHAMBER;

                        while(!(gamepad2.left_bumper && gamepad2.right_bumper)){
                            if(gamepad2.left_bumper && gamepad2.right_bumper){
                                robot.lift.liftMode = Lift.LiftMode.LOW_CHAMBER_SCORE;
                            }
                        }
                    }
                }
            }
            if(gamepad2.triangle){
                robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                robot.claw.clawPivot.setPosition(robot.claw.pivotSCORE);
                robot.extension.extMode = Extension.ExtMode.NEAR;
                robot.claw.clawSpin.setPosition(robot.claw.spinB);
                while(!gamepad2.right_bumper){
                    if(gamepad2.right_bumper){
                        robot.lift.liftMode = Lift.LiftMode.HIGH_CHAMBER;

                        while(!(gamepad2.left_bumper && gamepad2.right_bumper)){
                            if(gamepad2.left_bumper && gamepad2.right_bumper){
                                robot.lift.liftMode = Lift.LiftMode.HIGH_CHAMBER_SCORE;
                            }
                        }
                    }
                }


            }
            if(gamepad2.dpad_down && gamepad2.cross){
                robot.claw.clawPivot.setPosition(robot.claw.pivotDOWN);
                robot.claw.clawOpen.setPosition(robot.claw.openOPEN);
            }
            if(gamepad2.dpad_left && gamepad2.circle){
                robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                robot.claw.clawPivot.setPosition(robot.claw.pivotSCORE);
                robot.extension.extMode = Extension.ExtMode.INTAKE;
                while(true){
                    if(gamepad2.right_bumper){
                        robot.lift.liftMode = Lift.LiftMode.LOW_BUCKET;
                        robot.claw.clawPivot.setPosition(robot.claw.pivotBACK);
                        break;
                    }
                }
            }
            if(gamepad2.dpad_up && gamepad2.triangle){
                robot.claw.clawOpen.setPosition(robot.claw.openCLOSE);
                robot.claw.clawPivot.setPosition(robot.claw.pivotSCORE);
                robot.extension.extMode = Extension.ExtMode.INTAKE;
                while(true){
                    if(gamepad2.right_bumper){
                        robot.lift.liftMode = Lift.LiftMode.HIGH_BUCKET;
                        robot.claw.clawPivot.setPosition(robot.claw.pivotSCORE);
                        break;
                    }
                }
            }

        }
    }
}