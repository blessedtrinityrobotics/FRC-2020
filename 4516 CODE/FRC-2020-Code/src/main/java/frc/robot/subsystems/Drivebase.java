/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.PigeonIMU;


public class Drivebase extends SubsystemBase {

  // Starts Drive Train GB Motors
  public static WPI_TalonFX leftMasterMotor   = new WPI_TalonFX(Constants.leftMasterMotorPort);
  public static WPI_TalonFX leftSlaveMotor    = new WPI_TalonFX(Constants.leftSlaveMotor1Port);
  public static WPI_TalonFX rightMasterMotor  = new WPI_TalonFX(Constants.rightMasterMotorPort);
  public static WPI_TalonFX rightSlaveMotor   = new WPI_TalonFX(Constants.rightSlaveMotor1Port);
  public static TalonSRX pigeonTalon          = new TalonSRX(Constants.pigeonIMUPort);
  SpeedControllerGroup leftMotors = new SpeedControllerGroup(leftMasterMotor, leftSlaveMotor);
  SpeedControllerGroup rightMotors = new SpeedControllerGroup(rightMasterMotor, rightSlaveMotor);


  public DifferentialDrive rDrive; 
  public DifferentialDriveOdometry driveOdometry;
  public Pose2d pose = new Pose2d();

  public PigeonIMU gyro;

  double [] ypr  = new double[3];

