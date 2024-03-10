package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences;

import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.RotateAngle;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveSegment;
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

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class PassNotesSeq extends SequentialCommandGroup {
    public PassNotesSeq(Drivetrain drivetrain, Shooter shooter, Indexer indexer, Intake intake, Flip flip, double direction){
        
        //startup sequence
        addCommands(new FlipUp(flip));
        addCommands(new InstantCommand(()->shooter.shoot(RobotMap.AMP_SPEED)));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME));
        addCommands(new InstantCommand(()->shooter.shoot(RobotMap.PASS_SPEED)));
        addCommands(new FlipDown(flip));

        //drive to center and pick up first note
        DoubleSupplier driveAngle = () -> Units.degreesToRadians(0.0);
        Point finalPoint = new Point(Units.inchesToMeters(RobotMap.ROBOT_STARTING_ZONE_WIDTH-((RobotMap.CHASSIS_WIDTH+RobotMap.BUMPER_WIDTH)/2)), 0);
        double time = 3.0;
        double speed = finalPoint.x/time;
        addCommands(Commands.parallel(
            new driveSegment(drivetrain, driveAngle, finalPoint, speed, new Pose2d(), true),
            new AutoIntakeIn(intake, indexer, (time+.75)))
        );
        
        //turn around and shoot
        DoubleSupplier turnAround = () -> Units.degreesToRadians(180.0);
        addCommands(new RotateAngle(drivetrain, turnAround));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME));

        //turn to second note
        DoubleSupplier turnToNote = () -> Units.degreesToRadians(direction*-90.0);
        addCommands(new RotateAngle(drivetrain, turnToNote));

        //get second note
        Point notePoint = new Point(Units.inchesToMeters(0), Units.inchesToMeters(direction*RobotMap.CENTER_NOTE_TO_NOTE));
        double noteTime = 1.0;
        double noteSpeed = notePoint.y/noteTime;
        addCommands(Commands.parallel(
            new driveSegment(drivetrain, driveAngle, notePoint, noteSpeed, new Pose2d(), true),
            new AutoIntakeIn(intake, indexer, (time+.75)))
        );

        //drive out from speaker
        Point passPoint = new Point(0, Units.inchesToMeters(direction*RobotMap.PASS_SPEAKER_OFFSET));
        double passTime = 1.0;
        double passSpeed = Math.abs(passPoint.y)/passTime;
        addCommands(new driveSegment(drivetrain, driveAngle, passPoint, passSpeed, new Pose2d(), true));

        //turn to our field and shoot second note
        DoubleSupplier turnToAllies = () -> Units.degreesToRadians(direction*90.0);
        addCommands(new RotateAngle(drivetrain, turnToAllies));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME));
        
        //turn to third note
        addCommands(new RotateAngle(drivetrain, turnToNote));

        //drive back from speaker
        Point unpassPoint = new Point(0, -direction*RobotMap.PASS_SPEAKER_OFFSET);
        double unpassTime = 1.0;
        double unpassSpeed = Math.abs(unpassPoint.y)/unpassTime;
        addCommands(new driveSegment(drivetrain, driveAngle, unpassPoint, unpassSpeed, new Pose2d(), true));

        //get third note
        addCommands(Commands.parallel(
            new driveSegment(drivetrain, driveAngle, notePoint, noteSpeed, new Pose2d(), true),
            new AutoIntakeIn(intake, indexer, (time+.75)))
        );

        //drive out from speaker
        Point backNote = new Point(0, -direction*Units.inchesToMeters(RobotMap.CENTER_NOTE_TO_NOTE));
        addCommands(new driveSegment(drivetrain, driveAngle, backNote, noteSpeed, new Pose2d(), true));
        addCommands(new driveSegment(drivetrain, driveAngle, passPoint, passSpeed, new Pose2d(), true));

        // turn and fire third note
        addCommands(new RotateAngle(drivetrain, turnToAllies));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME));

        //turn to fourth note
        addCommands(new RotateAngle(drivetrain, turnToNote));

        //drive back from speaker
        addCommands(new driveSegment(drivetrain, driveAngle, notePoint, noteSpeed, new Pose2d(), true));
        addCommands(new driveSegment(drivetrain, driveAngle, unpassPoint, unpassSpeed, new Pose2d(), true));
        
        //get fourth note
        addCommands(Commands.parallel(
            new driveSegment(drivetrain, driveAngle, notePoint, noteSpeed, new Pose2d(), true),
            new AutoIntakeIn(intake, indexer, (time+.75)))
        );
        
        //drive front from speaker
        addCommands(new driveSegment(drivetrain, driveAngle, unpassPoint, unpassSpeed, new Pose2d(), true));
        
        // turn and fire
        addCommands(new RotateAngle(drivetrain, turnToAllies));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME));

        //turn to fifth note
        addCommands(new RotateAngle(drivetrain, turnToNote));

        //get fifth note
        Point finalNotePoint = new Point(0, direction*(RobotMap.CENTER_NOTE_TO_NOTE+RobotMap.PASS_SPEAKER_OFFSET));
        double finalNoteTime = 1.0;
        double finalNoteSpeed = Math.abs(finalNotePoint.y)/finalNoteTime;
        addCommands(Commands.parallel(
            new driveSegment(drivetrain, driveAngle, finalNotePoint, finalNoteSpeed, new Pose2d(), true),
            new AutoIntakeIn(intake, indexer, (time+.75)))
        );

        // turn and fire
        addCommands(new RotateAngle(drivetrain, turnToAllies));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME));
    }
}