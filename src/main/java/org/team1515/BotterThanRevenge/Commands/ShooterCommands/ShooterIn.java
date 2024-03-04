package org.team1515.BotterThanRevenge.Commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Shooter;

public class ShooterIn extends Command {
    private final Shooter shooter; 

    public ShooterIn(Shooter shooter) {
        this.shooter = shooter;
        shooter.setAmp(false);
        shooter.setSpeaker(false);
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        shooter.in();
    }

    @Override
    public void end(boolean interrupted) {
        shooter.end();
    }
}
