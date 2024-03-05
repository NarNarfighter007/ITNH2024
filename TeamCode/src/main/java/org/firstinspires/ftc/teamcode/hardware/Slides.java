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
            clampTimer = new ElapsedTime(), outtakeDelay = new ElapsedTime(), retractDelayTimer = new ElapsedTime();
    boolean toggle = false, debounce = false, debounce2 = false, holdIn = false;
    double time, startTime, fbTime, clampTime;
    Intake intake;
    public Slides(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        intake = new Intake(hardwareMap, gamepad1, gamepad2);
        slideMotorL =  hardwareMap.get(DcMotorEx.class, "SLM01");
//        slideMotorR =  hardwareMap.get(DcMotorEx.class, "RLM01");
        fourbarServo = hardwareMap.get(Servo.class, "FBS00");
        pitchServo = hardwareMap.get(Servo.class, "BXS01");
        rollServo = hardwareMap.get(Servo.class, "BRS05"); //BRS05
        leftDropServo = hardwareMap.get(Servo.class, "DLS02");
        rightDropServo = hardwareMap.get(Servo.class, "DRS04"); //DRS04

        slideMotorL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotorL.setTargetPosition(down);
        slideMotorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);

//        slideMotorR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        slideMotorR.setTargetPosition(down);
//        slideMotorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDropServo.setPosition(leftHold);
        rightDropServo.setPosition(rightHold);
        fourbarServo.setPosition(fourbarIntakeDown);
        pitchServo.setPosition(boxDown);
    }

    public void runSlides(){
//        slideTargetPosition += gamepad1.right_trigger * 50;
//        slideTargetPosition -= gamepad1.left_trigger * 50;
        if(time > startTime + extendDelay && slideTargetPosition > down+100) { //TODO: fix this its actually so bad
            if(outtakePos.equals("Left") || outtakePos.equals("Right")) {
                slideMotorL.setTargetPosition(slideTargetPosition - angledOuttakeSlideOffset - ((fourbarPos.equals("Down")) ? fourbarDownSlideOffset : 0));
            } else if(fourbarPos.equals("Down")){
                slideMotorL.setTargetPosition(slideTargetPosition - fourbarDownSlideOffset);
            } else{
                slideMotorL.setTargetPosition(slideTargetPosition);
            }
//            else{
//                slideMotorL.setTargetPosition(slideTargetPosition);
//            }
        } else if(slideTargetPosition == down && time > startTime + retractDelay){
            slideMotorL.setTargetPosition(slideTargetPosition);
        }

        slideMotorL.setPower(slidePower);
    }

    double startRetractTime = 0, retractTime = 0;
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
            startTime = time;
            outtakePos = "Horizontal";
            slideTargetPosition = down;
            pixelLayers = 0;
            startRetractTime = retractTime;
        } else if(gamepad1.x){
            if(getSlideCurPosL() < down + 50) {
                startTime = time;
                fbTime = delay.milliseconds();
            }
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
                holdIn = false;
            }
            if(pixelLayers <=10) pixelLayers++;
            debounce = true;
        } else if(!gamepad1.b && debounce){
            debounce = false;
        }

        if(gamepad1.x && !debounce2){
            if(pixelLayers == 1){
                fourbarPos = "Out";
                holdIn = false;
                pixelLayers--;
                startRetractTime = retractTime;
            }
            if(pixelLayers > 1) pixelLayers--;
            debounce2 = true;
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
//        if(time > startTime + extendDelay && getSlideTargetPos() > down) {
//            slideMotorL.setTargetPosition(slideTargetPosition);
////            slideMotorR.setTargetPosition(slideTargetPosition);
//        } else if(time > startTime + retractDelay && getSlideTargetPos() == down){
//            slideMotorL.setTargetPosition(slideTargetPosition);
//        }
        slideMotorL.setPower(slidePower);
//        slideMotorR.setPower(slidePower);
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
//        else if(slideMotor.getTargetPosition() < boxMin){
//            pitchServo.setPosition(boxMid);
//        }

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
        } else if(getSlideCurPosL() >= fourbarMin && slideTargetPosition <= slidesMin){
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
//        else if(holdIn && slideTargetPosition == down && getSlideCurPos() < slidesMin){
//            dropServo.setPosition(leftHold);
//        }
        else if(slideTargetPosition == down && getSlideCurPosL() < slidesMin){
            leftDropServo.setPosition(leftDrop);
            rightDropServo.setPosition(rightDrop);
        }
// && delay.milliseconds() > fbTime + 200
        if(getSlideCurPosL() > boxMin && slideTargetPosition > down + 100 && outtakePos.equals("Horizontal")){
            rollServo.setPosition(rollHorizontal);
        } else if(getSlideCurPosL() > boxMin && slideTargetPosition > down + 100 && outtakePos.equals("Left")){
            rollServo.setPosition(rollLeft);
        } else if(getSlideCurPosL() > boxMin && slideTargetPosition > down + 100 && outtakePos.equals("Right")){
            rollServo.setPosition(rollRight);
        } else{
            rollServo.setPosition(rollVertical);
        }

//        if(getSlideTargetPos() > down + 200 && getSlideCurPosL() < slidesMin){
//            intake.setEmergencyOuttake(0.11);
//        } else if(!gamepad2.dpad_left){
//            intake.setEmergencyOuttake(.65);
//        } else if(gamepad2.dpad_left){
//            intake.setEmergencyOuttake(0.11);
//        }

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

    @Deprecated
    public void outtakeYellow(){
        leftDropServo.setPosition(leftHold);
        slideMotorL.setTargetPosition(low);
//        slideMotorR.setTargetPosition(low);
        slideMotorL.setPower(slidePower);
//        slideMotorR.setPower(slidePower);
        timer.reset();
        if (slideMotorL.getCurrentPosition() > slidesMin) {
            fourbarServo.setPosition(fourbarOuttake);
            pitchServo.setPosition(boxUp);
            rollServo.setPosition(rollHorizontal);
        }
        if (slideMotorL.getCurrentPosition() >= low - 30) {
            leftDropServo.setPosition(leftDrop);
            timer.reset();
        }
        if (timer.milliseconds() > 1000) {
            leftDropServo.setPosition(leftHold);
            slideMotorL.setTargetPosition(down);
            fourbarServo.setPosition(fourbarIntakeDown);
            pitchServo.setPosition(boxDown);
            rollServo.setPosition(rollVertical);
        }
        while(slideMotorL.getCurrentPosition() > down + 20){}
    }

    public void autonExtend(){
        slideMotorL.setTargetPosition(low);
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

    public void autonClamp(){
        Timer clampSequence = new Timer();
        clampSequence.schedule(clamp, 0);
        clampSequence.schedule(grab, 200);
    }

    TimerTask clamp = new TimerTask() {
        @Override
        public void run() {
            autonFBIntakeDown(); autonPitchDown();
        }
    };
    TimerTask grab = new TimerTask() {
        @Override
        public void run() {
            autonGrab();
        }
    };

    public void outtakeSequence(){
        Timer outtakeSequence = new Timer();

        outtakeSequence.schedule(grab, 1);
        outtakeSequence.schedule(hover, 100);
        outtakeSequence.schedule(extend, 200);
        outtakeSequence.schedule(fbOut, 600);
        outtakeSequence.schedule(dispense, 1200);
        //back up
        outtakeSequence.schedule(fbIntakeUp, 2200);
        outtakeSequence.schedule(pitchAlmostDown, 2400);
        outtakeSequence.schedule(retract, 2600);
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
