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

public class EmptyIntake extends CommandBase {
  private final Conveyor conveyor;
  private final Intake intake;
  public EmptyIntake(Conveyor subsystem, Intake subsystem2) {
    conveyor = subsystem;
    intake = subsystem2;
    addRequirements(conveyor, intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intake.lowerIntake();
    intake.setIntakeMotors(-0.5);
    conveyor.conveyorFeed(-0.25);
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.setIntakeMotors(0);
    conveyor.conveyorFeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
