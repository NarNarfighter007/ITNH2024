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
public class BlueBotAuton extends LinearOpMode {
    public static int x1a = 23, y1a = 37, t1a = -90, x2 = 52, y2a = 48;
    public static int x1b = 18, y1b = 26, t1b = -90, y2b = 42;
    public static int x1c = 4, y1c = 34, t1c = -90, y2c = 36;
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
                .lineTo(new Vector2d(-36, 32))
                .turn(Math.toRadians(90))
                .back(4)
                .forward(8)
                .setReversed(false)
                .splineTo(new Vector2d(-36, 56), Math.toRadians(0))
                .lineTo(new Vector2d(-24, 56))
                .lineTo(new Vector2d(12, 60))
//                .lineTo(new Vector2d(20, 56))
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
                .strafeLeft(18)
                .build();
        TrajectorySequence case2 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .splineTo(new Vector2d(-36, 26), Math.toRadians(-90))
                .forward(10)
                .setReversed(false)
                .splineTo(new Vector2d(-36, 56), Math.toRadians(0))
                .lineTo(new Vector2d(-24, 56))
                .lineTo(new Vector2d(12, 60))
//                .lineTo(new Vector2d(20, 56))
                .addTemporalMarker(() -> {
                    intake.autonIntakeFlipUp();
                    slides.autonClamp(30);
                    slides.autonOuttakeSequenceHorizontal(200);
                    intake.stopIntake(200);
                })
                .waitSeconds(0.2)
                .splineTo(new Vector2d(x2, y2b), Math.toRadians(0))
                .waitSeconds(.1)
                .back(4)
                .strafeLeft(24)
                .build();
        TrajectorySequence case3 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .splineTo(new Vector2d(-48, 34), Math.toRadians(-90))
                .forward(8)
                .setReversed(false)
                .splineTo(new Vector2d(-36, 56), Math.toRadians(0))
                .lineTo(new Vector2d(-24, 56))
                .lineTo(new Vector2d(12, 60))
//                .lineTo(new Vector2d(20, 56))
                .addTemporalMarker(() -> {
                    intake.autonIntakeFlipUp();
                    slides.autonClamp(30);
                    slides.autonOuttakeSequenceHorizontal(200);
                    intake.stopIntake(200);
                })
                .waitSeconds(0.2)
                .splineTo(new Vector2d(x2, y2c), Math.toRadians(0))
                .waitSeconds(.1)
                .back(4)
                .strafeLeft(30)
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

