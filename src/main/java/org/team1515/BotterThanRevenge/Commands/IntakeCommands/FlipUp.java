package org.team1515.BotterThanRevenge.Commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Flip;

public class FlipUp extends Command {
    private final Flip flip; 

    public FlipUp(Flip flip) {
        this.flip = flip;
        addRequirements(flip);
    }

    @Override
    public void execute() {
        flip.flipUp();
    }

    @Override
    public boolean isFinished(){
        return flip.getUp();
    }

    @Override
    public void end(boolean interrupted) {
        flip.end();
    }
}
