package org.firstinspires.ftc.teamcode.auton.rr5;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Camera;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slides;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Config
@Autonomous(group = "Auton")
public class BlueTopCycleAuton extends LinearOpMode {
    public static int x1a = 23, y1a = 37, t1a = -90, x2 = 52, y2a = 42;
    public static int x1b = 18, y1b = 26, t1b = -90, y2b = 37;
    public static int x1c = 8, y1c = 34, t1c = -90, y2c = 30;
    int pos = 1;
    Intake intake;
    Slides slides;
    Camera camera;
    public static double startHeading = 90;
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        intake = new Intake(hardwareMap, gamepad1, gamepad2);
        camera = new Camera(hardwareMap, "BLUE");
        slides = new Slides(hardwareMap, gamepad1, gamepad2);

        Pose2d startPose = new Pose2d(12, 60, Math.toRadians(startHeading));
        drive.setPoseEstimate(startPose);
        TrajectorySequence case1 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .splineTo(new Vector2d(x1a, y1a), Math.toRadians(-90))
                .forward(5)
                .addTemporalMarker(()->{
                    slides.autonOuttakeSequence1(0);
                })
                .setReversed(false)
                .splineTo(new Vector2d(x2, y2a), Math.toRadians(0))
                .waitSeconds(0.5)
                .build();
        TrajectorySequence case2 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .lineTo(new Vector2d(x1b, y1b))
                .addTemporalMarker(()->{
                    slides.autonOuttakeSequence1(850);
                })
                .forward(8)
                .setReversed(false)
                .splineTo(new Vector2d(x2+1, y2b), Math.toRadians(0))
                .waitSeconds(0.5)
                .build();
        TrajectorySequence case3 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .lineTo(new Vector2d(x1c+8, y1c))
                .turn(Math.toRadians(-80))
                .lineTo(new Vector2d(x1c, y1c))
                .addTemporalMarker(()->{
                    slides.autonOuttakeSequence1(600);
                })
                .setReversed(false)
                .splineTo(new Vector2d(x2, y2c), Math.toRadians(0))
                .waitSeconds(0.5)
                .build();

        TrajectorySequence transition1 = drive.trajectorySequenceBuilder(case1.end())
                .setReversed(true)
                .splineTo(new Vector2d(36,6), Math.toRadians(180))
                .addTemporalMarker(() -> {
                    intake.autonIntakeFlipDown();
                    intake.autonIntakeOut();
                })
                .build();
        TrajectorySequence transition2 = drive.trajectorySequenceBuilder(case2.end())
                .setReversed(true)
                .splineTo(new Vector2d(36, 6), Math.toRadians(180))
                .addTemporalMarker(() -> {
                    intake.autonIntakeFlipDown();
                    intake.autonIntakeOut();
                })
                .build();
        TrajectorySequence transition3 = drive.trajectorySequenceBuilder(case3.end())
                .setReversed(true)
                .splineTo(new Vector2d(36, 6), Math.toRadians(180))
                .addTemporalMarker(() -> {
                    intake.autonIntakeFlipDown();
                    intake.autonIntakeOut();
                })
                .build();

        TrajectorySequence cycle1 = drive.trajectorySequenceBuilder(transition1.end())
                .setReversed(true)
                .splineTo(new Vector2d(-60, 1), Math.toRadians(180))
                .addTemporalMarker(()->{
                    slides.autonDispense();
                    intake.autonIntakeTwo();
                    intake.startIntake();
                })
                .waitSeconds(2.5)
                .setReversed(false)
                .splineTo(new Vector2d(12, 4), Math.toRadians(0))
                .waitSeconds(1)
                .addTemporalMarker(() -> {
                    slides.autonDispense();
                    intake.autonIntakeFlipUp();
                    slides.autonClamp(30);
                    slides.autonOuttakeSequence2(200);
                    intake.stopIntake(200);
                })
                .waitSeconds(0.2)
                .splineTo(new Vector2d(x2, y2c), Math.toRadians(0))
                .waitSeconds(.1)
                .back(6)
                .build();

        TrajectorySequence cycle2 = drive.trajectorySequenceBuilder(transition2.end())
                .setReversed(true)
                .splineTo(new Vector2d(-60, 4), Math.toRadians(180))
//                .lineToLinearHeading(new Pose2d(-56, 7, Math.toRadians(0)))
                .addTemporalMarker(()->{
                    slides.autonDispense();
                    intake.autonIntakeTwo();
                    intake.startIntake();
                })
                .waitSeconds(2.5)
                .setReversed(false)
                .splineTo(new Vector2d(12, 0), Math.toRadians(0))
                .waitSeconds(1)
                .addTemporalMarker(() -> {
                    slides.autonDispense();
                    intake.autonIntakeFlipUp();
                    slides.autonClamp(30);
                    slides.autonOuttakeSequence2(200);
                    intake.stopIntake(200);
                })
                .waitSeconds(0.2)
                .splineTo(new Vector2d(x2, y2c), Math.toRadians(0))
                .waitSeconds(.1)
                .back(6)
                .build();

        TrajectorySequence cycle3 = drive.trajectorySequenceBuilder(transition3.end())
                .setReversed(true)
                .splineTo(new Vector2d(-60, 4), Math.toRadians(180))
//                .lineToLinearHeading(new Pose2d(-56, 7, Math.toRadians(0)))
                .addTemporalMarker(()->{
                    slides.autonDispense();
                    intake.autonIntakeTwo();
                    intake.startIntake();
                })
                .waitSeconds(2.5)
                .setReversed(false)
                .splineTo(new Vector2d(12, 0), Math.toRadians(0))
                .waitSeconds(.5)
                .addTemporalMarker(() -> {
                    slides.autonDispense();
                    intake.autonIntakeFlipUp();
                    slides.autonClamp(30);
                    slides.autonOuttakeSequence2(200);
                    intake.stopIntake(200);
                })
                .waitSeconds(0.2)
                .splineTo(new Vector2d(x2, y2a), Math.toRadians(0))
                .waitSeconds(.1)
                .back(6)
                .build();

        while(opModeInInit()){
            camera.getAutonBlue();
            telemetry.addData("Pos: ", pos);
            telemetry.update();
            if(camera.getAutonBlue() != 0){
                pos = camera.getAutonBlue();
            }
        }
        waitForStart();
        switch(pos){
            case 1:
                drive.followTrajectorySequence(case1);
                drive.followTrajectorySequence(transition1);
                drive.followTrajectorySequence(cycle1);
                break;
            case 2:
                drive.followTrajectorySequence(case2);
                drive.followTrajectorySequence(transition2);
                drive.followTrajectorySequence(cycle2);
                break;
            case 3:
                drive.followTrajectorySequence(case3);
                drive.followTrajectorySequence(transition3);
                drive.followTrajectorySequence(cycle3);
                break;
            default:
                drive.followTrajectorySequence(case1);
                drive.followTrajectorySequence(transition1);
                drive.followTrajectorySequence(cycle1);
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