  public Drivebase() {

    // Configure Left GB Motors
    leftMasterMotor.selectProfileSlot(Constants.kSlot_Drive, Constants.PID_PRIMARY); // Profile Slot for PID Values
    leftMasterMotor.config_kP(Constants.kSlot_Drive, Constants.kGains_Drive.kP, Constants.kTimeoutMs); // P Value
    leftMasterMotor.config_kI(Constants.kSlot_Drive, Constants.kGains_Drive.kI, Constants.kTimeoutMs); // I Value
    leftMasterMotor.config_kD(Constants.kSlot_Drive, Constants.kGains_Drive.kD, Constants.kTimeoutMs); // D Value
    leftMasterMotor.config_kF(Constants.kSlot_Drive, Constants.kGains_Drive.kF, Constants.kTimeoutMs); // F Value
    //leftMasterMotor.configMotionAcceleration(Constants.kDriveTrainAccel, Constants.kTimeoutMs); // Motion Magic Acceleration Value
    //leftMasterMotor.configMotionCruiseVelocity(Constants.kDriveTrainVelocity, Constants.kTimeoutMs); // Motion Magic Velocity Value
    leftMasterMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, Constants.PID_PRIMARY, Constants.kTimeoutMs); // Select Sensor (Encoder)
    leftMasterMotor.setSensorPhase(false); // Reverse Direction of encoder
    leftMasterMotor.configOpenloopRamp(1, Constants.kTimeoutMs); // % Ramp - 1 sec to full throtle
    leftMasterMotor.setNeutralMode(NeutralMode.Coast); // Neutral Mode - Coast
    leftSlaveMotor.setNeutralMode(NeutralMode.Coast); // Neutral Mode - Coast
    leftMasterMotor.setInverted(false);
    leftSlaveMotor.setInverted(false);
    leftMasterMotor.configVoltageCompSaturation(Constants.operatingVoltage, Constants.kTimeoutMs);
    leftMasterMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, Constants.PID_PRIMARY, Constants.kTimeoutMs);
    leftMasterMotor.setSelectedSensorPosition(0);
    


    // Configure Right GB Motors
    rightMasterMotor.selectProfileSlot(Constants.kSlot_Drive, Constants.PID_PRIMARY); // Profile Slot for PID Values
    rightMasterMotor.config_kP(Constants.kSlot_Drive, Constants.kGains_Drive.kP, Constants.kTimeoutMs); // P Value
    rightMasterMotor.config_kI(Constants.kSlot_Drive, Constants.kGains_Drive.kI, Constants.kTimeoutMs); // I Value
    rightMasterMotor.config_kD(Constants.kSlot_Drive, Constants.kGains_Drive.kD, Constants.kTimeoutMs); // D Value
    rightMasterMotor.config_kF(Constants.kSlot_Drive, Constants.kGains_Drive.kF, Constants.kTimeoutMs); // F Value
    //rightMasterMotor.configMotionAcceleration(Constants.kDriveTrainAccel, Constants.kTimeoutMs); // Motion Magic Acceleration Value
    //rightMasterMotor.configMotionCruiseVelocity(Constants.kDriveTrainVelocity, Constants.kTimeoutMs); // Motion Magic Velocity Value
    rightMasterMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, Constants.PID_PRIMARY, Constants.kTimeoutMs); // Select Sensor (Encoder)
    rightMasterMotor.setSensorPhase(false); // Do not Reverse Direction of encoder
    rightMasterMotor.configOpenloopRamp(1, Constants.kTimeoutMs); // % Ramp - 1 sec to full throtle
    rightMasterMotor.setNeutralMode(NeutralMode.Coast); // Neutral Mode - Coast
    rightSlaveMotor.setNeutralMode(NeutralMode.Coast); // Neutral Mode - Coast
    rightMasterMotor.setInverted(false);
    rightSlaveMotor.setInverted(false);
    rightMasterMotor.configVoltageCompSaturation(Constants.operatingVoltage, Constants.kTimeoutMs);
    rightMasterMotor.setSelectedSensorPosition(0);


    rDrive = new DifferentialDrive(leftMotors, rightMotors);

    gyro = new PigeonIMU(pigeonTalon);


    driveOdometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getYaw()));

  }

  @Override
  public void periodic() {
    driveOdometry.update(Rotation2d.fromDegrees(getYaw()), getWheelDistanceMeters(leftMasterMotor.getSelectedSensorPosition()), getWheelDistanceMeters(-rightMasterMotor.getSelectedSensorPosition()));
    System.out.println(driveOdometry.getPoseMeters().toString());
  }


  /**
   * 
   * @return The Pose
   *
   */
  public Pose2d getPose(){
    return driveOdometry.getPoseMeters();
  }


  /**
   * 
   * @param totalEncoderValue Enocder value driven measured by encoder
   * @return Distance traveled in meters
   * 
   */
  public double getWheelDistanceMeters(double totalEncoderValue){
    return (Math.PI * Constants.wheelDiameterMeters * totalEncoderValue)/(Constants.gearRatio * Constants.CPR);
  }

  /**
   * Distance traveled on left side
   */
  public void leftDistanceTraveled(){
    double leftDistance = Math.abs(getWheelDistanceMeters(leftMasterMotor.getSelectedSensorPosition()));
    
    SmartDashboard.putNumber("Left Distance Traveled", leftDistance );
  }

  /**
   * Distance traveled on right side
   */
  public void rightDistanceTraveled(){
    double rightDistance = Math.abs(getWheelDistanceMeters(rightMasterMotor.getSelectedSensorPosition()));
    SmartDashboard.putNumber("Right Distance Traveled", rightDistance );
  }

  /**
   * Left speed in m/s
   */
  public void leftSpeed(){
    double leftSpeed = getWheelSpeed(leftMasterMotor.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Left Speed m/s", leftSpeed); 
  }

  /**
   * Right speed in m/s
   */
  public void rightSpeed(){
    double rightSpeed = getWheelSpeed(rightMasterMotor.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Right Speed m/s", rightSpeed);
  }

  /**
   * 
   * @param currentSpeed Speed measured by robot encoder
   * @return Wheel speed in sensor units per seconds 
   * 
   */
  public double getWheelSpeed(double currentSpeed){
    return ((currentSpeed / 600 ) * (Constants.CPR / Constants.gearRatio)) / (0.1);
  }


  /**
   *
   * @param currentSpeed Speed measured by robot encoder 
   * @return Wheel speed in meters per seconds
   *
   */
  public double getSpeedMetersPerSec(double currentSpeed) {
    return (getWheelSpeed(currentSpeed) * Constants.wheelDiameterMeters/2);
  }


  
  /**
   * 
   * @param leftPower Left side power
   * @param rightPower Right side power
   * 
   */
  public void tankDrive(double leftPower, double rightPower){
    rDrive.tankDrive(leftPower, rightPower); 
  }

  /**
   * Enables motor safety
   */
  public void enableSafety(){
    rDrive.setSafetyEnabled(true);
  }

  /**
   * Disables motor safety 
   */
  public void disableSafety(){
    rDrive.setSafetyEnabled(false);
  }



 

  /**
   * 
   * @param angle Angle to reset to
   * 
   */

  public void resetYaw(double angle){
    gyro.setYaw(angle);
    gyro.setFusedHeading(angle);

  }


  /**
   * 
   * @return Differential Drive Wheel Speeds in meters per seconds
   * 
   */
  public DifferentialDriveWheelSpeeds getWheelSpeeds(){

    return new DifferentialDriveWheelSpeeds(getSpeedMetersPerSec(-leftMasterMotor.getSelectedSensorVelocity()), getSpeedMetersPerSec(-rightMasterMotor.getSelectedSensorVelocity()));
  }


 
 


  /**
   * 
   * @return Yaw heading on gyro
   * 
   */
  public double getYaw(){
    gyro.getYawPitchRoll(ypr);
    return ypr[0];
  }



  /**
   * Drive with volts and enable voltage compensation
   * 
   * @param leftVolts Voltage for left motors
   * @param rightVolts Voltage for right motors
   * 
   */
  public void setDriveVolts(double leftVolts, double rightVolts){

    rDrive.tankDrive(leftVolts/Constants.operatingVoltage, -rightVolts/Constants.operatingVoltage);
  }


  /**
   * 
   * Disable Voltage Compensation
   * 
   */
  public void disableVoltageComp(){
    leftMasterMotor.enableVoltageCompensation(false);
    rightMasterMotor.enableVoltageCompensation(false);

  }

  /**
   * Enable Voltage Compensation
   */
  public void enableVoltageComp(){
    leftMasterMotor.enableVoltageCompensation(true);
    rightMasterMotor.enableVoltageCompensation(true);
  }


  /**
   * 
   * @param angle Angle to drive straight to
   * @param direction Direction to drive straight; 1.0 is Forward, -1.0 is backwards
   *  
   */
  
  public void driveToAngle(double angle, double power){
    for( int i = 0; i < 1; i++){
      resetYaw(0);
    }
    gyro.getYawPitchRoll(ypr);
    // Yaw = ypr[0]
    // Pitch = ypr[1]
    // Roll = ypr[2]
    double currentAngle = ypr[0];
    double targetAngle = angle;
    double speed = power;
    double kP = 0.0;
    double kD = 0.0;
    double derivative = 0;
    double previousError = 0;
    double error = targetAngle - currentAngle;
    derivative = ((error - previousError)/0.02);
    double turnCommand = (error * kP) +  (derivative * kD);
    previousError = error;
    leftMasterMotor.set(ControlMode.PercentOutput, (speed - turnCommand));
    leftSlaveMotor.follow(leftMasterMotor);
    rightMasterMotor.set(ControlMode.PercentOutput, (speed + turnCommand));
    rightSlaveMotor.follow(rightMasterMotor);

  

  }


  /**
   * 
   * @return left encoder
   */
  public double getLeftEncoder(){
    return leftMasterMotor.getSelectedSensorPosition(); 
  }

  /**
   * 
   * @return right encoder
   */
  public double getRightEncoder(){
    return rightMasterMotor.getSelectedSensorPosition(); 
  }

  public double getLeftVelocity(){
    return leftMasterMotor.getSelectedSensorVelocity();
  }

  public double getRightVelocity(){
    return rightMasterMotor.getSelectedSensorVelocity();

  }
  


  public void shooterRPM(double deltaX){
    double sensorVelocity = (Constants.CPR * (  Math.sqrt(
                                                    (2 * Constants.gInchSecondsSquared * Constants.outerPortHeightDelta) 
                                                      + 
                                                    ( (2 * Constants.gInchSecondsSquared * Constants.outerPortHeightDelta) 
                                                      / 
                                                      ( Math.pow( Math.tan(Math.toRadians(Constants.launchAngle) ), 2) ) )
                                                  ) 
                                                  / 
                                                  ( 6 * Constants.shooterRadius )   
                                                
                                              )
                            )
                            /
                            (600 * Constants.gearRatioShooter);
    leftMasterMotor.set(ControlMode.Velocity, sensorVelocity, DemandType.Neutral, sensorVelocity);
  }



}
