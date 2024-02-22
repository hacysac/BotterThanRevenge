package org.team1515.BotterThanRevenge.Commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Flip;

public class ManualFlip extends Command {
    private final Flip flip; 

    private final boolean up; //check if final fixes

    public ManualFlip(Flip flip, boolean up) {
        this.flip = flip;
        this.up = up;
        addRequirements(flip);
    }

    @Override
    public void execute() {
        if (up){
            flip.flipUp();
        }
        else{
            flip.flipDown();
        }
    }

    @Override
    public boolean isFinished(){
        return (up && flip.getUp()) || (!up && flip.getDown());
    }

    @Override
    public void end(boolean interrupted) {
        flip.end();
    }
}
