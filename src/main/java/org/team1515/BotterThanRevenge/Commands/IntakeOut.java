package org.team1515.BotterThanRevenge.Commands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Intake;

public class IntakeOut extends Command {
    private final Intake intake; 

    public IntakeOut(Intake intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        intake.out();
    }

    @Override
    public void end(boolean interrupted) {
        intake.endIntake();
    }
}
