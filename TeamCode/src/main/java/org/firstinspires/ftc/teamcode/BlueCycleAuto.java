package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class BlueCycleAuto extends LinearOpMode {
    Chassis chassis;
    IMU imu;
    @Override
    public void runOpMode() throws InterruptedException {
        chassis = new Chassis(hardwareMap, imu, gamepad1);

        waitForStart();


    }
}
