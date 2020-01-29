/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;



public class Intake extends SubsystemBase {

  // Starts Drive Train GB Motors
  public static VictorSPX leftIntake   = new VictorSPX(Constants.leftIntake);
  public static VictorSPX rightIntake  = new VictorSPX(Constants.rightIntake);



  public Intake() {

   
   
  }

  @Override
  public void periodic() {
      //comment
  }




  

}
  

