package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MH.MHRobot;

@TeleOp(name="Main Mech Hawks TeleOp")
public class TeleOpMode extends LinearOpMode {
    float driveSpeed = 0.85f;

    public MHRobot robot = new MHRobot();

    @Override
    public void runOpMode() throws InterruptedException {

        //TODO : Make it so teleop users can select team
        robot.init(hardwareMap, MHRobot.team.NULL);
        //set drive speed at 0.5 initially
        driveSpeed = 0.85f;

        while (!isStarted() && !isStopRequested()) {
            if (gamepad1.x) {
                robot.robotTeam = MHRobot.team.BLUE;
                telemetry.addData("Team : ", "BLUE");
                telemetry.update();
            }
            if (gamepad1.b) {
                robot.robotTeam = MHRobot.team.RED;
                telemetry.addData("Team : ", "RED");
                telemetry.update();
            }
            if (gamepad1.a) {
                robot.robotTeam = MHRobot.team.NULL;
                telemetry.addData("Team : ", "NULL");
                telemetry.update();
            }
        }

        while (opModeIsActive()) {
            teleLoop();
        }
    }
    public void teleLoop() {
        telemetry.addData("BL", robot.backLeftMotor.getVelocity());
        telemetry.addData("FL", robot.frontLeftMotor.getVelocity());
        telemetry.addData("BR", robot.backRightMotor.getVelocity());
        telemetry.addData("FR", robot.frontRightMotor.getVelocity());

        telemetry.update();

        //save processing/battery if limelight not needed
        if (gamepad1.right_bumper || gamepad2.right_trigger > 0.2) {
            robot.limelight.update();
        }

        //PLAYER 1 CONTROLS

        //movement: limelight circle if button, otherwise drive normal
        if (gamepad1.right_bumper) {
            //we are replacing 'limelight target' with 'limelight circle' which allows non-rotational movement
            //robot.limelight.target(robot, telemetry);
            robot.limelightCircle(gamepad1.left_stick_x,gamepad1.left_stick_y,driveSpeed,0.1f);
        } else {
            robot.joystickPowerDrive(gamepad1, driveSpeed);
        }

        //PLAYER 2 CONTROLS

        robot.intake.setPower(-gamepad2.left_stick_y);
        robot.uptake.setPower(gamepad2.right_stick_y);

        if (gamepad2.right_trigger > 0.2) {
            robot.runShooterAtVelocity(robot.outtakeP, 1500);
            //TODO : do the distance scaling thing
            //robot.runShooterAtVelocity(robot.outtakeP, robot.getShooterVelocityFromDistance(robot.limelight.latestAimPointDistance));
        } else {
            robot.runShooterAtVelocity(robot.outtakeP, 0);
        }
    }

}
