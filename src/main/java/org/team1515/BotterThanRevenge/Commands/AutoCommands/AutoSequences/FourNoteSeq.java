
package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences;

import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveArcLength;
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
import org.team1515.BotterThanRevenge.Utils.bezierUtil;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class FourNoteSeq extends SequentialCommandGroup{
    public FourNoteSeq(Drivetrain drivetrain, Shooter shooter, Indexer indexer, Intake intake, Flip flip, double direction){
        Pose2d subwoofer = new Pose2d(new Translation2d(0,0), new Rotation2d(0.0)); //Starting from subwoofer
        
        double subwooferToNoteX = Units.inchesToMeters(RobotMap.SUBWOOFER_TO_NOTE - RobotMap.CHASSIS_WIDTH - (2*RobotMap.BUMPER_WIDTH));
        double subwooferToNoteY = -direction * Units.inchesToMeters(RobotMap.NOTE_TO_NOTE - (0.5 * RobotMap.CHASSIS_WIDTH));
        double subwooferToCenter = Units.inchesToMeters(RobotMap.SUBWOOFER_TO_CENTER - RobotMap.CHASSIS_WIDTH - (2*RobotMap.BUMPER_WIDTH));
    
        DoubleSupplier angle = () -> Units.degreesToRadians(0.0); //make sure shooter is forward
        
        //start shooter + flip up to find offsets
        addCommands(new FlipUp(flip));
        addCommands(new InstantCommand(()->shooter.shoot(RobotMap.SPEAKER_SPEED)));
        
        //FEED PIECE + FLIP DOWN: run indexer 0.5 seconds?
        addCommands(Commands.parallel(
                new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME),
                new FlipDown(flip)
        ));
        //end shooter and indexer
        
        
        angle = () -> Units.degreesToRadians(0);
        double time = 1;
        double speed = subwooferToNoteX/time;

        //DRIVE BACK + flip down + auto intake 1 second limit?
        Pose2d startPoint = subwoofer;
        Point finalPoint = new Point(subwooferToNoteX, 0);
        addCommands(Commands.parallel(
                new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true),
                new AutoIntakeIn(intake, indexer, RobotMap.AUTO_INTAKE_TIME)
        ));
        
        //PICK UP PIECE: run intake+indexer 1 second limit
        

        //DRIVE FORWARD + flip up + start shooter
        startPoint = new Pose2d(new Translation2d(subwoofer.getX()+finalPoint.x, subwoofer.getY()+finalPoint.y), new Rotation2d(0.0));
        finalPoint = new Point(-subwooferToNoteX, 0);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        
        //FEED PIECE: run indexer 0.5 seconds?
        addCommands(Commands.parallel(
                new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME),
                new FlipDown(flip)
        ));
        //end shooter and indexer
        

        angle = ()->Units.degreesToRadians(-RobotMap.AUTO_NOTE_ANGLE_OFFSET*direction);
        double dist = Math.sqrt(Math.pow(subwooferToNoteX, 2)+Math.pow(subwooferToNoteY, 2));
        time = 1;
        speed = dist/time;

        //DRIVE DIAGONAL BACKWARD + flip down + auto intake 1.5 second limit?
        startPoint = subwoofer;
        finalPoint = new Point(subwooferToNoteX, subwooferToNoteY);
        addCommands(Commands.parallel(
                new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true),
                new AutoIntakeIn(intake, indexer, RobotMap.AUTO_INTAKE_TIME)
        ));

        
        //PICK UP PIECE: run intake+indexer 1 second limit
        

        angle = ()->Units.degreesToRadians(RobotMap.AUTO_NOTE_ANGLE_OFFSET*direction);
        
        //DRIVE DIAGONAL FORWARD + flip up + start shooter
        startPoint = new Pose2d(new Translation2d(subwoofer.getX()+finalPoint.x, subwoofer.getY()+finalPoint.y), new Rotation2d(150.0));
        finalPoint = new Point(-subwooferToNoteX, -subwooferToNoteY);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));

        //FEED PIECE: run indexer 0.5 seconds?
        addCommands(Commands.parallel(
                new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME),
                new FlipDown(flip)
        ));
        //end shooter and indexer
        

        angle = ()->Units.degreesToRadians(RobotMap.AUTO_NOTE_ANGLE_OFFSET*direction);
        dist = Math.sqrt(Math.pow(subwooferToNoteX, 2)+Math.pow(subwooferToNoteY, 2));
        time = 1;
        speed = dist/time;

        //DRIVE DIAGONAL BACKWARD + flip down + auto intake 1.5 second limit?
        startPoint = subwoofer;
        finalPoint = new Point(subwooferToNoteX, -subwooferToNoteY);
        addCommands(Commands.parallel(
                new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true),
                new AutoIntakeIn(intake, indexer, RobotMap.AUTO_INTAKE_TIME)
        ));

        
        //PICK UP PIECE: run intake+indexer 1 second limit
        

        angle = ()->Units.degreesToRadians(-RobotMap.AUTO_NOTE_ANGLE_OFFSET*direction);
        
        //DRIVE DIAGONAL FORWARD + flip up + start shooter
        startPoint = new Pose2d(new Translation2d(subwoofer.getX()+finalPoint.x, subwoofer.getY()+finalPoint.y), new Rotation2d(150.0));
        finalPoint = new Point(-subwooferToNoteX, subwooferToNoteY);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        
        //FEED PIECE: run indexer 0.5 seconds?
        addCommands(Commands.parallel(
                new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME),
                new FlipDown(flip)
        ));
        //end shooter and indexer
        

        angle = ()->Units.degreesToRadians(0.0);
        time = 3;

        //DRIVE TO CENTER + end shooter
        addCommands(new InstantCommand(()->shooter.end()));
        startPoint = subwoofer;
        finalPoint = new Point(subwooferToCenter, 0);
        //addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        Point[] centerPath = {
                new Point(0, 0),
                new Point(Units.inchesToMeters(subwooferToCenter/2), direction * Units.inchesToMeters(24)), 
                finalPoint
            };
        centerPath = bezierUtil.spacedPoints(centerPath, 50);
        //addCommands(new driveArcLength(drivetrain, centerPath, time, angle, startPoint));   
        //end all
    }
}
