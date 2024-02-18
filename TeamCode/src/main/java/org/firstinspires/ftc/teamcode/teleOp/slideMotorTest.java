package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class slideMotorTest extends OpMode {
    DcMotor slideMotor;
    @Override
    public void init() {
        slideMotor = hardwareMap.get(DcMotorEx.class, "SLM01");
    }

    @Override
    public void loop() {
        slideMotor.setPower(gamepad1.right_trigger);
        telemetry.addData("pos", slideMotor.getCurrentPosition());
        telemetry.addData("power", slideMotor.getPowerFloat());
    }
}
