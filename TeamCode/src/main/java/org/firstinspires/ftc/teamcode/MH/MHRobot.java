package org.firstinspires.ftc.teamcode.MH;

import static org.firstinspires.ftc.teamcode.MH.MHUtils.map;

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
    public enum team {
        BLUE,
        RED,
        NULL
    }
    public team robotTeam = team.NULL;

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
    public MHLimeLight limelight = new MHLimeLight();
    public MHImu imu = new MHImu();


    public void init(HardwareMap hardwareMap, MHRobot.team t)
    {
        robotTeam = t;
        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");

        backRightMotor = hardwareMap.get(DcMotorEx.class,"backRightDrive");
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor = hardwareMap.get(DcMotorEx.class,"backLeftDrive");
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor = hardwareMap.get(DcMotorEx.class,"frontRightDrive");
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeftMotor = hardwareMap.get(DcMotorEx.class,"frontLeftDrive");
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake = hardwareMap.get(DcMotorEx.class, "intake");
        uptake = hardwareMap.get(DcMotorEx.class, "uptake");

        outtakeP = hardwareMap.get(DcMotorEx.class, "outtakeP");
        outtakeP.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtakeP.setDirection(DcMotorSimple.Direction.FORWARD);
        outtakeP.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        outtakeN = hardwareMap.get(DcMotorEx.class, "outtakeN");
        outtakeN.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtakeN.setDirection(DcMotorSimple.Direction.REVERSE);
        outtakeN.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        //TODO : CHANGE THIS ANGLE AND HEIGHT OR DISTANCE AND ANGLE CALCS WILL BE WRONG
        limelight.init(hardwareMap.get(Limelight3A.class, "limelight"), 0, 0);

        imu.init(hardwareMap.get(IMU.class, "imu"), imu.getConfig1());
    }

    public void joystickPowerDrive(Gamepad gamepad, float speed) {
        powerDriveFromJoysticks(gamepad.left_stick_x, gamepad.left_stick_y, gamepad.right_stick_x, speed);
    }

    public void refreshVoltage() {
        voltage = (float) voltageSensor.getVoltage();
    }
    public void powerDriveFromJoysticks(float left_stick_x, float left_stick_y, float right_stick_x, float speed) {
        backRightMotor.setPower( speed * (left_stick_x - left_stick_y - right_stick_x));
        backLeftMotor.setPower( speed * (left_stick_x + left_stick_y - right_stick_x));
        frontRightMotor.setPower( speed * (-left_stick_x - left_stick_y - right_stick_x));
        frontLeftMotor.setPower( speed * (-left_stick_x + left_stick_y - right_stick_x));

    }
    public int getShooterVelocityFromDistance(float distance) {
        //TODO : Test for values and actually make velocity scale, change a & b
        float a = 1;
        float b = 1;
        return (int) Math.floor(a * distance + b);
    }
    public void runShooterAtVelocity(DcMotorEx shooter, int velocity) {
        float F =  (float) (-449.47321*Math.pow(velocity,-0.619854f)+48.96472-(2.35*voltage));

        //p used to be 300 - test!!
        PIDFCoefficients coefficients = new PIDFCoefficients(275, 0.0 ,0.005,15.2);
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coefficients);
        shooter.setVelocity(velocity);
    }

    //probably works? might need to be reversed bc motor goes the other way and I copied from shooter code
    public void runIntakeAtVelocity(int velocity) {
        float F =  (float) (-449.47321*Math.pow(velocity,-0.619854f)+48.96472-(2.35*voltage));

        //p used to be 300 - test!!
        PIDFCoefficients coefficients = new PIDFCoefficients(275, 0.0 ,0.005,15.2);
        intake.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coefficients);
        intake.setVelocity(velocity);
    }

    public void setAllMotorPowers(double power) {
        backRightMotor.setPower(power);
        backLeftMotor.setPower(power);
        frontRightMotor.setPower(power);
        frontLeftMotor.setPower(power);
    }
    public void setAllMotorZeroPowerBehaviours(DcMotor.ZeroPowerBehavior behaviour) {
        backRightMotor.setZeroPowerBehavior(behaviour);
        backLeftMotor.setZeroPowerBehavior(behaviour);
        frontRightMotor.setZeroPowerBehavior(behaviour);
        frontLeftMotor.setZeroPowerBehavior(behaviour);
    }

    public void limelightCircle(float leftStickX, float leftStickY, float speed, float rightStickTolerance) {
        //a good default tolerance (from 3583-2025) is 0.1f

        //dont move if issues
        if (!limelight.latestResult.isValid() || limelight.latestTagTeam == team.NULL) { return; };


        float rightStickXFromTx = 1;

        //make the amount you turn correspond to how big the angle is on a linear map
        rightStickXFromTx *= Math.abs(map(0f, 1, 0, 40, Math.abs(limelight.latestAimPointCorrectionAngle)));
        if (Math.abs(rightStickXFromTx) < rightStickTolerance) {
            rightStickXFromTx = 0;
            setAllMotorZeroPowerBehaviours(DcMotor.ZeroPowerBehavior.BRAKE);
            setAllMotorPowers(0);
        }
        //make the amount you turn left or right based on the angle
        rightStickXFromTx *= Math.signum(limelight.latestAimPointCorrectionAngle);
        powerDriveFromJoysticks(leftStickX, leftStickY, rightStickXFromTx, speed);
        Math.abs(rightStickXFromTx);
    }

}
