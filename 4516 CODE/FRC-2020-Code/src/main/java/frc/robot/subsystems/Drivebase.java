/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.PigeonIMU;


public class Drivebase extends SubsystemBase {

  // Starts Drive Train GB Motors
  private static WPI_TalonFX leftMasterMotor   = new WPI_TalonFX(Constants.leftMasterMotorPort);
  private static WPI_TalonFX leftSlaveMotor    = new WPI_TalonFX(Constants.leftSlaveMotor1Port);
  private static WPI_TalonFX rightMasterMotor  = new WPI_TalonFX(Constants.rightMasterMotorPort);
  private static WPI_TalonFX rightSlaveMotor   = new WPI_TalonFX(Constants.rightSlaveMotor1Port);
  private static TalonSRX pigeonTalon          = new TalonSRX(Constants.pigeonIMUPort);
  private SpeedControllerGroup leftMotors = new SpeedControllerGroup(leftMasterMotor, leftSlaveMotor);
  private SpeedControllerGroup rightMotors = new SpeedControllerGroup(rightMasterMotor, rightSlaveMotor);


  private DifferentialDrive rDrive; 
  private DifferentialDriveOdometry driveOdometry;
  private Pose2d pose;
  private double rightDistance;
  private double leftDistance;

  private PigeonIMU gyro;

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
    leftMasterMotor.setSensorPhase(true); // Reverse Direction of encoder
    leftSlaveMotor.setSensorPhase(true);
    leftMasterMotor.configOpenloopRamp(1, Constants.kTimeoutMs); // % Ramp - 1 sec to full throtle
    leftMasterMotor.setNeutralMode(NeutralMode.Brake); // Neutral Mode - Coast
    leftSlaveMotor.setNeutralMode(NeutralMode.Brake); // Neutral Mode - Coast
    leftMasterMotor.setInverted(true);
    leftSlaveMotor.setInverted(true);
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
    rightMasterMotor.setSensorPhase(true); // Do not Reverse Direction of encoder
    rightSlaveMotor.setSensorPhase(true);
    rightMasterMotor.configOpenloopRamp(1, Constants.kTimeoutMs); // % Ramp - 1 sec to full throtle
    rightMasterMotor.setNeutralMode(NeutralMode.Brake); // Neutral Mode - Coast
    rightSlaveMotor.setNeutralMode(NeutralMode.Brake); // Neutral Mode - Coast
    rightMasterMotor.setInverted(true);
    rightSlaveMotor.setInverted(true);
    rightMasterMotor.configVoltageCompSaturation(Constants.operatingVoltage, Constants.kTimeoutMs);
    rightMasterMotor.setSelectedSensorPosition(0);

    pose = new Pose2d();

    rDrive = new DifferentialDrive(leftMotors, rightMotors);

