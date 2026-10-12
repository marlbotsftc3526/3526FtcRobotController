package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Transfer {
    private OpMode myOpMode = null;

    public DcMotor transfer = null;
    public static final double transferUp = 2000;
    public static final double transferDown = -2000;
    public enum TransferMode{
        UP,
        DOWN,
        LAUNCH,
        OFF
    }

    public TransferMode transferMode = TransferMode.OFF;

    public void init() {
        transfer = myOpMode.hardwareMap.get(DcMotor.class, "transfer");
    }

    public void update(){
        if (transferMode == TransferMode.UP){
            transfer.setPower(transferUp);
        }else if (transferMode == TransferMode.DOWN){
            transfer.setPower(transferDown);
        }else if (transferMode == TransferMode.OFF){
            transfer.setPower(0);
        }
    }
}
