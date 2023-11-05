package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

@Autonomous
public class visionTest extends OpMode {
    IMU imu;
    Vision vision;
    Chassis chassis;
    List<AprilTagDetection> myAprilTagDetections;  // list of all detections
    AprilTagDetection myAprilTagDetection;         // current detection in for() loop
    int myAprilTagIdCode;                           // ID code of current detection, in for() loop


    @Override
    public void init() {
        vision = new Vision(hardwareMap);
        chassis = new Chassis(hardwareMap, (org.firstinspires.ftc.teamcode.IMU) imu, gamepad1);
    }

    @Override
    public void loop() {
        // Get a list of AprilTag detections.
        myAprilTagDetections = vision.aprilTagProcessor.getDetections();
//        Cycle through through the list and process each AprilTag.

        for (AprilTagDetection myAprilTagDetection : myAprilTagDetections) {
            if (myAprilTagDetection.metadata != null) {  // This check for non-null Metadata is not needed for reading only ID code.
                myAprilTagIdCode = myAprilTagDetection.id;
                telemetry.addData("id", myAprilTagDetection.id);
                telemetry.update();

                // Now take action based on this tag's ID code, or store info for later action.
                if(myAprilTagIdCode == 1){
                    telemetry.addData("x", myAprilTagDetection.ftcPose.x);
                    telemetry.addData("y", myAprilTagDetection.ftcPose.y);
                    telemetry.addData("z", myAprilTagDetection.ftcPose.z);
//                    if(myAprilTagDetection.ftcPose.x > 1) {
//                        chassis.strafeRight();
//                    } else if(myAprilTagDetection.ftcPose.x < -1){
//                        chassis.strafeLeft();
//                    } else{
//                        chassis.stop();
//                    }
                }
            }
        }
//    telemetry.addData("id", myAprilTagDetection.id);
//    telemetry.addData("x", myAprilTagDetection.ftcPose.x);
//    telemetry.addData("y", myAprilTagDetection.ftcPose.y);
//    telemetry.addData("z", myAprilTagDetection.ftcPose.z);
//    telemetry.update();
    }
}
