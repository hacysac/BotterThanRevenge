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

public class TwoSpeakerAmpSeq extends SequentialCommandGroup{
     public TwoSpeakerAmpSeq(Drivetrain drivetrain, Shooter shooter, Indexer indexer, Intake intake, Flip flip, boolean runBezier, double direction){

        double finalPoseY = direction*(0.5*RobotMap.CHASSIS_WIDTH + 0.5*RobotMap.SUBWOOFER_LONG_WIDTH + RobotMap.BUMPER_WIDTH); //assuming red
        Pose2d subwoofer = new Pose2d(new Translation2d(0,0), new Rotation2d(0.0));
        if (runBezier){
            subwoofer = new Pose2d(new Translation2d(Units.inchesToMeters(RobotMap.SUBWOOFER_DEPTH + RobotMap.AUTO_OFFSET), Units.inchesToMeters(finalPoseY)), new Rotation2d(0.0));
        }

        double subwooferToNoteX = Units.inchesToMeters(RobotMap.SUBWOOFER_TO_NOTE - RobotMap.CHASSIS_WIDTH - (RobotMap.BUMPER_WIDTH));
        double subwooferToNoteY = -direction * Units.inchesToMeters(RobotMap.NOTE_TO_NOTE - (0.5 * RobotMap.CHASSIS_WIDTH));
        double noteToAmpX = -Units.inchesToMeters(RobotMap.NOTE_TO_AMP_X - 0.5*RobotMap.CHASSIS_WIDTH + 10);
        double noteToAmpY = -direction * (Units.inchesToMeters(RobotMap.NOTE_TO_AMP_Y - 0.5*RobotMap.CHASSIS_WIDTH - 2*RobotMap.BUMPER_WIDTH + 18)); // TODO
        double ampToCenter = Units.inchesToMeters(RobotMap.SUBWOOFER_TO_CENTER - RobotMap.CHASSIS_WIDTH - (2*RobotMap.BUMPER_WIDTH)); //TODO find
        
        Point[] path = {
            new Point(0, 0),
            new Point(Units.inchesToMeters(53.5), direction * Units.inchesToMeters(2.3)), 
            new Point(subwoofer.getX(), subwoofer.getY())
        };
        path = bezierUtil.spacedPoints(path, 25);
        DoubleSupplier angle = () -> Units.degreesToRadians(0.0); //make sure shooter is forward
        
        //start shooter speed up
        //BEZIER
        if (runBezier){
            subwoofer = new Pose2d(new Translation2d(Units.inchesToMeters(RobotMap.SUBWOOFER_DEPTH + RobotMap.AUTO_OFFSET), Units.inchesToMeters(finalPoseY)), new Rotation2d(0.0));
            addCommands(Commands.parallel(
                new FlipUp(flip),
                new InstantCommand(()->shooter.shoot(RobotMap.SPEAKER_SPEED)),
                new driveArcLength(drivetrain, path, 3.5, angle)
            ));
        }
        else{    
            addCommands(Commands.parallel(
                new FlipUp(flip),
                new InstantCommand(()->shooter.shoot(RobotMap.SPEAKER_SPEED))
            ));
        }
        
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
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME));
        //end shooter and indexer

        angle = ()->Units.degreesToRadians(-RobotMap.AUTO_NOTE_ANGLE_OFFSET*direction);
        double dist = Math.sqrt(Math.pow(subwooferToNoteX, 2)+Math.pow(subwooferToNoteY, 2));
        time = 1;
        speed = dist/time;

        //DRIVE DIAGONAL BACKWARD + flip down + auto intake RobotMap.AUTO_INTAKE_TIME second limit?
        startPoint = subwoofer;
        finalPoint = new Point(subwooferToNoteX, subwooferToNoteY);
        addCommands(Commands.parallel(
                new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true),
                new AutoIntakeIn(intake, indexer, RobotMap.AUTO_INTAKE_TIME),
                new InstantCommand(()->shooter.shoot(RobotMap.AMP_SPEED))
        ));

        //PICK UP PIECE: run intake+indexer 1 second limit
        

        angle = ()->Units.degreesToRadians(direction*RobotMap.AUTO_AMP_ANGLE_OFFSET);
        dist = Math.sqrt(Math.pow(noteToAmpX, 2)+Math.pow(noteToAmpY, 2));
        time = 2;
        speed = dist/time;

        //DRIVE TO AMP
        startPoint = new Pose2d(new Translation2d(subwoofer.getX()+finalPoint.x, subwoofer.getY()+finalPoint.y), new Rotation2d(Units.degreesToRadians(-RobotMap.AUTO_NOTE_ANGLE_OFFSET*direction)));
        finalPoint = new Point(noteToAmpX, noteToAmpY);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));

        //FEED PIECE: run indexer 0.5 seconds?
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME));
        //end shooter and indexer

        angle = ()->Units.degreesToRadians(0.0);
        time = 1.5;
        speed = ampToCenter/time;

        //DRIVE TO CENTER
        new InstantCommand(()->shooter.end());
        startPoint = new Pose2d(new Translation2d(startPoint.getX()+finalPoint.x, startPoint.getY()+finalPoint.y), new Rotation2d(0.0));;
        finalPoint = new Point(ampToCenter/2, Units.inchesToMeters(96-(0.5 * RobotMap.CHASSIS_WIDTH))/2);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        
        angle = ()->Units.degreesToRadians(-direction*50);

        startPoint = new Pose2d(new Translation2d(startPoint.getX()+finalPoint.x, startPoint.getY()+finalPoint.y), new Rotation2d(0.0));
        finalPoint = new Point(ampToCenter, Units.inchesToMeters(96-(0.5 * RobotMap.CHASSIS_WIDTH)));
        addCommands(Commands.parallel(
            new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true),
            new AutoIntakeIn(intake, indexer, time+0.75)
        ));
    }
}
