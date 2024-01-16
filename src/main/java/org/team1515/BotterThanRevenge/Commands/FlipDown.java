package org.team1515.BotterThanRevenge.Commands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Intake;

public class FlipDown extends Command {
    private final Intake intake; 

    public FlipDown(Intake intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        intake.in();
    }

    @Override
    public boolean isFinished(){
        return intake.canCoderDown();
    }

    @Override
    public void end(boolean interrupted) {
        intake.endFlip();
    }
}
