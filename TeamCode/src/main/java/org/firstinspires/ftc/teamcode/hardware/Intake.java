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

public class Intake {
    DcMotor intakeMotor;
    CRServo transferServo;
    Gamepad gamepad1;
    ElapsedTime timer;
    final double intakePower = 0.8, transferPower = 1.0;
    public Intake(HardwareMap hardwareMap, Gamepad gamepad1){
        this.gamepad1 = gamepad1;
        intakeMotor = hardwareMap.get(DcMotor.class, "INM11");
        transferServo = hardwareMap.get(CRServo.class, "TNS10");

        transferServo.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void runIntake(){
        if(gamepad1.right_bumper) {
            intakeMotor.setPower(intakePower);
        } else{
            intakeMotor.setPower(0);
        }

        if(gamepad1.left_bumper){
            transferServo.setPower(transferPower);
        } else{
            transferServo.setPower(0);
        }
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
}

