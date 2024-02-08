package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ServoTest extends OpMode {
    Servo fourbarServo, dropServo, boxServo, boxRotServo, intake;

    @Override
    public void init() {
        fourbarServo = hardwareMap.get(Servo.class, "FBS00"); //FBS00 //72
        dropServo = hardwareMap.get(Servo.class, "PDS02"); //
        boxServo = hardwareMap.get(Servo.class, "BRS01"); //22
//        boxRotServo = hardwareMap.get(Servo.class, "TWS03");
        intake = hardwareMap.get(Servo.class, "FUS14");
    }

    @Override
    public void loop() {
        fourbarServo.setPosition(gamepad1.left_stick_y);
        dropServo.setPosition(gamepad1.right_stick_x);
        boxServo.setPosition(gamepad1.right_stick_y);
//        boxRotServo.setPosition(gamepad1.left_stick_y);
//        intake.setPosition(gamepad1.left_stick_y);
        telemetry.addData("fourbar", fourbarServo.getPosition());
        telemetry.addData("drop", dropServo.getPosition());
        telemetry.addData("box", boxServo.getPosition());
//        telemetry.addData("box rot", boxRotServo.getPosition());
//        telemetry.addData("intake", intake.getPosition());

//        fourbarServo.setPosition(0);
////        dropServo.setPosition(gamepad1.right_stick_x);
//        boxServo.setPosition(gamepad1.right_stick_y);
//        boxRotServo.setPosition(0.57);
    }
}
