package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slides {
    DcMotorEx slideMotor;
    Gamepad gamepad1, gamepad2;
    int slideTargetPosition = 0;
    int low = 100, mid = 200, high = 300;
    public Slides(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        slideMotor =  hardwareMap.get(DcMotorEx.class, "Slides");

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setTargetPosition(0);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }
    public void runSlides(){
        slideTargetPosition += gamepad2.right_stick_y * 0.05;
        slideMotor.setTargetPosition(slideTargetPosition);
        slideMotor.setPower(0.8);
        slideTargetPosition = slideMotor.getCurrentPosition();
    }

    public void runSlidesPresets(){
        slideMotor.setPower(0.8);
        if (gamepad2.a) {
            slideMotor.setTargetPosition(low);
        }
        else if(gamepad2.b) {
            slideMotor.setTargetPosition(mid);
        }
        else if(gamepad2.y) {
            slideMotor.setTargetPosition(high);
        }
    }

    public int getSlidePos(){
        return slideMotor.getCurrentPosition();
    }
}
