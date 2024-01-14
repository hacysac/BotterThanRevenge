package org.team1515.BotterThanRevenge.Commands.AutoCommands;

import java.util.ArrayList;

import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveSegment;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import org.team1515.BotterThanRevenge.Utils.Point;
import org.team1515.BotterThanRevenge.Utils.bezierUtil;
import org.team1515.BotterThanRevenge.Utils.Equation;

/**
 * A complex auto command that drives forward, releases a hatch, and then drives backward.
 */
public class driveArcLength extends SequentialCommandGroup {
  /**
   * Creates a new driveArcLength.
   *

   */
  public driveArcLength(Drivetrain drivetrain, Point[] points, double t, double theta) {
    double length = bezierUtil.bezierLength(points);
    double segmentLength = length/points.length;
    double speed = length/t;
    double segmentT = segmentLength/speed;
    Pose2d startPose = drivetrain.getOdometry();

    for(int i = 0; i<points.length-2;i++){
        addCommands(new driveSegment(drivetrain, theta, speed, points[i], points[i+1], segmentT, startPose));
        //System.out.println(segmentT);
        //addCommands(Commands.waitSeconds(0.01));
    }
  }

}