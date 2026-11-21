package org.firstinspires.ftc.teamcode.MH;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class MHRobot {
    VoltageSensor voltageSensor;
    public float voltage;
    public DcMotorEx intake;
    public DcMotorEx uptake;
    public DcMotorEx backRightMotor;
    public DcMotorEx backLeftMotor;
    public DcMotorEx frontRightMotor;
    public DcMotorEx frontLeftMotor;
    public DcMotorEx outtakeP;
    public DcMotorEx outtakeN;
    public MHLimeLight limelight;
    public MHImu imu;


    public void init(HardwareMap hardwareMap)
    {
        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");

        backRightMotor = hardwareMap.get(DcMotorEx.class,"backRightDrive");
        backLeftMotor = hardwareMap.get(DcMotorEx.class,"backLeftDrive");
        frontRightMotor = hardwareMap.get(DcMotorEx.class,"frontRightDrive");
        frontLeftMotor = hardwareMap.get(DcMotorEx.class,"frontLeftDrive");
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake = hardwareMap.get(DcMotorEx.class, "intake");
        uptake = hardwareMap.get(DcMotorEx.class, "uptake");

        outtakeP = hardwareMap.get(DcMotorEx.class, "outtakeP");
        outtakeP.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtakeP.setDirection(DcMotorSimple.Direction.REVERSE);
        outtakeN = hardwareMap.get(DcMotorEx.class, "outtakeN");
        outtakeN.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtakeN.setDirection(DcMotorSimple.Direction.REVERSE);

        limelight.init(hardwareMap.get(Limelight3A.class, "limelight"));

        imu.init(hardwareMap.get(IMU.class, "imu"), imu.getConfig1());
    }

    public void joystickPowerDrive(Gamepad gamepad, float speed) {
        powerDriveFromJoysticks(gamepad.left_stick_x, gamepad.left_stick_y, gamepad.right_stick_x, speed);
    }

    public void refreshVoltage() {
        voltage = (float) voltageSensor.getVoltage();
    }
    public void powerDriveFromJoysticks(float left_stick_x, float left_stick_y, float right_stick_x, float speed) {
        backRightMotor.setPower(speed * (left_stick_x - left_stick_y - right_stick_x));
        backLeftMotor.setPower(speed * (left_stick_x + left_stick_y - right_stick_x));
        frontRightMotor.setPower(speed * (-left_stick_x - left_stick_y - right_stick_x));
        frontLeftMotor.setPower(speed * (-left_stick_x + left_stick_y - right_stick_x));
    }
    public void runShooterAtVelocity(DcMotorEx shooter, int velocity) {
        float F =  (float) (-449.47321*Math.pow(velocity,-0.619854f)+48.96472-(2.35*voltage));

        //p used to be 300 - test!!
        PIDFCoefficients coefficients = new PIDFCoefficients(275, 0.0 ,0.005,15.2);
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coefficients);
        shooter.setVelocity(velocity);
    }

}
