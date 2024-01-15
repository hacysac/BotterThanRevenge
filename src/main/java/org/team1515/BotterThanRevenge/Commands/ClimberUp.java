package org.team1515.BotterThanRevenge.Commands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Climber;

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