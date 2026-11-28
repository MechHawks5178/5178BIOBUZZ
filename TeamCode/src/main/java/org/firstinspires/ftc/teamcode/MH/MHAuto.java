package org.firstinspires.ftc.teamcode.MH;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.MH.MHLimeLight;

//Looking at the robot with the ramp facing you, the odometry pods from left to right are 0, 1, and 2
@Autonomous(name="Mech Hawks Auto")
public class MHAuto extends LinearOpMode {
    public MHRobot robot = new MHRobot();

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap, MHRobot.team.NULL, telemetry);
        //first off, we need to know what team we're on for pedro pathing to work good. get team (same as teleop does)
        if (robot.getTeam() == MHRobot.team.RED) {
            //pedro pathing to hives (Zel)

            //outtake on! yaey
            robot.runShooterAtVelocity(robot.outtakeP, 1500);
            robot.runShooterAtVelocity(robot.outtakeN, 1500);

            //outtake off (will add later)


            //pedropathing to flowers probably to get nectars

            //intake and paddle
            robot.runIntake(1);


        } else if (robot.getTeam() == MHRobot.team.BLUE) {
            //pedro pathing to hives

            //outtake on
            robot.runShooterAtVelocity(robot.outtakeP, 1500);
            robot.runShooterAtVelocity(robot.outtakeN, 1500);

            //outtake off

            //pedropathing to flowers (nectar pls

            //intake and paddle
            robot.runIntake(1);
        }




    }



    //something i think its stick? basically pause before turning on. will check 2025 auto
    //pedro pathing to flowers(?)
    //intake on
    //pedro pathing to hives
    //outtake == on (both)
}
