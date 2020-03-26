package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;

import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Conveyor extends SubsystemBase {

    // Starts Conveyor Motors
    private static TalonSRX leftSideMotor       = new TalonSRX(Constants.leftSideMotorPort);
    private static VictorSPX rightSideMotor     = new VictorSPX(Constants.rightSideMotorPort);
    private static VictorSPX centerMotor        = new VictorSPX(Constants.centerMotorPort);
    private static VictorSPX feederMotor        = new VictorSPX(Constants.shooterFeedMotorPort);
    private static TimeOfFlight checkPointOne   = new TimeOfFlight(Constants.checkPointOnePort);
    private static TimeOfFlight checkPointRight = new TimeOfFlight(Constants.checkPointRightPort);
    private static TimeOfFlight checkPointLeft  = new TimeOfFlight(Constants.checkPointLeftPort);
    private static PWMSparkMax ledMotor         = new PWMSparkMax(Constants.ledMotorPort);
    private int genericCount        = 0;
    private int ballsCount          = 0;
    private int rightBallCount      = 0;
    private int leftBallCount       = 0; 
    private boolean flag            = false;
    private boolean s               = false; 
    private boolean isFinished      = false;
    private Timer time;
    private double stopTime         = 0;
    private int i                   = 0;
    private int j                   = 0;
    private double waitTime         = 0;
    private boolean ready           = false;
    private boolean stage1          = false;
    private boolean done            = false;


    public Conveyor() {
        leftSideMotor.setNeutralMode(NeutralMode.Brake);
        rightSideMotor.setNeutralMode(NeutralMode.Brake);
        centerMotor.setNeutralMode(NeutralMode.Brake);
        feederMotor.setNeutralMode(NeutralMode.Brake);
        feederMotor.setInverted(false);
        leftSideMotor.setInverted(false);
        rightSideMotor.setInverted(true);
        centerMotor.setInverted(false);
        checkPointOne.setRangingMode(RangingMode.Medium, 50);
        checkPointRight.setRangingMode(RangingMode.Short, 24);
        checkPointLeft.setRangingMode(RangingMode.Short, 24);

        time = new Timer();
    }
  
    @Override
    public void periodic() {
        //comment
    }

    
    public void LEDRed(){
        //SmartDashboard.putBoolean("Intake Complete?", false);
        ledMotor.set(Constants.red );
    }

    public void LEDGreen(){
        //SmartDashboard.putBoolean("Intake Complete?", true);
        ledMotor.set(Constants.green);
    }

    public void blinkRainbow(){
        ledMotor.set(Constants.rainbow);
    }


    public void leftActivate(double speed){
        leftSideMotor.set(ControlMode.PercentOutput, (speed));
        centerMotor.set(ControlMode.PercentOutput, speed/2);
        rightSideMotor.set(ControlMode.PercentOutput, (speed/2));
    }

    public void rightActivate(double speed){
        rightSideMotor.set(ControlMode.PercentOutput, (speed));
        centerMotor.set(ControlMode.PercentOutput, (-speed/2));
        leftSideMotor.set(ControlMode.PercentOutput, (speed/2));
        
    }

    public void conveyorFeed(double speed){
        rightSideMotor.set(ControlMode.PercentOutput, (speed));
        leftSideMotor.set(ControlMode.PercentOutput, (speed));
        feederMotor.set(ControlMode.PercentOutput, speed);
        //centerMotor.set(ControlMode.PercentOutput, (-speed/2));

    }

    public void conveyorShooterFeed(double speed){
        feederMotor.set(ControlMode.PercentOutput, speed);
    }

    public void conveyorSideFeed(double left, double right){
        rightSideMotor.set(ControlMode.PercentOutput, (right));
        leftSideMotor.set(ControlMode.PercentOutput, (left));
    }

   

    // public void countBalls(TimeOfFlight sensor){
    //     if(sensor.getRange() < 150 && flag){
    //         flag = false;
    //         ballsCount++;
    //     } else if(sensor.getRange() > 150 ){
    //         flag = true;
    //     }
    // }



    public int getBallCount(){
        return ballsCount;
    }

    public void setBallCount(int n){
        ballsCount = n;
    }
        
   
    public boolean isBallIn (TimeOfFlight sensor){
        double maxValue = 0;
        if(sensor == checkPointRight || sensor == checkPointLeft){
            maxValue = 100;
        } else {
            maxValue = 90;
        }
        if(sensor.getRange() < maxValue && sensor.getRange() > 25 ){
            return true;
        } else {
            return false;
        }
    }


    public void printTOFValues(){   
        SmartDashboard.putNumber("Time Of Flight Value Right Side ", checkPointRight.getRange());
        SmartDashboard.putNumber("Time Of Flight Value Intake Side ", checkPointOne.getRange());
        //SmartDashboard.putBoolean("Stage 1 ", stage1);
    }

    /**
     * 
     * @param centerSpeed Speed for center motor - left: negative; right: positive
     * @param leftSpeed Speed for left conveyor - in: positive; out: negative
     * @param rightSpeed Speed for right conveyor - in: positive; out: negative
     * @param feedSpeed Speed for feeder motor in conveyor - feed in: positive; feed out: negative
     */
    public void setConveyorMotors(double centerSpeed, double leftSpeed, double rightSpeed, double feedSpeed){
        rightSideMotor.set(ControlMode.PercentOutput, rightSpeed);
        leftSideMotor.set(ControlMode.PercentOutput, leftSpeed);
        centerMotor.set(ControlMode.PercentOutput, centerSpeed);
        feederMotor.set(ControlMode.PercentOutput, feedSpeed);
    }


    /**
     * @param leftSide Run left side of onveyor: true, else run right
     */
    public void engageConveyor(boolean leftSide){
        TimeOfFlight sensor;
        double direction = 0;
        if(leftSide){
            sensor = checkPointLeft;
            direction = 1.0;
        } else {
            direction = -1.0;
            sensor = checkPointRight;
        }
        //SmartDashboard.putString("State", "Waiting for ball");
        //SmartDashboard.putNumber("Balls Count", ballsCount);
        if(isBallIn(checkPointOne)){
            stage1 = true;
            //SmartDashboard.putString("State", "In first sensor");  
        } else {
            //SmartDashboard.putString("State", "Left first sensor");  
        }
        if(stage1){
            setConveyorMotors(direction * 0.5, 0.5, 0.5, 0);
            if(isBallIn(sensor)){
                
                //SmartDashboard.putString("State", "Under 2nd sensor");
                //SmartDashboard.putBoolean("Stage 1", false);
                //SmartDashboard.putBoolean("Stage 2", true);
                for(i = 0; i < 1; i++){
                    ballsCount = ballsCount + 1;    
                } 

                stage1 = false; 

            } else {
                
            }
        } else {
            setConveyorMotors(0, 0, 0, 0);
            
            //done = true;
            flag = false;
            i = 0;
            //SmartDashboard.putNumber("Balls Count", ballsCount);
        }
    }

    public void conveyorFeedTime(){
        while(j < 1 && !done){
            setTime();
            //System.out.println("set time once @ " + stopTime);
            j++;
        }

        if(time.get() > stopTime + .125){
            setConveyorMotors(0, 0, 0, 0);
            //System.out.println("time elapsed.");
            j = 0;
            done = true;
            //System.out.println(doneBoolean());
        } else {
            done = false;
            //System.out.println("time waiting: " + time.get() );
            setConveyorMotors(0, 0, 0, 0.5);
        }

    }

    public void resetI(){
        i = 0;
    }

    public boolean doneBoolean(){
        return done;
    }


    public void intake(){
        LEDRed();
        //countBalls(checkPointOne);
        SmartDashboard.putNumber("Balls count", ballsCount);
        //SmartDashboard.putNumber("intake sensor", checkPointOne.getRange());
         if(ballsCount == 1.0){ // first ball
            // run right side
            if(isBallIn(checkPointOne)){
                for(i = 0; i < 1; i++){
                    setTime();
                }
                leftSideMotor.set(ControlMode.PercentOutput, (.4));
                centerMotor.set(ControlMode.PercentOutput, (-0.4/2));
                rightSideMotor.set(ControlMode.PercentOutput, 0.375);
            } else {
                if(time.get() > stopTime + 0.2){
                    rightActivate(0);
                    i = 0;
                }
            }
        } else if(ballsCount == 2) { // 2nd ball
            // run left side 
            if(isBallIn(checkPointOne)){
                for(i = 0; i < 1; i++){
                    setTime();
                }
                rightSideMotor.set(ControlMode.PercentOutput, (.425));
                centerMotor.set(ControlMode.PercentOutput, (0.4/2));
                leftSideMotor.set(ControlMode.PercentOutput, 0.5);
            } else {
                rightSideMotor.set(ControlMode.PercentOutput, 0);
                if(time.get() > stopTime + 0.25){
                    rightActivate(0);
                    i = 0;
                }   
            }
        } else if(ballsCount == 3){ // 3rd ball
            // run right side again
            if(isBallIn(checkPointOne)){
                for(i = 0; i < 1; i++){
                    setTime();
                }
                leftSideMotor.set(ControlMode.PercentOutput, (.45));
                centerMotor.set(ControlMode.PercentOutput, (-0.45/2));
                rightSideMotor.set(ControlMode.PercentOutput, 0.45);
            } else {
                leftSideMotor.set(ControlMode.PercentOutput, 0);
                if(time.get() > stopTime + 0.275){
                    rightActivate(0);
                    i = 0;
                }
            }
        } else if(ballsCount == 4) {
            // run left side again
            if(isBallIn(checkPointOne)){
                for(i = 0; i < 1; i++){
                    setTime();
                }
                leftSideMotor.set(ControlMode.PercentOutput, (.4));
                centerMotor.set(ControlMode.PercentOutput, (0.4/2));
                rightSideMotor.set(ControlMode.PercentOutput, 0.375);
            } else {
                rightSideMotor.set(ControlMode.PercentOutput, 0);
                if(time.get() > stopTime + 0.25){
                    rightActivate(0);
                    i = 0;
                }
            }
        } else {
            
        }
    }

    public void leftWithSensor(){
        if(isBallIn(checkPointOne)){
            for(i = 0; i < 1; i++){
                setTime();
            }
            leftSideMotor.set(ControlMode.PercentOutput, (.4));
            centerMotor.set(ControlMode.PercentOutput, (0.4/2));
            rightSideMotor.set(ControlMode.PercentOutput, 0.375);
        } else {
            rightSideMotor.set(ControlMode.PercentOutput, 0);
            if(time.get() > stopTime + 0.25){
                rightActivate(0);
                i = 0;
            }
        }
    }

    public void rightWithSensor(){
        if(isBallIn(checkPointOne)){
            for(i = 0; i < 1; i++){
                setTime();
            }
            leftSideMotor.set(ControlMode.PercentOutput, (.4));
            centerMotor.set(ControlMode.PercentOutput, (-0.4/2));
            rightSideMotor.set(ControlMode.PercentOutput, 0.375);
        } else {
            if(time.get() > stopTime + 0.2){
                rightActivate(0);
                i = 0;
            }
        }
    }

    public void shooterFeed(){
        for(i = 0; i < 1; i++){
           // System.out.println("set time");
        }
       // SmartDashboard.putBoolean("Ready?", ready);
        if(time.get() >= stopTime + 3 && !ready) {
            i = 0;
            ready = true;
            //System.out.println("ready!");
            
        }

        if(ready){
    
            if(time.get() > stopTime + 6) {
                i = 0;
                conveyorFeed(0);
                isFinished = true;
                ready = false;
            } else {
                conveyorFeed(0.5);
                
            }
     
        }
    }

    public double getTime(){
        return time.get();
    }

    public void setTime(){
        stopTime = time.get();
    }

    public void startTime(){
        time.stop();
        time.reset();
        time.start();
    }

    public double getWaitTime(){
        return waitTime;
    }

    public void decrementBallCount(){
        ballsCount--;
    }

    public void incrementBallCount(){
        ballsCount++;
    }

       

    public void conveyorStop(){
        leftActivate(0);
        rightActivate(0);
    }

    public void resetBallCount(){
        ballsCount = 0;
        leftBallCount = 0;
        rightBallCount = 0;
    }
    
    public boolean isFinished(){
        return isFinished;
    }

    public void initIntake(){
        isFinished = false;
    }

    public void conveyorShooterRun(){

    }
  

  
  }
    
