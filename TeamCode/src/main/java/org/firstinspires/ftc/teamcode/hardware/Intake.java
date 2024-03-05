package org.firstinspires.ftc.teamcode.hardware;

//import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Timer;
import java.util.TimerTask;

public class Intake {
    DcMotor intakeMotor;
    CRServo transferServo, conveyorServo;
    Servo intakeFlipServo, stackIntakeL, stackIntakeR;
    Gamepad gamepad1, gamepad2;
    ElapsedTime timer = new ElapsedTime();
    int intakePos = 0, intakePos2;
    public double intakeFlipUp = .73, intakeFlipDown = 0.1, emergencyClosed = .65, emergencyOpen = 0.11;
    double intakeLOut = .65, intakeLIn = 0,  intakeROut = .15, intakeRIn = .8;
    boolean intakingTwo = false, transferringTwo = false, intakingStack = false;
    final double intakePower = 0.8, transferPower = 1.0;
    double time, startTime;
    final int outakePreloadTicks = 400;
    boolean toggle = false, flipDown = false, debounce = false;
    public Intake(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        intakeMotor = hardwareMap.get(DcMotor.class, "INM11");
        transferServo = hardwareMap.get(CRServo.class, "TNS10");
        conveyorServo = hardwareMap.get(CRServo.class, "CVS15");
        intakeFlipServo = hardwareMap.get(Servo.class, "FUS14");
        stackIntakeL = hardwareMap.get(Servo.class, "ILS13");
        stackIntakeR = hardwareMap.get(Servo.class, "IRS03");

        transferServo.setDirection(DcMotorSimple.Direction.REVERSE);
        conveyorServo.setDirection(DcMotorSimple.Direction.REVERSE);

        intakeFlipServo.setPosition(intakeFlipUp);
        stackIntakeL.setPosition(intakeLOut);
        stackIntakeR.setPosition(intakeROut);
    }

    public void runIntake(){
//        if(gamepad1.right_bumper) {
//            intakeMotor.setPower(intakePower);
//        } else if(gamepad1.right_trigger > 0.2){
//            intakeMotor.setPower(-intakePower);
//        } else if(!intakingTwo){
//            intakeMotor.setPower(0);
//        }
//
//        if(gamepad1.left_bumper){
//            transferServo.setPower(transferPower);
//        } else if(gamepad1.left_trigger > 0.2){
//            transferServo.setPower(-transferPower);
//        } else if(!transferringTwo){
//            transferServo.setPower(0);
//        }
        if(gamepad1.right_trigger > 0.2){
            intakeMotor.setPower(intakePower);
            transferServo.setPower(transferPower);
            conveyorServo.setPower(transferPower);
        } else if(gamepad1.left_trigger > 0.2){
            intakeMotor.setPower(-intakePower);
            transferServo.setPower(-transferPower);
//            conveyorServo.setPower(-transferPower);
        } else if(!intakingTwo){
            intakeMotor.setPower(0);
            transferServo.setPower(0);
            conveyorServo.setPower(0);
        }
        stackIntake();
        time = timer.milliseconds();
    }

    public void stackIntake(){
        if(gamepad1.dpad_down && !toggle){
            flipDown = !flipDown;
            toggle = true;
        } else if(!gamepad1.dpad_down && toggle){
            toggle = false;
        }

        if(flipDown){
            intakeFlipServo.setPosition(intakeFlipDown);
        } else if(!flipDown){
            intakeFlipServo.setPosition(intakeFlipUp);
        }

        if(gamepad1.dpad_right && !debounce){
            intakingStack = true;
            startTime = time;
            debounce = false;
        } else if(!gamepad1.dpad_right && debounce){
            debounce = true;
        }

        if(intakingStack){
            stackIntakeR.setPosition(intakeRIn);
            stackIntakeL.setPosition(intakeLIn);
            if(time >= startTime + 200){
                stackIntakeR.setPosition(intakeROut);
                stackIntakeL.setPosition(intakeLOut);
                intakingStack = false;
            }
        }
    }

    public void flipDown(){
        intakeFlipServo.setPosition(intakeFlipDown);
    }

    public void flipUp(){
        intakeFlipServo.setPosition(intakeFlipDown);
    }
    @Deprecated
    public void intakeOne(){
        if(intakeFlipServo.getPosition() == intakeFlipDown){
            stackIntakeL.setPosition(intakeLIn);
            stackIntakeR.setPosition(intakeRIn);
            timer.reset();
            while(timer.milliseconds() < 200){}
            stackIntakeL.setPosition(intakeLOut);
            stackIntakeR.setPosition(intakeROut);
        }
    }

    public void autonIntakeFlipDown(){
        intakeFlipServo.setPosition(intakeFlipDown);
    }

    public void autonIntakeFlipUp(){
        intakeFlipServo.setPosition(intakeFlipUp);
    }

    public void autonIntakeIn(){
        stackIntakeL.setPosition(intakeLIn);
        stackIntakeR.setPosition(intakeRIn);
    }

    public void autonIntakeOut(){
        stackIntakeL.setPosition(intakeLOut);
        stackIntakeR.setPosition(intakeROut);
    }

    public void startIntake(){
        intakeMotor.setPower(intakePower);
        transferServo.setPower(transferPower);
    }

    public void stopIntake(){
        intakeMotor.setPower(0);
        transferServo.setPower(0);
    }
    public void startReverseIntake(){
        intakeMotor.setPower(-0.2);
    }

    public void stopReverseIntake(){
        intakeMotor.setPower(0);
    }

    public void intakeTwo(){
        Timer intakeTwoTimer = new Timer();

        intakeTwoTimer.schedule(intakeIn1, 0);
        intakeTwoTimer.schedule(intakeOut1, 700);
        intakeTwoTimer.schedule(intakeIn2, 1400);
        intakeTwoTimer.schedule(intakeOut2, 2100);
    }

    TimerTask intakeIn1 = new TimerTask() {
        @Override
        public void run() {
            autonIntakeIn();
        }
    };
    TimerTask intakeIn2 = new TimerTask() {
        @Override
        public void run() {
            autonIntakeIn();
        }
    };
    TimerTask intakeOut1 = new TimerTask() {
        @Override
        public void run() {
            autonIntakeOut();
        }
    };
    TimerTask intakeOut2 = new TimerTask() {
        @Override
        public void run() {
            autonIntakeOut();
        }
    };

    public void setEmergencyOuttake(double pos){
//        emergencyOuttake.setPosition(pos);
    }
    public void telemetry(Telemetry telemetry){
//        telemetry.addData("intake position", intakeMotor.getCurrentPosition());
        telemetry.addData("flip pos", intakeFlipServo.getPosition());
//        telemetry.addData("intake stack", intakingStack);
        telemetry.addData("flip down", flipDown);
//        telemetry.addData("startTime", startTime);
//        telemetry.addData("time", time);
//        telemetry.addData("flipDown", flipDown);
    }

}

