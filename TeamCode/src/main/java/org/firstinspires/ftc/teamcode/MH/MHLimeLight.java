package org.firstinspires.ftc.teamcode.MH;

import static java.lang.Math.abs;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

public class MHLimeLight {

    public LLResult latestResult;
    public LLResultTypes.FiducialResult latestFiducial;
    //the tag id of the current tag being used for limelight stuff
    public int latestTag = 0;
    //the team of the current tag id
    public MHRobot.team latestTagTeam = MHRobot.team.BLUE;
    //latest tx, ty, ta values from limelight
    public float latestTy = 0;
    public float latestTx = 0;
    public float latestTa = 0;
    // this one is funky. Basically the latest angle of the apriltag based off the robot's angle. (side to side)
    // replaces target_yaw from last year.
    public float latestApriltagYaw = 0;
    //yaw camera needs to move to be pointing at the AIM POINT not the tag. updated with updateAimPointCorrectionAngleAndDistance
    public float latestAimPointCorrectionAngle = 0;
    //distance from camera to AIM POINT not tag. updated with updateAimPointCorrectionAngleAndDistance.
    public float latestAimPointDistance = 0;
    //set in init: physical position/angle of the limelight for distance calcs n stuff
    public float mountAngleDegrees;
    public float mountHeightInches;
    public Limelight3A limelight;

    public void init(Limelight3A l, float angle, float height) {
        mountAngleDegrees = angle;
        mountHeightInches = height;
        limelight = l;
        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(0);
        limelight.start();
        latestTag = 0;
    }

    public float getApriltagFloorDistance() {

        /*
        TODO : CHANGE apriltagHeightInches, it's the value from last year,
            account for qpriltag height being able to change. Oh no.
        */
        float apriltagHeightInches = 48f;

        float heightDifferenceIn = apriltagHeightInches - mountHeightInches;

        float totalAngleDegrees = mountAngleDegrees + latestTy;
        //returns distance
        return (float) ((float) heightDifferenceIn * (Math.cos(Math.toRadians(totalAngleDegrees)))/Math.sin(Math.toRadians(totalAngleDegrees)));
    }
    public void updateAimPointCorrectionAngleAndDistance() {
        //did all this math last year. all ik now is that it seeeeeeems to work.
        //all of this is FLOOR-WISE. no height involved.

        if (!latestResult.isValid()) {
            return;
        }
        float distance = getApriltagFloorDistance();
        float latestTxRadians = (float) Math.toRadians(latestTx);
        float latestApriltagYawRadians = (float) Math.toRadians(latestApriltagYaw);
        //goes with normal vector of apriltag pointing away from robot

        //TODO : UPDATE THIS VALUE. this says aim point is 9.5 inches FARTHER. will likely be closer to 0.
        float apriltagToAimPointDistanceInches = 9.5f;
        //this is floor distance to tag. x1 and y1 both ON THE FLOOR.
        float x1 = (float) distance * (float) Math.sin(latestTxRadians);
        float y1 = (float) distance * (float) Math.cos(latestTxRadians);
        //floor distance plus floor distance to aim point
        float x2 = apriltagToAimPointDistanceInches*(float) Math.sin(latestApriltagYawRadians) + x1;
        float y2 = apriltagToAimPointDistanceInches*(float) Math.cos(latestApriltagYawRadians) + y1;
        latestAimPointCorrectionAngle = (float) Math.toDegrees(Math.atan(x2/y2));
        //floor distance to AIM POINT NOT TAG!!
        latestAimPointDistance = (float) Math.sqrt(Math.pow(x2,2)+Math.pow(y2, 2));
    }

    public boolean localise(IMU imu, Telemetry telemetry)
    {
        //I literally have no clue what this function is for?? idk when you'd use it
        //cant hurt to keep it here tho just in case

        // First, tell Limelight which way your robot is facing

        float robotYaw = (float) imu.getRobotYawPitchRollAngles().getYaw();
        limelight.updateRobotOrientation(robotYaw);
        LLResult result = latestResult;
        if (result != null && result.isValid()) {
            Pose3D botpose_mt2 = result.getBotpose_MT2();
            if (botpose_mt2 != null) {
                float x = (float) botpose_mt2.getPosition().x;
                float y = (float) botpose_mt2.getPosition().y;
                telemetry.addData("x: ", x);
                telemetry.addData("y: ", y);
                telemetry.addData("Tx: ", result.getTx());
                telemetry.addData("Ty: ", result.getTy());
                telemetry.addData("Ta: ",result.getTa());
                telemetry.addData("Yaw: ", robotYaw);
            }
        }
        return true;
    }
    public boolean update() {
        latestResult = limelight.getLatestResult();
        if (latestResult != null) {
            if (!latestResult.getFiducialResults().isEmpty()) {

                latestFiducial = latestResult.getFiducialResults().get(0);
                latestTag = latestFiducial.getFiducialId();
                setlatestTagTeam();

                latestTy = (float) latestResult.getTy();
                latestTx = (float) latestResult.getTx();
                latestTa = (float) latestResult.getTa();

                latestApriltagYaw = (float) latestResult.getFiducialResults().get(0).getTargetPoseRobotSpace().getOrientation().getPitch();

                updateAimPointCorrectionAngleAndDistance();

                return true;
            }
        }
        return false;
    }
    public MHRobot.team getTagTeam(int tag) {
        if (latestTag == 30 || latestTag == 31 ||latestTag == 32||latestTag == 33||latestTag == 34||latestTag == 35||latestTag == 36||latestTag == 37) {
            return MHRobot.team.BLUE;
        } else if (latestTag == 38||latestTag == 39||latestTag == 40||latestTag == 41||latestTag == 42||latestTag == 43||latestTag == 44||latestTag == 45) {
            return MHRobot.team.RED;
        } else {
            return MHRobot.team.NULL;
        }
    }
    public void setlatestTagTeam() {
        latestTagTeam = getTagTeam(latestTag);
    }
    public boolean target(MHRobot robot, Telemetry telemetry)
    {
        IMU imu = robot.imu.imu;
        float robotYaw = (float) imu.getRobotYawPitchRollAngles().getYaw();
        limelight.updateRobotOrientation(robotYaw);
        LLResult result = latestResult;
        if (result != null && result.isValid()) {
            if (latestTagTeam != MHRobot.team.NULL) {

                Pose3D botpose_mt2 = result.getBotpose_MT2();
                if (botpose_mt2 != null) {
                    float Tx = (float) result.getTx();
                    float adjustment = (float) (1.00 / 90 * abs(Tx));
                    int direction = Tx > 0 ? -1 : 1;
                    telemetry.addData("adjustment: ", "%.2f %d", adjustment, direction);
                    robot.powerDriveFromJoysticks(0,0,direction,abs(adjustment));
                    return true;
                }
            }
        }
        return false;
    }
}
