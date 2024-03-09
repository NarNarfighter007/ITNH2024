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

import java.util.Timer;
import java.util.TimerTask;

@Config
public class Slides {
    //.2217 - pitch outtake
    DcMotorEx slideMotorL;
    Servo fourbarServo, pitchServo, rollServo, leftDropServo, rightDropServo;
    Gamepad gamepad1, gamepad2;
    int slideTargetPosition = 0;
    final int down = 0, low = 610, mid = 1200, high = 1800;
    final double slidePower = 0.6;
    public static double leftHold = 0, leftDrop = 0.6, rightHold = 0, rightDrop = 0.6, boxUp = .27,
            boxAlmostDown = 0.073, boxDown = 0.073, fourbarIntakeUp = .83, fourbarIntakeDown = .8, fourbarOuttake = .07, fourbarOuttakeDown = .7,
            fourbarMid = .975, rollVertical = .95, rollHorizontal = .43, rollLeft = .12, rollRight = .77; //fb up=.4
    public static double extendDelay = 0, retractDelay = 300; //ms
    final int slidesMin = 510, boxMin = 400, fourbarMin = 510, angledOuttakeSlideOffset = 150, fourbarDownSlideOffset = 500, verticalOuttakeSlideOffset = 180;
    public String outtakePos = "Horizontal", fourbarPos = "Out";
    public int pixelLayers = 0, pixelLayerTicks = 315, pixelLayerOffset = 180;
    ElapsedTime timer = new ElapsedTime(), timer2 = new ElapsedTime(), delay = new ElapsedTime(),
            clampTimer = new ElapsedTime(), fbRetractDelayTimer = new ElapsedTime(), retractDelayTimer = new ElapsedTime();
    boolean toggle = false, debounce = false, debounce2 = false, holdIn = false;
    double time, startTime, fbTime, clampTime;
    Intake intake;
    public Slides(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        intake = new Intake(hardwareMap, gamepad1, gamepad2);
        slideMotorL =  hardwareMap.get(DcMotorEx.class, "SLM01");

        fourbarServo = hardwareMap.get(Servo.class, "FBS00");
        pitchServo = hardwareMap.get(Servo.class, "BXS01");
        rollServo = hardwareMap.get(Servo.class, "BRS05"); //BRS05
        leftDropServo = hardwareMap.get(Servo.class, "DLS02");
        rightDropServo = hardwareMap.get(Servo.class, "DRS04"); //DRS04

        slideMotorL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotorL.setTargetPosition(down);
        slideMotorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rollServo.setPosition(rollVertical);
        leftDropServo.setPosition(leftHold);
        rightDropServo.setPosition(rightHold);
        fourbarServo.setPosition(fourbarIntakeDown);
        pitchServo.setPosition(boxDown);
    }

    public void runSlides(){
        slideTargetPosition += gamepad2.right_trigger * 50;
        slideTargetPosition -= gamepad2.left_trigger * 50;
        if(time > startTime + extendDelay && slideTargetPosition > down+100) { //TODO: fix this its actually so bad
            if(outtakePos.equals("Left") || outtakePos.equals("Right")) {
                slideMotorL.setTargetPosition(slideTargetPosition - angledOuttakeSlideOffset - ((fourbarPos.equals("Down")) ? fourbarDownSlideOffset : 0));
            } else if(fourbarPos.equals("Down")){
                slideMotorL.setTargetPosition(slideTargetPosition - fourbarDownSlideOffset);
            } else{
                slideMotorL.setTargetPosition(slideTargetPosition);
            }
        } else if(slideTargetPosition == down && time > startTime + retractDelay){
            slideMotorL.setTargetPosition(slideTargetPosition);
        }

        slideMotorL.setPower(slidePower);
    }

