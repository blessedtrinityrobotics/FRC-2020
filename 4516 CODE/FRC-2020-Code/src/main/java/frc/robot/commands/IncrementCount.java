/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Conveyor;

public class IncrementCount extends CommandBase {
  private Conveyor conveyor;
  private boolean done = false;
  public IncrementCount(Conveyor subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    conveyor = subsystem;
    addRequirements(conveyor);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    done = false;
    conveyor.incrementBallCount();

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    done = true;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return done;
  }
}
