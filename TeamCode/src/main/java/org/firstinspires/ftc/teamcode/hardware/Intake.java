package org.firstinspires.ftc.teamcode.hardware;

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
}