    double startRetractTime = 0, retractTime = 0, startFBRetractTime;
    public void runSlidesPixelLayer(){
        slideMotorL.setPower(slidePower);
        retractTime = retractDelayTimer.milliseconds();
        if(pixelLayers > 0 && outtakePos == "Horizontal") {
            slideTargetPosition = pixelLayers * pixelLayerTicks + pixelLayerOffset;
            if(pixelLayers == 1) slideTargetPosition+=40;
        } else if(pixelLayers > 0 && (outtakePos == "Left" || outtakePos == "Right")){
            slideTargetPosition = pixelLayers * pixelLayerTicks + pixelLayerOffset + angledOuttakeSlideOffset;
            if(pixelLayers == 1) slideTargetPosition+=40;
        } else if(pixelLayers > 0 && outtakePos == "Vertical"){
            slideTargetPosition = pixelLayers * pixelLayerTicks + pixelLayerOffset + verticalOuttakeSlideOffset;
            if(pixelLayers == 1) slideTargetPosition+=40;
        } else{
            slideTargetPosition = down;
        }

        if(getSlideCurPosL() <= 700 && retractTime > startRetractTime + retractDelay && slideTargetPosition == down){
            slideMotorL.setTargetPosition(slideTargetPosition);
        } else if(getSlideCurPosL() > 700 || slideTargetPosition != down){
            slideMotorL.setTargetPosition(slideTargetPosition);
        }

        time = timer2.milliseconds();
        if (gamepad1.a) {
            if(outtakePos.equals("Left"))
                startFBRetractTime = fbRetractDelayTimer.milliseconds();
            startTime = time;
            outtakePos = "Horizontal";
            slideTargetPosition = down;
            pixelLayers = 0;
            startRetractTime = retractTime;
            holdIn = false;


        } else if(gamepad1.x){
            if(getSlideCurPosL() < down + 50) {
                startTime = time;
                fbTime = delay.milliseconds();
            }
            if(outtakePos.equals("Left"))
                startFBRetractTime = fbRetractDelayTimer.milliseconds();
            fourbarPos = "Out";
        } else if(gamepad1.b) {
            if(getSlideCurPosL() < down + 50) {
                startTime = time;
                fbTime = delay.milliseconds();
            }
            fourbarPos = "Out";
        }

        if(gamepad1.b && !debounce){
            if(pixelLayers == 0){
                if(getSlideCurPosL() < down + 50) {
                    startTime = time;
                    fbTime = delay.milliseconds();
                }
                fourbarPos = "Out";
            }
            if(pixelLayers <=10) pixelLayers++;
            debounce = true;
        } else if(!gamepad1.b && debounce){
            debounce = false;
        }

        if(gamepad1.x && !debounce2){
            if(pixelLayers == 1){
                fourbarPos = "Out";
                pixelLayers--;
                startRetractTime = retractTime;
            }
            if(pixelLayers > 1) pixelLayers--;
            debounce2 = true;
            holdIn = false;
        } else if(!gamepad1.x && debounce2){
            debounce2 = false;
        }
    }

    public void runSlidesPresets(){
        time = timer2.milliseconds();
        if (gamepad1.a) {
            startTime = time;
            outtakePos = "Horizontal";
            holdIn = false;
            slideTargetPosition = down;
        } else if(gamepad1.x){
            if(getSlideCurPosL() < down + 50) {
                startTime = time;
                fbTime = delay.milliseconds();
            }
            fourbarPos = "Out";
            outtakePos = "Horizontal";
            slideTargetPosition = low;
        } else if(gamepad1.b) {
            if(getSlideCurPosL() < down + 50) {
                startTime = time;
                fbTime = delay.milliseconds();
            }
            fourbarPos = "Out";
            outtakePos = "Horizontal";
            slideTargetPosition = mid;
        } else if(gamepad1.y) {
            if(getSlideCurPosL() < down + 50) {
                startTime = time;
                fbTime = delay.milliseconds();
            }
            fourbarPos = "Out";
            outtakePos = "Horizontal";
            slideTargetPosition = high;
        }
        slideMotorL.setPower(slidePower);
    }

