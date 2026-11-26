package org.firstinspires.ftc.teamcode.MH;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.MH.MHLimeLight;

@Autonomous(name="Mech Hawks Auto")
public class MHAuto extends LinearOpMode {
    public MHRobot robot = new MHRobot();

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap, MHRobot.team.NULL, telemetry);
        //first off, we need to know what team we're on for pedro pathing to work good. get team (same as teleop does)
        if (robot.robotTeam == MHRobot.team.RED) {
            //pedro pathing to hives (Zel)

            //outtake on! yaey
            robot.runShooterAtVelocity(robot.outtakeP, 1500);
            robot.runShooterAtVelocity(robot.outtakeN, 1500);

            //outtake off (will add later)


            //pedropathing to flowers probably to get nectars

            //intake and paddle
            robot.runIntakeAtVelocity(1500);


        }




    }



    //something i think its stick? basically pause before turning on. will check 2025 auto
    //pedro pathing to flowers(?)
    //intake on
    //pedro pathing to hives
    //outtake == on (both)
}
