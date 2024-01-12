package org.team1515.BotterThanRevenge.Commands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Indexer;

public class IndexerDown extends Command {
    private final Indexer indexer;

    public IndexerDown(Indexer indexer) {
        this.indexer = indexer;
        addRequirements(indexer);
    }

    @Override
    public void execute() {
        indexer.down();
    }

    @Override
    public void end(boolean interrupted) {
        indexer.end();
    }
}
