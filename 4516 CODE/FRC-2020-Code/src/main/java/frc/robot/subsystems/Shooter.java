package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {

    // Starts Shooter Motors
    private static TalonSRX shooter1Master = new TalonSRX(Constants.shooter1MasterPort);
    private static VictorSPX shooter1Slave = new VictorSPX(Constants.shooter1SlavePort);
    private static TalonSRX shooter2Master = new TalonSRX(Constants.shooter2MasterPort);
    private static VictorSPX shooter2Slave = new VictorSPX(Constants.shooter2SlavePort);
    private  double sensorVelocity;
    private double RPM;
    private double initVelocity;

    
    public Shooter() {
      shooter1Master.setInverted(true);
      shooter1Slave.setInverted(false);
      shooter2Master.setInverted(false);
      shooter2Slave.setInverted(false);
      shooter1Master.setNeutralMode(NeutralMode.Coast);
      shooter1Slave.setNeutralMode(NeutralMode.Coast);
      shooter2Master.setNeutralMode(NeutralMode.Coast);
      shooter2Slave.setNeutralMode(NeutralMode.Coast);
      shooter1Master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
      shooter2Master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
      shooter1Master.setSensorPhase(true);
      shooter2Master.setSensorPhase(true);
      shooter1Master.enableCurrentLimit(true);
      shooter1Master.configContinuousCurrentLimit(40);
      shooter2Master.enableCurrentLimit(true);
      shooter2Master.configContinuousCurrentLimit(40);
      shooter1Master.selectProfileSlot(Constants.kSlot_Shooter, Constants.PID_PRIMARY); // Profile Slot for PID Values
      shooter1Master.config_kP(Constants.kSlot_Shooter, Constants.kGains_Shooter.kP, Constants.kTimeoutMs); // P Value
      shooter1Master.config_kI(Constants.kSlot_Shooter, Constants.kGains_Shooter.kI, Constants.kTimeoutMs); // I Value
      shooter1Master.config_kD(Constants.kSlot_Shooter, Constants.kGains_Shooter.kD, Constants.kTimeoutMs); // D Value
      shooter1Master.config_kF(Constants.kSlot_Shooter, Constants.kGains_Shooter.kF, Constants.kTimeoutMs); // F Value

      shooter2Master.selectProfileSlot(Constants.kSlot_Shooter, Constants.PID_PRIMARY); // Profile Slot for PID Values
      shooter2Master.config_kP(Constants.kSlot_Shooter, Constants.kGains_Shooter.kP, Constants.kTimeoutMs); // P Value
      shooter2Master.config_kI(Constants.kSlot_Shooter, Constants.kGains_Shooter.kI, Constants.kTimeoutMs); // I Value
      shooter2Master.config_kD(Constants.kSlot_Shooter, Constants.kGains_Shooter.kD, Constants.kTimeoutMs); // D Value
      shooter2Master.config_kF(Constants.kSlot_Shooter, Constants.kGains_Shooter.kF, Constants.kTimeoutMs); // F Value

    }
  
    @Override
    public void periodic() {
        //comment
    }
  
    /**
 * 
 * @param deltaX X distance from target
 * 
 */
  public void shooterRPM(double deltaX){
    initVelocity = Math.sqrt( ( (-Constants.gravity * deltaX)/( ((Constants.outerPortHeightDelta * Math.cos(Math.toRadians(Constants.launchAngle)))/(deltaX)) - Math.sin(Math.toRadians(Constants.launchAngle)) ) )/( 2 * Math.cos( Math.toRadians(Constants.launchAngle)) ) );
    RPM = (60* initVelocity)/(2 * Math.PI * Constants.shooterRadius);
    sensorVelocity = (Constants.CPR * RPM)/(60 * Constants.gearRatioShooter
    );

    shooter1Master.set(ControlMode.Velocity, sensorVelocity, DemandType.Neutral, sensorVelocity);
    shooter1Slave.follow(shooter1Master);
    shooter2Master.set(ControlMode.Velocity, sensorVelocity, DemandType.Neutral, sensorVelocity);
    shooter2Slave.follow(shooter1Master);
    SmartDashboard.putNumber("Shooter Speed in m/s", initVelocity);
    SmartDashboard.putNumber("RPM", RPM);
    SmartDashboard.putNumber("Shooter Sensor Speed", sensorVelocity);
  }

  public void setMotorSpeed(double speed){
    shooter1Master.set(ControlMode.PercentOutput, speed);
    shooter1Slave.set(ControlMode.PercentOutput, speed);
    shooter2Master.set(ControlMode.PercentOutput, speed);
    shooter2Slave.set(ControlMode.PercentOutput, speed);

  }

  public void setSpeed(double speed){
    //double ff = Constants.shooterFF.calculate(speed)/12;
    shooter1Master.set(ControlMode.Velocity, speed, DemandType.Neutral, speed);
    shooter1Slave.follow(shooter1Master);
    shooter2Master.set(ControlMode.Velocity, speed, DemandType.Neutral, speed);
    shooter2Slave.follow(shooter1Master);
    SmartDashboard.putNumber("Shooter speed", shooter1Master.getSelectedSensorVelocity());
  }

  


  
  }
    