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
    public static int x1a = 24, y1a = 36, t1a = 90, x2 = 48, y2a = 40;
    public static int x1b = 12, y1b = 36, t1b = -90, y2b = 36;
    public static int x1c = 12, y1c = 36, t1c = 225, y2c = 32;
    int pos = 1;
    Intake intake;
    Slides slides;
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        intake = new Intake(hardwareMap, gamepad1, gamepad2);
        slides = new Slides(hardwareMap, gamepad1, gamepad2);

        Pose2d startPose = new Pose2d(12, 60, Math.toDegrees(90));
        drive.setPoseEstimate(startPose);
        Trajectory case1 = drive.trajectoryBuilder(startPose)
//                .lineToLinearHeading(new Pose2d(x1a, y1a, Math.toRadians(t1a)))
                .splineToLinearHeading(new Pose2d(x1a, y1a, Math.toRadians(t1a)), 0)
                .addDisplacementMarker(() -> {
//                    intake.depositPurple();
//                    slides.autonExtend();
                })
                .splineTo(new Vector2d(x2, y2a), Math.toRadians(0))
                .addDisplacementMarker(() -> {
//                    slides.autonDispense();
//                    slides.autonRetract();
                })
                .build();
        Trajectory case2 = drive.trajectoryBuilder(startPose)
//                .lineTo(new Vector2d(x1b, y1b))
                .splineTo(new Vector2d(x1b, y1b), Math.toRadians(t1b))
                .addDisplacementMarker(() -> {
//                    intake.depositPurple();
//                    slides.autonExtend();
                })
                .splineTo(new Vector2d(x2, y2b), Math.toRadians(0))
                .addDisplacementMarker(() -> {
//                    slides.autonDispense();
//                    slides.autonRetract();
                })
                .build();
        Trajectory case3 = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(x1c, y1c), Math.toRadians(t1c))
                .addDisplacementMarker(() -> {
//                    intake.depositPurple();
//                    slides.autonExtend();
                })
                .splineTo(new Vector2d(x2, y2c), Math.toRadians(0))
                .addDisplacementMarker(() -> {
//                    slides.autonDispense();
//                    slides.autonRetract();
                })
                .build();

        waitForStart();
        switch(pos){
            case 1:
                drive.followTrajectory(case1);
                break;
            case 2:
                drive.followTrajectory(case2);
                break;
            case 3:
                drive.followTrajectory(case3);
                break;
            default:
                drive.followTrajectory(case1);
                break;
        }
    }
}