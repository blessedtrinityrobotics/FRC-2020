/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Intake;

public class RunLeftSideSensor extends CommandBase {
  private Conveyor conveyor;
  private Intake intake;
  public RunLeftSideSensor(Intake subIntake, Conveyor subConveyor) {
    intake = subIntake;
    conveyor = subConveyor;
    addRequirements(intake, conveyor);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    conveyor.startTime();
    //conveyor.resetI();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    conveyor.conveyorFeedTime();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    //conveyor.setConveyorMotors(0, 0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return conveyor.doneBoolean();
  }
}
