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
    double autonDriveSpeed = 0.3, teleopDriveSpeed = 0.8, scaleFactor = 1.0;

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

    //SampleMecanumDrive drive = new SampleMecanumDrive(opMode.hardwareMap);
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

        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

//        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    //AUTON CONSTRUCTOR
    public Chassis(HardwareMap hardwareMap, TeamIMU imu, Gamepad gamepad1, double headingOffset){
        this.gamepad1 = gamepad1;
        this.imu = imu;
        this.headingOffset = headingOffset;
//        frontLeftMotor = hardwareMap.get(DcMotor.class, "FLM02");
//        backLeftMotor = hardwareMap.get(DcMotor.class, "BLM00");
//        frontRightMotor = hardwareMap.get(DcMotor.class, "FRM03");
//        backRightMotor = hardwareMap.get(DcMotor.class, "BRM01");

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

        double botHeading = Math.toRadians(-imu.getHeadingFirstAngle() + headingOffset);

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
            headingOffset = -imu.getHeadingFirstAngle(); //TODO: Check this (not 100% sure if this is right)
        }
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

    public void lineTo(int targetX, int targetY) {
            double targetAngle = Math.toDegrees(Math.asin(targetY / targetX));
            double y, x;
            if (targetX > targetY) {
                x = 1;
                y = 1 / targetX;
            } else {
                y = 1;
                x = 1 / targetY;
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

    @Deprecated
    public void forward_inches(double inches, double tolerance) {
        double tolerance_r = tolerance * ticsPerInch_r; // update tolerance to be in ticsPerInch instead of inches
        double tolerance_l = tolerance * ticsPerInch_l;

        // rightDrive PID init
        double target_r = (inches * ticsPerInch_r) + frontRightMotor.getCurrentPosition();
        double error_r = target_r - frontRightMotor.getCurrentPosition();
        double power_r = 0;
        double errorSum_r = 0;
        double lastError_r = 0;
        double errorSlope_r = 0;

        // leftDrive PID init
        double target_l = (inches * ticsPerInch_l) + frontLeftMotor.getCurrentPosition();
        double error_l = target_l - frontLeftMotor.getCurrentPosition();
        double power_l = 0;
        double errorSum_l = 0;
        double lastError_l = 0;
        double errorSlope_l = 0;

        long curTime = System.nanoTime();
        long lastTime;

        while (this.opMode.opModeIsActive() && (Math.abs(error_r) > tolerance_r || Math.abs(error_l) > tolerance_l)) {
            lastTime = curTime;
            curTime = System.nanoTime();

            error_r = target_r - frontRightMotor.getCurrentPosition();
            error_l = target_l - frontLeftMotor.getCurrentPosition();

            errorSlope_r = (error_r - lastError_r) / (curTime - lastTime);
            errorSlope_l = (error_l - lastError_l) / (curTime - lastTime);
            power_r = error_r * this.kp + errorSum_r * this.ki  + errorSlope_r * this.kd;
            power_l = error_l * this.kp + errorSum_l * this.ki  + errorSlope_l * this.kd;

            // FTC Dashboard Telemetry
            TelemetryPacket packet = new TelemetryPacket();
            packet.put("currentPos_r", frontRightMotor.getCurrentPosition() / ticsPerInch_r);
            packet.put("currentPos_l", frontLeftMotor.getCurrentPosition() / ticsPerInch_l);
            packet.put("target", inches);
            packet.put("power_r", power_r);
            packet.put("error_r", error_r);
            packet.put("error_sum_r", errorSum_r);
            packet.put("error_slope_r", errorSlope_r);
            packet.put("power_l", power_l);
            packet.put("error_l", error_l);
            packet.put("error_sum_l", errorSum_l);
            packet.put("error_slope_l", errorSlope_l);
            FtcDashboard dashboard = FtcDashboard.getInstance();
            dashboard.sendTelemetryPacket(packet);
            setRightPower(power_r);
            setLeftPower(power_l);
            errorSum_r += error_r;
            errorSum_l += error_l;
            errorSum_r = clampErrorSum(errorSum_r);
            errorSum_l = clampErrorSum(errorSum_l);

            lastError_r = error_r;
            lastError_l = error_l;
        }
        this.stopDrive();
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
    @Deprecated
    public void backward_inches(double inches, double tolerance) {
        double tolerance_r = tolerance * backwardTicsPerInch_r; // update tolerance to be in tics not inches
        double tolerance_l = tolerance * backwardTicsPerInch_l;

        // rightDrive PID init
        double target_r = frontRightMotor.getCurrentPosition() - (inches * backwardTicsPerInch_r);
        double error_r = target_r - frontRightMotor.getCurrentPosition();
        double power_r = 0;
        double errorSum_r = 0;
        double lastError_r = 0;
        double errorSlope_r = 0;

        // leftDrive PID init
        double target_l = frontLeftMotor.getCurrentPosition() - (inches * backwardTicsPerInch_l);
        double error_l = target_l - frontLeftMotor.getCurrentPosition();
        double power_l = 0;
        double errorSum_l = 0;
        double lastError_l = 0;
        double errorSlope_l = 0;

        long curTime = System.nanoTime();
        long lastTime;

        while (this.opMode.opModeIsActive() && (Math.abs(error_r) > tolerance_r || Math.abs(error_l) > tolerance_l)) {
            lastTime = curTime;
            curTime = System.nanoTime();

            error_r = target_r - frontRightMotor.getCurrentPosition();
            error_l = target_l - frontLeftMotor.getCurrentPosition();

            errorSlope_r = (error_r - lastError_r) / (curTime - lastTime);
            errorSlope_l = (error_l - lastError_l) / (curTime - lastTime);

            power_r = error_r * this.kp + errorSum_r * this.ki + errorSlope_r * this.kd;;
            power_l = error_l * this.kp + errorSum_l * this.ki + errorSlope_l * this.kd;;

            // FTC Dashboard Telemetry
            TelemetryPacket packet = new TelemetryPacket();
            packet.put("currentPos_r", frontRightMotor.getCurrentPosition() / backwardTicsPerInch_r);
            packet.put("currentPos_l", frontLeftMotor.getCurrentPosition() / backwardTicsPerInch_l);
            packet.put("target", inches);
            packet.put("power_r", power_r);
            packet.put("error_r", error_r);
            packet.put("power_l", power_l);
            packet.put("error_l", error_l);
            FtcDashboard dashboard = FtcDashboard.getInstance();
            dashboard.sendTelemetryPacket(packet);

            setLeftPower(power_l);
            setRightPower(power_r);
            errorSum_r += error_r;
            errorSum_l += error_l;
            errorSum_r = clampErrorSum(errorSum_r);
            errorSum_l = clampErrorSum(errorSum_l);

            lastError_r = error_r;
            lastError_l = error_l;
        }
        this.stopDrive();
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
    @Deprecated
    public void turn(float targetHeading, float tolerance) {
        long startTime = System.nanoTime();
        long curTime = System.nanoTime();
        long lastTime;
        double lastError = 0;
        double errorSum = 0;
        double error = targetHeading - getHeading();
        error = (int) (error / 180) * -360 + error;
        double errorSlope = 0;
        while (this.opMode.opModeIsActive() && (Math.abs(error) > tolerance || Math.abs(errorSlope) > slopeTolerance)) {
            lastTime = curTime;
            curTime = System.nanoTime();

            error = targetHeading - getHeading();
            error = (int) (error / 180) * -360 + error;
            errorSlope = (error - lastError) / (curTime - lastTime);
            double power = error * this.turnKp + errorSum * this.turnKi + errorSlope * this.turnKd;
            TelemetryPacket packet = new TelemetryPacket();
            packet.put("currentHeading", getHeading());
            packet.put("targetHeading", targetHeading);
            packet.put("power", power);
            packet.put("curTime", (curTime - startTime) / 1000000000f);
            packet.put("errorSum", errorSum);
            FtcDashboard dashboard = FtcDashboard.getInstance();
            dashboard.sendTelemetryPacket(packet);
            setLeftPower(-power);
            setRightPower(power);
            errorSum += error;
            if (this.turnIntegralCap > 0 && Math.abs(errorSum) > this.turnIntegralCap) {
                errorSum = Math.signum(errorSum) * this.turnIntegralCap;
            }
            lastError = error;
        }
        this.stopDrive();
    }

    public void turnTo(double targetHeading, String direction){
        int tolerance = 8;
        double turnPower = 0.2;
        double imuHeading = imu.getHeadingFirstAngle();
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
            imuHeading = imu.getHeadingFirstAngle();
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
        telemetry.addData("odoLeft", backLeftMotor.getCurrentPosition());
        telemetry.addData("odoRight", backRightMotor.getCurrentPosition());
        telemetry.addData("odoStrafe", frontRightMotor.getCurrentPosition()); //currently positive to the right
    }
}
