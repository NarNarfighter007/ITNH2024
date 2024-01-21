package org.firstinspires.ftc.teamcode.auton.rr5;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slides;

@Config
@Autonomous
public class BlueTopAuton extends LinearOpMode {
    public static int x1a = 12, y1a = 36;
    Intake intake;
    Slides slides;
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        intake = new Intake(hardwareMap, gamepad1, gamepad2);
        slides = new Slides(hardwareMap, gamepad1, gamepad2);

        Pose2d startPose = new Pose2d(32, 60, Math.toDegrees(90));
        drive.setPoseEstimate(startPose);
        Trajectory case1 = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(x1a, y1a), Math.toRadians(135))
                .addDisplacementMarker(() -> {intake.depositPurple();})
                .build();
    }
}
