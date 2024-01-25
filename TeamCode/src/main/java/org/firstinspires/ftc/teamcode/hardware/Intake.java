package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
//import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {
    DcMotor intakeMotor;
    CRServo transferServo;
    Servo stackIntakeFlip, stackIntakeL, stackIntakeR;
    Gamepad gamepad1, gamepad2;
    ElapsedTime timer = new ElapsedTime();
    int intakePos = 0, intakePos2;
    double intakeFlipUp = .95, intakeFlipDown = .53;
    double intakeLOut = 1, intakeLIn = 0, intakeROut = 0, intakeRIn = 1;
    boolean intakingTwo = false, transferringTwo = false, intakingStack;
    final double intakePower = 0.8, transferPower = 1.0;
    double time;
    final int outakePreloadTicks = 400;
    public Intake(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        intakeMotor = hardwareMap.get(DcMotor.class, "INM11");
        transferServo = hardwareMap.get(CRServo.class, "TNS10");
        stackIntakeFlip = hardwareMap.get(Servo.class, "FUS14");
        stackIntakeL = hardwareMap.get(Servo.class, "ILS13");
        stackIntakeR = hardwareMap.get(Servo.class, "IRS15");

        stackIntakeFlip.setPosition(intakeFlipUp);
        stackIntakeL.setPosition(intakeLIn);
        stackIntakeR.setPosition(intakeRIn);
        transferServo.setDirection(DcMotorSimple.Direction.REVERSE);
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
        if(gamepad1.right_bumper){
            intakeMotor.setPower(intakePower);
            transferServo.setPower(transferPower);
        } else if(gamepad1.left_bumper){
            intakeMotor.setPower(-intakePower);
            transferServo.setPower(-transferPower);
        } else if(!intakingTwo){
            intakeMotor.setPower(0);
            transferServo.setPower(0);
        }

        time = timer.milliseconds();
    }

    public void stackIntake(){
        if(gamepad1.dpad_down){
            stackIntakeFlip.setPosition(intakeFlipDown);
        } else if(gamepad1.dpad_up){
            stackIntakeFlip.setPosition(intakeFlipUp);
        }

        if(gamepad1.dpad_left){
            intakingStack = true;
            double startTime = time;
        }

        if(intakingStack){

        }
    }
    @Deprecated
    public void intakeTwo(){
        if(gamepad2.a && !transferringTwo){
            intakeMotor.setPower(intakePower);
            intakePos = intakeMotor.getCurrentPosition();
            intakingTwo = true;
//            transferServo.setPower(transferPower);
            transferringTwo = true;
            timer.reset();
        }
        if(intakeMotor.getCurrentPosition()-intakePos > 380 && intakingTwo){
            intakeMotor.setPower(-intakePower);
            intakePos2 = intakeMotor.getCurrentPosition();
            intakingTwo = false;
        }
        if(!intakingTwo && transferringTwo){
            if(intakeMotor.getCurrentPosition() - intakePos2 < 25){
                intakeMotor.setPower(0);
            }
        }
        if(transferringTwo && timer.milliseconds() > 1600){
            transferServo.setPower(0);
            transferringTwo = false;
        }
    }

    public void outtakePurple(){
        int intakePos = intakeMotor.getCurrentPosition();
        while(intakePos - intakeMotor.getCurrentPosition() < outakePreloadTicks){
            intakeMotor.setPower(-.5);
        }
        intakeMotor.setPower(0);
    }

//    public class IntakeStack implements Action{
//        public void init(){
//            timer.startTime();
//        }
//        @Override
//        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
//            transferServo.setPower(0.6);
//            return timer.time() > 3000;
//        }
//    }
//
//    public Action intakeStack(){
//        return new IntakeStack();
//    }
//
//    public class DepositPurple implements Action{
//
//        @Override
//        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
//            intakeMotor.setPower(-0.5);
//            return intakeMotor.getPower() != -0.5;
//        }
//    }
//
//    public Action depositPurple(){
//        return new DepositPurple();
//    }

    public void telemetry(Telemetry telemetry){
        telemetry.addData("intake position", intakeMotor.getCurrentPosition());
    }

    public void depositPurple() {

    }
}

