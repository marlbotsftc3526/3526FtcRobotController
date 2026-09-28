package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class LimeLight{
    private OpMode myOpMode;
    public Limelight3A limelight = null;
    public LLResult result;
    public volatile double tx, ty, ta;
    public volatile boolean targetVisible;

    public LimeLight(OpMode opmode){
        this.myOpMode = opmode;
    }

    public void init (){
        limelight = myOpMode.hardwareMap.get(Limelight3A.class, "limelight");

        limelight.pipelineSwitch(0);

        limelight.start();
        limelight.getLatestResult();
        result = limelight.getLatestResult();
    }
    public void update() {
        result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            tx = result.getTx(); // How far left or right the target is (degrees)
            ty = result.getTy(); // How far up or down the target is (degrees)
            ta = result.getTa();// How big the target looks (0%-100% of the image)
            targetVisible = true;
        } else {
            targetVisible = false;
        }
    }
    public void teleOp() {
        update();
        if (targetVisible) {
            myOpMode.telemetry.addData("Limelight", "TARGET VISIBLE");
            myOpMode.telemetry.addData("tx", tx);
            myOpMode.telemetry.addData("ty", ty);
            myOpMode.telemetry.addData("ta", ta);
        } else {
            myOpMode.telemetry.addData("Limelight", "No Target Found");
        }

    }



}
