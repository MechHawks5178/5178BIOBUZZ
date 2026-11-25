package org.firstinspires.ftc.teamcode.Auto;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MH.MHRobot;

@Autonomous(name="Mech Hawks Auto")
public class MechAuto extends LinearOpMode {
    public MHRobot robot = new MHRobot();

    @Override
    public void runOpMode() throws InterruptedException {


        //read april tag up on high (or low) (two functions for reading april tags? h/l) <- is necessary with pedro pathing?
        //pedro pathing to hives



        robot.runShooterAtVelocity(robot.outtakeP, 1500);
        robot.runShooterAtVelocity(robot.outtakeN, 1500);
    }



    //something i think its stick? basically pause before turning on. will check 2025 auto
    //pedro pathing to flowers(?)
    //intake on
    //pedro pathing to hives
    //outtake == on (both)
}
