package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@TeleOp(group = "Main")

public class gamepadchassis extends OpMode {
    DcMotor frontLeft, frontRight, backLeft, backRight;
    DcMotorEx intake;
    Servo drone;
    Servo droneInfo;
    double x;
    @Override
    public void init() {

        frontLeft = hardwareMap.get(DcMotor.class, ("frontLeft"));
        frontRight = hardwareMap.get(DcMotor.class, ("frontRight"));
        backRight = hardwareMap.get(DcMotor.class, ("backRight"));
        backLeft = hardwareMap.get(DcMotor.class, ("backLeft"));
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        drone = hardwareMap.get(Servo.class, "drone");
        droneInfo = hardwareMap.get(Servo.class, "droneInfo");
        x = 0;
        //      telemetry.addData("imu", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES));


        //    SlippyRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //    SlippyLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      /*  frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); */
    }

    @Override

    public void loop() {


        frontLeft.setPower(((1.5 * gamepad1.left_stick_y) + -gamepad1.left_stick_x + -gamepad1.right_stick_x));
        frontRight.setPower(((-1.5 * gamepad1.left_stick_y) + gamepad1.left_stick_x + gamepad1.right_stick_x));
        backRight.setPower(((-1.5 * gamepad1.left_stick_y) + -gamepad1.left_stick_x + gamepad1.right_stick_x));
        backLeft.setPower(((1.5 * gamepad1.left_stick_y) + gamepad1.left_stick_x + -gamepad1.right_stick_x));

        intake.setPower(gamepad1.left_trigger);


        if(gamepad1.dpad_up){
            x = x + .01;
            drone.setPosition(x);

        }
        else if (gamepad1.dpad_down) {
            x = x - .01;
            drone.setPosition(x);
        }

        telemetry.update();
        telemetry.addData("Servo:", drone.getPosition());
        telemetry.addData("Motor Encoder frontLeft:", frontLeft.getCurrentPosition());
        telemetry.addData("Motor Encoder backLeft:", backLeft.getCurrentPosition());
        telemetry.addData("Motor Encoder frontRight:", frontRight.getCurrentPosition());
        telemetry.addData("Motor Encoder backRight:", backRight.getCurrentPosition());
        telemetry.addData("Motor Encoder Intake:", intake.getCurrentPosition());
        telemetry.addData("current", intake.getCurrent(CurrentUnit.MILLIAMPS));
    }
}
