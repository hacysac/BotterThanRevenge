package org.team1515.BotterThanRevenge.Commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Flip;

public class TriggeredFlipDown extends Command {
    private final Flip flip; 

    public TriggeredFlipDown(Flip flip) {
        this.flip = flip;
        addRequirements(flip);
    }

    @Override
    public void initialize() {
        flip.setUp(false);
    }

    @Override
    public void execute() {
        flip.slowDown();
    }

    @Override
    public boolean isFinished(){
        return flip.getUp();
    }

    @Override
    public void end(boolean interrupted) {
        flip.setCurrentTopValue();
        flip.setUp(true);
        flip.end();
    }
}
