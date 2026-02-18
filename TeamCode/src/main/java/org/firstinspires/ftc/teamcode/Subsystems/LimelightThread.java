package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;


public class LimelightThread implements Runnable {
    private Limelight3A limelight;
    private volatile boolean running = true;

    // Use 'volatile' so the Main Thread gets the latest updates instantly
    public volatile double tx, ty, ta;
    public volatile boolean targetVisible;

    public LimelightThread(Limelight3A limelight) {
        this.limelight = limelight;
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            // Fetch results from the Limelight
            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                tx = result.getTx();
                ty = result.getTy();
                ta = result.getTa();
                targetVisible = true;
            } else {
                targetVisible = false;
            }

            // Small sleep to prevent CPU hogging (10ms = 100fps)
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop() {
        running = false;
    }
}

