//package org.firstinspires.ftc.teamcode.auton;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//import org.firstinspires.ftc.teamcode.hardware.Chassis;
//import org.firstinspires.ftc.teamcode.hardware.Intake;
//import org.firstinspires.ftc.teamcode.hardware.Slides;
//import org.firstinspires.ftc.teamcode.hardware.TeamIMU;
//
//@Disabled
//@Autonomous
//public class BlueTopParkAuto extends LinearOpMode{
//    TeamIMU imu;
//    Chassis drive;
//    Intake intake;
//    Slides slides;
//    LinearOpMode opMode;
//    int pos = 1;
//    @Override
//    public void runOpMode() throws InterruptedException {
//        imu = new TeamIMU(hardwareMap);
//        drive = new Chassis(hardwareMap, imu, gamepad1, 90);
//        intake = new Intake(hardwareMap, gamepad1, gamepad2);
//        slides = new Slides(hardwareMap, gamepad1, gamepad2);
//        waitForStart();
//        drive.backward_inches(3);
//        drive.turnTo(0, "RIGHT");
//        drive.forward_inches(44);
////        if(pos == 1){
////            drive.backward_inches(24);
////            drive.turnTo(135, "LEFT");
////            drive.backward_inches(4);
////            intake.outtakePurple();
////            drive.forward_inches(5);
////            drive.turnTo(-15, "LEFT");
////            drive.forward_inches(39);
////            slides.outtakeYellow();
////        } else if(pos == 2){
////            drive.backward_inches(12);
////            intake.outtakePurple();
////            drive.forward_inches(12);
////            drive.turnTo(0, "LEFT");
////            drive.forward_inches(36);
////            slides.outtakeYellow();
////        } else if(pos == 3){
////            drive.backward_inches(28);
////            drive.turnTo(0, "RIGHT");
////            drive.backward_inches(6);
////            intake.outtakePurple();
////            drive.forward_inches(44);
////            slides.outtakeYellow();
////        }
//    }
//}
