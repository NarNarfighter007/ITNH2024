package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Hang {
    DcMotor hangMotor;
    Servo hookServo;
    Gamepad gamepad1, gamepad2;

    final double hangPower = 1, hookUp = 0, hookDown = 1;
    public Hang(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        hangMotor = hardwareMap.get(DcMotor.class, "HLM00");
        hookServo = hardwareMap.get(Servo.class, "SHS11");

    }

    public void runHang(){
        if(gamepad2.dpad_up){
            hangMotor.setPower(hangPower);
        } else if(gamepad2.dpad_down){
            hangMotor.setPower(-hangPower);
        } else{
            hangMotor.setPower(0);
        }
    }

    public void runHook() {
        if(gamepad2.right_bumper) {
            hookServo.setPosition(0);
        }
        if(gamepad2.left_bumper) {
            hookServo.setPosition(1);
        }
    }
}
