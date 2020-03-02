/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;



public class ControlPanelArm extends SubsystemBase {

  private final VictorSPX controlPanelMotor = new VictorSPX(Constants.controlPanelMotorPort);
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
  //private final DoubleSolenoid controlPanelArm = new DoubleSolenoid(Constants.controlChannelPortOne, Constants.controlChannelPortTwo);
  private String gameData = "";
  private boolean isFinished = false;


  public ControlPanelArm() {
    controlPanelMotor.setNeutralMode(NeutralMode.Brake);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * 
   * @return Best color guess
   */
  public Color getColors(){
    return colorSensor.getColor();
  }

  /**
   * 
   * @return Best color guess in string
   */
  public String stringColor(){
    return "" + getColors().toString();
  }

  /**
   * 
   * @return Red value of detected color
   */
  public double getRedColor(){
    return colorSensor.getColor().red;
  }

  /**
   * 
   * @return Blue value of detected color
   */
  public double getBlueColor(){
    return colorSensor.getColor().blue;
  }

  /**
   * 
   * @return Green value of detected color
   */
  public double getGreenColor(){
    return colorSensor.getColor().green;
  }

  /**
   * Rotation Control on Control Panel
   */
  public void rotationControl(){
    //controlPanelArm.set(DoubleSolenoid.Value.kForward);
    setMotor(0.5);
  }

  /**
   * Stop motor and reverse it to stop Control Panel Rotation
   */
  public void stop(){
    setMotor(-0.1);
  }

  /**
   * Control Panel Arm Down and stop motor
   */
  public void armDown(){
    //controlPanelArm.set(DoubleSolenoid.Value.kReverse);
    setMotor(0);
  }

  /**
   * Find game data color
   */
  public void determineGameColor(){
    gameData = DriverStation.getInstance().getGameSpecificMessage();
  }

  /**
   * 
   * @param speed Speed to set Control Panel Motor
   */
  public void setMotor(double speed){
    controlPanelMotor.set(ControlMode.PercentOutput, speed);
  }

  /**
   * Position Control on Control Panel
   */
  public void positionControl(){
    if(gameData.length() > 0){

      switch (gameData.charAt(0)) {
        case 'B' :
        // game looking for blue -> robot needs to stop on blue -> but stop on green to slow down

        // if(getBlueColor() >= 200){

        // }
        if(colorSensor.getColor() == Color.kGreen) {
          setMotor(0);
          isFinished = true;
        } else {
          setMotor(0.5);
        }
        break;
        case 'G' : 
        // game looking for green ->   robot needs to stop on yellow -> but stop on red to slow down
        break;
        case 'R' :
        // game looking for red ->   robot needs to stop on blue -> but stop on yellow to slow down
        break;
        case 'Y' :
        // game looking for yellow ->   robot needs to stop on green -> but stop on blue to slow down
        break;
        default  :
        break;
      }

    } else {

    }
  }

  /**
   * Has Position Control Finished
   * @return If position control has finished
   */
  public boolean isFinished(){
    return isFinished;
  }
  

  
  
}
