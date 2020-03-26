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
import frc.robot.subsystems.Intake;

public class RunRightSideSensor extends CommandBase {
  private Conveyor conveyor;
  private Intake intake;
  private int ballCount = 0;
  private boolean stage2 = false;
  public RunRightSideSensor(Intake subIntake, Conveyor subConveyor) {
    intake = subIntake;
    conveyor = subConveyor;
    addRequirements(intake, conveyor);
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
    //intake.intakeDown(0.75);

    
    //conveyor.rightWithSensor();
    if(ballCount == 0){               // right one
      conveyor.engageConveyor(false);
      //conveyor.setBallCount(1);
    } else if(ballCount == 1) {       // left one
      conveyor.engageConveyor(true);
      //conveyor.setBallCount(2);
    } else if(ballCount == 2 && !stage2){
      //System.out.println("Moving balls up");
      conveyor.conveyorFeedTime();
      if(conveyor.doneBoolean()){
        System.out.println("Ready for 3rd and 4th balls");
        stage2 = true;
      }
    } else if(stage2 && ballCount == 2) {
      conveyor.engageConveyor(false);
    } else if(ballCount == 3){
      conveyor.engageConveyor(true);
    }
   
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.setIntakeMotors(0);
    conveyor.setConveyorMotors(0, 0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