    public void runDispenser(){
        if(slideMotorL.getCurrentPosition() > boxMin && slideMotorL.getTargetPosition() > slidesMin) {
            pitchServo.setPosition(boxUp);
        } else if(slideMotorL.getCurrentPosition() <= boxMin && holdIn && getSlideTargetPos() == down){
            pitchServo.setPosition(boxDown);
        } else if(slideMotorL.getCurrentPosition() <= boxMin && holdIn && getSlideTargetPos() > down){
            pitchServo.setPosition(boxAlmostDown);
        } else if(slideMotorL.getCurrentPosition() <= boxMin && !holdIn){
            pitchServo.setPosition(boxAlmostDown);
        }

        if(gamepad1.dpad_up && !toggle){
            holdIn = !holdIn;
            clampTime = clampTimer.milliseconds();
            toggle = true;
        } else if(!gamepad1.dpad_up && toggle){
            toggle = false;
        }

        if(true){
            fourbarPos = "Out";
        } else if(false){
            fourbarPos = "Down";
        }

        if(getSlideCurPosL() > slidesMin && slideTargetPosition > slidesMin && fourbarPos.equals("Out")){
            fourbarServo.setPosition(fourbarOuttake);
        } else if(getSlideCurPosL() > slidesMin && slideTargetPosition > slidesMin && fourbarPos.equals("Down")){
            fourbarServo.setPosition(fourbarOuttakeDown);
        } else if(getSlideCurPosL() < slidesMin && slideTargetPosition > slidesMin){
            fourbarServo.setPosition(fourbarIntakeUp);
        } else if(getSlideCurPosL() >= fourbarMin && slideTargetPosition <= slidesMin && fbRetractDelayTimer.milliseconds() > startFBRetractTime + 200 ){
            fourbarServo.setPosition(fourbarMid);
        } else if(getSlideTargetPos() <= slidesMin && holdIn && getSlideCurPosL() < fourbarMin){
            fourbarServo.setPosition(fourbarIntakeDown);
        } else if(getSlideTargetPos() <= slidesMin && !holdIn && getSlideCurPosL() < fourbarMin){
            fourbarServo.setPosition(fourbarIntakeUp);
        }

        if(gamepad1.left_bumper && gamepad1.right_bumper && getSlideCurPosL() > slidesMin){
            leftDropServo.setPosition(leftDrop);
            rightDropServo.setPosition(rightDrop);
        } else if(gamepad1.left_bumper && getSlideCurPosL() > slidesMin){
            leftDropServo.setPosition(leftDrop);
            rightDropServo.setPosition(rightHold);
        } else if(gamepad1.right_bumper && getSlideCurPosL() > slidesMin){
            leftDropServo.setPosition(leftHold);
            rightDropServo.setPosition(rightDrop);
        } else if(slideTargetPosition == down && getSlideCurPosL() < down + 100 && holdIn && clampTimer.milliseconds() > clampTime + 600){
            leftDropServo.setPosition(leftHold);
            rightDropServo.setPosition(rightHold);
        } else if(slideTargetPosition == down && getSlideCurPosL() < down + 100 && !holdIn){
            leftDropServo.setPosition(leftDrop);
            rightDropServo.setPosition(rightDrop);
        } else if(slideTargetPosition > down || getSlideCurPosL() > slidesMin){
            leftDropServo.setPosition(leftHold);
            rightDropServo.setPosition(rightHold);
        }

        else if(slideTargetPosition == down && getSlideCurPosL() < slidesMin){
            leftDropServo.setPosition(leftDrop);
            rightDropServo.setPosition(rightDrop);
        }

        if(getSlideCurPosL() > boxMin && slideTargetPosition > down + 100 && outtakePos.equals("Horizontal")){
            rollServo.setPosition(rollHorizontal);
        } else if(getSlideCurPosL() > boxMin && slideTargetPosition > down + 100 && outtakePos.equals("Left")){
            rollServo.setPosition(rollLeft);
        } else if(getSlideCurPosL() > boxMin && slideTargetPosition > down + 100 && outtakePos.equals("Right")){
            rollServo.setPosition(rollRight);
        } else{
            rollServo.setPosition(rollVertical);
        }

        if(gamepad2.a){
            outtakePos = "Horizontal";
        } else if(gamepad2.x){
            outtakePos = "Left";
        } else if(gamepad2.b){
            outtakePos = "Right";
        } else if(gamepad2.y){
            outtakePos = "Vertical";
        }
    }

