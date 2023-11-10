package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ServoTest extends OpMode {
    Servo fourbarServo, dropServo, boxServo;

    @Override
    public void init() {
        fourbarServo = hardwareMap.get(Servo.class, "FBS01"); //90deg = 0.21, straight(intake) = 0.78, out but not all the way = .5
        dropServo = hardwareMap.get(Servo.class, "PDS02"); //hold = 0.7, drop = 1.0
        boxServo = hardwareMap.get(Servo.class, "BRS00"); //1.0
    }

    @Override
    public void loop() {
        fourbarServo.setPosition(gamepad1.left_stick_y);
//        dropServo.setPosition(0.7);
        boxServo.setPosition(0.4);
        telemetry.addData("fourbar", fourbarServo.getPosition());
        telemetry.addData("drop", dropServo.getPosition());
        telemetry.addData("box", boxServo.getPosition());
    }
}
