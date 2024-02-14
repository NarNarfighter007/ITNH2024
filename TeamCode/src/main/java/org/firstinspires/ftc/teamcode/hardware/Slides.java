package org.firstinspires.ftc.teamcode.hardware;

import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.roadrunner.Action;
//import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Slides {
    DcMotorEx slideMotor;
    Servo fourbarServo, pitchServo, rollServo, leftDropServo, rightDropServo;
    Gamepad gamepad1, gamepad2;
    int slideTargetPosition = 0;
    final int down = 0, low = 1730, mid = 1980, high = 2300;
    final double slidePower = 0.8;
//    public static double hold = 0.18, drop = 0, boxUp = 0.47, boxDown = 0.334, boxRotIntake = 0.57, boxRotOuttake = 0,
//        intake = 0.97, outtake = 0;

    public static double leftHold = 0.1, leftDrop = 0.25, rightHold = 0.25, rightDrop = .1, boxUp = .565,
            boxMid = .5, boxDown = 0.385, boxRotIntake = 0, boxRotOuttake = .5, intake = 0.64, outtake = 0.27,
            rollVertical = 0, rollHorizontal = .5, rollLeft = .25, rollRight = .75; //box down = 0.16
    public static double extendDelay = 0;
    final int slidesMin = 1500;
    final int boxMin = 400;
    ElapsedTime timer = new ElapsedTime();
    ElapsedTime timer2 = new ElapsedTime();
    ElapsedTime delay = new ElapsedTime();
    boolean toggle = false, holdIn = false;
    double time, startTime, outtakeTime;
    public Slides(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

        slideMotor =  hardwareMap.get(DcMotorEx.class, "SLM01");
        fourbarServo = hardwareMap.get(Servo.class, "FBS00");
        pitchServo = hardwareMap.get(Servo.class, "OPS01");
        rollServo = hardwareMap.get(Servo.class, "ORS05");
        leftDropServo = hardwareMap.get(Servo.class, "DLS02");
        rightDropServo = hardwareMap.get(Servo.class, "DRS04");

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setTargetPosition(down);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDropServo.setPosition(leftHold);
        rightDropServo.setPosition(rightHold);
        fourbarServo.setPosition(intake);
        pitchServo.setPosition(boxDown);
//        boxRotServo.setPosition(boxRotIntake);
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
        outtakeTime = delay.milliseconds();
        if(slideMotor.getCurrentPosition() > boxMin && slideMotor.getTargetPosition() > slidesMin) {
            pitchServo.setPosition(boxUp);
        } else if(slideMotor.getCurrentPosition() <= boxMin){
            pitchServo.setPosition(boxDown);
        } else if(slideMotor.getTargetPosition() < boxMin){
            pitchServo.setPosition(boxMid);
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

        if(gamepad1.left_bumper && gamepad1.right_bumper && getSlideCurPos() > slidesMin){
            leftDropServo.setPosition(leftDrop);
            rightDropServo.setPosition(rightDrop);
        } else if(gamepad1.left_bumper && getSlideCurPos() > slidesMin){
            leftDropServo.setPosition(leftDrop);
            rightDropServo.setPosition(rightHold);
        } else if(gamepad1.right_bumper && getSlideCurPos() > slidesMin){
            leftDropServo.setPosition(leftHold);
            rightDropServo.setPosition(rightHold);
        } else if(slideTargetPosition > down || getSlideCurPos() > slidesMin){
            leftDropServo.setPosition(leftHold);
            rightDropServo.setPosition(rightHold);
        }
//        else if(holdIn && slideTargetPosition == down && getSlideCurPos() < slidesMin){
//            dropServo.setPosition(leftHold);
//        }
        else if(slideTargetPosition == down && getSlideCurPos() < slidesMin){
            leftDropServo.setPosition(leftDrop);
            rightDropServo.setPosition(rightDrop);
        }

        if(getSlideCurPos() > slidesMin && getSlideTargetPos() > down + 100){
            rollServo.setPosition(rollVertical);
        } else if(false){
            rollServo.setPosition(rollLeft);
        } else if(false){
            rollServo.setPosition(rollRight);
        } else{
            rollServo.setPosition(rollHorizontal);
        }
    }

    @Deprecated
    public void outtakeYellow(){
        leftDropServo.setPosition(leftHold);
        slideMotor.setTargetPosition(low);
        slideMotor.setPower(slidePower);
        timer.reset();
        if (slideMotor.getCurrentPosition() > slidesMin) {
            fourbarServo.setPosition(outtake);
            pitchServo.setPosition(boxUp);
            rollServo.setPosition(rollHorizontal);
        }
        if (slideMotor.getCurrentPosition() >= low - 30) {
            leftDropServo.setPosition(leftDrop);
            timer.reset();
        }
        if (timer.milliseconds() > 1000) {
            leftDropServo.setPosition(leftHold);
            slideMotor.setTargetPosition(down);
            fourbarServo.setPosition(intake);
            pitchServo.setPosition(boxDown);
            rollServo.setPosition(rollVertical);
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
        slideMotor.setTargetPosition(down);
        slideMotor.setPower(slidePower);
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
        telemetry.addData("box", pitchServo.getPosition());
//        telemetry.addData("box rot", boxRotServo.getPosition());
    }
}
