package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Chassis {
    public DcMotor frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;

    Gamepad gamepad1;
    LinearOpMode opMode;
    TeamIMU imu;
    double headingOffset = 0;
    double autonDriveSpeed = 0.6, teleopDriveSpeed = 0.8, scaleFactor = 1.0;
    //SampleMecanumDrive drive = new SampleMecanumDrive(opMode.hardwareMap);
    //Teleop Constructor
    public Chassis(HardwareMap hardwareMap, TeamIMU imu, Gamepad gamepad1){
        this.gamepad1 = gamepad1;
        this.imu = imu;

//        frontLeftMotor = hardwareMap.get(DcMotor.class, "FLM02");
//        backLeftMotor = hardwareMap.get(DcMotor.class, "BLM00");
//        frontRightMotor = hardwareMap.get(DcMotor.class, "FRM03");
//        backRightMotor = hardwareMap.get(DcMotor.class, "BRM01");

        frontLeftMotor = hardwareMap.get(DcMotor.class, "FLM12");
        backLeftMotor = hardwareMap.get(DcMotor.class, "BLM13");
        frontRightMotor = hardwareMap.get(DcMotor.class, "FRM02");
        backRightMotor = hardwareMap.get(DcMotor.class, "BRM03");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void fieldCentricDrive(){
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        double botHeading = Math.toRadians(-imu.getHeadingFirstAngle());

        double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
        double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

//        scaleFactor = driveSpeed + gamepad1.right_trigger * (1 - driveSpeed);
        scaleFactor = teleopDriveSpeed;
        frontLeftMotor.setPower(frontLeftPower * scaleFactor);
        backLeftMotor.setPower(backLeftPower * scaleFactor);
        frontRightMotor.setPower(frontRightPower * scaleFactor);
        backRightMotor.setPower(backRightPower * scaleFactor);
    }

    public void moveForward(){
        frontLeftMotor.setPower(autonDriveSpeed);
        backLeftMotor.setPower(autonDriveSpeed);
        frontRightMotor.setPower(autonDriveSpeed);
        backRightMotor.setPower(autonDriveSpeed);
    }

    public  void moveBackward(){
        frontLeftMotor.setPower(-autonDriveSpeed);
        backLeftMotor.setPower(-autonDriveSpeed);
        frontRightMotor.setPower(-autonDriveSpeed);
        backRightMotor.setPower(-autonDriveSpeed);
    }

    public void strafeLeft(){
        frontLeftMotor.setPower(-autonDriveSpeed);
        backLeftMotor.setPower(autonDriveSpeed);
        frontRightMotor.setPower(autonDriveSpeed);
        backRightMotor.setPower(-autonDriveSpeed);
    }

    public void strafeRight(){
        frontLeftMotor.setPower(autonDriveSpeed);
        backLeftMotor.setPower(-autonDriveSpeed);
        frontRightMotor.setPower(-autonDriveSpeed);
        backRightMotor.setPower(autonDriveSpeed);
    }

    public void stop(){
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);
    }

    public void turnLeft(int degrees){

    }

    public void turnRight(int degrees){

    }

    public void lineTo(int targetX, int targetY){
        double angle = Math.toDegrees(Math.asin(targetY/targetX));
        double y;
        double x;
        if(targetX > targetY){
            x = 1;
            y = 1/targetX;
        } else{
            y = 1;
            x = 1/targetY;
        }
        double rx = 0;

        double botHeading = Math.toRadians(-imu.getHeadingFirstAngle());

        double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
        double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

//        scaleFactor = driveSpeed + gamepad1.right_trigger * (1 - driveSpeed);
        scaleFactor = autonDriveSpeed;
        frontLeftMotor.setPower(frontLeftPower * scaleFactor);
        backLeftMotor.setPower(backLeftPower * scaleFactor);
        frontRightMotor.setPower(frontRightPower * scaleFactor);
        backRightMotor.setPower(backRightPower * scaleFactor);
    }
}
