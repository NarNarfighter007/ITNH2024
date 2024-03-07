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

import java.util.Timer;
import java.util.TimerTask;

public class Intake {
    DcMotor intakeMotor;
    CRServo transferServo, conveyorServo;
    Servo intakeFlipServo, stackIntakeL, stackIntakeR;
    Gamepad gamepad1, gamepad2;
    ElapsedTime timer = new ElapsedTime();
    int intakePos = 0, intakePos2;
    public double intakeFlipUp = .73, intakeFlipDown = 0.1, emergencyClosed = .65, emergencyOpen = 0.11;
    double intakeLOut = 1, intakeLIn = .2,  intakeROut = .0, intakeRIn = .8;
    boolean intakingTwo = false, transferringTwo = false, intakingStack = false;
    final double intakePower = 0.8, transferPower = 1.0;
    double time, startTime;
    final int outakePreloadTicks = 400;
    boolean toggle = false, flipDown = false, debounce = false;
    public Intake(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        intakeMotor = hardwareMap.get(DcMotor.class, "INM11");
        transferServo = hardwareMap.get(CRServo.class, "TNS10");
        conveyorServo = hardwareMap.get(CRServo.class, "CVS15");
        intakeFlipServo = hardwareMap.get(Servo.class, "FUS14");
        stackIntakeL = hardwareMap.get(Servo.class, "ILS13");
        stackIntakeR = hardwareMap.get(Servo.class, "IRS03");

        transferServo.setDirection(DcMotorSimple.Direction.REVERSE);
        conveyorServo.setDirection(DcMotorSimple.Direction.REVERSE);

        intakeFlipServo.setPosition(intakeFlipUp);
        stackIntakeL.setPosition(intakeLOut);
        stackIntakeR.setPosition(intakeROut);
    }

    public void runIntake(){
        if(gamepad1.right_trigger > 0.2){
            intakeMotor.setPower(intakePower);
            transferServo.setPower(transferPower);
            conveyorServo.setPower(transferPower);
        } else if(gamepad1.left_trigger > 0.2){
            intakeMotor.setPower(-intakePower);
            transferServo.setPower(-transferPower);
        } else if(!intakingTwo){
            intakeMotor.setPower(0);
            transferServo.setPower(0);
            conveyorServo.setPower(0);
        }
        stackIntake();
        time = timer.milliseconds();
    }

    public void stackIntake(){
        if(gamepad1.dpad_down && !toggle){
            flipDown = !flipDown;
            toggle = true;
        } else if(!gamepad1.dpad_down && toggle){
            toggle = false;
        }

        if(flipDown){
            intakeFlipServo.setPosition(intakeFlipDown);
        } else if(!flipDown){
            intakeFlipServo.setPosition(intakeFlipUp);
        }

        if(gamepad1.dpad_right && !debounce){
            intakingStack = true;
            startTime = time;
            debounce = false;
        } else if(!gamepad1.dpad_right && debounce){
            debounce = true;
        }

        if(intakingStack){
            stackIntakeR.setPosition(intakeRIn);
            stackIntakeL.setPosition(intakeLIn);
            if(time >= startTime + 200){
                stackIntakeR.setPosition(intakeROut);
                stackIntakeL.setPosition(intakeLOut);
                intakingStack = false;
            }
        }
    }

    public void flipDown(){
        intakeFlipServo.setPosition(intakeFlipDown);
    }

    public void flipUp(){
        intakeFlipServo.setPosition(intakeFlipDown);
    }

    public void autonIntakeFlipDown(){
        intakeFlipServo.setPosition(intakeFlipDown);
    }

    public void autonIntakeFlipUp(){
        intakeFlipServo.setPosition(intakeFlipUp);
    }

    public void autonIntakeIn(){
        stackIntakeL.setPosition(intakeLIn);
        stackIntakeR.setPosition(intakeRIn);
    }

    public void autonIntakeOut(){
        stackIntakeL.setPosition(intakeLOut);
        stackIntakeR.setPosition(intakeROut);
    }

    public void startIntake(){
        intakeMotor.setPower(intakePower);
        transferServo.setPower(transferPower);
        conveyorServo.setPower(transferPower);
    }

    public void stopIntake(int delayMillis){
        Timer stopIntakeTimer = new Timer();
        TimerTask stopIntake = new TimerTask(){
            @Override
            public void run() {
                intakeMotor.setPower(0);
                transferServo.setPower(0);
                conveyorServo.setPower(0);
            }
        };

    }
    public void startReverseIntake(){
        intakeMotor.setPower(-0.2);
    }

    public void stopReverseIntake(){
        intakeMotor.setPower(0);
    }

    public void autonIntakeTwo(){
        Timer intakeTwoTimer = new Timer();

        intakeTwoTimer.schedule(intakeIn1, 0);
        intakeTwoTimer.schedule(intakeOut1, 900);
        intakeTwoTimer.schedule(intakeIn2, 1800);
        intakeTwoTimer.schedule(intakeOut2, 2700);
    }

    TimerTask intakeIn1 = new TimerTask() {
        @Override
        public void run() {
            autonIntakeIn();
        }
    };
    TimerTask intakeIn2 = new TimerTask() {
        @Override
        public void run() {
            autonIntakeIn();
        }
    };
    TimerTask intakeOut1 = new TimerTask() {
        @Override
        public void run() {
            autonIntakeOut();
        }
    };
    TimerTask intakeOut2 = new TimerTask() {
        @Override
        public void run() {
            autonIntakeOut();
        }
    };

    public void setEmergencyOuttake(double pos){
//        emergencyOuttake.setPosition(pos);
    }
    public void telemetry(Telemetry telemetry){
//        telemetry.addData("intake position", intakeMotor.getCurrentPosition());
        telemetry.addData("flip pos", intakeFlipServo.getPosition());
//        telemetry.addData("intake stack", intakingStack);
        telemetry.addData("flip down", flipDown);
//        telemetry.addData("startTime", startTime);
//        telemetry.addData("time", time);
//        telemetry.addData("flipDown", flipDown);
    }

}

