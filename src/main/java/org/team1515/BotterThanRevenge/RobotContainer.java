// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team1515.BotterThanRevenge;

import java.util.Optional;
import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.Commands.AutoCommands.RotateAngle;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.TwoSpeakerAmpSeq;
import org.team1515.BotterThanRevenge.Commands.ClimberCommands.ClimberDown;
import org.team1515.BotterThanRevenge.Commands.ClimberCommands.ClimberUp;
import org.team1515.BotterThanRevenge.Commands.ClimberCommands.SingleClimber;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.DriveBackSeq;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.FourNoteSeq;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.PassNotesSeq;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.ThreeNoteSeq;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.TwoAmpSeq;
//import org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.TwoNoteSeq;
import org.team1515.BotterThanRevenge.Commands.DefaultDriveCommand;
import org.team1515.BotterThanRevenge.Commands.IndexerCommands.IndexerDown;
import org.team1515.BotterThanRevenge.Commands.IndexerCommands.IndexerUp;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.AutoIntakeIn;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.FlipDown;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.FlipUp;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.IntakeIn;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.IntakeOut;
import org.team1515.BotterThanRevenge.Commands.ShooterCommands.ShooterIn;
import org.team1515.BotterThanRevenge.Commands.ShooterCommands.ToggleAmp;
import org.team1515.BotterThanRevenge.Commands.ShooterCommands.ToggleSpeaker;
import org.team1515.BotterThanRevenge.Utils.*;
import org.team1515.BotterThanRevenge.Subsystems.*;
import edu.wpi.first.math.geometry.Pose2d;
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
  private PhotonVision photon;

  public static Drivetrain drivetrain;
  public static Intake intake;
  public static Flip flip;
  private static Indexer indexer;
  private static Climber climber;
  public static Shooter shooter;

  public double direction = 1;

  public static SendableChooser<Integer> AutoChooser = new SendableChooser<>();
  
  public RobotContainer() {
    mainController = new XboxController(0);
    secondController = new XboxController(1);

    gyro = new Gyroscope();
    photon = new PhotonVision();

    drivetrain = new Drivetrain(new Pose2d(), photon); 
    intake = new Intake();
    indexer = new Indexer();
    flip = new Flip();
    climber = new Climber();
    shooter = new Shooter();

    AutoChooser.setDefaultOption("Drive Back", 0);
    //AutoChooser.addOption("2 Note Seq", new TwoNoteSeq(drivetrain, shooter, indexer, intake, flip, false, team));
    AutoChooser.addOption("3 Note Source Side Seq", 1);
    AutoChooser.addOption("3 Note Amp Side Seq", 2);
    AutoChooser.addOption("4 Note Seq", 3);
    AutoChooser.addOption("2 Amp Seq", 4);
    AutoChooser.addOption("2 Note + Amp Seq", 5);
    //AutoChooser.addOption("Center Pass Seq", 6);
    SmartDashboard.putData(AutoChooser);

    configureBindings();

  }

  private void configureBindings() {
    drivetrain.setDefaultCommand(
        new DefaultDriveCommand(drivetrain,
            () -> -modifyAxis(mainController.getLeftY() * getRobotSpeed()),
            () -> -modifyAxis(mainController.getLeftX() * getRobotSpeed()),
            () -> modifyAxis(mainController.getRightX() * getRobotSpeed()),
            () -> direction,
            () -> Controls.DRIVE_ROBOT_ORIENTED.getAsBoolean()));
    
    //DoubleSupplier angle = () -> Units.degreesToRadians(180);
    Controls.RESET_GYRO.onTrue(new InstantCommand(()->drivetrain.zeroGyro()));
    Controls.CHANGE_DIRECTION.onTrue(new InstantCommand(()->direction = -direction));
    //Controls.ROTATE_ANGLE_TARGET.onTrue(new RotateAngle(drivetrain, angle));

    DoubleSupplier zeroAngle = () -> -Units.degreesToRadians(gyro.getYaw());
    Controls.ZERO_ROBOT.onTrue(new RotateAngle(drivetrain, zeroAngle));

    //Intake
    Controls.AUTO_INTAKE.toggleOnTrue(new AutoIntakeIn(intake, indexer)); // infinite until sensor
    Controls.INTAKE.whileTrue(new IntakeIn(intake));
    Controls.OUTTAKE.whileTrue(new IntakeOut(intake));

    //Flip
    //Controls.FLIP.onTrue(new SetFlip(flip));
    Controls.FLIP_UP.onTrue(new FlipUp(flip));
    Controls.FLIP_DOWN.onTrue(new FlipDown(flip));
    // Indexer
    Controls.INDEXER_UP.whileTrue(new IndexerUp(indexer));
    Controls.INDEXER_DOWN.whileTrue(new IndexerDown(indexer));

    //Climber
    Controls.CLIMBER_UP.whileTrue(new ClimberUp(climber));
    Controls.CLIMBER_DOWN.whileTrue(new ClimberDown(climber));
    Controls.LEFT_CLIMBER_DOWN.whileTrue(new SingleClimber(climber, true));
    Controls.RIGHT_CLIMBER_DOWN.whileTrue(new SingleClimber(climber, false));

    //Shooter Hold Down
    //Controls.SHOOT_SPEAKER.whileTrue(new ShooterShoot(shooter, RobotMap.SPEAKER_SPEED));
    //Controls.SHOOT_AMP.whileTrue(new ShooterShoot(shooter, RobotMap.AMP_SPEED));
    Controls.SHOOTER_IN.whileTrue(new ShooterIn(shooter));
  
    //Shooter Toggle
    Controls.SHOOT_SPEAKER.toggleOnTrue(new ToggleSpeaker(shooter));
    Controls.SHOOT_AMP.toggleOnTrue(new ToggleAmp(shooter));
  }

  public Command getAutonomousCommand() {
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

    if (AutoChooser.getSelected() == 0){
      return new DriveBackSeq(drivetrain);
    }
    else if (AutoChooser.getSelected() == 1){
      return new ThreeNoteSeq(drivetrain, shooter, indexer, intake, flip, false, team);
    }
    else if (AutoChooser.getSelected() == 2){
      return new ThreeNoteSeq(drivetrain, shooter, indexer, intake, flip, false, -team);
    }
    else if (AutoChooser.getSelected() == 3){
      return new FourNoteSeq(drivetrain, shooter, indexer, intake, flip, team);
    }
    else if (AutoChooser.getSelected() == 4){
      return new TwoAmpSeq(drivetrain, shooter, indexer, intake, flip, -team);
    }
    else if (AutoChooser.getSelected() == 5){
      return new TwoSpeakerAmpSeq(drivetrain, shooter, indexer, intake, flip, false, -team);
    }
    else if (AutoChooser.getSelected() == 6){
      return new PassNotesSeq(drivetrain, shooter, indexer, intake, flip, team);
    }
    return new InstantCommand(()-> System.out.println("pain"));
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
