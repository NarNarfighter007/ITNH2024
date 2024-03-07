package org.firstinspires.ftc.teamcode.hardware;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Chassis {
    public DcMotor frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;
    Gamepad gamepad1;
    LinearOpMode opMode;
    TeamIMU imu;
    double headingOffset = 0;
    double autonDriveSpeed = 0.3, teleopDriveSpeed = 0.8, scaleFactor = 1.0, normalSpeed = .8, slowSpeed = .3;
    final double driveSpeed = 0.6;
    public static double ticsPerInch = 1881;
    public static double ticsPerInch_r = 1730, backwardTicsPerInch_r = 1720, ticsPerInch_l = 1670, backwardTicsPerInch_l = 1700;
    public static double kp = 12e-6, ki = 9e-6, kd = 0;
    public static double integralCap = 1e4;

    // turning variables
    double heading = headingOffset;
    final double turnTicsPerDegree = 310;
    final double turnSlowPower = 0.2;
    public static double turnKp = 0.015, turnKi = 0.0011, turnKd = 7000000; //2000000
    public static  double turnIntegralCap = 200;
    public static double slopeTolerance = 0.000000028;
    boolean toggle = false, slow = false;

    //SampleMecanumDrive drive = new SampleMecanumDrive(opMode.hardwareMap);
    public Chassis(HardwareMap hardwareMap, TeamIMU imu, Gamepad gamepad1){
        this.gamepad1 = gamepad1;
        this.imu = imu;

        frontLeftMotor = hardwareMap.get(DcMotor.class, "FLM12");
        backLeftMotor = hardwareMap.get(DcMotor.class, "BLM13");
        frontRightMotor = hardwareMap.get(DcMotor.class, "FRM02");
        backRightMotor = hardwareMap.get(DcMotor.class, "BRM03");

        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    //AUTON CONSTRUCTOR
    public Chassis(HardwareMap hardwareMap, TeamIMU imu, Gamepad gamepad1, double headingOffset){
        this.gamepad1 = gamepad1;
        this.imu = imu;
        this.headingOffset = headingOffset;

        frontLeftMotor = hardwareMap.get(DcMotor.class, "FLM12");
        backLeftMotor = hardwareMap.get(DcMotor.class, "BLM13");
        frontRightMotor = hardwareMap.get(DcMotor.class, "FRM02");
        backRightMotor = hardwareMap.get(DcMotor.class, "BRM03");

        //BRM03 = LEFT ODO
        //BLM13 = RIGHT ODO
        //FRM02 = STRAFE ODO
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void fieldCentricDrive(){
        double y = -gamepad1.left_stick_y; // -gamepad1.left_stick_y
        double x = gamepad1.left_stick_x; //gamepad1.left_stick_x
        double rx = gamepad1.right_stick_x; //gamepad1.right_stick_x

        double botHeading = Math.toRadians(-imu.getHeadingFirstAngleDeg() - headingOffset);

        double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading); //1 90deg
        double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator; //1
        double backLeftPower = (rotY - rotX + rx) / denominator; //-1
        double frontRightPower = (rotY - rotX - rx) / denominator; //-1
        double backRightPower = (rotY + rotX - rx) / denominator; //1

//        scaleFactor = driveSpeed + gamepad1.right_trigger * (1 - driveSpeed);
        scaleFactor = teleopDriveSpeed;

        frontLeftMotor.setPower(frontLeftPower * scaleFactor);
        backLeftMotor.setPower(backLeftPower * scaleFactor);
        frontRightMotor.setPower(frontRightPower * scaleFactor);
        backRightMotor.setPower(backRightPower * scaleFactor);

        if(gamepad1.right_stick_button){
            headingOffset = -imu.getHeadingFirstAngleDeg(); //TODO: Check this (not 100% sure if this is right)
        }

        if(gamepad1.left_stick_button && !toggle){
            slow = !slow;
            toggle = true;
        } else if(!gamepad1.b && toggle){
            toggle = false;
        }
        if(slow){
            teleopDriveSpeed = slowSpeed;
        } else{
            teleopDriveSpeed = normalSpeed;
        }
    }

    public void forward_inches(double inches){
//        inches /= 2;
        int leftPos = backLeftMotor.getCurrentPosition(), rightPos = -frontRightMotor.getCurrentPosition();
        int targetLeft = (int) (leftPos + inches*ticsPerInch), targetRight = (int) (rightPos + inches*ticsPerInch);
        while(leftPos < targetLeft - 50 && rightPos < targetRight - 50){
            leftPos = backLeftMotor.getCurrentPosition();
            rightPos = -frontRightMotor.getCurrentPosition();
            if(leftPos < targetLeft - 50) {
                setLeftPower(autonDriveSpeed);
            } else{
                setLeftPower(0);
            }
            if(rightPos < targetRight - 50) {
                setRightPower(autonDriveSpeed);
            } else{
                setRightPower(0);
            }
        }
        stopDrive();
    }

    public void backward_inches(double inches){
//        inches /= 2;
        int leftPos = backLeftMotor.getCurrentPosition(), rightPos = -frontRightMotor.getCurrentPosition();
        int targetLeft = (int) (leftPos - inches*ticsPerInch), targetRight = (int) (rightPos - inches*ticsPerInch);
        while(leftPos > targetLeft + 50 && rightPos > targetRight + 50){
            leftPos = backLeftMotor.getCurrentPosition();
            rightPos = -frontRightMotor.getCurrentPosition();
            if(leftPos > targetLeft + 50) {
                setLeftPower(-autonDriveSpeed);
            } else{
                setLeftPower(0);
            }
            if(rightPos > targetRight + 50) {
                setRightPower(-autonDriveSpeed);
            } else{
                setRightPower(0);
            }
        }
        stopDrive();
    }

    public void turnTo(double targetHeading, String direction){
        int tolerance = 8;
        double turnPower = 0.2;
        double imuHeading = imu.getHeadingFirstAngleDeg();
        imuHeading = (imuHeading + 360) % 360;
        double curHeading = (imuHeading + headingOffset) % 360; // this is our 'adjusted' current heading
        int multiplier = 1;
        if(direction.equals("RIGHT")){
            multiplier = 1;
        } else if(direction.equals("LEFT")){
            multiplier = -1;
        }
        double error = Math.abs(curHeading - targetHeading);
        setRightPower(-multiplier * turnPower);
        setLeftPower(multiplier * turnPower);
        while (error > tolerance) {
            imuHeading = imu.getHeadingFirstAngleDeg();
            imuHeading = (imuHeading + 360) % 360;
            curHeading = (imuHeading + headingOffset) % 360;
            error = Math.abs(curHeading - targetHeading);
        }
        stopDrive();
    }

    public void setRightPower(double power){
        frontRightMotor.setPower(power);
        backRightMotor.setPower(power);
    }

    public void setLeftPower(double power){
        frontLeftMotor.setPower(power);
        backLeftMotor.setPower(power);
    }

    public void stopDrive() {
        setLeftPower(0.0);
        setRightPower(0.0);
    }

    public double clampErrorSum(double errorSum) {
        if (this.integralCap > 0 && Math.abs(errorSum) > this.integralCap)
            return errorSum = Math.signum(errorSum) * this.integralCap;
        else
            return errorSum;
    }

    public double clampDegrees(double raw){
        // take a raw degree input and wrap it to be between 0 and 360
        while(raw > 360){
            raw -= 360;
        }
        while(raw < 0){
            raw += 360;
        }
        return raw;
    }

    public double getHeading() {
        // get our heading from the axial encoders
        // return in degrees
        double raw = (frontRightMotor.getCurrentPosition() - frontLeftMotor.getCurrentPosition()) / turnTicsPerDegree;
        return clampDegrees(raw + headingOffset);
    }

    public void getTelemetry(Telemetry telemetry){
//        telemetry.addData("heading", getHeading());
        telemetry.addData("odoLeft", backLeftMotor.getCurrentPosition());
        telemetry.addData("odoRight", backRightMotor.getCurrentPosition());
        telemetry.addData("odoStrafe", frontRightMotor.getCurrentPosition()); //currently positive to the right
    }
}
