/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
public class Intake extends SubsystemBase {
  /**
   * Creates a new Intake.
   */

    // Starts Intake Motors
  private static VictorSPX leftIntake   = new VictorSPX(Constants.leftIntakePort);
  private static VictorSPX rightIntake  = new VictorSPX(Constants.rightIntakePort);
  private DoubleSolenoid intakeSolenoid =  new DoubleSolenoid(Constants.solenoidChlTwo,Constants.solenoidChlOne);


  public Intake() {
    intakeSolenoid.clearAllPCMStickyFaults();
    leftIntake.setInverted(false);
    rightIntake.setInverted(true);
    leftIntake.setNeutralMode(NeutralMode.Brake);
    rightIntake.setNeutralMode(NeutralMode.Brake);
  }

    public void intakeUp(){
      intakeSolenoid.set(DoubleSolenoid.Value.kForward);
      setIntakeMotors(0);
    }

    public void intakeDown(){
      intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
      setIntakeMotors(.5);
    }

    public void setIntakeMotors(double speed){
      leftIntake.set(ControlMode.PercentOutput, speed);
      rightIntake.set(ControlMode.PercentOutput, speed);
    }
  
    public void stopIntake(){
      setIntakeMotors(0);
      intakeUp();
    }
  
    public void startIntake(){
      setIntakeMotors(0.75);
      intakeDown();
    }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
