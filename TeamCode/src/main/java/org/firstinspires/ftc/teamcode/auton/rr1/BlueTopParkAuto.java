package org.firstinspires.ftc.teamcode.auton.rr1;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.teamcode.hardware.Camera;
import org.firstinspires.ftc.teamcode.hardware.Chassis;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slides;
import org.firstinspires.ftc.teamcode.hardware.TeamIMU;

@Autonomous
public class BlueTopParkAuto extends LinearOpMode{
    TeamIMU imu;
    Chassis drive;
    Intake intake;
    Slides slides;
    Camera camera;
    LinearOpMode opMode;
    int pos = 1;
    @Override
    public void runOpMode() throws InterruptedException {
        imu = new TeamIMU(hardwareMap);
        drive = new Chassis(hardwareMap, imu, gamepad1, 90);
        intake = new Intake(hardwareMap, gamepad1, gamepad2);
        slides = new Slides(hardwareMap, gamepad1, gamepad2);
        camera = new Camera(hardwareMap, "BLUE");
        while(opModeInInit()){
            telemetry.addData("BlueReturn: ", camera.getAutonBlue());
            pos = camera.getAutonBlue();
            telemetry.update();
        }
        waitForStart();

        drive.backward_inches(2);
        drive.turnTo(0, "RIGHT");
        drive.forward_inches(44);
//        if(pos == 1){
//            drive.backward_inches(24);
//            drive.turnTo(135, "LEFT");
//            drive.backward_inches(4);
//            intake.outtakePurple();
//            drive.forward_inches(5);
//            drive.turnTo(-15, "LEFT");
//            drive.forward_inches(39);
////            slides.outtakeYellow();
//        } else if(pos == 2){
//            drive.backward_inches(36);
//            intake.outtakePurple();
//            drive.forward_inches(12);
//            drive.turnTo(0, "LEFT");
//            drive.forward_inches(36);
////            slides.outtakeYellow();
//        } else if(pos == 3){
//            drive.backward_inches(28);
//            drive.turnTo(0, "RIGHT");
//            drive.backward_inches(6);
//            intake.outtakePurple();
//            drive.forward_inches(44);
//            slides.outtakeYellow();
//        }
    }
}
