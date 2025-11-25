package org.firstinspires.ftc.teamcode.OpModes;

import static org.firstinspires.ftc.teamcode.OpModes.PedroAutoBlueSide.HEADING_KEY;
import static org.firstinspires.ftc.teamcode.OpModes.PedroAutoBlueSide.X_POS_KEY;
import static org.firstinspires.ftc.teamcode.OpModes.PedroAutoBlueSide.Y_POS_KEY;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.RobotHardware;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Meet0Teleop", group="Linear OpMode")

public class Meet0Teleop extends LinearOpMode{
    RobotHardware robot;

    @Override
    public void runOpMode() {
        //calling constructor
        robot = new RobotHardware(this);
        //calling init function
        robot.init();
        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Waiting for Start");
        telemetry.addData("x position", blackboard.get(X_POS_KEY));
        telemetry.addData("y position", blackboard.get(Y_POS_KEY));
        telemetry.addData("heading", blackboard.get(HEADING_KEY));
        telemetry.update();



        /*if(myOpMode.gamepad2.left_bumper){
            robot.drivetrain.side = Drivetrain.SideMode.BLUE;
        }else if(myOpMode.gamepad2.right_bumper){
            robot.drivetrain.side = Drivetrain.SideMode.RED;
        }*/

        waitForStart();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            robot.teleOp();
            telemetry.update();
        }
    }

}
