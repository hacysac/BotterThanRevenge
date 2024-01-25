// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team1515.BotterThanRevenge;

import java.util.Optional;
import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.Commands.DefaultDriveCommand;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.RotateAngle;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.AmpSeq;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.DriveBackSeq;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.OneNoteSeq;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.ThreeNoteSeq;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.Utils.*;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class RobotContainer {
  public static XboxController mainController;
  public static XboxController secondController;

  public static Gyroscope gyro;
  private Drivetrain drivetrain;
  public static PhotonVision photon;

  public static SendableChooser<Command> autoChooser;

  public RobotContainer() {
    mainController = new XboxController(0);
    secondController = new XboxController(1);

    gyro = new Gyroscope();
    photon = new PhotonVision();

    drivetrain = new Drivetrain(new Pose2d(), photon);

    autoChooser = new SendableChooser<Command>();

    configureBindings();
  }

  private void configureBindings() {
    drivetrain.setDefaultCommand(
      //potential solution to inverted controls while driving
        new DefaultDriveCommand(drivetrain,
            () -> modifyAxis(-mainController.getLeftY() * getRobotSpeed()),
            () -> modifyAxis(-mainController.getLeftX() * getRobotSpeed()),
            () -> -modifyAxis(mainController.getRightX() * getRobotSpeed()),
            () -> Controls.DRIVE_ROBOT_ORIENTED.getAsBoolean()));
            
    Controls.RESET_GYRO.onTrue(new InstantCommand(()->drivetrain.zeroGyro()));
    Controls.ZERO_ROBOT.onTrue(new InstantCommand(()->drivetrain.setOdometry(new Pose2d(new Translation2d(0,0), new Rotation2d(0.0)))));
    DoubleSupplier angle = () -> -photon.getAngle();
    Controls.ROTATE_ANGLE_TARGET.onTrue(new RotateAngle(drivetrain, angle));
    Controls.GET_DIST_TARGET.onTrue(new InstantCommand(()->System.out.println(photon.getDist())));

    Optional<Alliance> ally = DriverStation.getAlliance();
    int team = -1; // default blue
    if (ally.isPresent()) {
        if (ally.get() == Alliance.Red) {
            team = 1;
        }
        else if (ally.get() == Alliance.Blue) {
            team = -1;
        }
    }
    autoChooser.setDefaultOption("Drive Back", new DriveBackSeq(drivetrain));
    autoChooser.addOption("1 Note Seq", new OneNoteSeq(drivetrain, team));
    autoChooser.addOption("3 Note Seq", new ThreeNoteSeq(drivetrain, team));
    SmartDashboard.putData(autoChooser);
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
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
