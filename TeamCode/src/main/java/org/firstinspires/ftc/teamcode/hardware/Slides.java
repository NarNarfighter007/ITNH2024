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
    Servo dropServo, fourbarServo, boxServo;
    Gamepad gamepad1, gamepad2;
    int slideTargetPosition = 0;
    final int down = 0, low = 1730, mid = 1980, high = 2300;
    final double slidePower = 0.8;
//    final double hold = 0.68, drop = 1.0, box = 0.4, intake = 0.90, outtake = 0.51; //outtake = 0.21
    public static double hold = 0.2, drop = 0.3, open = 0.3, boxUp = .31, boxDown = 0.15, intake = .66, outtake = 0.26; //box down = 0.16
    public static double extendDelay = 0;
    final int slidesMin = 881;
    final int boxMin = 400;
    ElapsedTime timer = new ElapsedTime();
    ElapsedTime timer2 = new ElapsedTime();
    boolean toggle = false, holdIn = false;
    double time, startTime;
    public Slides(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

        slideMotor =  hardwareMap.get(DcMotorEx.class, "SLM01");
        dropServo = hardwareMap.get(Servo.class, "PDS02");
        fourbarServo = hardwareMap.get(Servo.class, "FBS00");
        boxServo = hardwareMap.get(Servo.class, "BRS01");

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setTargetPosition(down);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        dropServo.setPosition(open);
        fourbarServo.setPosition(intake);
        boxServo.setPosition(boxDown);
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
            holdIn = false;
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
        } else if(slideMotor.getCurrentPosition() <= boxMin){
            boxServo.setPosition(boxDown);
        }

        if(gamepad1.dpad_up && !toggle){
            holdIn = !holdIn;
            toggle = true;
        } else if(!gamepad1.dpad_up && toggle){
            toggle = false;
        }

        if(getSlideCurPos() > slidesMin && getSlideTargetPos() > slidesMin){
            fourbarServo.setPosition(outtake);
        } else if(getSlideTargetPos() <= slidesMin){
            fourbarServo.setPosition(intake);
        }

        if(gamepad1.dpad_left &&  getSlideCurPos() > slidesMin){
            dropServo.setPosition(drop);
        } else if(slideTargetPosition > down || getSlideCurPos() > slidesMin){
            dropServo.setPosition(hold);
        } else if(holdIn && slideTargetPosition == down && getSlideCurPos() < slidesMin){
            dropServo.setPosition(hold);
        } else if(slideTargetPosition == down && getSlideCurPos() < slidesMin){
            dropServo.setPosition(open);
        }
    }

    @Deprecated
    public void outtakeYellow(){
        slideMotor.setTargetPosition(low);
        slideMotor.setPower(slidePower);
        dropServo.setPosition(hold);
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
            boxServo.setPosition(intake);
            boxServo.setPosition(boxDown);
        }
        while(timer.milliseconds() < 7000){}
    }

    public void autonExtend(){

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
    }
//    public class Dispense implements Action{
//        boolean dispensed = false;
//        public void init(){
//            slideMotor.setPower(slidePower);
//            boxServo.setPosition(boxDown);
//        }
//
//        @Override
//        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
//            slideMotor.setTargetPosition(low);
//            telemetryPacket.put("Slide pos", slideMotor.getCurrentPosition());
//
//            if(slideMotor.getCurrentPosition() == low){
//                fourbarServo.setPosition(outtake);
//                dropServo.setPosition(drop);
//                dispensed = true;
//            }
//            return !dispensed;
//        }
//    }
//
//    public class Retract implements Action{
//        boolean retracted = false;
//        public void init(){
//            fourbarServo.setPosition(intake);
//            dropServo.setPosition(hold);
//            boxServo.setPosition(boxUp);
//        }
//        @Override
//        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
//            slideMotor.setPower(slidePower);
//            slideMotor.setTargetPosition(down);
//            if(slideMotor.getCurrentPosition() < boxMin){
//                boxServo.setPosition(boxDown);
//            }
//            if(slideMotor.getCurrentPosition() < 10){
//                retracted = true;
//            }
//            return !retracted;
//        }
//    }
//    public Action dispense(){
//        return new Dispense();
//    }
//
//    public Action retract(){
//        return new Retract();
//    }
}
