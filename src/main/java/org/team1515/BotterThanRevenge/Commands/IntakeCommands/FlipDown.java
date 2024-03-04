package org.team1515.BotterThanRevenge.Commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Flip;

public class FlipDown extends Command {
    private final Flip flip; 

    public FlipDown(Flip flip) {
        this.flip = flip;
        addRequirements(flip);
    }

    @Override
    public void initialize() {
        flip.setUp(false);
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished(){
        return flip.getDown();
    }

    @Override
    public void end(boolean interrupted) {
        //flip.setCurrentLowValue();
        flip.end();
    }
}
