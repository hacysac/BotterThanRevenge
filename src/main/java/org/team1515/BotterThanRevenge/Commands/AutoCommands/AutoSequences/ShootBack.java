package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences;

import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveLine;
import org.team1515.BotterThanRevenge.Commands.IndexerCommands.AutoFeed;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.FlipUp;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;`
import org.team1515.BotterThanRevenge.Subsystems.Flip;
import org.team1515.BotterThanRevenge.Subsystems.Indexer;
import org.team1515.BotterThanRevenge.Subsystems.Shooter;
import org.team1515.BotterThanRevenge.Utils.Point;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class DriveBack2Seq extends SequentialCommandGroup {
    public DriveBack2Seq(Shooter shooter, Flip flip, Indexer indexer, Drivetrain drivetrain){
        addCommands(Commands.parallel(
           new FlipUp(flip),
           new InstantCommand(()->shooter.shootSpeaker())
       ).withTimeout(2));
       addCommands(Commands.waitSeconds(1));
       addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME).withTimeout(2));
       addCommands(Commands.waitSeconds(1));
       addCommands(new InstantCommand(()->shooter.end()));
        Point finalPose = new Point(Units.inchesToMeters(RobotMap.ROBOT_STARTING_ZONE_WIDTH + 5), 0);
        //addCommands(new FlipUp(flip).withTimeout(2));
        addCommands(new driveLine(drivetrain, 0, finalPose, 3).withTimeout(2));
    }
}