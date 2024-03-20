package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences.Better;

import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveLine;
import org.team1515.BotterThanRevenge.Commands.IndexerCommands.AutoFeed;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.AutoIntakeIn;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.FlipDown;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.FlipUp;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.Subsystems.Flip;
import org.team1515.BotterThanRevenge.Subsystems.Indexer;
import org.team1515.BotterThanRevenge.Subsystems.Intake;
import org.team1515.BotterThanRevenge.Subsystems.Shooter;
import org.team1515.BotterThanRevenge.Utils.Point;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class BetterTwoNote extends SequentialCommandGroup{
    public BetterTwoNote(Drivetrain drivetrain, Shooter shooter, Indexer indexer, Intake intake, Flip flip, double direction){        
        double subwooferToNoteX = Units.inchesToMeters(RobotMap.SUBWOOFER_TO_NOTE - RobotMap.CHASSIS_WIDTH + 1);

        Point pose0 = new Point(0, 0);
        Point pose1 = new Point(subwooferToNoteX, 0);
        Point finalPose = new Point(Units.inchesToMeters(RobotMap.ROBOT_STARTING_ZONE_WIDTH + 5), direction*Units.inchesToMeters(63));
        
        addCommands(new FlipUp(flip));
        addCommands(new InstantCommand(()->shooter.shootSpeaker()));
        
        //Score Stoed Note
        addCommands(Commands.parallel(
                new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME),
                new FlipDown(flip)
        ));

        //Pick Up Note 1
        addCommands(Commands.parallel(
                new driveLine(drivetrain, 0, pose1, 1),
                new AutoIntakeIn(intake, indexer, RobotMap.AUTO_INTAKE_TIME)
        ));

        //Score Note 1
        addCommands(new driveLine(drivetrain, 0, pose0, 1));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME));

        //Drive Back
        addCommands(new InstantCommand(()->shooter.end()));
        addCommands(new driveLine(drivetrain, 0, finalPose, 1));
    }
}
