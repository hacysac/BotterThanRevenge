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
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class PassNotesSeq extends SequentialCommandGroup {
    public PassNotesSeq(Drivetrain drivetrain, Shooter shooter, Indexer indexer, Intake intake, Flip flip, double direction){
        
        //startup sequence
        addCommands(new FlipUp(flip));
        addCommands(new InstantCommand(()->shooter.shoot(RobotMap.AMP_SPEED)));

        addCommands(Commands.parallel(
            Commands.sequence(
                new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME),
                new InstantCommand(()->shooter.shoot(RobotMap.PASS_SPEED))),
            new FlipDown(flip)
        ).withTimeout(2));

        //drive to center and pick up first note
        DoubleSupplier driveAngle = () -> Units.degreesToRadians(0.0);
        Point finalPoint = new Point(Units.inchesToMeters(RobotMap.ROBOT_STARTING_ZONE_WIDTH-((RobotMap.CHASSIS_WIDTH+RobotMap.BUMPER_WIDTH)/2)), 0);
        Pose2d startPose = new Pose2d(0,0, new Rotation2d());
        double time = 3.0;
        double speed = finalPoint.x/time;
        addCommands(Commands.parallel(
            new driveSegment(drivetrain, driveAngle, finalPoint, speed, startPose, true),
            new AutoIntakeIn(intake, indexer, (time+.75))).withTimeout(3));
        startPose.plus(new Transform2d(finalPoint.x, finalPoint.y, new Rotation2d()));
        
        //turn around and shoot
        DoubleSupplier turnAround = () -> Units.degreesToRadians(180.0);
        addCommands(new RotateAngle(drivetrain, turnAround).withTimeout(2));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME).withTimeout(2));

        //turn to second note
        DoubleSupplier turnToNote = () -> Units.degreesToRadians(direction*-90.0);
        addCommands(new RotateAngle(drivetrain, turnToNote).withTimeout(2));

        //get second note
        Point notePoint = new Point(Units.inchesToMeters(0), Units.inchesToMeters(direction*RobotMap.CENTER_NOTE_TO_NOTE));
        double noteTime = 1.0;
        double noteSpeed = notePoint.y/noteTime;
        addCommands(Commands.parallel(
            new driveSegment(drivetrain, driveAngle, notePoint, noteSpeed, startPose, true),
            new AutoIntakeIn(intake, indexer, RobotMap.AUTO_INTAKE_TIME).withTimeout(1))
        );
        startPose.plus(new Transform2d(notePoint.x, notePoint.y, new Rotation2d()));

        //drive out from speaker
        Point passPoint = new Point(0, Units.inchesToMeters(direction*RobotMap.PASS_SPEAKER_OFFSET));
        double passTime = 1.0;
        double passSpeed = Math.abs(passPoint.y)/passTime;
        addCommands(new driveSegment(drivetrain, driveAngle, passPoint, passSpeed, startPose, true).withTimeout(1));
        startPose.plus(new Transform2d(passPoint.x, passPoint.y, new Rotation2d()));

        //turn to our field and shoot second note
        DoubleSupplier turnToAllies = () -> Units.degreesToRadians(direction*90.0);
        addCommands(new RotateAngle(drivetrain, turnToAllies).withTimeout(2));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME).withTimeout(2));
        
        //turn to third note
        addCommands(new RotateAngle(drivetrain, turnToNote).withTimeout(2));

        //drive back from speaker
        Point unpassPoint = new Point(0, -direction*RobotMap.PASS_SPEAKER_OFFSET);
        double unpassTime = 1.0;
        double unpassSpeed = Math.abs(unpassPoint.y)/unpassTime;
        addCommands(new driveSegment(drivetrain, driveAngle, unpassPoint, unpassSpeed, startPose, true).withTimeout(1));
        startPose.plus(new Transform2d(unpassPoint.x, unpassPoint.y, new Rotation2d()));

        //get third note
        addCommands(Commands.parallel(
            new driveSegment(drivetrain, driveAngle, notePoint, noteSpeed, startPose, true),
            new AutoIntakeIn(intake, indexer, (time+.75))).withTimeout(2)
        );
        startPose.plus(new Transform2d(notePoint.x, notePoint.y, new Rotation2d()));

        //drive out from speaker
        Point backNote = new Point(0, -direction*Units.inchesToMeters(RobotMap.CENTER_NOTE_TO_NOTE));
        addCommands(new driveSegment(drivetrain, driveAngle, backNote, noteSpeed, startPose, true).withTimeout(2));
        addCommands(new driveSegment(drivetrain, driveAngle, passPoint, passSpeed, startPose, true).withTimeout(2));
        startPose.plus(new Transform2d(backNote.x, backNote.y, new Rotation2d()));
        startPose.plus(new Transform2d(passPoint.x, passPoint.y, new Rotation2d()));

        // turn and fire third note
        addCommands(new RotateAngle(drivetrain, turnToAllies).withTimeout(2));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME).withTimeout(2));

        //turn to fourth note
        addCommands(new RotateAngle(drivetrain, turnToNote).withTimeout(2));

        //drive back from speaker
        addCommands(new driveSegment(drivetrain, driveAngle, notePoint, noteSpeed, startPose, true).withTimeout(2));
        addCommands(new driveSegment(drivetrain, driveAngle, unpassPoint, unpassSpeed, startPose, true).withTimeout(2));
        startPose.plus(new Transform2d(notePoint.x, notePoint.y, new Rotation2d()));
        startPose.plus(new Transform2d(unpassPoint.x, unpassPoint.y, new Rotation2d()));
        
        
        //get fourth note
        addCommands(Commands.parallel(
            new driveSegment(drivetrain, driveAngle, notePoint, noteSpeed, startPose, true),
            new AutoIntakeIn(intake, indexer, (time+.75))).withTimeout(2)
        );
        startPose.plus(new Transform2d(notePoint.x, notePoint.y, new Rotation2d()));
        
        //drive front from speaker
        addCommands(new driveSegment(drivetrain, driveAngle, unpassPoint, unpassSpeed, startPose, true).withTimeout(2));
        startPose.plus(new Transform2d(unpassPoint.x, unpassPoint.y, new Rotation2d()));
        
        // turn and fire
        addCommands(new RotateAngle(drivetrain, turnToAllies).withTimeout(2));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME).withTimeout(2));

        //turn to fifth note
        addCommands(new RotateAngle(drivetrain, turnToNote).withTimeout(2));

        //get fifth note
        Point finalNotePoint = new Point(0, direction*(RobotMap.CENTER_NOTE_TO_NOTE+RobotMap.PASS_SPEAKER_OFFSET));
        double finalNoteTime = 1.0;
        double finalNoteSpeed = Math.abs(finalNotePoint.y)/finalNoteTime;
        addCommands(Commands.parallel(
            new driveSegment(drivetrain, driveAngle, finalNotePoint, finalNoteSpeed, startPose, true),
            new AutoIntakeIn(intake, indexer, (time+.75))).withTimeout(2)
        );
        startPose.plus(new Transform2d(finalNotePoint.x, finalNotePoint.y, new Rotation2d()));

        // turn and fire
        addCommands(new RotateAngle(drivetrain, turnToAllies).withTimeout(2));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME).withTimeout(2));
    }
}