package org.team1515.BotterThanRevenge.Commands.AutoCommands;

import java.util.ArrayList;
import java.util.function.DoubleFunction;
import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.RobotContainer;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveSegment;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
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

   //working driveBezier
  public driveArcLength(Drivetrain drivetrain, Point[] points, double t, DoubleSupplier theta, boolean working) {
    double length = bezierUtil.bezierLength(points);
    double segmentLength = length/points.length;
    double speed = length/t;
    double startAngle = RobotContainer.gyro.getGyroscopeRotation().getRadians();
    Pose2d startPose = drivetrain.getOdometry();
    DoubleSupplier turnAmount = () -> theta.getAsDouble() - (RobotContainer.gyro.getGyroscopeRotation().getRadians() - startAngle);
    double segmentT = segmentLength/speed;

    for(int i = 0; i<points.length-2;i++){
        addCommands(new driveSegment(drivetrain, turnAmount, points[i], points[i+1], segmentT, startPose));
    }
    addCommands(new InstantCommand(()->drivetrain.drive(new Translation2d(0,0), 0, true, true)));
  }

  
  //error correction not working
  public driveArcLength(Drivetrain drivetrain, Point[] points, double t, DoubleSupplier theta) {
    double startAngle = RobotContainer.gyro.getGyroscopeRotation().getRadians();
    Pose2d startPose = drivetrain.getOdometry();
    DoubleSupplier turnAmount = () -> theta.getAsDouble() - (RobotContainer.gyro.getGyroscopeRotation().getRadians() - startAngle);
    int numSegments = points.length-1;
    double segmentT = t/(double) numSegments; // check this

    for(int i = 0; i<points.length-1;i++){
        Point start = new Point(drivetrain.getOdometry().getX()-startPose.getX(), drivetrain.getOdometry().getY()-startPose.getY());
        addCommands(new driveSegment(drivetrain, turnAmount , start, points[i+1], segmentT, startPose));
    }

    Point lastPoint = new Point(drivetrain.getOdometry().getX()-startPose.getX(), drivetrain.getOdometry().getY()-startPose.getY());
    Point projected = new Point(points[-1].x, points[-1].y);

    turnAmount = () -> 0.0;

    //addCommands(new driveSegment(drivetrain, turnAmount, lastPoint, projected, segmentT, startPose, true));
    addCommands(new InstantCommand(()->drivetrain.drive(new Translation2d(0,0), 0, true, true)));
  }


  //accel deccel
  public driveArcLength(Drivetrain drivetrain, Point[] points, double t, DoubleSupplier theta, double initialSpeed, double finalSpeed, int numAccel, int numDeccel) {
    double length = bezierUtil.bezierLength(points);
    double segmentLength = length/(points.length);
    int numSegments = points.length-1;
    int numMax = numSegments-(numAccel+numDeccel); // -1 because we discount the last point, stay at max speed for one segment less so we decelerate for the correct number of segments

    // //calculate segment time at max speed by subtracting time accelerating and time decelerating
    // DoubleFunction speed = (double maxSpeed) -> initialSpeed;
    // //DoubleFunction maxSpeed = (double factor) -> length/t * factor; // max speed is 175% the average speed (this is an arbitratary measurement, check in testing)
    // DoubleFunction accel = (double maxSpeed) -> (maxSpeed - initialSpeed)/numAccel;
    // DoubleFunction decel = (double maxSpeed) -> (finalSpeed - maxSpeed)/numDeccel;

    // DoubleFunction accelT = (double maxSpeed) -> 0.0;
    // for(int i = 0; i < numAccel; i++){
    //   accelT = (double maxSpeed) -> ((double) accelT.apply(maxSpeed) + (segmentLength/(double) speed.apply(maxSpeed))); // also for some reason cant make this non final
    //   speed = (double maxSpeed) -> ((double) speed.apply(maxSpeed) + (double) accel.apply(maxSpeed));
    // }
    // DoubleFunction decelT = (double maxSpeed) -> 0.0;
    // for(int i = numMax; i < points.length-2; i++){
    //   decelT = (double maxSpeed) -> ((double) decelT.apply(maxSpeed) + (segmentLength/(double) speed.apply(maxSpeed)));
    //   speed = (double maxSpeed) -> ((double) speed.apply(maxSpeed) + (double) decel.apply(maxSpeed));
    // }
    // DoubleFunction middleT = (double maxSpeed) -> t - ((double) accelT.apply(maxSpeed)+(double) decelT.apply(maxSpeed));
    // DoubleFunction middleSegmentT = (double maxSpeed) -> (double) middleT.apply(maxSpeed)/numMax; // MUST = SEGMENTLENGTH/MAXSPEED
    
    
    //segmentLength = middleSegmentT.apply(maxSpeed)*maxSpeed (segment length is known this is a one variable equation
    
    double startAngle = RobotContainer.gyro.getGyroscopeRotation().getRadians();
    Pose2d startPose = drivetrain.getOdometry();
    DoubleSupplier turnAmount = () -> theta.getAsDouble() - (RobotContainer.gyro.getGyroscopeRotation().getRadians() - startAngle);
    
    double averageSegTime = t/(double)numSegments;
    double initialSegTime = segmentLength/initialSpeed;
    double accelAmount = (averageSegTime-initialSegTime)/numAccel;
    double accelTimeLost = 0.0;
    for (int i = 0; i < numAccel; i++){
      accelTimeLost += (initialSegTime+accelAmount*i) - averageSegTime;
    }
    double finalSegTime = segmentLength/finalSpeed;
    double deccelAmount = (finalSegTime-averageSegTime)/numDeccel;
    double deccelTimeLost = 0.0;
    for (int i = 1; i <= numDeccel; i--){
      deccelTimeLost += (averageSegTime+deccelAmount*i) - averageSegTime;
    }
    double totalTimeSpent = deccelTimeLost + averageSegTime*numDeccel + accelTimeLost + averageSegTime*numAccel;
    double timeLeft = t - totalTimeSpent;
    double minTime = timeLeft/numMax;
    
    for(int i = 0; i < numAccel; i++){
      double segmentT = initialSegTime+accelAmount*i;
      addCommands(new driveSegment(drivetrain, turnAmount , points[i], points[i+1], segmentT, startPose));
    }
    for(int i = numAccel; i < numMax;i++){
        addCommands(new driveSegment(drivetrain, turnAmount, points[i], points[i+1], minTime, startPose));
    }
    for(int i = numMax+numAccel; i < points.length-1; i++){
      double segmentT = averageSegTime+deccelAmount*(i-(numMax+numAccel));
      addCommands(new driveSegment(drivetrain, turnAmount, points[i], points[i+1], segmentT, startPose));
    }
  }

}