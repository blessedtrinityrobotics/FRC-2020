package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.playingwithfusion.TimeOfFlight;

import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Conveyor extends SubsystemBase {

    // Starts Conveyor Motors
    private static VictorSPX leftSideMotor      = new VictorSPX(Constants.leftSideMotorPort);
    private static VictorSPX rightSideMotor     = new VictorSPX(Constants.rightSideMotorPort);
    private static VictorSPX centerMotor        = new VictorSPX(Constants.centerMotorPort);
    private static TimeOfFlight checkPointOne   = new TimeOfFlight(Constants.checkPointOnePort);
    private static TimeOfFlight checkPointRight = new TimeOfFlight(Constants.checkPointRightPort);
    private static TimeOfFlight checkPointLeft  = new TimeOfFlight(Constants.checkPointLeftPort);
    private static PWMSparkMax ledMotor         = new PWMSparkMax(Constants.ledMotorConstant);
    private int ballsCount = 0;
    private boolean s = false; 
    private boolean count = true;


    public Conveyor() {
        leftSideMotor.setNeutralMode(NeutralMode.Brake);
        rightSideMotor.setNeutralMode(NeutralMode.Brake);
        centerMotor.setNeutralMode(NeutralMode.Brake);
        leftSideMotor.setInverted(false);
        rightSideMotor.setInverted(true);
    }
  
    @Override
    public void periodic() {
        //comment
    }

    public void blinkRed(){
        ledMotor.set(Constants.red);
    }

    public void blinkGreen(){
        ledMotor.set(Constants.blue);
    }

    public void blinkRainbow(){
        ledMotor.set(Constants.rainbow);
    }


    public void leftActivate(double speed){
        leftSideMotor.set(ControlMode.PercentOutput, (speed/2));
        centerMotor.set(ControlMode.PercentOutput, speed);
    }

    public void rightActivate(double speed){
        rightSideMotor.set(ControlMode.PercentOutput, (speed/2));
        centerMotor.set(ControlMode.PercentOutput, -speed);
    }

    public void countBalls(){
        if(checkPointOne.getRange() < 100 && count){
            ballsCount++;
            count = false;
        } else if(checkPointOne.getRange() > 150 ){
            count = true;
        }
    }

    public int getBallCount(){
        return ballsCount;
    }
        
   
    public boolean isBallIn (TimeOfFlight sensor){
        if(sensor.getRange() < 100 ){
            return true;
        } else {
            return false;
        }
    }

    public void conveyorIntakeRun(){
        if(getBallCount()<2){
            blinkRed();
            if(isBallIn(checkPointRight)==false){
                rightActivate(.5);
                blinkRed();
            } else {
                rightActivate(0);
                blinkRed();
                s = true; 
            }
        }

        if(s==true){
            if(isBallIn(checkPointLeft)==false){
                leftActivate(.5);
                blinkRed();
            } else {
                leftActivate(0);
                s = false; 
                resetBallCount();
                blinkGreen();
            }
        }
    }
    
    public void conveyorStop(){
        leftActivate(0);
        rightActivate(0);
    }

    public void resetBallCount(){
        ballsCount = 0;
    }
    

  

  
  }
    
