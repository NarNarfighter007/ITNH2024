package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ServoTest extends OpMode {
    Servo fourbarServo, leftDropServo, rightDropServo, pitchServo, rollServo, emergencyOutake;
    CRServo transferServo;

    DcMotor intakeMotor;

    @Override
    public void init() {
        fourbarServo = hardwareMap.get(Servo.class, "FBS00"); //FBS00 //72
        leftDropServo = hardwareMap.get(Servo.class, "DLS02");
        rightDropServo = hardwareMap.get(Servo.class, "DRS04");
        pitchServo = hardwareMap.get(Servo.class, "OPS01"); //22
        rollServo = hardwareMap.get(Servo.class, "ORS05");
        emergencyOutake = hardwareMap.get(Servo.class, "EMS03");

        transferServo = hardwareMap.get(CRServo.class, "TNS10");
        transferServo.setDirection(DcMotorSimple.Direction.REVERSE);

        intakeMotor = hardwareMap.get(DcMotor.class, "INM11");
    }

    @Override
    public void loop() {
//        fourbarServo.setPosition(gamepad1.left_stick_y);
//        leftDropServo.setPosition(gamepad1.right_stick_x);
//        boxServo.setPosition(gamepad1.right_stick_y);
//
//        boxRotServo.setPosition(gamepad2.left_stick_y);
//        emergencyOutake.setPosition(gamepad2.right_stick_y);
//        rightDropServo.setPosition(gamepad2.right_stick_x);
        if(gamepad1.right_bumper){
            intakeMotor.setPower(0.8);
            transferServo.setPower(1);
        } else if (gamepad1.left_bumper){
            transferServo.setPower(-1);
            intakeMotor.setPower(-0.8);
        } else {
            transferServo.setPower(0);
            intakeMotor.setPower(0);
        }




        telemetry.addData("fourbar", fourbarServo.getPosition());
        telemetry.addData("Leftdrop", leftDropServo.getPosition());
        telemetry.addData("outtake pitch", pitchServo.getPosition());
        telemetry.addData("rightDrop", rightDropServo.getPosition());
        telemetry.addData("outtake roll", rollServo.getPosition());
//        telemetry.addData("box rot", boxRotServo.getPosition());
//        telemetry.addData("intake", intake.getPosition());

//        fourbarServo.setPosition(0);
////        dropServo.setPosition(gamepad1.right_stick_x);
//        boxServo.setPosition(gamepad1.right_stick_y);
//        boxRotServo.setPosition(0.57);
    }
}
