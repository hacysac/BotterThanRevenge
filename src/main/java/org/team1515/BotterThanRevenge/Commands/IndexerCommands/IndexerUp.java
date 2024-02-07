package org.team1515.BotterThanRevenge.Commands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Indexer;

public class IndexerUp extends Command {
    private final Indexer indexer;

    public IndexerUp(Indexer indexer) {
        this.indexer = indexer;
        addRequirements(indexer);
    }

    @Override
    public void execute() {
        indexer.up();
    }

    @Override
    public void end(boolean interrupted) {
        indexer.end();
    }
}
