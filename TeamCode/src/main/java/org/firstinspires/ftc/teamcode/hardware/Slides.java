package org.firstinspires.ftc.teamcode.hardware;

import android.drm.DrmStore;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
//import com.acmerobotics.roadrunner.Action;
//import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Timer;

@Config
public class Slides {
    DcMotorEx slideMotor;
    Servo dropServo, fourbarServo, boxServo, boxRotServo;
    Gamepad gamepad1, gamepad2;
    int slideTargetPosition = 0;
    final int down = 0, low = 1730, mid = 1980, high = 2300;
    final double slidePower = 0.8;
    public static double hold = 0.18, drop = 0, boxUp = 0.47, boxDown = 0.334, boxRotIntake = 0.57, boxRotOuttake = 0,
        intake = 0.97, outtake = 0;
    public static double extendDelay = 0;
    final int slidesMin = 1500, boxMin = 400, slideBoxRotMin = 950;
    ElapsedTime timer = new ElapsedTime();
    ElapsedTime timer2 = new ElapsedTime();
    double time, startTime;
    public Slides(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

        slideMotor =  hardwareMap.get(DcMotorEx.class, "SLM01");
        dropServo = hardwareMap.get(Servo.class, "PDS02");
        fourbarServo = hardwareMap.get(Servo.class, "FBS00");
        boxServo = hardwareMap.get(Servo.class, "BRS01");
        boxRotServo = hardwareMap.get(Servo.class, "TWS03");

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setTargetPosition(down);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        dropServo.setPosition(hold);
        fourbarServo.setPosition(intake);
        boxServo.setPosition(boxDown);
        boxRotServo.setPosition(boxRotIntake);
    }

    public void runSlides(){
        slideTargetPosition += gamepad1.right_trigger * 50;
        slideTargetPosition -= gamepad1.left_trigger * 50;
        if(time > startTime + extendDelay) {
            slideMotor.setTargetPosition(slideTargetPosition);
        }
        slideMotor.setPower(slidePower);
    }

    public void runSlidesPresets(){
        time = timer2.milliseconds();
        slideMotor.setPower(0.8);
        if (gamepad1.a) {
            if(getSlideCurPos() < down + 50) {
                startTime = time;
            }
            slideTargetPosition = down;
        } else if(gamepad1.x){
            if(getSlideCurPos() < down + 50) {
                startTime = time;
            }
            slideTargetPosition = low;
        } else if(gamepad1.b) {
            if(getSlideCurPos() < down + 50) {
                startTime = time;
            }
            slideTargetPosition = mid;
        } else if(gamepad1.y) {
            if(getSlideCurPos() < down + 50) {
                startTime = time;
            }
            slideTargetPosition = high;
        }
        if(time > startTime + extendDelay) {
            slideMotor.setTargetPosition(slideTargetPosition);
        }
        slideMotor.setPower(slidePower);
    }

    public void runDispenser(){
        if(slideMotor.getCurrentPosition() > boxMin) {
            boxServo.setPosition(boxUp);
        } else if(slideMotor.getCurrentPosition() <= slidesMin){
            boxServo.setPosition(boxDown);
        }

        if(getSlideCurPos() > slidesMin && getSlideTargetPos() > slidesMin){
            fourbarServo.setPosition(outtake);
        } else if(getSlideCurPos() <= boxMin){
            fourbarServo.setPosition(intake);
        }

        if(getSlideCurPos() > slideBoxRotMin && getSlideTargetPos() > slideBoxRotMin){
            boxRotServo.setPosition(boxRotOuttake);
        } else if(getSlideTargetPos() <= slideBoxRotMin){
            boxRotServo.setPosition(boxRotIntake);
        }

        if(gamepad1.dpad_left &&  getSlideCurPos() > slidesMin){
            dropServo.setPosition(drop);
        } else {
            dropServo.setPosition(hold);
        }
    }

    public void outtakeYellow(){
        dropServo.setPosition(hold);
        slideMotor.setTargetPosition(low);
        slideMotor.setPower(slidePower);
        timer.reset();
        if (slideMotor.getCurrentPosition() > slidesMin) {
            fourbarServo.setPosition(outtake);
            boxServo.setPosition(boxUp);
        }
        if (slideMotor.getCurrentPosition() >= low - 30) {
            dropServo.setPosition(drop);
            timer.reset();
        }
        if (timer.milliseconds() > 1000) {
            dropServo.setPosition(hold);
            slideMotor.setTargetPosition(down);
            fourbarServo.setPosition(intake);
            boxServo.setPosition(boxDown);
        }
        while(slideMotor.getCurrentPosition() > down + 20){}
    }

    public void autonExtend(){
        slideMotor.setTargetPosition(low);
        slideMotor.setPower(slidePower);
    }

    public void autonDispense(){

    }

    public void autonRetract(){

    }
    public int getSlideCurPos(){
        return slideMotor.getCurrentPosition();
    }

    public int getSlideTargetPos(){
        return slideMotor.getTargetPosition();
    }

    public void telemetry(Telemetry telemetry){
        telemetry.addData("Slide pos", getSlideCurPos());
        telemetry.addData("Slide target", getSlideTargetPos());
        telemetry.addData("Below slide min", getSlideCurPos() < slidesMin);
        telemetry.addData("fb", fourbarServo.getPosition());
        telemetry.addData("box", boxServo.getPosition());
        telemetry.addData("drop", dropServo.getPosition());
        telemetry.addData("box rot", boxRotServo.getPosition());
    }
}
