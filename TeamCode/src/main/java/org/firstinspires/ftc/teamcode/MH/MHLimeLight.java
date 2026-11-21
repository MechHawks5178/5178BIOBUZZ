package org.firstinspires.ftc.teamcode.MH;

import static java.lang.Math.abs;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

public class MHLimeLight {
    enum team {
        BLUE,
        RED
    }
    public int currentTag = 0;
    public team currentTagTeam = team.BLUE;
    public Limelight3A limelight;

    public void init(Limelight3A l) {
        limelight = l;
        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(0);
        limelight.start();
        currentTag = 0;
    }
    public boolean localise(IMU imu, Telemetry telemetry)
    {
        // First, tell Limelight which way your robot is facing

        double robotYaw = imu.getRobotYawPitchRollAngles().getYaw();
        limelight.updateRobotOrientation(robotYaw);
        LLResult result  = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            Pose3D botpose_mt2 = result.getBotpose_MT2();
            if (botpose_mt2 != null) {
                double x = botpose_mt2.getPosition().x;
                double y = botpose_mt2.getPosition().y;
                telemetry.addData("Tx: ", result.getTx());
                telemetry.addData("Ty: ", result.getTy());
                telemetry.addData("Ta: ",result.getTa());
                telemetry.addData("Yaw: ", robotYaw);
            }
        }
        return true;
    }
    public boolean read() {
        LLResult result = limelight.getLatestResult();
        if (result != null) {
            if (!result.getFiducialResults().isEmpty()) {
                currentTag = result.getFiducialResults().get(0).getFiducialId();
                return true;
            }
        }
        return false;
    }
    public void setCurrentTagTeam() {
        read();
        if (currentTag == 30 || currentTag == 31 ||currentTag == 32||currentTag == 33||currentTag == 34||currentTag == 35||currentTag == 36||currentTag == 37) {
            currentTagTeam = team.BLUE;
        } else if (currentTag == 38||currentTag == 39||currentTag == 40||currentTag == 41||currentTag == 42||currentTag == 43||currentTag == 44||currentTag == 45) {
            currentTagTeam = team.RED;
        }
    }
    public boolean target(MHRobot robot, Telemetry telemetry)
    {
        IMU imu = robot.imu.imu;
        //TODO: only target on tags 20 and 24
        double robotYaw = imu.getRobotYawPitchRollAngles().getYaw();
        limelight.updateRobotOrientation(robotYaw);
        LLResult result  = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            if (!result.getFiducialResults().isEmpty()) {
                currentTag = result.getFiducialResults().get(0).getFiducialId();
            }
            if ((currentTag == 30 || currentTag == 31 ||currentTag == 32||currentTag == 33||currentTag == 34||currentTag == 35||currentTag == 36||currentTag == 37) || (currentTag == 38||currentTag == 39||currentTag == 40||currentTag == 41||currentTag == 42||currentTag == 43||currentTag == 44||currentTag == 45)) {


                Pose3D botpose_mt2 = result.getBotpose_MT2();
                if (botpose_mt2 != null) {
                    float Tx = (float) result.getTx();
                    float adjustment = (float) (1.00 / 90 * abs(Tx));
                    int direction = Tx > 0 ? -1 : 1;
                    telemetry.addData("adjustment: ", "%.2f %d", adjustment, direction);
                    robot.powerDriveFromJoysticks(0,0,direction,abs(adjustment));
                }
            }
        }
        return true;
    }
}
