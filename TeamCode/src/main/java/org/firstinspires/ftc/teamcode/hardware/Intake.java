package org.firstinspires.ftc.teamcode.hardware;

//import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {
    DcMotor intakeMotor;
    CRServo transferServo, conveyorServo;
    Servo intakeFlipServo, stackIntakeL, emergencyOuttake;
    Gamepad gamepad1, gamepad2;
    ElapsedTime timer = new ElapsedTime();
    int intakePos = 0, intakePos2;
    public double intakeFlipUp = .68, intakeFlipDown = 0.05, emergencyClosed = .65, emergencyOpen = 0.11;
    double intakeLOut = 1, intakeLIn = 0, intakeROut = 0, intakeRIn = 1;
    boolean intakingTwo = false, transferringTwo = false, intakingStack = false;
    final double intakePower = 0.8, transferPower = 1.0;
    double time, startTime;
    final int outakePreloadTicks = 400;
    boolean toggle = false, flipDown = false;
    public Intake(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        intakeMotor = hardwareMap.get(DcMotor.class, "INM11");
        transferServo = hardwareMap.get(CRServo.class, "TNS10");
        conveyorServo = hardwareMap.get(CRServo.class, "CVS15");
        intakeFlipServo = hardwareMap.get(Servo.class, "FUS14");
        stackIntakeL = hardwareMap.get(Servo.class, "ILS13");
        emergencyOuttake = hardwareMap.get(Servo.class, "EMS03");

        transferServo.setDirection(DcMotorSimple.Direction.REVERSE);
        conveyorServo.setDirection(DcMotorSimple.Direction.REVERSE);

        intakeFlipServo.setPosition(intakeFlipUp);
        stackIntakeL.setPosition(intakeROut);
        emergencyOuttake.setPosition(emergencyClosed);
    }

    public void runIntake(){
//        if(gamepad1.right_bumper) {
//            intakeMotor.setPower(intakePower);
//        } else if(gamepad1.right_trigger > 0.2){
//            intakeMotor.setPower(-intakePower);
//        } else if(!intakingTwo){
//            intakeMotor.setPower(0);
//        }
//
//        if(gamepad1.left_bumper){
//            transferServo.setPower(transferPower);
//        } else if(gamepad1.left_trigger > 0.2){
//            transferServo.setPower(-transferPower);
//        } else if(!transferringTwo){
//            transferServo.setPower(0);
//        }
        if(gamepad1.right_trigger > 0.2){
            intakeMotor.setPower(intakePower);
            transferServo.setPower(transferPower);
            conveyorServo.setPower(transferPower);
        } else if(gamepad1.left_trigger > 0.2){
            intakeMotor.setPower(-intakePower);
            transferServo.setPower(-transferPower);
            conveyorServo.setPower(-transferPower);
        } else if(!intakingTwo){
            intakeMotor.setPower(0);
            transferServo.setPower(0);
            conveyorServo.setPower(0);
        }
//        stackIntake();
        time = timer.milliseconds();
    }

//    public void stackIntake(){
//        if(gamepad1.dpad_down && !toggle){
//            flipDown = !flipDown;
//            toggle = true;
//        } else if(!gamepad1.dpad_down && toggle){
//            toggle = false;
//        }
//
//        if(flipDown){
//            intakeFlipServo.setPosition(intakeFlipDown);
//        } else if(!flipDown && stackIntakeR.getPosition() == intakeROut && !intakingStack){
//            intakeFlipServo.setPosition(intakeFlipUp);
//        }
//
//        if(gamepad1.dpad_right){
//            intakingStack = true;
//            startTime = time;
//        }
//
//        if(intakingStack){
//            stackIntakeR.setPosition(intakeRIn);
//            if(time >= startTime + 200){
//                stackIntakeR.setPosition(intakeROut);
//                intakingStack = false;
//            }
//        }
//
//    }

    public void flipDown(){
        intakeFlipServo.setPosition(intakeFlipDown);
    }

    public void flipUp(){
        intakeFlipServo.setPosition(intakeFlipDown);
    }
    public void intakeOne(){
        if(intakeFlipServo.getPosition() == intakeFlipDown){
            stackIntakeL.setPosition(intakeRIn);
            timer.reset();
            while(timer.milliseconds() < 200){}
            stackIntakeL.setPosition(intakeROut);
        }
    }
    @Deprecated
    public void intakeTwo(){
        if(gamepad2.a && !transferringTwo){
            intakeMotor.setPower(intakePower);
            intakePos = intakeMotor.getCurrentPosition();
            intakingTwo = true;
//            transferServo.setPower(transferPower);
            transferringTwo = true;
            timer.reset();
        }
        if(intakeMotor.getCurrentPosition()-intakePos > 380 && intakingTwo){
            intakeMotor.setPower(-intakePower);
            intakePos2 = intakeMotor.getCurrentPosition();
            intakingTwo = false;
        }
        if(!intakingTwo && transferringTwo){
            if(intakeMotor.getCurrentPosition() - intakePos2 < 25){
                intakeMotor.setPower(0);
            }
        }
        if(transferringTwo && timer.milliseconds() > 1600){
            transferServo.setPower(0);
            conveyorServo.setPower(0);
            transferringTwo = false;
        }
    }

    public void autonIntakeFlipDown(){
//        intakeFlipServo.setPosition(intakeFlipDown);
    }

    public void autonIntakeFlipUp(){
//        intakeFlipServo.setPosition(intakeFlipUp);
    }

    public void autonIntakeIn(){
//        stackIntakeL.setPosition(intakeRIn);
    }

    public void autonIntakeOut(){
//        stackIntakeL.setPosition(intakeROut);
    }

    public void startReverseIntake(){
        intakeMotor.setPower(-0.2);
    }

    public void stopReverseIntake(){
        intakeMotor.setPower(0);
    }

    public void setEmergencyOuttake(double pos){
//        emergencyOuttake.setPosition(pos);
    }
    public void telemetry(Telemetry telemetry){
        telemetry.addData("intake position", intakeMotor.getCurrentPosition());
        telemetry.addData("flip pos", intakeFlipServo.getPosition());
        telemetry.addData("intake stack", intakingStack);
        telemetry.addData("startTime", startTime);
        telemetry.addData("time", time);
        telemetry.addData("flipDown", flipDown);
    }

}

