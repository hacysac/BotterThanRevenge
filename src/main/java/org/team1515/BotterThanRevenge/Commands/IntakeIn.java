package org.team1515.BotterThanRevenge.Commands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Intake;

public class IntakeIn extends Command {
    private final Intake intake; 

    public IntakeIn(Intake intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        intake.in();
    }

    @Override
    public void end(boolean interrupted) {
        intake.end();
    }




}
