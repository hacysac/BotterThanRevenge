package org.team1515.BotterThanRevenge.Commands.ClimberCommands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Climber;

public class SingleClimber extends Command {

    private final Climber climber;
    private boolean left;

    public SingleClimber(Climber climber, boolean left) {
        this.climber = climber;
        this.left = left;
        addRequirements(climber);
    }

    @Override
    public void execute() {
        if (left){
            climber.lDown();
        }
        else{
            climber.rDown();
        }
    }

    @Override
    public void end(boolean interrupted) {
        climber.end();
    }
}