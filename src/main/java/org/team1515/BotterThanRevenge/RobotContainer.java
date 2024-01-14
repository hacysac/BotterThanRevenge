// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team1515.BotterThanRevenge;

import org.team1515.BotterThanRevenge.Commands.ClimberDown;
import org.team1515.BotterThanRevenge.Commands.ClimberUp;
import org.team1515.BotterThanRevenge.Subsystems.Climber;
import org.team1515.BotterThanRevenge.Utils.Utilities;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class RobotContainer {

  public static XboxController mainController;
  public static XboxController secondController;

  private static Climber climber;

  public RobotContainer() {
    mainController = new XboxController(0);
    secondController = new XboxController(1);

    climber = new Climber();

    configureBindings();
  }

  private void configureBindings() {
    Controls.CLIMB_UP.whileTrue(new ClimberUp(climber));
    Controls.CLIMB_DOWN.whileTrue(new ClimberDown(climber));
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
