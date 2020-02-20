package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.playingwithfusion.TimeOfFlight;

import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Conveyor extends SubsystemBase {

    // Starts Conveyor Motors
    private static TalonSRX leftSideMotor      = new TalonSRX(Constants.leftSideMotorPort);
    private static VictorSPX rightSideMotor     = new VictorSPX(Constants.rightSideMotorPort);
    private static VictorSPX centerMotor        = new VictorSPX(Constants.centerMotorPort);
    private static TimeOfFlight checkPointOne   = new TimeOfFlight(Constants.checkPointOnePort);
    private static TimeOfFlight checkPointRight = new TimeOfFlight(Constants.checkPointRightPort);
    private static TimeOfFlight checkPointLeft  = new TimeOfFlight(Constants.checkPointLeftPort);
    private static PWMSparkMax ledMotor         = new PWMSparkMax(Constants.ledMotorPort);
    private int ballsCount = 0;
    private boolean s = false; 
    private boolean count = true;
    private boolean isFinished = false;
    private boolean check;


    public Conveyor() {
        leftSideMotor.setNeutralMode(NeutralMode.Brake);
        rightSideMotor.setNeutralMode(NeutralMode.Brake);
        centerMotor.setNeutralMode(NeutralMode.Brake);
        leftSideMotor.setInverted(false);
        rightSideMotor.setInverted(true);
        leftSideMotor.setNeutralMode(NeutralMode.Brake);
        rightSideMotor.setNeutralMode(NeutralMode.Brake);
        centerMotor.setNeutralMode(NeutralMode.Brake);
    }
  
    @Override
    public void periodic() {
        //comment
    }

    
    public void LEDRed(){
        SmartDashboard.putBoolean("Intake Complete?", false);
        ledMotor.set(Constants.red);
    }

    public void LEDGreen(){
        SmartDashboard.putBoolean("Intake Complete?", true);
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

    public void countBalls(){
        if(checkPointOne.getRange() < 150 && count){
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

        if(ballsCount <= 2){ // Enter loop b/w 0 balls and 2 balls
            checkSet(false);
            LEDRed(); // TURN LED RED
            if(isBallIn(checkPointRight) == true){ // Check if there is a ball right under shooter
                rightActivate(0);
                s = true; 
            } else { // No ball under shooter -> continue
                if(isBallIn(checkPointOne) == true){ // check if there is ball on the first sensor
                    rightActivate(0.75); // Run right intake when sensor detects ball
                } else {
                    rightActivate(0); // Stop when ball leaves the sensor
                }
            }
        } else { 
            s = true; // Set flag variable to true after 2 balls have entered
        }

        if(s==true && ballsCount <= 5){ // Flag set to true -> Switch conveyor sides
            if(isBallIn(checkPointLeft) == true){ // Check if there is a ball right under shooter
                leftActivate(0);
                s = true; 
                LEDGreen(); // TURN LED GREEN - INTAKE FINISHED
            } else { // No ball under shooter -> continue
                if(isBallIn(checkPointOne) == true){ // check if there is ball on the first sensor
                    leftActivate(0.75); // Run right intake when sensor detects ball
                } else {
                    leftActivate(0); // Stop when ball leaves the sensor
                }
            }
        } else { // Intake finished -> reset all variables
            s = false;
            isFinished = true;
            resetBallCount();
            checkSet(true);
        }
        
    }

    public void checkSet(boolean a){
        check = a;
    }

    public void conveyorShoot(){
        if(check==true&& (ballsCount>3)){
            if(isBallIn(checkPointOne)){
                leftActivate(-.5);
            }

            else if(check=true && (ballsCount>0)){
                rightActivate(-.5);
            }

            else{
                conveyorIntakeRun();
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
    
    public boolean isFinished(){
        return isFinished;
    }

    public void initIntake(){
        isFinished = false;
    }

    public void conveyorShooterRun(){

    }
  

  
  }
    
