package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slides;
import org.firstinspires.ftc.teamcode.hardware.TeamIMU;

public class RedTopAuto extends LinearOpMode {
    //    Chassis chassis;
    TeamIMU imu;
    MecanumDrive chassis;
    Intake intake;
    Slides slides;
    @Override
    public void runOpMode() throws InterruptedException {
//        chassis = new Chassis(hardwareMap, imu, gamepad1);
        intake = new Intake(hardwareMap, gamepad1, gamepad2);
        slides = new Slides(hardwareMap, gamepad1, gamepad2);
        Pose2d startPose = new Pose2d(36, -60, Math.toDegrees(-90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);
        waitForStart();
        int pos = 1; //use position detection
        switch(pos) {
            case 1:
                Actions.runBlocking(
                        drive.actionBuilder(startPose)
                                .splineTo(new Vector2d(24, -36), 0)
                                .build()
                );
                break;
            case 2:
                Actions.runBlocking(
                        drive.actionBuilder(startPose)
                                .splineTo(new Vector2d(12, -36), 0)
                                .build()
                );
                break;
            case 3:
                Actions.runBlocking(
                        drive.actionBuilder(startPose)
                                .splineTo(new Vector2d(0, -36), 0)
                                .build()
                );
                break;
        }

        Actions.runBlocking(new SequentialAction(
                intake.depositPurple(),
                drive.actionBuilder(drive.pose)
                        .splineTo(new Vector2d(48, -36), 0)
                        .build(),
                slides.dispense(), //TODO: maybe update dispense method
                new ParallelAction(
                        slides.retract(),
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(60, -60), 0)
                                .build()
                )
        ));
    }
}