    public void autonExtend(){
        slideMotorL.setTargetPosition(pixelLayerTicks + pixelLayerOffset + 120);
        slideMotorL.setPower(slidePower);
    }

    public void autonExtendHigh(){
        slideMotorL.setTargetPosition(pixelLayerTicks * 2 + pixelLayerOffset + 120);
        slideMotorL.setPower(slidePower);
    }

    public void autonRetract(){
        slideMotorL.setTargetPosition(down);
        slideMotorL.setPower(slidePower);
    }

    public void autonFBOuttake(){
        fourbarServo.setPosition(fourbarOuttake);
    }

    public void autonFBIntakeDown(){
        fourbarServo.setPosition(fourbarIntakeDown);
    }

    public void autonFBIntakeUp(){
        fourbarServo.setPosition(fourbarIntakeUp);
    }

    public void autonDispense(){
        leftDropServo.setPosition(leftDrop);
        rightDropServo.setPosition(rightDrop);
    }

    public void autonGrab(){
        leftDropServo.setPosition(leftHold);
        rightDropServo.setPosition(rightHold);
    }

    public void autonPitchUp(){
        pitchServo.setPosition(boxUp);
    }

    public void autonPitchDown(){
        pitchServo.setPosition(boxDown);
    }

    public void autonPitchHover(){
        pitchServo.setPosition(boxAlmostDown);
    }

    public void autonRollVertical(){
        rollServo.setPosition(rollVertical);
    }

    public void autonRollHorizontal(){
        rollServo.setPosition(rollHorizontal);
    }
    public void autonClamp(int delayMillis){
        Timer clampSequence = new Timer();
        clampSequence.schedule(clamp, 0 + delayMillis);
        clampSequence.schedule(clampGrab, 200 + delayMillis);
    }

    TimerTask clamp = new TimerTask() {
        @Override
        public void run() {
            autonFBIntakeDown(); autonPitchDown();
        }
    };
    TimerTask clampGrab = new TimerTask() {
        @Override
        public void run() {
            autonGrab();
        }
    };

    TimerTask grab = new TimerTask() {
        @Override
        public void run() {
            autonGrab();
        }
    };

    TimerTask grab2 = new TimerTask() {
        @Override
        public void run() {
            autonGrab();
        }
    };

    public void autonOuttakeSequence1(int delayMillis){
        Timer outtakeSequence = new Timer();

        outtakeSequence.schedule(grab, delayMillis);
        outtakeSequence.schedule(hover, 100 + delayMillis);
        outtakeSequence.schedule(extend, 400 + delayMillis);
        outtakeSequence.schedule(fbOut, 1000 + delayMillis);
        outtakeSequence.schedule(dispense, 3000 + delayMillis);
        //back up
        outtakeSequence.schedule(fbIntakeUp, 4000 + delayMillis);
        outtakeSequence.schedule(pitchAlmostDown, 4300 + delayMillis);
        outtakeSequence.schedule(retract, 4600 + delayMillis);
        outtakeSequence.schedule(open, 4600 + delayMillis);
    }
    TimerTask hover = new TimerTask() {
        @Override
        public void run() {
            autonPitchHover();autonFBIntakeUp();
        }
    };
    TimerTask extend = new TimerTask(){
        @Override
        public void run() {
            autonExtend();
        }
    };
    TimerTask fbOut = new TimerTask() {
        @Override
        public void run() {
            autonFBOuttake();autonPitchUp();
        }
    };
    TimerTask dispense = new TimerTask(){

        @Override
        public void run() {
            autonDispense();
        }
    };
    TimerTask fbIntakeUp = new TimerTask(){

        @Override
        public void run() {
            autonFBIntakeUp();
        }
    };
    TimerTask pitchAlmostDown = new TimerTask() {
        @Override
        public void run() {
            autonPitchHover();
        }
    };
    TimerTask retract = new TimerTask() {
        @Override
        public void run() {
            autonRetract();
        }
    };
    TimerTask open = new TimerTask(){

        @Override
        public void run() {
            autonDispense();
        }
    };

