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
    //.2217 - pitch outtake
    DcMotorEx slideMotorL;
    Servo fourbarServo, pitchServo, rollServo, leftDropServo, rightDropServo;
    Gamepad gamepad1, gamepad2;
    int slideTargetPosition = 0;
    final int down = 0, low = 700, mid = 1200, high = 1800;
    final double slidePower = 0.8;
    public static double leftHold = .45, leftDrop = 1, rightHold = 0.35, rightDrop = 1, boxUp = .85,
            boxAlmostDown = .7, boxDown = .69, fourbarIntakeUp = .99, fourbarIntakeDown = 0.92, fourbarOuttake = 0.4, fourbarOuttakeDown = .7,
            fourbarMid = 0.65, rollVertical = 0.84, rollHorizontal = 0.24, rollLeft = 0, rollRight = .73; //box down = 0.16
    public static double extendDelay = 100, retractDelay = 100; //ms
    final int slidesMin = 600, boxMin = 400, fourbarMin = 1000, angledOuttakeSlideOffset = 100, fourbarDownSlideOffset = 500;
    public String outtakePos = "Horizontal", fourbarPos = "Out";
    public int pixelLayers = 0, pixelLayerTicks = 300, pixelLayerOffset = 400;
    ElapsedTime timer = new ElapsedTime(), timer2 = new ElapsedTime(), delay = new ElapsedTime(),
            clampTimer = new ElapsedTime(), outtakeDelay = new ElapsedTime();
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
        rollServo = hardwareMap.get(Servo.class, "BRS05");
        leftDropServo = hardwareMap.get(Servo.class, "DLS02");
        rightDropServo = hardwareMap.get(Servo.class, "DRS04");

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
//        boxRotServo.setPosition(boxRotIntake);
    }

    public void runSlides(){
//        slideTargetPosition += gamepad1.right_trigger * 50;
//        slideTargetPosition -= gamepad1.left_trigger * 50;
        if(time > startTime + extendDelay) {
            if(getSlideTargetPos() > down + 100 && (outtakePos.equals("Left") || outtakePos.equals("Right"))){
                slideMotorL.setTargetPosition(slideTargetPosition - angledOuttakeSlideOffset - ((fourbarPos.equals("Down")) ? fourbarDownSlideOffset:0));
            } else if(getSlideTargetPos() > down + 100 && fourbarPos.equals("Down")){
                slideMotorL.setTargetPosition(slideTargetPosition - fourbarDownSlideOffset);
            }
            else{
                slideMotorL.setTargetPosition(slideTargetPosition);
            }
//            slideMotorR.setTargetPosition(slideTargetPosition);
        }

        if(pixelLayers > 0) {
            slideMotorL.setTargetPosition(pixelLayers * pixelLayerTicks - pixelLayerOffset);
        } else{
            slideMotorL.setTargetPosition(down);
        }

        slideMotorL.setPower(slidePower);
//        slideMotorR.setPower(slidePower);
    }

    public void runSlidesPresets(){
        time = timer2.milliseconds();
//        if (gamepad1.a) {
//            if(getSlideCurPosL() < down + 50) {
//                startTime = time;
//            }
//            outtakePos = "Horizontal";
//            holdIn = false;
//            slideTargetPosition = down;
//        } else if(gamepad1.x){
//            if(getSlideCurPosL() < down + 50) {
//                startTime = time;
//                fbTime = delay.milliseconds();
//            }
//            fourbarPos = "Out";
//            outtakePos = "Horizontal";
//            slideTargetPosition = low;
//        } else if(gamepad1.b) {
//            if(getSlideCurPosL() < down + 50) {
//                startTime = time;
//                fbTime = delay.milliseconds();
//            }
//            fourbarPos = "Out";
//            outtakePos = "Horizontal";
//            slideTargetPosition = mid;
//        } else if(gamepad1.y) {
//            if(getSlideCurPosL() < down + 50) {
//                startTime = time;
//                fbTime = delay.milliseconds();
//            }
//            fourbarPos = "Out";
//            outtakePos = "Horizontal";
//            slideTargetPosition = high;
//        }
//        if(time > startTime + extendDelay) {
//            slideMotorL.setTargetPosition(slideTargetPosition);
//            slideMotorR.setTargetPosition(slideTargetPosition);
//        }
//        slideMotorL.setPower(slidePower);
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

        if(gamepad1.b && !debounce){
            if(pixelLayers == 0){
                if(getSlideCurPosL() < down + 50) {
                    startTime = time;
                    fbTime = delay.milliseconds();
                }
                fourbarPos = "Out";
                outtakePos = "Horizontal";
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
                outtakePos = "Horizontal";
                holdIn = false;
            }
            if(pixelLayers > 1) pixelLayers--;
            debounce2 = true;
        } else if(!gamepad1.b && debounce2){
            debounce2 = false;
        }

        if(gamepad1.a){
            pixelLayers = 0;
        }

        if(true){
            fourbarPos = "Out";
        } else if(true){
            fourbarPos = "Down";
        }

        if(getSlideCurPosL() > slidesMin && getSlideTargetPos() > slidesMin && fourbarPos.equals("Out")){
            fourbarServo.setPosition(fourbarOuttake);
        } else if(getSlideCurPosL() > slidesMin && getSlideTargetPos() > slidesMin && fourbarPos.equals("Down")){
            fourbarServo.setPosition(fourbarOuttakeDown);
        } else if(getSlideCurPosL() < slidesMin && getSlideTargetPos() > slidesMin){
            fourbarServo.setPosition(fourbarIntakeUp);
        } else if(getSlideCurPosL() >= fourbarMin && getSlideTargetPos() <= slidesMin){
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
        } else if(slideTargetPosition == down && getSlideCurPosL() < down + 100 && holdIn && clampTimer.milliseconds() > clampTime + 400){
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
        if(getSlideCurPosL() > boxMin && getSlideTargetPos() > down + 100 && outtakePos.equals("Horizontal")){
            rollServo.setPosition(rollHorizontal);
        } else if(getSlideCurPosL() > boxMin && getSlideTargetPos() > down + 100 && outtakePos.equals("Left")){
            rollServo.setPosition(rollLeft);
        } else if(getSlideCurPosL() > boxMin && getSlideTargetPos() > down + 100 && outtakePos.equals("Right")){
            rollServo.setPosition(rollRight);
        } else{
            rollServo.setPosition(rollVertical);
        }

        if(getSlideTargetPos() > down + 200 && getSlideCurPosL() < slidesMin/2.0){
            intake.setEmergencyOuttake(0.15);
        } else if(!gamepad2.dpad_left){
            intake.setEmergencyOuttake(0.41);
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
//            slideMotorR.setTargetPosition(down);
            fourbarServo.setPosition(fourbarIntakeDown);
            pitchServo.setPosition(boxDown);
            rollServo.setPosition(rollVertical);
        }
        while(slideMotorL.getCurrentPosition() > down + 20){}
    }

    public void autonExtend(){
        slideMotorL.setTargetPosition(low);
//        slideMotorR.setTargetPosition(low);
        slideMotorL.setPower(slidePower);
//        slideMotorR.setPower(slidePower);
    }

    public void autonFBOut(){
        fourbarServo.setPosition(fourbarOuttake);
    }

    public void autonFBIn(){
        fourbarServo.setPosition(fourbarIntakeDown);
    }

    public void autonDispense(){
        leftDropServo.setPosition(leftDrop);
        rightDropServo.setPosition(rightDrop);
    }

    public void autonRetract(){
        slideMotorL.setTargetPosition(down);
//        slideMotorR.setTargetPosition(down);
        slideMotorL.setPower(slidePower);
//        slideMotorR.setPower(slidePower);
    }

    public int getSlideCurPosL(){
        return slideMotorL.getCurrentPosition();
    }

    public int getSlideTargetPos(){
        return slideMotorL.getTargetPosition();
    }

    public void telemetry(Telemetry telemetry){
        telemetry.addData("Slide pos", getSlideCurPosL());
        telemetry.addData("Slide target", getSlideTargetPos());
        telemetry.addData("Below slide min", getSlideCurPosL() < slidesMin);
        telemetry.addData("fb", fourbarServo.getPosition());
        telemetry.addData("box", pitchServo.getPosition());
//        telemetry.addData("slide power", slideMotorL.getPower());
//        telemetry.addData("box rot", boxRotServo.getPosition());
        telemetry.addData("getSlideCurPosL() > boxMin", getSlideCurPosL() > boxMin);
        telemetry.addData("getSlideTargetPos() > down + 100", getSlideTargetPos() > down + 100);
    }
}
