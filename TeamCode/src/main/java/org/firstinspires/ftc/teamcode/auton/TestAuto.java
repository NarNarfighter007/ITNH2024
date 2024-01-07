package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous
public class TestAuto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d startPose = new Pose2d(12, 60, Math.toDegrees(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);
        waitForStart();
        Actions.runBlocking(
                drive.actionBuilder(startPose)
                        .splineTo(new Vector2d(24, 36), 0)
                        .build()
        );

        while(opModeIsActive() && !isStopRequested()){
            telemetry.addData("heading", drive.pose.heading);
            telemetry.addData("x", drive.pose.position.x);
            telemetry.addData("y", drive.pose.position.y);
            telemetry.update();
        }
    }
}
