package org.team1515.BotterThanRevenge.Commands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Indexer;
import org.team1515.BotterThanRevenge.Subsystems.Intake;

public class IntakeIn extends Command {
    private final Intake intake; 
    private final Indexer indexer;

    public IntakeIn(Intake intake, Indexer indexer) {
        this.intake = intake;
        this.indexer = indexer;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        intake.in();
        indexer.up();
    }

    @Override
    public boolean isFinished() {
        return indexer.isBlocked();
    }

    @Override
    public void end(boolean interrupted) {
        intake.endIntake();
    }
}
