package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class LiftTest extends OpMode {
    DcMotor lift;
    Servo hook;
    @Override
    public void init() {
        lift = hardwareMap.get(DcMotor.class, "HLM00");
        hook = hardwareMap.get(Servo.class, "SHS11");
   //     telemetry.addData("Servo:", hook.getPosition());
    }

    @Override
    public void loop() {
        lift.setPower(gamepad1.right_stick_y);
        if(gamepad1.a) {
            hook.setPosition(1);
        } else if(gamepad1.b) {
            hook.setPosition(.55); //UPRIGHT POSITION
        }
        telemetry.addData("Servo:", hook.getPosition());
    }
}