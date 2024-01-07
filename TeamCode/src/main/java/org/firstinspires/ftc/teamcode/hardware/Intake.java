package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {
    DcMotor intakeMotor;
    CRServo transferServo;
    Gamepad gamepad1, gamepad2;
    ElapsedTime timer = new ElapsedTime();
    int intakePos = 0;
    boolean intakingTwo = false, transferringTwo = false;
    final double intakePower = 0.8, transferPower = 1.0;
    public Intake(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        intakeMotor = hardwareMap.get(DcMotor.class, "INM11");
        transferServo = hardwareMap.get(CRServo.class, "TNS10");

        transferServo.setDirection(DcMotorSimple.Direction.REVERSE);
    }


    public void runIntake(){
        if(gamepad1.right_bumper) {
            intakeMotor.setPower(intakePower);
        } else if(gamepad1.right_trigger > 0.2){
            intakeMotor.setPower(-intakePower);
        } else if(!intakingTwo){
            intakeMotor.setPower(0);
        }

        if(gamepad1.left_bumper){
            transferServo.setPower(transferPower);
        } else if(gamepad1.left_trigger > 0.2){
            transferServo.setPower(-transferPower);
        } else if(!transferringTwo){
            transferServo.setPower(0);
        }

        intakeTwo();
//        if(gamepad2.a){
//            timer.reset();
//        }
//        intakeTwo();

    }
    public void intakeTwo(){
        if(gamepad2.a){
            intakeMotor.setPower(intakePower);
            intakePos = intakeMotor.getCurrentPosition();
            intakingTwo = true;
            transferServo.setPower(transferPower);
            transferringTwo = true;
        }
        if(intakeMotor.getCurrentPosition()-intakePos > 850 && intakingTwo){
            intakeMotor.setPower(0);
            intakingTwo = false;
            timer.reset();
        }
        if(transferringTwo && timer.milliseconds() > 500){
            transferServo.setPower(0);
            transferringTwo = false;
        }
        //800ms
    }

    public class IntakeStack implements Action{
        public void init(){
            timer.startTime();
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            transferServo.setPower(0.6);
            return timer.time() > 3000;
        }
    }

    public Action intakeStack(){
        return new IntakeStack();
    }

    public class DepositPurple implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intakeMotor.setPower(-0.5);
            return intakeMotor.getPower() != -0.5;
        }
    }

    public Action depositPurple(){
        return new DepositPurple();
    }

    public void telemetry(Telemetry telemetry){
        telemetry.addData("intake position", intakeMotor.getCurrentPosition());
    }
}

