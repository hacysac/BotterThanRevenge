package org.team1515.BotterThanRevenge.Commands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Indexer;
import org.team1515.BotterThanRevenge.Subsystems.Intake;

public class AutoIntakeIn extends Command {
    private final Intake intake; 
    private final Indexer indexer;
    private double time;
    private double startTime;

    public AutoIntakeIn(Intake intake, Indexer indexer) {
        this.intake = intake;
        this.indexer = indexer;
        this.time = 1000; // 1000 seconds will not be reached
        addRequirements(intake);
        addRequirements(indexer);
    }

    public AutoIntakeIn(Intake intake, Indexer indexer, double time) {
        this.intake = intake;
        this.indexer = indexer;
        this.time = time;
        addRequirements(intake);
        addRequirements(indexer);
    }

    @Override
    public void initialize() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void execute() {
        intake.in();
        indexer.up();
    }

    @Override
    public boolean isFinished() {
        return indexer.isBlocked() || intake.getDone() || (System.currentTimeMillis()-startTime) >= time*1000;
    }

    @Override
    public void end(boolean interrupted) {
        intake.endIntake();
        intake.setDone(true);
    }
}
