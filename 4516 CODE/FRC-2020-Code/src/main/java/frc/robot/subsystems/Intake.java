/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;



public class Intake extends SubsystemBase {

  // Starts Drive Train GB Motors
  private static VictorSPX leftIntake   = new VictorSPX(Constants.leftIntake);
  private static VictorSPX rightIntake  = new VictorSPX(Constants.rightIntake);
 
 // private Compressor cp26Compressor = new Compressor();

  private DoubleSolenoid intakeSolenoid =  new DoubleSolenoid(Constants.solenoidChlTwo,Constants.solenoidChlOne );


  public Intake() {
    intakeSolenoid.clearAllPCMStickyFaults();
  }

  @Override
  public void periodic() {
      //comment
  }

  public void intakeDown(){
    intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
  }

  public void intakeUp(){
    intakeSolenoid.set(DoubleSolenoid.Value.kForward);
  }
  

}
  

