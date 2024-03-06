package org.team1515.BotterThanRevenge.Commands.AutoCommands;

import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.RobotContainer;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.Utils.Point;
import org.team1515.BotterThanRevenge.Utils.bezierUtil;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * A complex auto command that creates segments to drive along a bezier curve.
 */
public class driveArcLength extends SequentialCommandGroup {
  /**
   * Creates a new driveArcLength.
   *
   */
  
  //error correction
  public driveArcLength(Drivetrain drivetrain, Point[] points, double t, DoubleSupplier theta) {
  
    double startAngle = RobotContainer.gyro.getGyroscopeRotation().getRadians();
    Pose2d startPose = new Pose2d();
    DoubleSupplier turnAmount = () -> theta.getAsDouble() - (RobotContainer.gyro.getGyroscopeRotation().getRadians() - startAngle);

    double speed = bezierUtil.bezierLength(points)/t; // check this

    for(int i = 0; i<points.length-1;i++){
        addCommands(new driveSegment(drivetrain, turnAmount, points[i+1], speed, startPose));
    }

    addCommands(new InstantCommand(()->drivetrain.drive(new Translation2d(0,0), 0, true, true)));
    
    //ending odometry
    addCommands(new InstantCommand(()->System.out.println("odem x: " + (drivetrain.getOdometry().getX()-startPose.getX()) + " odem y: " + (drivetrain.getOdometry().getY()-startPose.getY()) + "\n")));
  
  }
  public driveArcLength(Drivetrain drivetrain, Point[] points, double t, DoubleSupplier theta, Pose2d originalPose) {
  
    double startAngle = RobotContainer.gyro.getGyroscopeRotation().getRadians();
    Pose2d startPose = originalPose;
    DoubleSupplier turnAmount = () -> theta.getAsDouble() - (RobotContainer.gyro.getGyroscopeRotation().getRadians() - startAngle);

    double speed = bezierUtil.bezierLength(points)/t; // check this

    for(int i = 0; i<points.length-1;i++){
        addCommands(new driveSegment(drivetrain, turnAmount, points[i+1], speed, startPose));
    }

    addCommands(new InstantCommand(()->drivetrain.drive(new Translation2d(0,0), 0, true, true)));
    
    //ending odometry
    addCommands(new InstantCommand(()->System.out.println("odem x: " + (drivetrain.getOdometry().getX()-startPose.getX()) + " odem y: " + (drivetrain.getOdometry().getY()-startPose.getY()) + "\n")));
  
  }
}