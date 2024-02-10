// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team1515.BotterThanRevenge;

import java.util.Optional;
import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.Commands.DefaultDriveCommand;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.RotateAngle;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveArcLength;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.TwoSpeakerAmpSeq;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.DriveBackSeq;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.ThreeNoteSeq;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.TwoAmpSeq;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.TwoNoteSeq;

import org.team1515.BotterThanRevenge.Commands.IndexerCommands.IndexerDown;
import org.team1515.BotterThanRevenge.Commands.IndexerCommands.IndexerUp;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.AutoIntakeIn;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.IntakeIn;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.IntakeOut;
import org.team1515.BotterThanRevenge.Commands.ShooterCommands.ShooterIn;
import org.team1515.BotterThanRevenge.Commands.ShooterCommands.ShooterToggle;
import org.team1515.BotterThanRevenge.Utils.*;
import org.team1515.BotterThanRevenge.Subsystems.*;

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

  public static Intake intake;
  private static Indexer indexer;
  private static Climber climber;
  public static Shooter shooter;
  
  public static Gyroscope gyro;
  private Drivetrain drivetrain;
  public static PhotonVision photon;

  public static SendableChooser<Command> AutoChooser = new SendableChooser<>();

  public RobotContainer() {
    mainController = new XboxController(0);
    secondController = new XboxController(1);
    
    intake = new Intake();
    indexer = new Indexer();
    //climber = new Climber();
    shooter = new Shooter();
    
    gyro = new Gyroscope();
    photon = new PhotonVision();

    drivetrain = new Drivetrain(new Pose2d(), photon);

    Optional<Alliance> ally = DriverStation.getAlliance();
    int team = 1; // default blue
    if (ally.isPresent()) {
        if (ally.get() == Alliance.Red) {
            team = -1;
        }
        else if (ally.get() == Alliance.Blue) {
            team = 1;
        }
    }

    AutoChooser.setDefaultOption("Drive Back", new DriveBackSeq(drivetrain));
    AutoChooser.addOption("2 Note Seq", new TwoNoteSeq(drivetrain, team));
    AutoChooser.addOption("3 Note Seq", new ThreeNoteSeq(drivetrain, team));
    AutoChooser.addOption("2 Amp Seq", new TwoAmpSeq(drivetrain, -team));
    AutoChooser.addOption("2 Note + Amp Seq", new TwoSpeakerAmpSeq(drivetrain, -team));
    SmartDashboard.putData(AutoChooser);

    configureBindings();

  }

  private void configureBindings() {
    
    drivetrain.setDefaultCommand(
        new DefaultDriveCommand(drivetrain,
            () -> -modifyAxis(mainController.getLeftY() * getRobotSpeed()),
            () -> -modifyAxis(mainController.getLeftX() * getRobotSpeed()),
            () -> -modifyAxis(mainController.getRightX() * getRobotSpeed()),
            () -> Controls.DRIVE_ROBOT_ORIENTED.getAsBoolean()));
            
    Controls.RESET_GYRO.onTrue(new InstantCommand(()->drivetrain.zeroGyro()));
    Controls.ZERO_ROBOT.onTrue(new InstantCommand(()->drivetrain.setOdometry(new Pose2d(new Translation2d(0,0), new Rotation2d(0.0)))));
    DoubleSupplier angle = () -> -photon.getAngle();
    Controls.ROTATE_ANGLE_TARGET.onTrue(new RotateAngle(drivetrain, angle));
    Controls.GET_DIST_TARGET.onTrue(new InstantCommand(()->System.out.println(photon.getDist())));

    //Intake
    Controls.AUTO_INTAKE.toggleOnTrue(new AutoIntakeIn(intake, indexer)); // infinite until sensor
    Controls.INTAKE.whileTrue(new IntakeIn(intake));
    Controls.OUTTAKE.whileTrue(new IntakeOut(intake));
    //Controls.FLIP.onTrue(new Flip(intake));
    
    //Indexer
    Controls.INDEXER_UP.whileTrue(new IndexerUp(indexer));
    Controls.INDEXER_DOWN.whileTrue(new IndexerDown(indexer));

    //Climber
    //Controls.CLIMB_UP.whileTrue(new ClimberUp(climber));
    //Controls.CLIMB_DOWN.whileTrue(new ClimberDown(climber));

    //Shooter Hold Down
    // Controls.SHOOT_SPEAKER.whileTrue(new ShooterShoot(shooter, RobotMap.SPEAKER_SPEED));
    // Controls.SHOOT_AMP.whileTrue(new ShooterShoot(shooter, RobotMap.AMP_SPEED));
  
    //Shooter Toggle
    Controls.SHOOT_SPEAKER.toggleOnTrue(new ShooterToggle(shooter, RobotMap.SPEAKER_SPEED));
    Controls.SHOOT_AMP.toggleOnTrue(new ShooterToggle(shooter, RobotMap.AMP_SPEED));

    Controls.SHOOTER_IN.whileTrue(new ShooterIn(shooter));
    
  }

  public Command getAutonomousCommand() {
    return AutoChooser.getSelected();
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
