
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class RobotHardware {
    private OpMode myOpMode = null;

    public Drivetrain drivetrain;
    public LimeLight limelight;

    public RobotHardware(OpMode opmode) {
        myOpMode = opmode;
    }

    public void init() {
        limelight = new LimeLight(myOpMode);
        limelight.init();
        drivetrain = new Drivetrain(myOpMode, limelight);
        drivetrain.init();

        myOpMode.telemetry.addData(">", "Hardware Initialized");
    }

    public void teleOp() {
        limelight.teleOp();
        drivetrain.teleOp();
        if(myOpMode.gamepad1.a){
            drivetrain.drivetrainMode = Drivetrain.DrivetrainMode.LIMELIGHT;
        }else if(myOpMode.gamepad1.b){
            drivetrain.drivetrainMode = Drivetrain.DrivetrainMode.MANUAL;
        }
    }

    public void stop(){
        drivetrain.stop();
    }
}