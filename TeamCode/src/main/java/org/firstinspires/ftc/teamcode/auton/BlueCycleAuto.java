package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware.Chassis;
import org.firstinspires.ftc.teamcode.hardware.TeamIMU;

public class BlueCycleAuto extends LinearOpMode {
    Chassis chassis;
    TeamIMU imu;
    @Override
    public void runOpMode() throws InterruptedException {
        chassis = new Chassis(hardwareMap, imu, gamepad1);

        waitForStart();


    }
}
