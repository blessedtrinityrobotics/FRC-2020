/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;

public class ConveyorFeedLeft extends CommandBase {
  private final Conveyor conveyor; 
  public ConveyorFeedLeft(Conveyor subsystem2) {
    conveyor = subsystem2;
    addRequirements(conveyor);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // reset conveyor/shooter
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Timer.delay(1);
    Robot.m_robotContainer.conveyor.leftActivate(.5);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Robot.m_robotContainer.conveyor.leftStatus();
  }
}