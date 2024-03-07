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
public class BlueBotCycleAuton extends LinearOpMode {
    //    public static int x1a = 24, y1a = 36, t1a = -90, x2 = 48, y2a = 40; //maybe sort of proper vals
    public static int x1a = 30, y1a = 38, t1a = -90, x2 = 58, y2a = 53; //compensated vals
    public static int x1b = 20, y1b = 28, t1b = -90, y2b = 51;
    public static int x1c = 16, y1c = 34, t1c = -90, y2c = 46;
    public static int offsetHeading = 0;
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
                .addTemporalMarker(()->{
                    slides.autonOuttakeSequence1(100);
                })
                .splineTo(new Vector2d(x2, y2a), Math.toRadians(180))
                .build();
        TrajectorySequence case2 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .lineTo(new Vector2d(x1b, y1b))
                .addTemporalMarker(() -> {slides.autonExtend();})
                .waitSeconds(0.4)
                .addTemporalMarker(() -> {slides.autonFBOuttake();slides.autonPitchUp();})
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(x2, y2b, Math.toRadians(offsetHeading)))
                .addTemporalMarker(() -> {slides.autonDispense();})
                .waitSeconds(0.5)
                .back(7)
                .addTemporalMarker(() ->{slides.autonFBIntakeDown();})
                .waitSeconds(0.25)
                .addTemporalMarker(() -> {slides.autonPitchDown();})
                .waitSeconds(0.25)
                .addTemporalMarker(()-> {slides.autonRetract();})
                .build();
        TrajectorySequence case3 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .lineTo(new Vector2d(x1c+8, y1c))
                .turn(Math.toRadians(-90))
                .lineTo(new Vector2d(x1c, y1c))
                .addTemporalMarker(() -> {slides.autonExtend();})
                .waitSeconds(0.4)
                .addTemporalMarker(() -> {slides.autonFBOuttake();slides.autonPitchUp();})
                .waitSeconds(0.5)
                .lineToSplineHeading(new Pose2d(x2+3, y2c, Math.toRadians(offsetHeading)))
                .addTemporalMarker(() -> {slides.autonDispense();})
                .waitSeconds(0.5)
                .back(7)
                .addTemporalMarker(() ->{slides.autonFBIntakeDown();})
                .waitSeconds(0.25)
                .addTemporalMarker(() -> {slides.autonPitchDown();})
                .waitSeconds(0.25)
                .addTemporalMarker(()-> {slides.autonRetract();})
                .build();

        TrajectorySequence transition1 = drive.trajectorySequenceBuilder(case1.end())
                .setReversed(true)
//                .splineTo(new Vector2d(40,12), Math.toRadians(180))
                .splineTo(new Vector2d(24,6), Math.toRadians(180-offsetHeading))
//                .lineToSplineHeading(new Pose2d(40, 12, Math.toRadians(0)))
//                .lineToSplineHeading(new Pose2d(24, 12, Math.toRadians(0)))
                .addTemporalMarker(() -> {
                    intake.autonIntakeFlipDown();
                    intake.autonIntakeOut();
                })
                .build();
        TrajectorySequence transition2 = drive.trajectorySequenceBuilder(case2.end())
                .setReversed(true)
                .lineToLinearHeading(new Pose2d(0, 12, Math.toRadians(180+offsetHeading)))
                .addTemporalMarker(() -> {
                    intake.autonIntakeFlipDown();
                    intake.autonIntakeOut();
                })
                .build();
        TrajectorySequence transition3 = drive.trajectorySequenceBuilder(case3.end())
                .setReversed(true)
                .lineToLinearHeading(new Pose2d(0, 12, Math.toRadians(180+offsetHeading)))
                .addTemporalMarker(() -> {
                    intake.autonIntakeFlipDown();
                    intake.autonIntakeOut();
                })
                .build();

        TrajectorySequence cycle = drive.trajectorySequenceBuilder(transition1.end())
                .setReversed(true)
                .lineToLinearHeading(new Pose2d(-51, 7, Math.toRadians(0)))
                .addTemporalMarker(() -> {
                    intake.startIntake();
                    intake.autonIntakeIn();
                })
                .waitSeconds(0.25)
                .addTemporalMarker(() -> {
                    intake.autonIntakeOut();
                })
                .waitSeconds(0.25)
                .addTemporalMarker(() -> {
                    intake.autonIntakeIn();
                })
                .waitSeconds(0.25)
                .addTemporalMarker(() -> {
                    intake.autonIntakeOut();
                    intake.flipUp();
                })
                .lineToLinearHeading(new Pose2d(12, 12, Math.toRadians(0)))
                .addTemporalMarker(() -> {
                    slides.autonExtend();
                })
                .lineToLinearHeading(new Pose2d(x2+6, y2c, Math.toRadians(0)))
                .addTemporalMarker(() -> {
                    slides.autonDispense();
                })
                .waitSeconds(0.5)
                .addTemporalMarker(() -> {
                    slides.autonRetract();
                })
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
                break;
            case 2:
                drive.followTrajectorySequence(case2);
                drive.followTrajectorySequence(transition2);
                break;
            case 3:
                drive.followTrajectorySequence(case3);
                drive.followTrajectorySequence(transition3);
                break;
            default:
                drive.followTrajectorySequence(case1);
                drive.followTrajectorySequence(transition1);
                break;
        }
        drive.followTrajectorySequence(cycle);
        while(!isStopRequested() && opModeIsActive()){
            telemetry.addData("x", drive.getPoseEstimate().getX());
            telemetry.addData("y", drive.getPoseEstimate().getY());
            telemetry.addData("heading", drive.getPoseEstimate().getHeading());
            telemetry.update();
        }
    }
}