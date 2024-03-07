package org.firstinspires.ftc.teamcode.auton.rr5;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Disabled
@Config
@Autonomous
public class TestAuton extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPose = new Pose2d(12, 60, Math.toDegrees(-90));
        Pose2d startPose2 = new Pose2d(12, 60, Math.toDegrees(90));

        drive.setPoseEstimate(startPose);
        Trajectory traj = drive.trajectoryBuilder(startPose,true)
                .splineTo(new Vector2d(24, 36), Math.toRadians(-90))
                .build();
        Trajectory traj2 = drive.trajectoryBuilder(startPose)
                .lineTo(new Vector2d(24, 36))
                .build();
        Trajectory traj3 = drive.trajectoryBuilder(new Pose2d(0, 0, Math.toDegrees(0)))
                .splineTo(new Vector2d(24, 12), Math.toDegrees(0))
                .build();
        Trajectory traj4 = drive.trajectoryBuilder(startPose2, true)
                .splineTo(new Vector2d(24, 36), Math.toRadians(90))
                .build();
        Trajectory traj5 = drive.trajectoryBuilder(startPose)
                .strafeRight(12)
                .build();
        waitForStart();

        drive.followTrajectory(traj5);
//        drive.followTrajectory(traj2);
//        drive.followTrajectory(traj3);
//        drive.followTrajectory(traj4);
        while(!isStopRequested() && opModeIsActive()){
            telemetry.addData("x", drive.getPoseEstimate().getX());
            telemetry.addData("y", drive.getPoseEstimate().getY());
            telemetry.addData("heading", drive.getPoseEstimate().getHeading());
            telemetry.update();
        }
    }
}
