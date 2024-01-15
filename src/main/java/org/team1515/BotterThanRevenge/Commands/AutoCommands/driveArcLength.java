package org.team1515.BotterThanRevenge.Commands.AutoCommands;

import java.util.ArrayList;
import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.RobotContainer;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveSegment;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import org.team1515.BotterThanRevenge.Utils.Point;
import org.team1515.BotterThanRevenge.Utils.bezierUtil;
import org.team1515.BotterThanRevenge.Utils.Equation;

/**
 * A complex auto command that creates segments to drive along a bezier curve.
 */
public class driveArcLength extends SequentialCommandGroup {
  /**
   * Creates a new driveArcLength.
   *

   */
  public driveArcLength(Drivetrain drivetrain, Point[] points, double t, DoubleSupplier theta) {
    double length = bezierUtil.bezierLength(points);
    double segmentLength = length/points.length;
    double speed = length/t;
    double startAngle = RobotContainer.gyro.getGyroscopeRotation().getRadians();
    Pose2d startPose = drivetrain.getOdometry();
    DoubleSupplier turnAmount = () -> theta.getAsDouble() - (RobotContainer.gyro.getGyroscopeRotation().getRadians() - startAngle);
    double segmentT = segmentLength/speed;

    for(int i = 0; i<points.length-2;i++){
        addCommands(new driveSegment(drivetrain, turnAmount , speed, points[i], points[i+1], segmentT, startPose, true));
    }

    Point lastPoint = new Point(drivetrain.getOdometry().getX()-startPose.getX(), drivetrain.getOdometry().getY()-startPose.getY());
    Point projected = new Point(points[-1].x, points[-1].y);
    double dist = Math.sqrt(Math.pow(projected.x-lastPoint.x,2)+Math.pow(projected.y-lastPoint.y,2));
    speed = dist/segmentT;

    turnAmount = () -> 0.0;

    addCommands(new driveSegment(drivetrain, turnAmount, speed, lastPoint, projected, segmentT, startPose, true));
    addCommands(new InstantCommand(()->drivetrain.drive(new Translation2d(0,0), 0, true, true)));
  }

}