    public void autonOuttakeSequence2(int delayMillis){
        Timer outtakeSequence2 = new Timer();

        outtakeSequence2.schedule(grab2, 200 + delayMillis);
        outtakeSequence2.schedule(hover2, 300 + delayMillis);
        outtakeSequence2.schedule(extend2, 600 + delayMillis);
        outtakeSequence2.schedule(fbOut2, 1000 + delayMillis);
        outtakeSequence2.schedule(dispense2, 3000 + delayMillis);
        //back up
        outtakeSequence2.schedule(fbIntakeUp2, 5500 + delayMillis);
        outtakeSequence2.schedule(pitchAlmostDown2, 5700 + delayMillis);
        outtakeSequence2.schedule(retract2, 5900 + delayMillis);
    }
    TimerTask open2 = new TimerTask(){

        @Override
        public void run() {
            autonDispense();
        }
    };

    TimerTask hover2 = new TimerTask() {
        @Override
        public void run() {
            autonPitchHover();autonFBIntakeUp();
        }
    };
    TimerTask extend2 = new TimerTask(){
        @Override
        public void run() {
            autonExtend();
        }
    };
    TimerTask fbOut2 = new TimerTask() {
        @Override
        public void run() {
            autonFBOuttake();autonPitchUp();
        }
    };
    TimerTask dispense2 = new TimerTask(){

        @Override
        public void run() {
            autonDispense();
        }
    };
    TimerTask fbIntakeUp2 = new TimerTask(){

        @Override
        public void run() {
            autonFBIntakeUp();
        }
    };
    TimerTask pitchAlmostDown2 = new TimerTask() {
        @Override
        public void run() {
            autonPitchHover();
        }
    };
    TimerTask retract2 = new TimerTask() {
        @Override
        public void run() {
            autonRetract();
        }
    };

    public void autonOuttakeSequenceHorizontal(int delayMillis){
        Timer outtakeSequence = new Timer();

        outtakeSequence.schedule(grab, delayMillis);
        outtakeSequence.schedule(hover, 100 + delayMillis);
        outtakeSequence.schedule(extendHigh, 400 + delayMillis);
        outtakeSequence.schedule(fbOut, 1000 + delayMillis);
        outtakeSequence.schedule(horizontal, 1400 + delayMillis);
        outtakeSequence.schedule(dispense, 3000 + delayMillis);
        //back up
        outtakeSequence.schedule(vertical, 3300 + delayMillis);
        outtakeSequence.schedule(fbIntakeUp, 3500 + delayMillis);
        outtakeSequence.schedule(pitchAlmostDown, 3800 + delayMillis);
        outtakeSequence.schedule(retract, 4100 + delayMillis);
        outtakeSequence.schedule(open, 4100 + delayMillis);
    }

    TimerTask extendHigh = new TimerTask() {
        @Override
        public void run() {
            autonExtendHigh();
        }
    };
    TimerTask horizontal = new TimerTask() {
        @Override
        public void run() {
            rollServo.setPosition(rollHorizontal);
        }
    };
    TimerTask vertical = new TimerTask() {
        @Override
        public void run() {
            rollServo.setPosition(rollVertical);
        }
    };
    public int getSlideCurPosL(){
        return slideMotorL.getCurrentPosition();
    }

    public int getSlideTargetPos(){
        return slideMotorL.getTargetPosition();
    }

    public void telemetry(Telemetry telemetry){
//        telemetry.addData("time > startTime + retractDelay", time > startTime + retractDelay);
//        telemetry.addData("time", time);
//        telemetry.addData("startTime", startTime);
        telemetry.addData("Slide pos", getSlideCurPosL());
        telemetry.addData("Slide target", getSlideTargetPos());
        telemetry.addData("Pixel Level", pixelLayers);
//        telemetry.addData("Below slide min", getSlideCurPosL() < slidesMin);
//        telemetry.addData("fb", fourbarServo.getPosition());
//        telemetry.addData("box", pitchServo.getPosition());
//        telemetry.addData("slide power", slideMotorL.getPower());
//        telemetry.addData("outtakePos", outtakePos);
//        telemetry.addData("roll", rollServo.getPosition());
    }
}
