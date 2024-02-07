package org.team1515.BotterThanRevenge.Commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Shooter;

public class ShooterToggle extends Command {
    private final Shooter shooter; 
    private double speed;

    public ShooterToggle(Shooter shooter, double speed) {
        this.shooter = shooter;
        this.speed = speed;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        shooter.shoot(speed);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        shooter.end();
    }
}
