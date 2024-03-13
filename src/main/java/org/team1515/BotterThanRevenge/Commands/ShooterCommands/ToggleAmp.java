package org.team1515.BotterThanRevenge.Commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Shooter;

public class ToggleAmp extends Command {
    private final Shooter shooter; 

    public ToggleAmp(Shooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        shooter.shootAmp();
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
