// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team1515.BotterThanRevenge;

import org.team1515.BotterThanRevenge.Commands.ClimberCommands.ZeroClimber;

import com.team364.swervelib.util.CTREConfigs;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  public static CTREConfigs ctreConfigs;
  

  private RobotContainer m_robotContainer;

  @Override
  public void robotInit() {
    ctreConfigs = new CTREConfigs();
    m_robotContainer = new RobotContainer();

    CameraServer.startAutomaticCapture();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    SmartDashboard.putNumber("NavX", RobotContainer.gyro.getGyroscopeRotation().getDegrees());
    // SmartDashboard.putBooleanArray("switchBox", new boolean[] {RobotContainer.autoController.getRawButton(1), RobotContainer.autoController.getRawButton(2), RobotContainer.autoController.getRawButton(3), RobotContainer.autoController.getRawButton(4), RobotContainer.autoController.getRawButton(5), RobotContainer.autoController.getRawButton(6)});
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    InstantCommand resetOdometry = new InstantCommand(()->RobotContainer.drivetrain.resetOdometry());
    Command zeroClimber = new ZeroClimber(RobotContainer.climber);

    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      SequentialCommandGroup sequence = new SequentialCommandGroup(resetOdometry, m_robotContainer.getAutonomousCommand(), zeroClimber);
      sequence.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    //schedual an initial flip up command
    // FlipUp flipUpCommand = new FlipUp(RobotContainer.flip);
    // flipUpCommand.schedule();
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}
}
