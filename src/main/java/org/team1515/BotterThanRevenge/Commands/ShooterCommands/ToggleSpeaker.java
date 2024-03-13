package org.team1515.BotterThanRevenge.Commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Shooter;

public class ToggleSpeaker extends Command {
    private final Shooter shooter; 

    public ToggleSpeaker(Shooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        shooter.shootSpeaker();
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
