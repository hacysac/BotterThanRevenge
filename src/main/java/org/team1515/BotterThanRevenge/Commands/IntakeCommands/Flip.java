package org.team1515.BotterThanRevenge.Commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Intake;

public class Flip extends Command {
    private final Intake intake; 

    public Flip(Intake intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        if (intake.getDown()){
            intake.flipUp();
        }
        else{
            intake.flipDown();
        }
    }

    @Override
    public boolean isFinished(){
        if (intake.getDown()){
            return intake.canCoderUp();
        }
        else{
            return intake.canCoderDown();
        }
    }

    @Override
    public void end(boolean interrupted) {
        intake.endFlip();
        intake.setDown(!intake.getDown()); // reset the intake to the opposite state
    }
}
