package org.team1515.BotterThanRevenge.Commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Intake;

public class ManualFlip extends Command {
    private final Intake intake; 

    private boolean up;

    public ManualFlip(Intake intake, boolean up) {
        this.intake = intake;
        this.up = up;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        if (up){
            intake.flipUp();
        }
        else{
            intake.flipDown();
        }
    }

    @Override
    public boolean isFinished(){
        return true;
    }

    @Override
    public void end(boolean interrupted) {
        intake.endFlip();
    }
}