    gyro = new PigeonIMU(pigeonTalon);
    resetYaw(0);
    driveOdometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getYaw()));
  }

  public void setCoastMode(){
    leftMasterMotor.setNeutralMode(NeutralMode.Coast); // Neutral Mode - Coast
    leftSlaveMotor.setNeutralMode(NeutralMode.Coast); // Neutral Mode - Coast
    rightMasterMotor.setNeutralMode(NeutralMode.Coast); // Neutral Mode - Coast
    rightSlaveMotor.setNeutralMode(NeutralMode.Coast); // Neutral Mode - Coast
  }

  public void setBrakeMode(){
    leftMasterMotor.setNeutralMode(NeutralMode.Brake); // Neutral Mode - Coast
    leftSlaveMotor.setNeutralMode(NeutralMode.Brake); // Neutral Mode - Coast
    rightMasterMotor.setNeutralMode(NeutralMode.Brake); // Neutral Mode - Coast
    rightSlaveMotor.setNeutralMode(NeutralMode.Brake); // Neutral Mode - Coast
  }


  @Override
  public void periodic() {
    driveOdometry.update(Rotation2d.fromDegrees(getYaw()), getWheelDistanceMeters(-leftMasterMotor.getSelectedSensorPosition()), getWheelDistanceMeters(rightMasterMotor.getSelectedSensorPosition()));
    System.out.println(driveOdometry.getPoseMeters().toString());
  }

  public void printPose(){
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

  public void resetDrivetrain(){
    resetYaw(0);
    leftMasterMotor.setSelectedSensorPosition(0);
    rightMasterMotor.setSelectedSensorPosition(0);
    rightDistance = 0;
    leftDistance = 0;
    driveOdometry.resetPosition(new Pose2d(), new Rotation2d(Math.toRadians(getYaw())));
  }

  
 

  /**
   * Distance traveled on left side
   */
  public void leftDistanceTraveled(){
    leftDistance = getWheelDistanceMeters(leftMasterMotor.getSelectedSensorPosition());
    
    SmartDashboard.putNumber("Left Distance Traveled", leftDistance );
  }

  /**
   * Distance traveled on right side
   */
  public void rightDistanceTraveled(){
    rightDistance = getWheelDistanceMeters(rightMasterMotor.getSelectedSensorPosition());
    SmartDashboard.putNumber("Right Distance Traveled", rightDistance );
  }

  /**
   * Left speed in m/s
   */
  public void leftSpeed(){
    double leftSpeed = getSpeedMetersPerSec(leftMasterMotor.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Left Speed m/s", leftSpeed); 
  }

  /**
   * Right speed in m/s
   */
  public void rightSpeed(){
    double rightSpeed = getSpeedMetersPerSec(rightMasterMotor.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Right Speed m/s", rightSpeed);
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
   * @param totalEncoderValue Enocder value driven measured by encoder
   * @return Distance traveled in meters
   * 
   */
  public double getWheelDistanceMeters(double totalEncoderValue){
    return (Math.PI * Constants.wheelDiameterMeters * totalEncoderValue)/(Constants.gearRatio * Constants.CPR);
  }

  /**
   * 
   * @param currentSpeed Speed measured by robot encoder (sensor units per 100 ms)
   * @return 
   * 
   */
  public double getWheelSpeed(double currentSpeed){
    double speedSecs = (currentSpeed / 0.1); // Raw Speed in sensor units per seconds
    double wheelSpeedSecs  = (speedSecs) / (Constants.CPR * Constants.gearRatio); // Wheel speed in sensor units per seconds
    return wheelSpeedSecs;
  }


  /**
   *
   * @param currentSpeed Speed measured by robot encoder 
   * @return Wheel speed in meters per seconds
   *
   */
  public double getSpeedMetersPerSec(double currentSpeed) {
    return (getWheelSpeed(currentSpeed) * (Constants.wheelDiameterMeters/2));
  }


  /**
   * 
   * @return Differential Drive Wheel Speeds in meters per seconds
   * 
   */
  public DifferentialDriveWheelSpeeds getWheelSpeeds(){

    return new DifferentialDriveWheelSpeeds(getSpeedMetersPerSec(-leftMasterMotor.getSelectedSensorVelocity()), getSpeedMetersPerSec(rightMasterMotor.getSelectedSensorVelocity()));
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

    rDrive.tankDrive(-leftVolts/Constants.operatingVoltage, -rightVolts/Constants.operatingVoltage);
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
    double currentAngle = getYaw();
    double targetAngle = angle;
    double speed = power;
    double kP = 0.0;
    double kD = 0.0;
    double derivative = 0;
    double previousError = 0;
    double error = targetAngle - currentAngle;
    derivative = ((error - previousError)/0.02);
    double turnCommand = (error * kP) +  (derivative * kD);
    tankDrive((speed - turnCommand), (speed + turnCommand));
    previousError = error;
  }

  public void turnToAngle(double angle){
    double currentAngle = getYaw();
    double targetAngle = angle;
    double kP = 0.0;
    double kD = 0.0;
    double derivative = 0;
    double previousError = 0;
    double error = targetAngle - currentAngle;
    derivative = ((error - previousError)/0.02);
    double turnCommand = (error * kP) + (derivative * kD);
    tankDrive(turnCommand, -turnCommand);
    previousError = error;
  }


  




}
