package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.teamcode.RobotHardware;

import java.util.List;

@Disabled
@Autonomous(name = "AutoMeetZeroRedRight", group = "Linear Opmode")

public class AutoMeetZeroRedRight extends LinearOpMode {
    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/Marlbots.tflite";

    private static final String[] LABELS = {
            "Marlbots",
            "M"
    };

   /* private static final String VUFORIA_KEY =
            "AfDxTOz/////AAABmZP0ZciU3EdTii04SAkq0dI8nEBh4mM/bXMf3H6bRJJbH/XCSdLIe5SDSavwPb0wJvUdnsmXcal43ZW2YJRG6j65bfewYJPCb+jGn7IW7kd5rKWs11G7CtFSMGEOhA5NU8gi39eHW0pmXC8NEXBn3CmK67TIENGm/YBN6f+xmkmDvBQjaJc2hJ93HPvhAnIiAbJT9/fWijwg9IovTok/xAcAcuIKz3XK/lnJXu6XdJ1MyRtoXO7yf1W4ReDHngWCtKI9B7bAnD6zPNhZoVLVzl34E8XKed/dGShIoCmIUTe0HoUniP0ye3AnwhFgxLhgPcysF8uVqKN0VKBpDH1zU7J7keZdjWHM6jvn29oLMK7W";
    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    //private VuforiaLocalizer vuforia;

    //private TFObjectDetector tfod;

    RobotHardware robot = new RobotHardware(this);
    private ElapsedTime runtime = new ElapsedTime();
    public void strafe(double power){
        robot.drivetrain.frontRight.setPower(power);
        robot.drivetrain.backLeft.setPower(power);
        robot.drivetrain.frontLeft.setPower(-power);
        robot.drivetrain.backRight.setPower(-power);
    }
    public void stoptherobot(){
        robot.drivetrain.frontRight.setPower(0);
        robot.drivetrain.backLeft.setPower(0);
        robot.drivetrain.frontLeft.setPower(0);
        robot.drivetrain.backRight.setPower(0);
    }


    public void runOpMode() {
        robot.init();

        runtime.reset();

        waitForStart();
        runtime.reset();
        while(runtime.seconds() <= 2.5){
            strafe(0.4);
        }
        stoptherobot();
        runtime.reset();
        while(runtime.seconds() <= 3.75){
            strafe(-0.6);
        }
        stop();


    }

}