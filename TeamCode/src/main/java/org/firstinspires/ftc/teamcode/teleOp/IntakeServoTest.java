package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
@TeleOp
public class IntakeServoTest extends OpMode {
    Servo flip, left, right;

    @Override
    public void init() {
        flip = hardwareMap.get(Servo.class, "FUS14");
        left = hardwareMap.get(Servo.class, "ILS13");
        right = hardwareMap.get(Servo.class, "IRS03");
    }

    @Override
    public void loop() {
//        left.setPosition(0.5);
//        flip.setPosition(0.5);
//        right.setPosition(0.5);
//        left.setPosition(gamepad1.left_stick_y);
        flip.setPosition(gamepad1.right_stick_x);
//        right.setPosition(gamepad1.right_stick_y);
        telemetry.addData("flip", flip.getPosition());
        telemetry.addData("left", left.getPosition());
        telemetry.addData("right", right.getPosition());
    }
}
