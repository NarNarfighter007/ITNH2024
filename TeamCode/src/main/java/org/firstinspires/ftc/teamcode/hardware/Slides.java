package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Slides {
    DcMotorEx slideMotor;
    Servo dropServo, fourbarServo, boxServo;
    Gamepad gamepad1, gamepad2;
    int slideTargetPosition = 0;
    final int down = 0, low = 1100, mid = 2530, high = 3400;
    final double slidePower = 0.8;
    final double hold = 0.68, drop = 1.0, box = 0.4, intake = 0.90, outtake = 0.51; //outtake = 0.21
    final int slidesMin = 881;
    public Slides(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

        slideMotor =  hardwareMap.get(DcMotorEx.class, "SLM01");
        dropServo = hardwareMap.get(Servo.class, "PDS02");
        fourbarServo = hardwareMap.get(Servo.class, "FBS01");
        boxServo = hardwareMap.get(Servo.class, "BRS00");

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setTargetPosition(down);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        dropServo.setPosition(hold);
        fourbarServo.setPosition(intake);
        boxServo.setPosition(box);
    }

    public void runSlides(){
        slideTargetPosition += gamepad1.right_trigger * 5;
        slideTargetPosition -= gamepad1.left_trigger * 5;
        slideMotor.setTargetPosition(slideTargetPosition);
        slideMotor.setPower(slidePower);
    }

    public void runSlidesPresets(){
        slideMotor.setPower(0.8);
        if (gamepad1.a) {
            slideTargetPosition = down;
        } else if(gamepad1.x){
            slideTargetPosition = low;
        } else if(gamepad1.b) {
            slideTargetPosition = mid;
        } else if(gamepad1.y) {
            slideTargetPosition = high;
        }
        slideMotor.setTargetPosition(slideTargetPosition);
        slideMotor.setPower(slidePower);
    }

    public void runDispenser(){
        boxServo.setPosition(box);
        if(gamepad1.dpad_left){
            dropServo.setPosition(drop);
        } else{
            dropServo.setPosition(hold);
        }

        if(getSlideCurPos() > slidesMin && getSlideTargetPos() > slidesMin){
            fourbarServo.setPosition(outtake);
        } else if(getSlideTargetPos() <= slidesMin){
            fourbarServo.setPosition(intake);
            dropServo.setPosition(hold);
        }
    }

    public int getSlideCurPos(){
        return slideMotor.getCurrentPosition();
    }

    public int getSlideTargetPos(){
        return slideMotor.getTargetPosition();
    }
}
