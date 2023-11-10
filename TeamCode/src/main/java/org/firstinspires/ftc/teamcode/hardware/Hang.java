package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Hang {
    DcMotor hangMotor;
    Gamepad gamepad1, gamepad2;

    final double hangPower = 1;
    public Hang(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        hangMotor = hardwareMap.get(DcMotor.class, "HLM00");
    }

    public void runHang(){
        if(gamepad2.dpad_up){
            hangMotor.setPower(hangPower);
        } else if(gamepad2.dpad_down){
            hangMotor.setPower(hangPower);
        } else{
            hangMotor.setPower(0);
        }
    }
}
