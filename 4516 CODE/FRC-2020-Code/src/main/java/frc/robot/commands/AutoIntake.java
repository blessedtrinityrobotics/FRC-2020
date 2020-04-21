/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Conveyor;

public class AutoIntake extends CommandBase {
  private Conveyor conveyor;
  private int ballCount = 0;
  private boolean stage2 = false;
  private boolean complete = false;
  public AutoIntake( Conveyor subConveyor) {

    conveyor = subConveyor;
    addRequirements(conveyor);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    conveyor.setBallCount(0);
    conveyor.startTime(); 
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    ballCount = conveyor.getBallCount();
    SmartDashboard.putNumber("Ball Count", conveyor.getBallCount());
    if(ballCount == 3){
      complete = true;
      conveyor.setDone(complete);
    } else {
      complete = false;
      conveyor.setDone(complete);
    }
    SmartDashboard.putBoolean("Intake Complete?", complete);
    
    //conveyor.rightWithSensor();
    if(ballCount == 0){               // right one
      conveyor.engageConveyor(true, 0.5);
    } else if(ballCount == 1) {       // left one
      conveyor.engageConveyor(false, 0.5);
      stage2 = false;
    } else if(ballCount == 2 && !stage2){
      //System.out.println("Moving balls up");
      conveyor.conveyorFeedTime();
      if(conveyor.doneBoolean()){
        //System.out.println("Ready for 3rd and 4th balls");
        stage2 = true;
      }
    } else if(stage2 && ballCount == 2) {
      //System.out.println("3rd");
      conveyor.engageConveyor(true, 0.375);
    } else {
      conveyor.setConveyorMotors(0, 0, 0, 0);
    }
   

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted){
    conveyor.setConveyorMotors(0, 0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return complete;  
  }
}
