package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.Airplane;
import org.firstinspires.ftc.teamcode.hardware.Chassis;
import org.firstinspires.ftc.teamcode.hardware.Hang;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slides;
import org.firstinspires.ftc.teamcode.hardware.TeamIMU;
import org.firstinspires.ftc.teamcode.hardware.Vision;

@TeleOp
public class Teleop extends OpMode{
        Chassis chassis;
        Slides slides;
        TeamIMU imu;
        Intake intake;
        Vision vision;
        Hang hang;
        Airplane airplane;

    @Override
    public void init() {
        imu = new TeamIMU(hardwareMap);
        chassis = new Chassis(hardwareMap, imu, gamepad1);
        slides = new Slides(hardwareMap, gamepad1, gamepad2);
        intake = new Intake(hardwareMap, gamepad1, gamepad2);
//        vision = new Vision(hardwareMap);
        hang = new Hang(hardwareMap, gamepad1, gamepad2);
        airplane = new Airplane(hardwareMap, gamepad1, gamepad2);
    }

    @Override
    public void loop() {
        chassis.fieldCentricDrive();
        slides.runSlides();
        slides.runSlidesPresets();
        intake.runIntake();
        slides.runDispenser();
        hang.runHang();
        hang.runHook();
        airplane.runAirplane();
//        intake.telemetry(telemetry);
        slides.telemetry(telemetry);
//        telemetry.addData("imu", imu.getHeadingFirstAngle());
    }
}
