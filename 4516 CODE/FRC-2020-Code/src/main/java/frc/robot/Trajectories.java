package frc.robot;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;

import java.util.List;

public class Trajectories {



    // X direction -> Forward is positive
    // Y direction -> Left is positive


    public static final Trajectory driveOff = TrajectoryGenerator.generateTrajectory(
        new Pose2d(0, 0, new Rotation2d(0)) , 
        List.of(
        ), 
        new Pose2d(2, 0, new Rotation2d(0)), 
        Constants.slowConfig
    );

    public static final Trajectory driveBack = TrajectoryGenerator.generateTrajectory (
        new Pose2d(Robot.m_robotContainer.drivetrain.getPose().getTranslation().getX(), 0, new Rotation2d(0)), 
        List.of(), 
        new Pose2d(-1, 0, new Rotation2d(0)), 
        Constants.reverseConfig
        );


    public static final Trajectory driveLeft = TrajectoryGenerator.generateTrajectory(
        new Pose2d(0, 0, new Rotation2d(0)), 
        List.of(), 
        new Pose2d(2, 1, new Rotation2d(0)), 
        Constants.defaultConfig
    );

    public static final Trajectory driveRight = TrajectoryGenerator.generateTrajectory(
        new Pose2d(0, 0, new Rotation2d(0)), 
        List.of(), 
        new Pose2d(3, -1, new Rotation2d(0)), 
        Constants.defaultConfig
    );

 


}