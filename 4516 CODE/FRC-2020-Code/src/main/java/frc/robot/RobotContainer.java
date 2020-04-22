/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.AlignRobotCenter;
import frc.robot.commands.AutoIntake;
import frc.robot.commands.AutoShoot;
import frc.robot.commands.CheckForValidTarget;
import frc.robot.commands.ConveyorFeed;
import frc.robot.commands.ConveyorReverse;
import frc.robot.commands.Drive;
import frc.robot.commands.EmptyIntake;
import frc.robot.commands.IntakeDown;
import frc.robot.commands.IntakeUp;
import frc.robot.commands.ResetPose;
import frc.robot.commands.Shoot;
import frc.robot.commands.ShooterFeedReverse;
import frc.robot.commands.SortConveyor;
import frc.robot.commands.SpinIntake;
import frc.robot.commands.SplitConveyor;
import frc.robot.commands.ToggleLimelight;
import frc.robot.commands.UnJam;
import frc.robot.commands.resetBalls;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivebase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.controller.PIDController;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  public Drivebase drivetrain = new Drivebase();
  public Intake intake        = new Intake();
  public Conveyor conveyor    = new Conveyor();
  public Limelight limelight  = new Limelight();
  public Shooter shooter      = new Shooter();


  SendableChooser<String> autoChooser = new SendableChooser<>();



  // Starts Xbox Controllers
  public XboxController driverController   = new XboxController(Constants.driveControllerPort);
  public XboxController operatorController = new XboxController(Constants.operatorControllerPort);

  // Starts Driver Buttons
  Button xButtonDriver             = new JoystickButton(driverController, Constants.xButton);
  Button aButtonDriver             = new JoystickButton(driverController, Constants.aButton);
  Button bButtonDriver             = new JoystickButton(driverController, Constants.bButton);
  Button yButtonDriver             = new JoystickButton(driverController, Constants.yButton);
  Button backButtonDriver          = new JoystickButton(driverController, Constants.backButton);
  Button startButtonDriver         = new JoystickButton(driverController, Constants.startButton);
  Button leftBumperButtonDriver    = new JoystickButton(driverController, Constants.leftBumperButton);
  Button rightBumperButtonDriver   = new JoystickButton(driverController, Constants.rightBumperButton);
  Button leftStickButtonDriver     = new JoystickButton(driverController, Constants.leftStickButton);
  Button rightStickButtonDriver    = new JoystickButton(driverController, Constants.rightStickButton);

  // Starts Operator Buttons
  Button xButtonOperator           = new JoystickButton(operatorController, Constants.xButton);
  Button aButtonOperator           = new JoystickButton(operatorController, Constants.aButton);
  Button bButtonOperator           = new JoystickButton(operatorController, Constants.bButton);
  Button yButtonOperator           = new JoystickButton(operatorController, Constants.yButton);
  Button backButtonOperator        = new JoystickButton(operatorController, Constants.backButton);
  Button startButtonOperator       = new JoystickButton(operatorController, Constants.startButton);
  Button leftBumperButtonOperator  = new JoystickButton(operatorController, Constants.leftBumperButton);
  Button rightBumperButtonOperator = new JoystickButton(operatorController, Constants.rightBumperButton);
  Button leftStickButtonOperator   = new JoystickButton(operatorController, Constants.leftStickButton);
  Button rightStickButtonOperator  = new JoystickButton(operatorController, Constants.rightStickButton);

 
  

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    drivetrain.setDefaultCommand( new Drive(drivetrain));    
    limelight.setDefaultCommand(new CheckForValidTarget(limelight)); 

    configureButtonBindings();   
    
    autoChooser.addOption("Drive Off Left", "leftDrive");
    autoChooser.addOption("Shoot Right Drive", "RshootDrive");
    //autoChooser.addOption("Drive Middle Shoot", "DriveMShoot");
    autoChooser.addOption("Drive Off Right", "rightDrive");
    autoChooser.setDefaultOption("Drive Off Forward", "driveOff");
    //autoChooser.addOption("David Drive", "davieDrive");
    autoChooser.addOption("Shoot", "shoot");
    autoChooser.addOption("Drive Bak", "back");
    autoChooser.addOption("6 Ball Trench Auto", "6trench");
    Shuffleboard.getTab("Autonomous").add(autoChooser);

  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
        
    //Intake Commands 
    //aButtonOperator.whenPressed(new IntakeProcedure(intake,conveyor)); // Intake down and start intake procedure
    aButtonDriver.whileHeld(new AlignRobotCenter(drivetrain, limelight)); // Align to target
    bButtonDriver.whenPressed(new ToggleLimelight(limelight)); // Turn on/off limelight
    yButtonDriver.whenPressed(new ResetPose(drivetrain));
    xButtonDriver.whenPressed(new UnJam(conveyor));
    
    //leftStickButtonOperator.whileHeld(new ConveyorReverse(conveyor));
    //rightStickButtonOperator.whenPressed(new resetBalls(conveyor));

    leftBumperButtonOperator.whileHeld(new SpinIntake(intake));
    rightBumperButtonOperator.whenPressed(new SortConveyor(conveyor));
    bButtonOperator.whenPressed(new IntakeUp(intake)); // Intake up and stop intake procedure
    aButtonOperator.whileHeld(new EmptyIntake(conveyor, intake));
    xButtonOperator.whileHeld(new ConveyorFeed(conveyor)); // Feed conveyor to shoot
    yButtonOperator.whileHeld(new Shoot(shooter)); // Shoot balls when robot is on white line
    leftStickButtonOperator.whileHeld(new ConveyorReverse(conveyor));
    rightStickButtonOperator.whenPressed(new IntakeDown(intake));
    backButtonOperator.whileHeld(new ShooterFeedReverse(conveyor, 0.5));
    startButtonOperator.whileHeld(new SplitConveyor(conveyor));



    //Shooting Commands 
    //xButtonOperator.whenPressed(new ShootingS());
  }
  public Command getAutomousCommand(){
    
    
    if(autoChooser.getSelected().equals("rightDrive")){
      //Follow Trajectories 
      Command ramsete = generateRamseteCommand(Trajectories.driveL);
      return ramsete.andThen(() -> drivetrain.tankDrive(0, 0), drivetrain);

    } else if(autoChooser.getSelected().equals("leftDrive")){
      //Follow Trajectories
      Command ramsete = generateRamseteCommand(Trajectories.driveLeft); 
      return ramsete.andThen(() -> drivetrain.tankDrive(0, 0), drivetrain);
  
    } else if(autoChooser.getSelected().equals("RshootDrive")){
      // robot on right side and shoot then drive off
      Command ramsete = generateRamseteCommand(Trajectories.driveOff);
      return ramsete.andThen(() -> drivetrain.tankDrive(0, 0), drivetrain);
    } else if(autoChooser.getSelected().equals("shoot")){
      Command ramsete = generateRamseteCommand(Trajectories.driveOff);
      return new AutoShoot(shooter, conveyor);//.andThen(ramsete).andThen(() -> drivetrain.tankDrive(0, 0));
    } else if(autoChooser.getSelected().equals("back")){
      Command ramsete = generateRamseteCommand(Trajectories.driveBackToZero);
      return ramsete.andThen(() -> drivetrain.tankDrive(0, 0), drivetrain);
    } else if(autoChooser.getSelected().equals("6trench")){
      Command firstShoot = new AutoShoot(shooter, conveyor);
      Command ramsete = generateRamseteCommand(Trajectories.driveLeft);
      Command intakeBalls = new SpinIntake(intake);
      Command sortConveyor = new AutoIntake(conveyor).alongWith(intakeBalls);
      Command stopIntake = new IntakeUp(intake);
      Command unjam = new UnJam(conveyor);
      Command ramseteReverse = generateRamseteCommand(Trajectories.driveBackToZero);
      //return ramsete.andThen(() -> drivetrain.tankDrive(0, 0), drivetrain).andThen(ramseteReverse).andThen(() -> drivetrain.tankDrive(0, 0), drivetrain);
      return firstShoot.andThen(ramsete.alongWith(sortConveyor)).andThen(() -> drivetrain.tankDrive(0, 0), drivetrain).andThen(unjam).andThen(stopIntake).andThen(ramseteReverse).andThen(() -> drivetrain.tankDrive(0, 0), drivetrain);
    } else {
      // drive off the line
      Command ramsete = generateRamseteCommand(Trajectories.driveOff);
      //Command ramseteReverse = generateRamseteCommand(Trajectories.driveBackToZero);
      return ramsete.andThen(() -> drivetrain.tankDrive(0, 0), drivetrain);//.andThen(ramseteReverse).andThen(() -> drivetrain.tankDrive(0, 0), drivetrain);
    }  
  }

  /**
   * Driver Axis
   * 
   * @param axis Axis number to return
   * @return Values of axis
   */
  public double getDriverRawAxis(final int axis) {
    //System.out.println("Left Stick X: " + driverController.getRawAxis(axis));
    if( Math.abs(driverController.getRawAxis(axis)) < 0.01){
      return 0;
    } else {
      return driverController.getRawAxis(axis);
    } 
  }

  /**
   * Operator Axis
   * 
   * @param axis Axis number to return
   * @return Values of axis
   */
  public double getOperatorRawAxis(final int axis) {
    return operatorController.getRawAxis(axis);
  }

 /**
   * 
   * @param trajectory Trajectory Path to use in the ramsete Command
   * @return Command generated by the ramsete controller based on the trajectory parameter
   */
  public Command generateRamseteCommand(Trajectory trajectory) {
    RamseteCommand ramseteCommand = new RamseteCommand(
      trajectory,
      drivetrain::getPose, 
      new RamseteController(Constants.kRamseteB, Constants.kRamseteZeta), 
      Constants.kDriveFF,
      Constants.kDriveKinematics, 
      drivetrain::getWheelSpeeds, 
      new PIDController(Constants.kPDriveVel, 0, 0), 
      new PIDController(Constants.kPDriveVel, 0, 0), 
      drivetrain::setDriveVolts,  
      drivetrain
    );
    return ramseteCommand;
  }
  
  


  
  
}
