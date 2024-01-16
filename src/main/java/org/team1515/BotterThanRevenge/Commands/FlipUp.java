package org.team1515.BotterThanRevenge.Commands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Intake;

public class FlipUp extends Command {
    private final Intake intake; 

    public FlipUp(Intake intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        intake.flipUp();
    }

    @Override
    public boolean isFinished(){
        return intake.canCoderUp();
    }

    @Override
    public void end(boolean interrupted) {
        intake.endFlip();
    }
}