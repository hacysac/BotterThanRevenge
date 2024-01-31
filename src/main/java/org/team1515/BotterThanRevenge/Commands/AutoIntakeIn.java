package org.team1515.BotterThanRevenge.Commands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Indexer;
import org.team1515.BotterThanRevenge.Subsystems.Intake;

public class AutoIntakeIn extends Command {
    private final Intake intake; 
    private final Indexer indexer;

    public AutoIntakeIn(Intake intake, Indexer indexer) {
        this.intake = intake;
        this.indexer = indexer;
        addRequirements(intake);
        addRequirements(indexer);
    }

    @Override
    public void execute() {
        intake.in();
        indexer.up();
    }

    @Override
    public boolean isFinished() {
        return indexer.isBlocked() || intake.getDone();
    }

    @Override
    public void end(boolean interrupted) {
        intake.endIntake();
        intake.setDone(true);
    }
}
