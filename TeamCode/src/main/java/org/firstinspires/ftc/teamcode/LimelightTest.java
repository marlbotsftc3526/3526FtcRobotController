package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
@Autonomous
public class LimelightTest extends LinearOpMode {

    private Limelight3A limelight;
    RobotHardware robot;
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException
    {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        robot = new RobotHardware(this);
        robot.init();
        telemetry.setMsTransmissionInterval(11);

        limelight.pipelineSwitch(0);

        /*
         * Starts polling for data.
         */
        limelight.start();
        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
           // LLResult result = limelight.getLatestResult();
           // if (result != null) {
            //    if (result.isValid()) {
                //    Pose3D botpose = result.getBotpose();
                //    telemetry.addData("tx", result.getTx());
                 //   telemetry.addData("ty", result.getTy());
                //    telemetry.addData("Botpose", botpose.toString());
                        robot.drivetrain.limeLight();



              //  }


           // }
            robot.drivetrain.localizer.update();
            telemetry.update();
        }
    }
}