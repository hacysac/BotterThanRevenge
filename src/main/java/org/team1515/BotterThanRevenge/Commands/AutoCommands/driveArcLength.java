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
    // total t
    // t1 = t3 = t2 = 1/3 (t)    
    
    double length = bezierUtil.bezierLength(points);
    double segmentLength = length/(points.length-1);
    double speed = length/t;
    double startAngle = RobotContainer.gyro.getGyroscopeRotation().getRadians();
    Pose2d startPose = drivetrain.getOdometry();
    DoubleSupplier turnAmount = () -> theta.getAsDouble() - (RobotContainer.gyro.getGyroscopeRotation().getRadians() - startAngle);
    double segmentT = segmentLength/speed;

    for(int i = 0; i<points.length-1;i++){
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
  public driveArcLength(Drivetrain drivetrain, Point[] points, double t, DoubleSupplier theta, double initialSpeed, double finalSpeed, int numAccel, int numDecel) {
    double length = bezierUtil.bezierLength(points);
    double segmentLength = length/(points.length-1);
    int numSegments = points.length-1;
    int numMax = numSegments-(numAccel+numDecel-1); // -1 because we discount the last point, stay at max speed for one segment less so we decelerate for the correct number of segments
    
    //calculate segment time at max speed by subtracting time accelerating and time decelerating
    double speed = initialSpeed;
    double maxSpeed = length/t * 1.75; // max speed is 175% the average speed (this is an arbitratary measurement, check in testing)
    double accel = (maxSpeed - initialSpeed)/numAccel;
    double decel = (finalSpeed - maxSpeed)/numDecel;
    
    double accelT = 0.0;
    for(int i = 0; i < numAccel; i++){
      accelT += segmentLength/speed;
      speed += accel;
    }
    double decelT = 0.0;
    for(int i = numMax; i < points.length-2; i++){
      decelT += segmentLength/speed;
      speed += decel;
    }
    double middleT = t - (accelT+decelT);
    double middleSegmentT = middleT/numMax; // MUST = SEGMENTLENGTH/MAXSPEED

    
    double startAngle = RobotContainer.gyro.getGyroscopeRotation().getRadians();
    Pose2d startPose = drivetrain.getOdometry();
    DoubleSupplier turnAmount = () -> theta.getAsDouble() - (RobotContainer.gyro.getGyroscopeRotation().getRadians() - startAngle);

    speed = initialSpeed;
    for(int i = 0; i < numAccel; i++){
      double segmentT = segmentLength/speed;
      addCommands(new driveSegment(drivetrain, turnAmount , speed, points[i], points[i+1], segmentT, startPose, true));
      speed += accel;
    }
    speed = maxSpeed;
    for(int i = numAccel; i < numMax;i++){
        addCommands(new driveSegment(drivetrain, turnAmount , speed, points[i], points[i+1], middleSegmentT, startPose, true));
    }
    for(int i = numMax; i < points.length-2; i++){
      speed += decel;
      double segmentT = segmentLength/speed;
      addCommands(new driveSegment(drivetrain, turnAmount , speed, points[i], points[i+1], segmentT, startPose, true));
    }
  }

}