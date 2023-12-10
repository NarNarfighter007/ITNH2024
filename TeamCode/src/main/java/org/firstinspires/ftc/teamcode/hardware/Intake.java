package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    DcMotor intakeMotor, transferMotor;
    Gamepad gamepad1;

    final double intakePower = 0.8, transferPower = 0.8;
    public Intake(HardwareMap hardwareMap, Gamepad gamepad1){
        this.gamepad1 = gamepad1;
        intakeMotor = hardwareMap.get(DcMotor.class, "INM11");
        transferMotor = hardwareMap.get(DcMotor.class, "TRM10");
    }

    public void runIntake(){
        if(gamepad1.right_bumper) {
            intakeMotor.setPower(intakePower);
        } else{
            intakeMotor.setPower(0);
        }

        if(gamepad1.left_bumper){
            transferMotor.setPower(transferPower);
        } else{
            transferMotor.setPower(0);
        }
    }
    public class DepositPurple implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intakeMotor.setPower(-0.5);
            return intakeMotor.getPower() != -0.5;
        }
    }

    public class IntakeStack implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intakeMotor.setPower(intakePower);
            transferMotor.setPower(transferPower);
            new SleepAction(1.0);
            intakeMotor.setPower(0);
            transferMotor.setPower(0);
            return false;
        }
    }
    public Action depositPurple(){
        return new DepositPurple();
    }

}

