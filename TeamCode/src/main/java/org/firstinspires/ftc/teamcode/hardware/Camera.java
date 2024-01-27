package org.firstinspires.ftc.teamcode.hardware;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.auton.ocv.BlueOCVPipeline;
import org.firstinspires.ftc.teamcode.auton.ocv.RedOCVPipeline;
import org.opencv.core.Scalar;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Config //Disable if not using FTC Dashboard https://github.com/PinkToTheFuture/OpenCV_FreightFrenzy_2021-2022#opencv_freightfrenzy_2021-2022
public class Camera {
    private OpenCvCamera webcam;
//alskdfjlaksjf
    private static final int CAMERA_WIDTH  = 800; // width  of wanted camera resolution
    private static final int CAMERA_HEIGHT = 448; // height of wanted camera resolution

    private double CrLowerUpdate = 0.0;
    private double CbLowerUpdate = 140.0;
    private double CrUpperUpdate = 190.0;
    private double CbUpperUpdate = 255;

    public static double borderLeftX    = 0.0;   //fraction of pixels from the left side of the cam to skip
    public static double borderRightX   = 0.0;   //fraction of pixels from the right of the cam to skip
    public static double borderTopY     = 0.0;   //fraction of pixels from the top of the cam to skip
    public static double borderBottomY  = 0.0;   //fraction of pixels from the bottom of the cam to skip

    private double lowerruntime = 0;
    private double upperruntime = 0;

    //Blue
    public static Scalar scalarLowerYCrCbBlue = new Scalar(0.0, 0.0, 140.0);
    public static Scalar scalarUpperYCrCbBlue = new Scalar(255.0,190.0 ,255.0 );
    public static Scalar scalarLowerYCrCbRed = new Scalar(  0.0, 150.0, 0.0);
    public static Scalar scalarUpperYCrCbRed = new Scalar(255.0,255.0 ,128.0 );
    BlueOCVPipeline bluePipeline;
    RedOCVPipeline redPipeline;

    public Camera(HardwareMap hardwareMap, String color){
        // OpenCV webcam
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "camera"), cameraMonitorViewId);
        if(color.equals("BLUE")) {
            webcam.setPipeline(bluePipeline = new BlueOCVPipeline(borderLeftX, borderRightX, borderTopY, borderBottomY));
            // Configuration of Pipeline
            bluePipeline.configureScalarLower(scalarLowerYCrCbBlue.val[0], scalarLowerYCrCbBlue.val[1], scalarLowerYCrCbBlue.val[2]);
            bluePipeline.configureScalarUpper(scalarUpperYCrCbBlue.val[0], scalarUpperYCrCbBlue.val[1], scalarUpperYCrCbBlue.val[2]);
            webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
            {
                @Override
                public void onOpened()
                {
                    webcam.startStreaming(CAMERA_WIDTH, CAMERA_HEIGHT, OpenCvCameraRotation.UPSIDE_DOWN);
                }

                @Override
                public void onError(int errorCode)
                {
                    /*
                     * This will be called if the camera could not be opened
                     */
                }
            });
        } else{
            webcam.setPipeline(redPipeline = new RedOCVPipeline(borderLeftX, borderRightX, borderTopY, borderBottomY));
            // Configuration of Pipeline
            redPipeline.configureScalarLower(scalarLowerYCrCbRed.val[0], scalarLowerYCrCbRed.val[1], scalarLowerYCrCbRed.val[2]);
            redPipeline.configureScalarUpper(scalarUpperYCrCbRed.val[0], scalarUpperYCrCbRed.val[1], scalarUpperYCrCbRed.val[2]);
            // Webcam Streaming
            webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
                @Override
                public void onOpened() {
                    webcam.startStreaming(CAMERA_WIDTH, CAMERA_HEIGHT, OpenCvCameraRotation.UPSIDE_DOWN);
                }

                @Override
                public void onError(int errorCode) {
                    /*
                     * This will be called if the camera could not be opened
                     */
                }
            });
        }
    }

    public int getAutonBlue()
    {

        if(bluePipeline.getRectArea() > 9000 && bluePipeline.getRectMidpointY() > 125){
            if(bluePipeline.getRectMidpointX() > 500){
                return 3; //AUTON A
            }
            else if(bluePipeline.getRectMidpointX() > 200){
                return 2; //AUTON B
            }
            else {
                return 1; //AUTON C
            }
        }
        return 0;
    }

    public int getAutonRed() {
        //OpenCV Pipeline
            if (redPipeline.getRectArea() > 9000 && redPipeline.getRectMidpointY() > 125) {
                if (redPipeline.getRectMidpointX() > 500) {
                    return 3; //AUTONOMOUS_A();
                } else if (redPipeline.getRectMidpointX() > 200) {
                    return 2; //AUTONOMOUS_B();
                } else {
                    return 1; //AUTONOMOUS_C();
                }

            }
        return 0;
    }

    public void redTelemetry(Telemetry telemetry){
        telemetry.addData("RectArea: ", redPipeline.getRectArea());
        telemetry.addData("RectLocX: ", redPipeline.getRectMidpointX());
        telemetry.addData("RectLocY: ", redPipeline.getRectMidpointY());

        telemetry.update();
    }

    public void blueTelemetry(Telemetry telemetry){
        telemetry.addData("RectArea: ", bluePipeline.getRectArea());
        telemetry.addData("RectLocX: ", bluePipeline.getRectMidpointX());
        telemetry.addData("RectLocY: ", bluePipeline.getRectMidpointY());

        telemetry.update();
    }
}
