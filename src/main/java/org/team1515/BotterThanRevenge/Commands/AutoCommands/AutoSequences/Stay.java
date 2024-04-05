package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences;

import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.IndexerCommands.AutoFeed;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.TriggeredFlipDown;
import org.team1515.BotterThanRevenge.Subsystems.Flip;
import org.team1515.BotterThanRevenge.Subsystems.Indexer;
import org.team1515.BotterThanRevenge.Subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Stay extends SequentialCommandGroup{
     public Stay(Shooter shooter, Flip flip, Indexer indexer){
        addCommands(Commands.parallel(
            new TriggeredFlipDown(flip),
            new InstantCommand(()->shooter.shootSpeaker())
        ).withTimeout(2));
        addCommands(Commands.waitSeconds(1));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME).withTimeout(2));
        addCommands(Commands.waitSeconds(1));
        addCommands(new InstantCommand(()->shooter.end()));
        //end all
    }
}
