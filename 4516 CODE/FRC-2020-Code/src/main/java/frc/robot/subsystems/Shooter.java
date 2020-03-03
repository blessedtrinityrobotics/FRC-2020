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
      shooter1Slave.setInverted(true);
      shooter2Master.setInverted(false);
      shooter2Slave.setInverted(false);
      shooter1Master.configOpenloopRamp(2);
      shooter1Slave.configOpenloopRamp(2);
      shooter2Master.configOpenloopRamp(2);
      shooter2Slave.configOpenloopRamp(2);
      shooter1Master.setNeutralMode(NeutralMode.Coast);
      shooter1Slave.setNeutralMode(NeutralMode.Coast);
      shooter2Master.setNeutralMode(NeutralMode.Coast);
      shooter2Slave.setNeutralMode(NeutralMode.Coast);
      shooter1Master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
      shooter2Master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
      shooter1Master.setSensorPhase(true);
      shooter2Master.setSensorPhase(true);

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
    sensorVelocity = (Constants.CPR * RPM)/(600 * Constants.gearRatioShooter);

    // shooter1Master.set(ControlMode.Velocity, sensorVelocity, DemandType.Neutral, sensorVelocity);
    // shooter1Slave.follow(shooter1Master);
    // shooter2Master.set(ControlMode.Velocity, sensorVelocity, DemandType.Neutral, sensorVelocity);
    // shooter2Slave.follow(shooter1Master);
    SmartDashboard.putNumber("Shooter Speed in m/s", initVelocity);
    SmartDashboard.putNumber("RPM", RPM);
    SmartDashboard.putNumber("Shooter Sensor Speed", sensorVelocity);
  }

  public void setMotorSpeed(double speed){
    shooter1Master.set(ControlMode.PercentOutput, speed);
    shooter1Slave.set(ControlMode.PercentOutput, -speed);
    shooter2Master.set(ControlMode.PercentOutput, speed);
    shooter2Slave.set(ControlMode.PercentOutput, -speed);

  }

  


  
  }
    