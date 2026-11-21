package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.MH.MHRobot;
import org.firstinspires.ftc.teamcode.MH.MHUtils;

@TeleOp(name="Mechawks-Tele-1")
public class TeleOpMode extends LinearOpMode {
    float drive_speed = 0.85f;

    private MHRobot robot;

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);
        //set drive speed at 0.5 initially
        drive_speed = 0.85f;

            waitForStart();

        while (opModeIsActive()) {
            teleLoop();
        }
    }
    public void teleLoop() {
        robot.limelight.localise(robot.imu.imu, telemetry);

        telemetry.addData("outtake P Velocity: ", robot.outtakeP.getVelocity());
        telemetry.addData("outtake P current(mA): ", robot.outtakeP.getCurrent(CurrentUnit.MILLIAMPS));
        telemetry.addData("outtake N Velocity: ", robot.outtakeN.getVelocity());
        telemetry.addData("outtake N current(mA): ", robot.outtakeN.getCurrent(CurrentUnit.MILLIAMPS));
        telemetry.addData("current tag: ", robot.limelight.currentTag);

        robot.limelight.read();
        robot.joystickPowerDrive(gamepad1, drive_speed);
        if (gamepad1.right_bumper) { robot.limelight.target(robot, telemetry); }
        robot.intake.setPower(gamepad2.left_stick_y);
        robot.uptake.setPower(gamepad2.right_stick_y);
        if (gamepad2.right_trigger > 0.2) {
            robot.runShooterAtVelocity(robot.outtakeP, 1500);
        }
    }
}
