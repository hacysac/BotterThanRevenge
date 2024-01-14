// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team1515.BotterThanRevenge;

<<<<<<< HEAD
import org.team1515.BotterThanRevenge.Commands.IndexerDown;
import org.team1515.BotterThanRevenge.Commands.IndexerUp;
import org.team1515.BotterThanRevenge.Subsystems.Indexer;
import org.team1515.BotterThanRevenge.Utils.*;
=======
import org.team1515.BotterThanRevenge.Commands.ClimberDown;
import org.team1515.BotterThanRevenge.Commands.ClimberUp;
import org.team1515.BotterThanRevenge.Subsystems.Climber;
import org.team1515.BotterThanRevenge.Utils.Utilities;
>>>>>>> 0ddeb6c0fcce8b53ef3de4a7a06e7721868a0028

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class RobotContainer {

  public static XboxController mainController;
  public static XboxController secondController;

<<<<<<< HEAD
  private static Indexer indexer;
=======
  private static Climber climber;
>>>>>>> 0ddeb6c0fcce8b53ef3de4a7a06e7721868a0028

  public RobotContainer() {
    mainController = new XboxController(0);
    secondController = new XboxController(1);

<<<<<<< HEAD
    indexer = new Indexer();
=======
    climber = new Climber();
>>>>>>> 0ddeb6c0fcce8b53ef3de4a7a06e7721868a0028

    configureBindings();
  }

  private void configureBindings() {
<<<<<<< HEAD
    Controls.INDEXER_UP.whileTrue(new IndexerUp(indexer));
    Controls.INDEXER_DOWN.whileTrue(new IndexerDown(indexer));
=======
    Controls.CLIMB_UP.whileTrue(new ClimberUp(climber));
    Controls.CLIMB_DOWN.whileTrue(new ClimberDown(climber));
>>>>>>> 0ddeb6c0fcce8b53ef3de4a7a06e7721868a0028
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
