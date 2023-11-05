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
    DcMotor frontLeft, frontRight, backLeft, backRight, transferMotor, lin, slides;
    DcMotorEx intake;
    Servo drone, droneInfo;
    double x;
    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, ("frontLeft"));
        frontRight = hardwareMap.get(DcMotor.class, ("frontRight"));
        backRight = hardwareMap.get(DcMotor.class, ("backRight"));
        backLeft = hardwareMap.get(DcMotor.class, ("backLeft"));
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        drone = hardwareMap.get(Servo.class, "drone");
        transferMotor = hardwareMap.get(DcMotor.class, "transfer");
        lin = hardwareMap.get(DcMotor.class, "lin");
        slides = hardwareMap.get(DcMotor.class, "slides");
        x = 0;



        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        //      telemetry.addData("imu", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES));


        //    SlippyRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //    SlippyLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      /*  frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); */
        slides.setTargetPosition(0);
        slides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    @Override
    public void loop() {


      /*  frontLeft.setPower(((1.5 * gamepad1.left_stick_y) + -gamepad1.left_stick_x + -gamepad1.right_stick_x));
        frontRight.setPower(((-1.5 * gamepad1.left_stick_y) + gamepad1.left_stick_x + gamepad1.right_stick_x));
        backRight.setPower(((-1.5 * gamepad1.left_stick_y) + -gamepad1.left_stick_x + gamepad1.right_stick_x));
        backLeft.setPower(((1.5 * gamepad1.left_stick_y) + gamepad1.left_stick_x + -gamepad1.right_stick_x)); */
        double power = 2;
        double y = -(Math.pow(gamepad1.left_stick_y, power) * Math.signum(gamepad1.left_stick_y));
        double x = Math.pow(gamepad1.left_stick_x * 1.1, power) * Math.signum(gamepad1.left_stick_x);
        double rx = Math.pow(gamepad1.right_stick_x * 0.75, power) * Math.signum(gamepad1.right_stick_x);
        double sSpeed = 0.8;
        int drop1 = 352;
        int drop2 = 1162;
        int drop3 = 2225;
        int drop4 = 3100;
        /*double y = -(gamepad1.left_stick_y); // Remember, Y stick value is reversed
        double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = gamepad1.right_stick_x;*/

        // Denominator is the largest motor power (absolute value) or 1`
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);

        intake.setPower(gamepad1.left_trigger);

        drone.setPosition(Math.max(0.0, 0.6-gamepad1.right_trigger));

      /*  if (gamepad1.dpad_up) {
            transferMotor.setPower(-1);
        } else if (gamepad1.dpad_down){
            transferMotor.setPower(1);
        } else {
            transferMotor.setPower(0);
        } */

        if (gamepad1.dpad_up) {
            lin.setPower(1);
        } else if (gamepad1.dpad_down){
            lin.setPower(-1);
        } else {
            lin.setPower(0);
        }

        if (gamepad1.dpad_right) {
            slides.setPower(.8);
        } else if (gamepad1.dpad_left){
            slides.setPower(-.8);
        }
//        else {
//            slides.setPower(0);
//        }


        //target slides positions
        if (gamepad1.x) {
           slides.setTargetPosition(drop1);
        }
        if (gamepad1.y) {
            slides.setTargetPosition(drop2);
        }
        if (gamepad1.a) {
            slides.setTargetPosition(drop3);
        }
        if (gamepad1.b) {
            slides.setTargetPosition(drop4);
        }
        if(gamepad1.left_bumper){
            slides.setTargetPosition(0);
        }
        slides.setPower(sSpeed);

        telemetry.addData("Servo:", drone.getPosition());
        telemetry.addData("lift pos", slides.getCurrentPosition());
//        telemetry.addData("Motor Encoder frontLeft:", frontLeft.getCurrentPosition());
//        telemetry.addData("Motor Encoder backLeft:", backLeft.getCurrentPosition());
//        telemetry.addData("Motor Encoder frontRight:", frontRight.getCurrentPosition());
//        telemetry.addData("Motor Encoder backRight:", backRight.getCurrentPosition());
//        telemetry.addData("Motor Encoder Intake:", intake.getCurrentPosition());
    }
}
