package org.team1515.BotterThanRevenge.Commands.ClimberCommands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Climber;

public class ZeroClimber extends Command {

    private final Climber climber;

    public ZeroClimber(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }

    @Override
    public void execute() {
        climber.down();
    }

    @Override
    public boolean isFinished() {
        return climber.getDown();
    }

    @Override
    public void end(boolean interrupted) {
        climber.end();
    }
}