package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TeamIMU {
    BNO055IMU imu;
    public double x, y, z;
    HardwareMap hardwareMap;

    public TeamIMU(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode to make this JSON
        parameters.loggingEnabled = false;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new BNO055IMU.Parameters().accelerationIntegrationAlgorithm;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    public TeamIMU(HardwareMap hardwareMap, LinearOpMode opMode) {
        this(hardwareMap);
    }

    public double getHeadingFirstAngle(){
        double heading;
        heading = imu.getAngularOrientation().firstAngle;
        return Math.toDegrees(heading);
    }
}