package org.firstinspires.ftc.teamcode.auton.rr5;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Camera;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slides;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.Vector;

@Config
@Autonomous
public class RedTopAuton extends LinearOpMode {
    //    public static int x1a = 24, y1a = 36, t1a = -90, x2 = 48, y2a = 40; //maybe sort of proper vals
    public static int x1a = 30, y1a = -33, t1a = -90, x2 = 66, y2a = -52; //compensated vals
    public static int x1b = 20, y1b = -30, t1b = -90, y2b = -46;
    public static int x1c = 10, y1c = -34, t1c = -90, y2c = -38;
    int pos = 1;
    Intake intake;
    Slides slides;
    Camera camera;
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        intake = new Intake(hardwareMap, gamepad1, gamepad2);
        camera = new Camera(hardwareMap, "RED");
//        slides = new Slides(hardwareMap, gamepad1, gamepad2);

        Pose2d startPose = new Pose2d(12, -60, Math.toDegrees(90));
        drive.setPoseEstimate(startPose);
        TrajectorySequence case1 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .lineTo(new Vector2d(x1a, y1a))
//                .splineTo(new Vector2d(x1a, y1a), Math.toRadians(t1a))
                .addDisplacementMarker(() -> {
//                    intake.startReverseIntake();
//                    slides.autonExtend();
                })
                .waitSeconds(1)
                .lineToLinearHeading(new Pose2d(x2, y2a, Math.toRadians(0)))
//                .splineTo(new Vector2d(x2, y2a), Math.toRadians(0))
                .addDisplacementMarker(() -> {
//                    slides.autonDispense();
//                    slides.autonRetract();
                })
                .build();
        TrajectorySequence case2 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .lineTo(new Vector2d(x1b, y1b))
                .addDisplacementMarker(() -> {
                    intake.startReverseIntake();
                })
                .waitSeconds(1)
                .addDisplacementMarker(() -> {
                    intake.stopReverseIntake();
                })
                .lineToLinearHeading(new Pose2d(x2, y2b, Math.toRadians(0)))
                .addDisplacementMarker(() -> {
//                    slides.autonDispense();
//                    slides.autonRetract();
                })
                .build();
        TrajectorySequence case3 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .lineTo(new Vector2d(x1c+16, y1c-4))
                .turn(Math.toRadians(50))
                .lineTo(new Vector2d(x1c, y1c))
                .lineToLinearHeading(new Pose2d(x2-6, y2c, Math.toRadians(0)))
                .addDisplacementMarker(() -> {
//                    slides.autonDispense();
//                    slides.autonRetract();
                })
                .build();

        while(opModeInInit()){
            camera.getAutonRed();
            telemetry.addData("Pos: ", pos);
            telemetry.update();
            if(camera.getAutonRed() != 0){
                pos = camera.getAutonRed();
            }
        }
        waitForStart();
        switch(pos){
            case 3:
                drive.followTrajectorySequence(case1);
                break;
            case 2:
                drive.followTrajectorySequence(case2);
                break;
            case 1:
                drive.followTrajectorySequence(case3);
                break;
            default:
                drive.followTrajectorySequence(case1);
                break;
        }
        while(!isStopRequested() && opModeIsActive()){
            telemetry.addData("x", drive.getPoseEstimate().getX());
            telemetry.addData("y", drive.getPoseEstimate().getY());
            telemetry.addData("heading", drive.getPoseEstimate().getHeading());
            telemetry.update();
        }
    }
}