package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences;

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

public class TwoSpeakerAmpSeq extends SequentialCommandGroup{
    public TwoSpeakerAmpSeq(Drivetrain drivetrain, Shooter shooter, Indexer indexer, Intake intake, Flip flip, double direction){  
        double subwooferToNoteX = Units.inchesToMeters(RobotMap.SUBWOOFER_TO_NOTE - RobotMap.CHASSIS_WIDTH + 1);
        double subwooferToNoteY = Units.inchesToMeters(RobotMap.NOTE_TO_NOTE - (0.5 * RobotMap.CHASSIS_WIDTH) + 3);
        double noteToAmpX = -Units.inchesToMeters(RobotMap.NOTE_TO_AMP_X - 0.5*RobotMap.CHASSIS_WIDTH + 10);
        double noteToAmpY = -direction * (Units.inchesToMeters(RobotMap.NOTE_TO_AMP_Y - 0.5*RobotMap.CHASSIS_WIDTH - 2*RobotMap.BUMPER_WIDTH + 18)); // TODO
        double ampToCenter = Units.inchesToMeters(RobotMap.AMP_TO_CENTER - RobotMap.CHASSIS_WIDTH - (2*RobotMap.BUMPER_WIDTH)); //TODO find
        
        Point pose0 = new Point(0, 0);
        Point pose1 = new Point(subwooferToNoteX, 0);
        Point pose2 = new Point(subwooferToNoteX, -direction*subwooferToNoteY);
        Point pose3 = new Point(subwooferToNoteX+noteToAmpX, subwooferToNoteY+noteToAmpY);
        Point finalPose = new Point(pose3.x + ampToCenter, pose3.y + direction*(Units.inchesToMeters(96-(0.5 * RobotMap.CHASSIS_WIDTH))));

        double firstRotation = -RobotMap.AUTO_NOTE_ANGLE_OFFSET*direction;
        double secondRotation = direction*RobotMap.AUTO_AMP_ANGLE_OFFSET;
        
        addCommands(new FlipUp(flip).withTimeout(2));
        addCommands(new InstantCommand(()->shooter.shootSpeaker()));
        
        addCommands(Commands.waitSeconds(0.5));
        
        //Score Stoed Note
        addCommands(Commands.parallel(
                new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME),
                new FlipDown(flip)
        ).withTimeout(2));

        //Pick Up Note 1
        addCommands(Commands.parallel(
                new driveLine(drivetrain, 0, pose1, 1),
                new AutoIntakeIn(intake, indexer, RobotMap.AUTO_INTAKE_TIME)
        ).withTimeout(2));

        //Score Note 1
        addCommands(new driveLine(drivetrain, 0, pose0, 1).withTimeout(2));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME).withTimeout(2));

        //Pick Up Note 2
        addCommands(Commands.parallel(
                new driveLine(drivetrain, firstRotation, pose2, 1, true),
                new AutoIntakeIn(intake, indexer, RobotMap.AUTO_INTAKE_TIME)
        ).withTimeout(2));

        //Score Note 2
        addCommands(new driveLine(drivetrain, secondRotation, pose3, 1));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME));

        //Drive Back
        addCommands(new InstantCommand(()->shooter.end()));
        addCommands(new driveLine(drivetrain, 0, finalPose, 1));
    }
}
