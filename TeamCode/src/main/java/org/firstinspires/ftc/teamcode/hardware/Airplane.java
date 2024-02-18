package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Airplane {
    Servo planeServo;
    Gamepad gamepad1, gamepad2;

    final double hold = 0.178, release = 0.4;
    public Airplane(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        planeServo = hardwareMap.get(Servo.class, "APS12");
        planeServo.setPosition(hold);
    }

    public void runAirplane(){
        if(gamepad2.dpad_right){
            planeServo.setPosition(release);
        } else {
            planeServo.setPosition(hold);
        }
    }
}
