/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;


/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {


  // Motor Ports
    // Left Drive Train GB Motors
    public static final int  leftMasterMotorPort     = 5;
    public static final int  leftSlaveMotor1Port     = 8;
    // Right Drive Train GB Motors
    public static final int  rightMasterMotorPort    = 7;
    public static final int  rightSlaveMotor1Port    = 4;
    // Control Panel Motor
    public static final int controlPanelMotorPort    = 0;
    // Intake Motors
    public static final int leftIntakePort           = 9;
    public static final int rightIntakePort          = 10; 
    // Conveyor Motors
    public static final int leftSideMotorPort        = 3;
    public static final int rightSideMotorPort       = 14;
    public static final int centerMotorPort          = 15;
    public static final int shooterFeedMotorPort     = 11;
    // Shooter Motors
    public static final int shooter1MasterPort       = 6; 
    public static final int shooter1SlavePort        = 12;
    public static final int shooter2MasterPort       = 2;
    public static final int shooter2SlavePort        = 13;
  //End of Motor Ports

  // Time of Flight Sensor Ports
    public static final int checkPointOnePort        = 16;
    public static final int checkPointRightPort      = 17;
    public static final int checkPointLeftPort       = 18;
  // End of ToF Sensor Ports


  // Pigeon IMU
    public static final int pigeonIMUPort         = 3;
  //End of Pigeon IMU

  // Pneumatic Ports
    public static final int solenoidChlTwo        = 1; // intake pneumatic port one
    public static final int solenoidChlOne        = 0; // intake pneumatic port two
    public static final int controlChannelPortOne = 0; // control panel arm port one
    public static final int controlChannelPortTwo = 0; // control panel arm port two
  // End of Pneumatic Ports

  // XboxController Ports
    public static final int driveControllerPort      = 0;
    public static final int operatorControllerPort   = 1;
  //End of XboxController Ports

  //Constants
    public static final int operatingVoltage          = 10;
    public static final int wheelDiameter             = 6;    // Wheel Diameter In Inches
    public static final double wheelDiameterMeters    = wheelDiameter/39.37;  // Wheel diameter in Meters
    public static final double gearRatio              = 9.97; // Gear Ratio
    public static final int CPR                       = 2048; // Falcon 500 encoder Counts per Revolution
    public static final int kMaxRPM                   = 6380; // Falcon 500 RPM
    public static final double turningPower           = 0.625;  // Turning Power for Drive (%)
    public static final double waitTime               = 1.0;  // Time to wait before grabing final gyro angle for vision approach
  //End of Constants

  //Axis
    public static final int leftStickX       = 0;  
    public static final int leftStickY       = 1;
    public static final int leftTriggerAxis  = 2;
    public static final int rightTriggerAxis = 3;
    public static final int rightStickX      = 4;
    public static final int rightStickY      = 5;
  //End of Axis

  //Buttons
    public static final int aButton           = 1;
    public static final int bButton           = 2;
    public static final int xButton           = 3;
    public static final int yButton           = 4;
    public static final int leftBumperButton  = 5;
    public static final int rightBumperButton = 6;
    public static final int backButton        = 7;
    public static final int startButton       = 8;
    public static final int leftStickButton   = 9;
    public static final int rightStickButton  = 10;
  //End of Buttons

  //LEDMOTOR
    public static final int ledMotorPort    = 9;
    public static final double red          = 0.61;
    public static final double rainbow      = -0.99;
    public static final double green        = 0.77;
    public static final double black        = 0.99 ;  

  /**
	 * Set to zero to skip waiting for confirmation.
	 * Set to nonzero to wait and report to DS if action fails.
	 */
  public final static int kTimeoutMs = 30;
    
  //DriveTrain Constants

  public static final double ksVolts                                = 0.105;
  public static final double kvVoltSecondsPerMeter                  = 2.28;
  public static final double kaVoltSecondsSquaredPerMeter           = 0.269;
  public static final double kPDriveVel                             = 17.5;
  public static final double kTrackwidthMeters                      = 0.83;
  public static final double kMaxSpeedMetersPerSecond               = 4.5;
  public static final double kMaxAccelerationMetersPerSecondSquared = 2.0;
  public static final double kRamseteB                              = 2.0;
  public static final double kRamseteZeta                           = 0.7;

  public static final DifferentialDriveKinematics kDriveKinematics  = 
        new DifferentialDriveKinematics(kTrackwidthMeters);
  
  public static final SimpleMotorFeedforward kDriveFF = 
        new SimpleMotorFeedforward(ksVolts, kvVoltSecondsPerMeter, kaVoltSecondsSquaredPerMeter);

  public static final DifferentialDriveVoltageConstraint autoVoltageConstraint = 
        new DifferentialDriveVoltageConstraint(kDriveFF, kDriveKinematics, operatingVoltage);

  public static final TrajectoryConfig defaultConfig = 
        new TrajectoryConfig(kMaxSpeedMetersPerSecond, 
                             kMaxAccelerationMetersPerSecondSquared)
            .setKinematics(kDriveKinematics)
            .addConstraint(autoVoltageConstraint);

  public static final TrajectoryConfig reverseConfig = 
            new TrajectoryConfig(kMaxSpeedMetersPerSecond/2, 
                                 kMaxAccelerationMetersPerSecondSquared/3)
                .setKinematics(kDriveKinematics)
                .addConstraint(autoVoltageConstraint)
                .setReversed(true);
            
  public static final TrajectoryConfig slowConfig = 
        new TrajectoryConfig(kMaxSpeedMetersPerSecond/2, 
                             kMaxAccelerationMetersPerSecondSquared/3)
                            .setKinematics(kDriveKinematics)
                            .addConstraint(autoVoltageConstraint);
  




// Shooter Constants
public static final double gravity               = 9.81;   // Gravity in meters per second per second (Positive as negative cancel out later)
public static final double outerPortHeightDelta  = 1.88;   // Delta between outer port and center of shooter
public static final double launchAngle           = 40;     // Degrees of launch angle (from horizontal)
public static final double shooterRadius         = 0.051;  // Shooter wheel radius in meters
public static final double gearRatioShooter      = 4;      // Gear Ratio
public static final double cameraAngle           = 25;     // Limelight Camera Angle 






  // Shooter Constants
  public static final double shooterkS = 0.576;
  public static final double shooterkA = 0.163;
  public static final double shooterkV = 0.0589;
  public static final double shooterkP = 1.06;

  public static final SimpleMotorFeedforward shooterFF = new SimpleMotorFeedforward(shooterkS, shooterkV, shooterkA);

  /**
   * PID Gains may have to be adjusted based on the responsiveness of control loop.
     * kF: 1023 represents output value to Talon at 100%, 6800 represents Velocity units at 100% output
     * Not all set of Gains are used in this project and may be removed as desired.
     * 
   * 	                                    	            kP         kI   kD   kF   Iz   PeakOut */
  public final static Gains kGains_Drive   = new Gains( 0.0,       0.0, 0.0, 0.0, 100, 0.50 );
  public final static Gains kGains_Shooter = new Gains( 0.75,      0.0, 0.1, 0.1, 100, 0.50 );
 


  /** ---- Flat constants, you should not need to change these ---- */
  /* We allow either a 0 or 1 when selecting an ordinal for remote devices [You can have up to 2 devices assigned remotely to a talon/victor] */
  public final static int REMOTE_0 = 0;
  public final static int REMOTE_1 = 1;
  /* We allow either a 0 or 1 when selecting a PID Index, where 0 is primary and 1 is auxiliary */
  public final static int PID_PRIMARY = 0;
  public final static int PID_TURN    = 1;
  /* ---- Named slots, used to clarify code ---- */
  public final static int kSlot_Drive = 0;
  public final static int kSlot_Shooter = 0;





}
