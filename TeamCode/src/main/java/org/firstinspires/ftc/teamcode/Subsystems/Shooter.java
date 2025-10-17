package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Shooter {
    /* Declare OpMode members. */
    private LinearOpMode myOpMode = null;   // gain access to methods in the calling OpMode.

    public DcMotorEx shoot = null;
    public CRServo transfer = null;

    //TODO Adjust based on desired states
    public enum ShootMode {
        ON,
        OFF,
    }

    public enum TransferMode {
        ON,
        OFF,
    }

    // Define Drive constants.  Make them public so they CAN be used by the calling OpMode
    //TODO Update values based on desired position
    public static final double REVOLUTIONS_PER_MINUTE = 4000;
    public static final double TICKS_PER_REVOLUTION = 28;
    public static final double TRANSFER_SPEED = -1;
    double TICKS_PER_SECOND;
    ShootMode shootMode = ShootMode.OFF;
    TransferMode transferMode = TransferMode.OFF;
    //Constructor
    public Shooter(LinearOpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {
        shoot = myOpMode.hardwareMap.get(DcMotorEx.class, "shooter");
        shoot.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
       // shoot.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        shoot.setDirection(DcMotor.Direction.REVERSE);

        transfer = myOpMode.hardwareMap.get(CRServo.class, "transfer");

        myOpMode.telemetry.addData(">", "Shooter Initialized");
    }

    public void update() {

        if (shootMode == ShootMode.ON) {
            TICKS_PER_SECOND = REVOLUTIONS_PER_MINUTE / 60 * TICKS_PER_REVOLUTION;
            // Send calculated power to wheels
            shoot.setVelocity(TICKS_PER_SECOND);
        } else if (shootMode == ShootMode.OFF) {
            shoot.setVelocity(0);
        }
        if (transferMode == TransferMode.ON) {
            transfer.setPower(TRANSFER_SPEED);
        }
        else if (transferMode == TransferMode.OFF) {
            transfer.setPower(0);
        }
    }

    public void teleOp() {
        update();
        //Set states based on gamepad presses
        //TODO Update based on desired control scheme
        if (myOpMode.gamepad1.right_bumper) {
            shootMode = ShootMode.ON;
        } else if (myOpMode.gamepad1.left_bumper) {
            shootMode = ShootMode.OFF; //s=S
        }

        if (myOpMode.gamepad1.right_trigger>.5) {
            transferMode = TransferMode.ON;
        } else {
            transferMode = TransferMode.OFF;
        }


    }

        public void stop () {
            shoot.setPower(0);
            transfer.setPower(0);
        }
    }


