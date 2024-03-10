package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.Chassis;
import org.firstinspires.ftc.teamcode.hardware.TeamIMU;

@TeleOp
public class dragTest extends OpMode {
    Chassis chassis;
    TeamIMU imu;

    @Override
    public void init() {
        imu = new TeamIMU(hardwareMap);
        chassis = new Chassis(hardwareMap, imu, gamepad2);
    }

    @Override
    public void loop() {
        chassis.getTelemetry(telemetry);
    }
}
