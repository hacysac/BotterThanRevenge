package org.team1515.BotterThanRevenge.Commands.IndexerCommands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Indexer;

public class AutoFeed extends Command {
    private final Indexer indexer;
    private double time;
    private double startTime;

    public AutoFeed(Indexer indexer, double time) {
        this.indexer = indexer;
        this.time = time;
        addRequirements(indexer);
    }

    @Override
    public void initialize() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void execute() {
        indexer.up();
    }

    @Override
    public boolean isFinished() {
        return (System.currentTimeMillis()-startTime) >= time*1000 && !indexer.isBlocked();
    }

    @Override
    public void end(boolean interrupted) {
        indexer.end();
    }
}
