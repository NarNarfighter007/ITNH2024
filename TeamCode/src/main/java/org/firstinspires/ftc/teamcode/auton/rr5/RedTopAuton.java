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
public class RedTopAuton extends LinearOpMode {
    public static int x1a = 23, y1a = -37, t1a = -90, x2 = 51, y2a = -40+1;
    public static int x1b = 18, y1b = -30, t1b = -90, y2b = -33+1;
    public static int x1c = 8, y1c = -34, t1c = -90, y2c = -27+1;
    int pos = 1;
    Intake intake;
    Slides slides;
    Camera camera;
    public static double startHeading = -90;
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        intake = new Intake(hardwareMap, gamepad1, gamepad2);
        camera = new Camera(hardwareMap, "RED");
        slides = new Slides(hardwareMap, gamepad1, gamepad2);

        Pose2d startPose = new Pose2d(12, -60, Math.toRadians(startHeading));
        drive.setPoseEstimate(startPose);
        TrajectorySequence case1 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .splineTo(new Vector2d(x1a, y1a), Math.toRadians(90))
                .forward(5)
                .addTemporalMarker(()->{
                    slides.autonOuttakeSequence1(0);
                })
                .setReversed(false)
                .splineTo(new Vector2d(x2, y2a), Math.toRadians(0))
                .waitSeconds(0.5)
                .back(4)
                .strafeRight(18)
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
                .back(4)
                .strafeRight(23)
                .build();
        TrajectorySequence case3 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .lineTo(new Vector2d(x1c+4, y1c))
                .turn(Math.toRadians(80))
                .lineTo(new Vector2d(x1c, y1c))
                .addTemporalMarker(()->{
                    slides.autonOuttakeSequence1(450);
                })
                .setReversed(false)
                .splineTo(new Vector2d(x2, y2c-4), Math.toRadians(0))
                .waitSeconds(0.5)
                .back(4)
                .strafeRight(28)
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