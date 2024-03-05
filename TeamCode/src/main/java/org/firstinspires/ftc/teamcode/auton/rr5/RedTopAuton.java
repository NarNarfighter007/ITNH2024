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
@Autonomous
public class RedTopAuton extends LinearOpMode {
    //    public static int x1a = 24, y1a = 36, t1a = -90, x2 = 48, y2a = 40; //maybe sort of proper vals
    public static int x1a = 30, y1a = -33, t1a = -90, x2 = 56, y2a = -52; //compensated vals
    public static int x1b = 20, y1b = -28, t1b = -90, y2b = -46;
    public static int x1c = 12, y1c = -32, t1c = -90, y2c = -38;
    int pos = 1;
    Intake intake;
    Slides slides;
    Camera camera;
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        intake = new Intake(hardwareMap, gamepad1, gamepad2);
        camera = new Camera(hardwareMap, "RED");
        slides = new Slides(hardwareMap, gamepad1, gamepad2);
        Pose2d startPose = new Pose2d(12, -60, Math.toDegrees(90));
        drive.setPoseEstimate(startPose);
        TrajectorySequence test = drive.trajectorySequenceBuilder(startPose)
                .addTemporalMarker(() -> {slides.autonExtend();})
                .waitSeconds(0.5)
                .addTemporalMarker(() -> {slides.autonFBOuttake();})
                .waitSeconds(1)
                .addTemporalMarker(() -> {slides.autonPitchUp();})
                .waitSeconds(0.5)

                .addTemporalMarker(() -> {slides.autonDispense();})
                .waitSeconds(0.5)
                .addTemporalMarker(() ->{slides.autonFBIntakeDown();})
                .waitSeconds(0.25)
                .addTemporalMarker(() -> {slides.autonPitchDown();})
                .waitSeconds(0.5)
                .addTemporalMarker(()-> {slides.autonRetract();})
                .waitSeconds(30)
                .build();

        TrajectorySequence case1 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .lineTo(new Vector2d(x1a, y1a))
                .addTemporalMarker(() -> {slides.autonExtend();intake.setEmergencyOuttake(intake.emergencyOpen);})
                .waitSeconds(0.4)
                .addTemporalMarker(() -> {slides.autonFBOuttake();slides.autonPitchUp();})
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(x2, y2a, Math.toRadians(0)))
                .addTemporalMarker(() -> {slides.autonDispense();})
                .waitSeconds(0.5)
                .back(7)
                .addTemporalMarker(() ->{slides.autonFBIntakeDown();})
                .waitSeconds(0.25)
                .addTemporalMarker(() -> {slides.autonPitchDown();})
                .waitSeconds(0.25)
                .addTemporalMarker(()-> {slides.autonRetract();intake.setEmergencyOuttake(intake.emergencyClosed);})
                .strafeLeft(28)
                .build();
        TrajectorySequence case2 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .lineTo(new Vector2d(x1b, y1b))
                .addTemporalMarker(() -> {slides.autonExtend();intake.setEmergencyOuttake(intake.emergencyOpen);})
                .waitSeconds(0.4)
                .addTemporalMarker(() -> {slides.autonFBOuttake();slides.autonPitchUp();})
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(x2+1, y2b, Math.toRadians(0)))
                .addTemporalMarker(() -> {slides.autonDispense();})
                .waitSeconds(0.5)
                .back(7)
                .addTemporalMarker(() ->{slides.autonFBIntakeDown();})
                .waitSeconds(0.25)
                .addTemporalMarker(() -> {slides.autonPitchDown();})
                .waitSeconds(0.25)
                .addTemporalMarker(()-> {slides.autonRetract();intake.setEmergencyOuttake(intake.emergencyClosed);})
                .strafeLeft(26)
                .build();
        TrajectorySequence case3 = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .lineTo(new Vector2d(x1c+8, y1c))
                .turn(Math.toRadians(85))
                .lineTo(new Vector2d(x1c, y1c))
                .addTemporalMarker(() -> {slides.autonExtend();intake.setEmergencyOuttake(intake.emergencyOpen);})
                .waitSeconds(0.4)
                .addTemporalMarker(() -> {slides.autonFBOuttake();slides.autonPitchUp();})
                .waitSeconds(0.5)
                .lineToSplineHeading(new Pose2d(x2+1, y2c, Math.toRadians(0)))
                .addTemporalMarker(() -> {slides.autonDispense();})
                .waitSeconds(0.5)
                .back(7)
                .addTemporalMarker(() ->{slides.autonFBIntakeDown();})
                .waitSeconds(0.25)
                .addTemporalMarker(() -> {slides.autonPitchDown();})
                .waitSeconds(0.25)
                .addTemporalMarker(()-> {slides.autonRetract();intake.setEmergencyOuttake(intake.emergencyClosed);})
                .strafeLeft(24)
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