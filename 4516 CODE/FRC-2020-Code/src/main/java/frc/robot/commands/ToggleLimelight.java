/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.Limelight;

public class ToggleLimelight extends CommandBase {
  private final Limelight limelight;
  private boolean done = false;

  public ToggleLimelight(Limelight subsystem) {
    limelight = subsystem;
    addRequirements(limelight);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Robot.m_robotContainer.limelight.toggleVision();
    done = false;
    
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
