/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.Drivebase;
import frc.robot.subsystems.Limelight;

public class AlignLeft extends CommandBase {
  private final Drivebase drivebase;
  private final Limelight limelight;
  
  /**
   * Creates a new AlignLeft.
   */
  public AlignLeft(Drivebase subsystem, Limelight limeLightSub1) {
    // Use addRequirements() here to declare subsystem dependencies.
    drivebase = subsystem;
    limelight = limeLightSub1;
    addRequirements(drivebase,limelight);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    limelight.approachTargetWithVision(0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivebase.tankDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
