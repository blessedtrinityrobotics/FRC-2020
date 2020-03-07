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
    private static TimeOfFlight checkPointOne   = new TimeOfFlight(Constants.checkPointOnePort);
    private static TimeOfFlight checkPointRight = new TimeOfFlight(Constants.checkPointRightPort);
    private static TimeOfFlight checkPointLeft  = new TimeOfFlight(Constants.checkPointLeftPort);
    private static PWMSparkMax ledMotor         = new PWMSparkMax(Constants.ledMotorPort);
    private int genericCount        = 0;
    private int ballsCount          = 0;
    private int rightBallCount      = 0;
    private int leftBallCount       = 0; 
    private boolean flag            = true;
    private boolean s               = false; 
    private boolean isFinished      = false;
    private Timer time;
    private double stopTime         = 0;
    private int i                   = 0;
    private double waitTime         = 0;
    private boolean ready           = false;


    public Conveyor() {
        leftSideMotor.setNeutralMode(NeutralMode.Brake);
        rightSideMotor.setNeutralMode(NeutralMode.Brake);
        centerMotor.setNeutralMode(NeutralMode.Brake);
        leftSideMotor.setInverted(false);
        rightSideMotor.setInverted(true);
        leftSideMotor.setNeutralMode(NeutralMode.Brake);
        rightSideMotor.setNeutralMode(NeutralMode.Brake);
        centerMotor.setNeutralMode(NeutralMode.Brake);
        checkPointOne.setRangingMode(RangingMode.Short, 24);
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
        //centerMotor.set(ControlMode.PercentOutput, (-speed/2));

    }

   

    public void countBalls(TimeOfFlight sensor){
        if(sensor.getRange() < 150 && flag){
            flag = false;
            ballsCount++;
        } else if(sensor.getRange() > 150 ){
            flag = true;
        }
    }



    public int getBallCount(int ballCount){
        return ballCount;
    }
        
   
    public boolean isBallIn (TimeOfFlight sensor){
        if(sensor.getRange() < 100 ){
            return true;
        } else {
            return false;
        }
    }


    public void printTOFValues(){   
        SmartDashboard.putNumber("Time Of Flight Value Intake Side ", checkPointOne.getRange());
    }

 
    public boolean leftStatus(){
        countBalls(checkPointLeft);
        if(leftBallCount >= 3){
            return true; 
        }
        else {
            return false; 
        }
    }

    public void runWithSensor(){
       LEDGreen();

            if(isBallIn(checkPointOne)){
                for(i = 0; i < 1; i++){
                    setTime();
                }
                rightSideMotor.set(ControlMode.PercentOutput, (.375));
                centerMotor.set(ControlMode.PercentOutput, (-0.375/2));
                leftSideMotor.set(ControlMode.PercentOutput, 0.5);
            } else {
                if(time.get() > stopTime + 0.125){
                    rightActivate(0);
                    i = 0;
                }
            }      
    }

    public void intake(){
        LEDRed();
        countBalls(checkPointOne);
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

    public boolean rightStatus(){ // during shooter
        countBalls(checkPointRight);
        if(rightBallCount >= 2){
            return true;
        }
        else{
            return false;
        }
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
    
