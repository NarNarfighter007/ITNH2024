package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Transfer extends OpMode {
    public DcMotor transferMotor;
    @Override
    public void init() {
        transferMotor = hardwareMap.get(DcMotor.class, "TM01");
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            transferMotor.setPower(1);
        }
        else {
            transferMotor.setPower(0);
        }
    }
}
