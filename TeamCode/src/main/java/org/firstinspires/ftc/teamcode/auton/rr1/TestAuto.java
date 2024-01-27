package org.firstinspires.ftc.teamcode.auton.rr1;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.ocv.BlueOCVPipeline;
import org.firstinspires.ftc.teamcode.hardware.Camera;
import org.firstinspires.ftc.teamcode.hardware.Chassis;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Scalar;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;



@Autonomous
public class TestAuto extends LinearOpMode {
    Camera camera;
    @Override
    public void runOpMode() throws InterruptedException {
//        camera = new Camera(hardwareMap);
        camera = new Camera(hardwareMap, "RED");

        while(opModeInInit()){
            camera.getAutonRed();
            camera.redTelemetry(telemetry);
            telemetry.addData("RedReturn: ", camera.getAutonRed());
            telemetry.update();
        }
        waitForStart();

        camera.redTelemetry(telemetry);

        telemetry.update();
    }
}
