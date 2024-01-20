// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team1515.BotterThanRevenge;

import java.util.ArrayList;
import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.Commands.DefaultDriveCommand;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveArcLength;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveSegment;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.Utils.*;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class RobotContainer {

  public static XboxController mainController;
  public static XboxController secondController;

  public static Gyroscope gyro;
  private Drivetrain drivetrain;
  private PhotonVision photon;

  public RobotContainer() {
    mainController = new XboxController(0);
    secondController = new XboxController(1);

    gyro = new Gyroscope();
    photon = new PhotonVision();

    drivetrain = new Drivetrain(new Pose2d());

    configureBindings();
  }

  private void configureBindings() {
    drivetrain.setDefaultCommand(
        new DefaultDriveCommand(drivetrain,
            () -> -modifyAxis(-mainController.getLeftY() * getRobotSpeed()),
            () -> -modifyAxis(-mainController.getLeftX() * getRobotSpeed()),
            () -> modifyAxis(mainController.getRightX() * getRobotSpeed()),
            () -> Controls.DRIVE_ROBOT_ORIENTED.getAsBoolean()));
            
    Controls.RESET_GYRO.onTrue(new InstantCommand(()->drivetrain.zeroGyro()));
    Controls.GET_ANGLE_TARGET.onTrue(new InstantCommand(()->System.out.println(photon.getAngle())));
  }

  public Command getAutonomousCommand() {
    
    Point[] points = {
      new Point(0.0,0.0),
      new Point(1.0,1.0),
      new Point(2.0,0.0),
      new Point(3.0,-1.0),
      new Point(4.0,0.0)
    };
    points = bezierUtil.spacedPoints(points);

    DoubleSupplier ds = ()->Units.degreesToRadians(0.0);
    return new driveArcLength(drivetrain, points, 10, ds);
    //return new driveSegment(drivetrain, ds, 1, new Point(0.0, 0.0), new Point(0.0, 5.0), 5);
  }

  public static double getRobotSpeed() {
    return Controls.getLeftTriggerMain() ? 0.45 : 0.7;
    // return 0.7;
  }

  private static double modifyAxis(double value) {
    value = Utilities.deadband(value, 0.08);

    // Square the axis
    value = Math.copySign(value * value, value);

    return value;
  }
}
