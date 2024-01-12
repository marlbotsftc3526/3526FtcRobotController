package org.firstinspires.ftc.teamcode;
import static org.firstinspires.ftc.teamcode.MotionProfile.motionProfile;
import static org.firstinspires.ftc.teamcode.MotionProfile.motionProfileTime;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.ElapsedTime;
public class Intake {
    private LinearOpMode myOpMode = null;

    public DcMotor intake = null;

    public NormalizedColorSensor colorLeft;
    public NormalizedColorSensor colorRight;

    // Once per loop, we will update this hsvValues array. The first element (0) will contain the
    // hue, the second element (1) will contain the saturation, and the third element (2) will
    // contain the value. See http://web.archive.org/web/20190311170843/https://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html
    // for an explanation of HSV color.
    final float[] hsvValues = new float[3];

    public Intake(LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init(){
        intake = myOpMode.hardwareMap.get(DcMotor.class, "intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setDirection(DcMotor.Direction.FORWARD);

        colorLeft = myOpMode.hardwareMap.get(NormalizedColorSensor.class, "colorLeft");
        colorRight = myOpMode.hardwareMap.get(NormalizedColorSensor.class, "colorRight");
    }
    public void teleOp(){
        if(myOpMode.gamepad2.dpad_left){
            intake.setPower(0.5);
        }else if(myOpMode.gamepad2.dpad_right) {
            intake.setPower(-0.5);
        }else if(myOpMode.gamepad2.dpad_up){
            intake.setPower(0);
        }
        colorSensorTelemetry();
    }
    public void setPowerPower(double x){
        intake.setPower(x);
    }

    public void colorSensorTelemetry(){
        // Get the normalized colors from the sensor
        NormalizedRGBA colors = colorLeft.getNormalizedColors();

        /* Use telemetry to display feedback on the driver station. We show the red, green, and blue
         * normalized values from the sensor (in the range of 0 to 1), as well as the equivalent
         * HSV (hue, saturation and value) values. See http://web.archive.org/web/20190311170843/https://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html
         * for an explanation of HSV color. */

        // Update the hsvValues array by passing it to Color.colorToHSV()
        Color.colorToHSV(colors.toColor(), hsvValues);

        myOpMode.telemetry.addLine()
                .addData("Red", "%.3f", colors.red)
                .addData("Green", "%.3f", colors.green)
                .addData("Blue", "%.3f", colors.blue);
        myOpMode.telemetry.addLine()
                .addData("Hue", "%.3f", hsvValues[0])
                .addData("Saturation", "%.3f", hsvValues[1])
                .addData("Value", "%.3f", hsvValues[2]);
        myOpMode.telemetry.addData("Alpha", "%.3f", colors.alpha);
    }
}
