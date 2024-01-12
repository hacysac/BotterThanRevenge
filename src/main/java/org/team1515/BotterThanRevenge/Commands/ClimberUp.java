package org.team1515.BotterThanRevenge.Commands;

import org.team1515.BotterThanRevenge.Subsystems.Climber;

import edu.wpi.first.wpilibj2.command.Command;

public class ClimberUp extends Command {
    private final Climber climber;

    public ClimberUp(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }

    @Override
    public void execute() {
        climber.up();
    }

    @Override
    public void end(boolean interrupted) {
        climber.end();
    }
}
