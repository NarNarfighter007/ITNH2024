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
//public class RedTopParkAuto extends LinearOpMode{
//    TeamIMU imu;
//    Chassis drive;
//    Intake intake;
//    Slides slides;
//    LinearOpMode opMode;
//    int pos = 1;
//    @Override
//    public void runOpMode() throws InterruptedException {
//        imu = new TeamIMU(hardwareMap);
//        drive = new Chassis(hardwareMap, imu, gamepad1, 270);
//        intake = new Intake(hardwareMap, gamepad1, gamepad2);
//        slides = new Slides(hardwareMap, gamepad1, gamepad2);
//        waitForStart();
//        drive.backward_inches(3);
//        drive.turnTo(0, "LEFT");
//        drive.forward_inches(44);
//    }
//}
