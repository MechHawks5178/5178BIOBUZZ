package org.firstinspires.ftc.teamcode.MH;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

public class MHImu {
    public IMU imu;
    public void init (IMU i , IMU.Parameters p) {
        imu = i;
        imu.initialize(p);
    }

    public IMU.Parameters getConfig1() {

        RevHubOrientationOnRobot hubOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        );
        return new IMU.Parameters(hubOrientation);
    }
}
