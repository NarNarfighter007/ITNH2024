package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
@TeleOp
public class IntakeExtenstionTest extends OpMode {
    Servo fourbarServo, dropServo, boxServo;

 //   @OverridefourbarServo
    public void init() {
        fourbarServo = hardwareMap.get(Servo.class, "FBS01");
        dropServo = hardwareMap.get(Servo.class, "PDS02");
        boxServo = hardwareMap.get(Servo.class, "BRS00");
    }

    @Override
    public void loop() {
//        .setPosition(gamepad1.left_stick_y);
        dropServo.setPosition(gamepad1.right_stick_x);
        boxServo.setPosition(gamepad1.right_stick_y);
        telemetry.addData("fourbar", fourbarServo.getPosition());
        telemetry.addData("drop", dropServo.getPosition());
        telemetry.addData("box", boxServo.getPosition());
    }
}
