/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;

public class Limelight extends SubsystemBase {
  private boolean validTarget = false;
  private boolean LEDStatus = true; 
  private double steer_cmd = 0;
  private double drive_cmd = 0;
  private double STEER_DERIVATIVE;
  private double STEER_ERROR_PRIOR;
  private double STEER_INTEGRAL;
  private boolean isFinished = false;

  public Limelight() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }


  /**
   * This function implements a simple method of generating driving and steering commands
   * based on the tracking data from a limelight camera.
  */
  public void approachTargetWithVision(double xTarget) {
    final double STEER_P = 0.025;                    
    final double DRIVE_P = 0.0;    
    final double STEER_D = 0.005;   
    final double STEER_I = 0.015;              
    final double targetDistance = 132;  // Inches to target       
    final double maxDrive = 0.75;        // Simple speed limit so we don't drive too fast
    final double xError;
    final double distanceError;
    final double distance;

    double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    //SmartDashboard.putNumber("TX Error", tx);
    //SmartDashboard.putNumber("TY Error", ty);
    xError = xTarget - tx;
    distance = (1.55)/(Math.tan( Math.toRadians(ty + Constants.cameraAngle)));
    SmartDashboard.putNumber("Distance", distance);
    //SmartDashboard.putNumber("TY", ty);
    distanceError = (targetDistance - distance);
    STEER_DERIVATIVE = (xError - STEER_ERROR_PRIOR)/0.02;
    STEER_INTEGRAL = STEER_INTEGRAL + (xError*0.02);
    
    if (tv < 1.0) {
      validTarget = false;
      drive_cmd = 0.0;
      steer_cmd = 0.0;
    } else {
      validTarget = true;
      // Start with proportional steering
      steer_cmd = (xError * STEER_P) + (STEER_DERIVATIVE * STEER_D) + (STEER_INTEGRAL * STEER_I);
      //SmartDashboard.putNumber("Steer Command", steer_cmd);
      // try to drive forward until the target area reaches our desired area
      //drive_cmd = (distanceError * DRIVE_P);
      // don't let the robot drive too fast into the goal
      if (drive_cmd > maxDrive){
        drive_cmd = maxDrive;
      }
    }
    Robot.m_robotContainer.drivetrain.tankDrive(steer_cmd, -steer_cmd);

    STEER_ERROR_PRIOR = xError;

    if(xError <= 5 && distanceError <= 5){
      isFinished = true;
    }

  }

  public boolean getLEDStatus(){
    return LEDStatus;
  }

  /**
   * 
   * @return Boolean if approach target has finished
   */
  public boolean isFinished(){
    return isFinished;
  }

  

  /**
   * 
   * @return Boolean if the light is on
   */
  public boolean checkTarget(){
    double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);

    if(tv >= 1.00) {
      SmartDashboard.putBoolean("Valid Vision Target", true);  
      return true;
    } else {
      SmartDashboard.putBoolean("Valid Vision Target", false); 
      return false;
    }
  }

  /**
   * Toggle LED and Vision tracking
   */
  public void toggleVision(){
    if (LEDStatus) {
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
      LEDStatus = false; 
    } else {
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(0);
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);  
      LEDStatus = true; 
    }
  }

  /**
   * Clear vision tracking variables
   */
  public void clearVars(){
    STEER_INTEGRAL = 0;
    STEER_DERIVATIVE = 0;
  }

  public boolean LEDStatus(){
    return LEDStatus;
  }

  public double getDistance(){
    double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    double distance = (1.219)/(Math.tan( Math.toRadians(ty + Constants.cameraAngle)));

    return distance;
  }

  

  



}
