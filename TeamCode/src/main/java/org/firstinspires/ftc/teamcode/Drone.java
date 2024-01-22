package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.TouchSensor;
import static org.firstinspires.ftc.teamcode.MotionProfile.motionProfile;
import static org.firstinspires.ftc.teamcode.MotionProfile.motionProfileTime;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
public class Drone {
    private LinearOpMode myOpMode = null;
    public Servo drone = null;
    public double dronePosition;

    public static final double DRONE_hold = 1;
    public static final double DRONE_shoot = 0.7;


    public Drone(LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init(){
        drone = myOpMode.hardwareMap.get(Servo.class, "drone");
    }
    public void setPos(double pos){
        drone.setPosition(pos);
    }
    public void teleOp(){
        if(myOpMode.gamepad1.a && myOpMode.gamepad1.dpad_down){
            drone.setPosition(DRONE_shoot);
        }

    }
}
