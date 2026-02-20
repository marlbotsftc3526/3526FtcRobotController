package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

    public class LimeLight implements Runnable{
        private OpMode myOpMode = null;
        public Limelight3A limelight = null;

        public LLResult result;

        public List<LLResultTypes.FiducialResult> fiducials;

        private volatile boolean running = true;

        // Use 'volatile' so the Main Thread gets the latest updates instantly
        public volatile double tx, ty, ta;
        public volatile boolean targetVisible;

        public LimeLight (OpMode opmode) {
            myOpMode = opmode;
        }

        public void init (){
            limelight = myOpMode.hardwareMap.get(Limelight3A.class, "limelight");
            limelight.start();
            limelight.pipelineSwitch(0);
            limelight.getLatestResult();

            result = limelight.getLatestResult();

            fiducials = result.getFiducialResults();
            limelight.start();
        }

        public void teleOp() {

        }

        @Override
        public void run() {
            while (running && !Thread.currentThread().isInterrupted()) {
                // Fetch results from the Limelight
                result = limelight.getLatestResult();

                if (result != null && result.isValid()) {
                    tx = result.getTx();
                    ty = result.getTy();
                    ta = result.getTa();
                    targetVisible = true;
                } else {
                    targetVisible = false;
                }

                // Small sleep to prevent CPU hogging (10ms = 100fps)
                /*try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }*/
            }
        }

        public void stop() {
            running = false;
        }

    }

    //pipeline 0 is red goal
    //pineline 1 is blue goal
    //pipeline