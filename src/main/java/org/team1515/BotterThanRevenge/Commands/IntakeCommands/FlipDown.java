package org.team1515.BotterThanRevenge.Commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Flip;

public class FlipDown extends Command {
    private final Flip flip; 
    private boolean runWhileUp;

    public FlipDown(Flip flip) {
        this.flip = flip;
        this.runWhileUp = true;
        addRequirements(flip);
    }

    public FlipDown(Flip flip, boolean runWhileUp) {
        this.flip = flip;
        this.runWhileUp = runWhileUp;
        addRequirements(flip);
    }

    @Override
    public void initialize() {
        if (runWhileUp){
            flip.setUp(false);
        }
    }

    @Override
    public void execute() {
        if (runWhileUp || !flip.getUpState()){
            flip.flipDown();
        }
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
