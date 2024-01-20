package org.team1515.BotterThanRevenge.Commands.AutoCommands;

import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.RobotContainer;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.Utils.Point;
import org.team1515.BotterThanRevenge.Utils.calcUtil;

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
    Pose2d startPose = drivetrain.getOdometry();
    DoubleSupplier turnAmount = () -> theta.getAsDouble() - (RobotContainer.gyro.getGyroscopeRotation().getRadians() - startAngle);
    int numSegments = points.length-1;
    double segmentT = t/(double) numSegments; // check this
    
    //starting odometry
    addCommands(new InstantCommand(()->System.out.println("odem x: " + drivetrain.getOdometry().getX() + " odem y: " + drivetrain.getOdometry().getY() + "\n")));

    double [] rampArr = calcUtil.rampArr(points.length, 0.5, 1.5);
    for(int i = 0; i<points.length-1;i++){
        addCommands(new driveSegment(drivetrain, turnAmount, points[i+1], segmentT, startPose, rampArr[i]));
    }

    // //to account for slow down
    // //20ms roborio cycle
    // double cycleSeconds = 0.02;
    // //1 wasted cycle updating per command
    // double totalExtra = cycleSeconds*points.length;
    // //extra speed neded to account
    // double speedCorrection = totalExtra/t;
    // //ramp multiplier to account
    // double constantRamp = 1.0+speedCorrection;

    // for(int i = 0; i<points.length-1;i++){
    //     addCommands(new driveSegment(drivetrain, turnAmount, points[i+1], segmentT, startPose, ));
    // }

    //correction on final point if needed
    //Point lastPoint = new Point(drivetrain.getOdometry().getX()-startPose.getX(), drivetrain.getOdometry().getY()-startPose.getY());
    //Point projected = new Point(points[-1].x, points[-1].y);
    //turnAmount = () -> 0.0;
    //addCommands(new driveSegment(drivetrain, turnAmount, lastPoint, projected, segmentT, startPose, true));

    addCommands(new InstantCommand(()->drivetrain.drive(new Translation2d(0,0), 0, true, true)));
    
    //ending odometry
    addCommands(new InstantCommand(()->System.out.println("odem x: " + (drivetrain.getOdometry().getX()-startPose.getX()) + " odem y: " + (drivetrain.getOdometry().getY()-startPose.getY()) + "\n")));
  
  }


  //accel deccel
  /* 
  public driveArcLength(Drivetrain drivetrain, Point[] points, double t, DoubleSupplier theta, double initialSpeed, double finalSpeed, int numAccel, int numDeccel) {
    
    double startAngle = RobotContainer.gyro.getGyroscopeRotation().getRadians();
    Pose2d startPose = drivetrain.getOdometry();
    DoubleSupplier turnAmount = () -> theta.getAsDouble() - (RobotContainer.gyro.getGyroscopeRotation().getRadians() - startAngle);
    
    int numSegments = points.length-1;
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
      addCommands(new driveSegment(drivetrain, turnAmount, points[i+1], segmentT, startPose));
    }
    for(int i = numAccel; i < numMax;i++){
        addCommands(new driveSegment(drivetrain, turnAmount, points[i+1], minTime, startPose));
    }
    for(int i = numMax+numAccel; i < points.length-1; i++){
      double segmentT = averageSegTime+deccelAmount*(i-(numMax+numAccel));
      addCommands(new driveSegment(drivetrain, turnAmount, points[i+1], segmentT, startPose));
    }
  }
  */
}