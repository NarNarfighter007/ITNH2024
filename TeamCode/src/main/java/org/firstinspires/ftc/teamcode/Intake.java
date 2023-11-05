package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    DcMotor intakeMotor;
    Gamepad gamepad1;
    public Intake(HardwareMap hardwareMap, Gamepad gamepad1){
        this.gamepad1 = gamepad1;
        intakeMotor = hardwareMap.get(DcMotor.class, "Intake");

    }

    public void runIntake(){
        intakeMotor.setPower(gamepad1.right_trigger);

    }

}

