package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.util.ElapsedTime.Resolution.MILLISECONDS;
import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

//import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name="Robot Test")
public class RobotTestMode extends LinearOpMode {

    DcMotorEx intake;
    DcMotorEx midtake;

    @Override
    public void runOpMode()
    {
        intake = hardwareMap.get(DcMotorEx.class,"intake");
        midtake = hardwareMap.get(DcMotorEx.class,"midtake");

        waitForStart();

        while (opModeIsActive()) {
            intake.setPower(gamepad1.left_stick_y);
            midtake.setPower(gamepad1.left_stick_y);
        }

        //you probably dont want to do this.
        //telemetry.setAutoClear(false)

    }
}