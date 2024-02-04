// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team1515.BotterThanRevenge;

import org.team1515.BotterThanRevenge.Utils.*;
import org.team1515.BotterThanRevenge.Commands.*;
import org.team1515.BotterThanRevenge.Subsystems.*;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class RobotContainer {

  public static XboxController mainController;
  public static XboxController secondController;

  public static Intake intake;
  public static Shooter shooter;
  private static Indexer indexer;
  private static Climber climber;

  public RobotContainer() {
    mainController = new XboxController(0);
    secondController = new XboxController(1);

    //intake = new Intake();
    //indexer = new Indexer();
    //climber = new Climber();
    shooter = new Shooter();

    configureBindings();

  }

  private void configureBindings() {
    
    Controls.INDEXER_UP.whileTrue(new IndexerUp(indexer));
    Controls.INDEXER_DOWN.whileTrue(new IndexerDown(indexer));

    //Controls.CLIMB_UP.whileTrue(new ClimberUp(climber));
    //Controls.CLIMB_DOWN.whileTrue(new ClimberDown(climber));

    //Controls.AUTO_INTAKE.onTrue(new SequentialCommandGroup(new InstantCommand(()->intake.setDown(!intake.getDone())), new AutoIntakeIn(intake, indexer)));
    //make command interuptable? TODO: test this
    Controls.INTAKE.whileTrue(new IntakeIn(intake));
    Controls.OUTTAKE.whileTrue(new IntakeOut(intake));
    //Controls.FLIP.onTrue(new Flip(intake));

    Controls.SHOOT_SPEAKER.whileTrue(new ShooterShoot(shooter, RobotMap.SPEAKER_SPEED));
    Controls.SHOOT_AMP.whileTrue(new ShooterShoot(shooter, RobotMap.AMP_SPEED));
    Controls.SHOOTER_IN.whileTrue(new ShooterIn(shooter));

  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
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
