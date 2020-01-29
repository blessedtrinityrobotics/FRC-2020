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
            new Translation2d(1, 0) 
        ), 
        new Pose2d(1, 0, new Rotation2d(0)), 
        Constants.defaultConfig
    );


    public static final Trajectory driveStraight = TrajectoryGenerator.generateTrajectory(
        new Pose2d(0, 0, new Rotation2d(0)), 
        List.of(
            new Translation2d(1, 0),
            new Translation2d(2, 0)
        ), 
        new Pose2d(2, 0, new Rotation2d(0)), 
        Constants.defaultConfig
    );

    public static final Trajectory cargoOnCargoSideRight = TrajectoryGenerator.generateTrajectory(
        new Pose2d(0, 0, new Rotation2d(0)), 
        List.of(
            new Translation2d(0.5, 0),
            new Translation2d(1, -4),
            new Translation2d(4, -4)
        ), 
        new Pose2d(4, -3.5, Rotation2d.fromDegrees(-90) ), 
        Constants.defaultConfig
    );


}