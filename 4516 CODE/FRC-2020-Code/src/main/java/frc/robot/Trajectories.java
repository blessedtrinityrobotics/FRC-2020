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

    static Pose2d zeroPose = new Pose2d(0, 0, new Rotation2d(0));
    static Pose2d fiveOnePose = new Pose2d(4.75, 1, new Rotation2d(0));
    static Pose2d twoPose = new Pose2d(2, 0, new Rotation2d(0));
    static Pose2d threeOnePose = new Pose2d(3, 1, new Rotation2d(0));

    public static final Trajectory driveOff = TrajectoryGenerator.generateTrajectory(
        zeroPose, 
        List.of(), 
        twoPose, 
        Constants.slowConfig
    );

    public static final Trajectory driveBackToZero = TrajectoryGenerator.generateTrajectory (
        fiveOnePose, 
        List.of(), 
        zeroPose, 
        Constants.reverseConfig
        );


    public static final Trajectory driveLeft = TrajectoryGenerator.generateTrajectory(
        zeroPose, 
        List.of(
            new Translation2d(2.5, 0.875)            
        ), 
        fiveOnePose, 
        Constants.slowConfig
    );

    public static final Trajectory driveL = TrajectoryGenerator.generateTrajectory(
        zeroPose, 
        List.of(), 
        threeOnePose, 
        Constants.slowConfig
    );

 


}