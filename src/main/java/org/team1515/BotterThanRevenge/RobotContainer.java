// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team1515.BotterThanRevenge;

import org.team1515.BotterThanRevenge.Commands.*;
import org.team1515.BotterThanRevenge.Subsystems.*;
import org.team1515.BotterThanRevenge.Utils.*;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class RobotContainer {

  public static XboxController mainController;
  public static XboxController secondController;

  public static Intake intake;


  public RobotContainer() {
    mainController = new XboxController(0);
    secondController = new XboxController(1);

    intake = new Intake();

    configureBindings();

  }

  private void configureBindings() {
    Controls.INTAKE.whileTrue(new IntakeIn(intake));
    Controls.INTAKE.whileTrue(new IntakeOut(intake));

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
