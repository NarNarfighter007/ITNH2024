package org.firstinspires.ftc.teamcode.auton.rr5;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Camera;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slides;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Disabled
@Config
@Autonomous(group = "Auton")
public class BlueBotCycleAuton extends LinearOpMode {
    public static int x1a = 23, y1a = 37, t1a = -90, x2 = 52, y2a = 42;
    public static int x1b = 18, y1b = 26, t1b = -90, y2b = 37;
    public static int x1c = 4, y1c = 34, t1c = -90, y2c = 30;
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

        Pose2d startPose = new Pose2d(-36, 60, Math.toRadians(startHeading));
        drive.setPoseEstimate(startPose);
        TrajectorySequence case1 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .lineTo(new Vector2d(-32, 32))
                .turn(Math.toRadians(80))
                .lineTo(new Vector2d(-24, 32))
                .addTemporalMarker(()->{
                    slides.autonDispense();
                    slides.autonFBIntakeUp();
                    intake.autonIntakeFlipDown();
                })
                .splineTo(new Vector2d(-62, 6), Math.toRadians(180))
                .addTemporalMarker(()->{
                    intake.startIntake();
                    intake.autonIntakeTwo();
                })
                .waitSeconds(2.5)
                .setReversed(false)
                .splineTo(new Vector2d(12, 1), Math.toRadians(0))
                .waitSeconds(1)
                .addTemporalMarker(() -> {
                    intake.autonIntakeFlipUp();
                    slides.autonClamp(30);
                    slides.autonOuttakeSequenceHorizontal(200);
                    intake.stopIntake(200);
                })
                .waitSeconds(0.2)
                .splineTo(new Vector2d(x2, y2a), Math.toRadians(0))
                .waitSeconds(.1)
                .back(4)
                .build();
        TrajectorySequence case2 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .splineTo(new Vector2d(-36, 26), Math.toRadians(-90))
                .forward(10)
                .addTemporalMarker(()->{
                    slides.autonDispense();
                    slides.autonFBIntakeUp();
                    intake.autonIntakeFlipDown();
                })
                .splineTo(new Vector2d(-54, 6), Math.toRadians(180))
                .back(8)
                .addTemporalMarker(()->{
                    intake.startIntake();
                    intake.autonIntakeTwo();
                })
                .waitSeconds(2.5)
                .setReversed(false)
                .splineTo(new Vector2d(12, 1), Math.toRadians(0))
                .waitSeconds(1)
                .addTemporalMarker(() -> {
                    intake.autonIntakeFlipUp();
                    slides.autonClamp(30);
                    slides.autonOuttakeSequence2(200);
                    intake.stopIntake(200);
                })
                .waitSeconds(0.2)
                .splineTo(new Vector2d(x2, y2b), Math.toRadians(0))
                .waitSeconds(.1)
                .back(4)
                .build();
        TrajectorySequence case3 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .splineTo(new Vector2d(-48, 34), Math.toRadians(-90))
                .forward(8)
                .splineTo(new Vector2d(-36, 40), Math.toRadians(-90))
                .lineToLinearHeading(new Pose2d(-40, 12, Math.toRadians(-45)))
                .addTemporalMarker(()->{
                    slides.autonDispense();
                    slides.autonFBIntakeUp();
                    intake.autonIntakeFlipDown();
                })
                .waitSeconds(5)
                .lineToLinearHeading(new Pose2d(-60,6, Math.toRadians(0)))
//                .splineTo(new Vector2d(-60, 6), Math.toRadians(0))
                .addTemporalMarker(()->{
                    intake.startIntake();
                    intake.autonIntakeTwo();
                })
                .waitSeconds(3)
                .setReversed(false)
                .splineTo(new Vector2d(12, 1), Math.toRadians(0))
                .waitSeconds(1)
                .addTemporalMarker(() -> {
                    intake.autonIntakeFlipUp();
                    slides.autonClamp(30);
                    slides.autonOuttakeSequence2(200);
                    intake.stopIntake(200);
                })
                .waitSeconds(0.2)
                .splineTo(new Vector2d(x2, y2c), Math.toRadians(0))
                .waitSeconds(.1)
                .back(4)
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
        pos = 3;
        switch(pos){
            case 1:
                drive.followTrajectorySequence(case1);
                break;
            case 2:
                drive.followTrajectorySequence(case2);
                break;
            case 3:
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

