package org.firstinspires.ftc.teamcode;
import static org.firstinspires.ftc.teamcode.MotionProfile.motionProfile;
import static org.firstinspires.ftc.teamcode.MotionProfile.motionProfileTime;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
public class Intake {
    private LinearOpMode myOpMode = null;

    public DcMotor intake = null;

    public NormalizedColorSensor colorLeft;
    public NormalizedColorSensor colorRight;

    public boolean LeftPixel;
    public boolean RightPixel;

    View relativeLayout;

    // Once per loop, we will update this hsvValues array. The first element (0) will contain the
    // hue, the second element (1) will contain the saturation, and the third element (2) will
    // contain the value. See http://web.archive.org/web/20190311170843/https://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html
    // for an explanation of HSV color.
    final float[] hsvValuesL = new float[3];
    final float[] hsvValuesR = new float[3];

    public Intake(LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init(){
        intake = myOpMode.hardwareMap.get(DcMotor.class, "intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setDirection(DcMotor.Direction.FORWARD);

        colorLeft = myOpMode.hardwareMap.get(NormalizedColorSensor.class, "colorLeft");
        colorRight = myOpMode.hardwareMap.get(NormalizedColorSensor.class, "colorRight");

        int relativeLayoutId = myOpMode.hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", myOpMode.hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity) myOpMode.hardwareMap.appContext).findViewById(relativeLayoutId);

    }
    public void teleOp(){
        if(myOpMode.gamepad2.dpad_left){
            intake.setPower(0.5);
        }else if(myOpMode.gamepad2.dpad_right) {
            intake.setPower(-0.5);
        }else if(myOpMode.gamepad2.dpad_up){
            intake.setPower(0);
        }

        /* If this color sensor also has a distance sensor, display the measured distance.
         * Note that the reported distance is only useful at very close range, and is impacted by
         * ambient light and surface reflectivity. */

        myOpMode.telemetry.addData("Distance Left (cm)", "%.3f", ((DistanceSensor) colorLeft).getDistance(DistanceUnit.CM));
        myOpMode.telemetry.addData("Distance Right (cm)", "%.3f", ((DistanceSensor) colorRight).getDistance(DistanceUnit.CM));

        if(((DistanceSensor) colorLeft).getDistance(DistanceUnit.CM) <= 8){
            LeftPixel = true;
        }else{
            LeftPixel = false;

        }
        if(((DistanceSensor) colorRight).getDistance(DistanceUnit.CM) <= 8){
            RightPixel = true;
        }else{
            RightPixel = false;
        }

        myOpMode.telemetry.addData("leftpixel", LeftPixel);
        myOpMode.telemetry.addData("rightpixel", RightPixel);
    }
    public void setPowerPower(double x){
        intake.setPower(x);
    }

}
