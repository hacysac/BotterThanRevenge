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

public class TwoNoteSeq extends SequentialCommandGroup{
     public TwoNoteSeq(Drivetrain drivetrain, Shooter shooter, Indexer indexer, Intake intake, Flip flip, boolean runBezier, double direction){
        //double finalPoseY = direction*(0.5*RobotMap.CHASSIS_WIDTH + 0.5*RobotMap.SUBWOOFER_LONG_WIDTH + RobotMap.BUMPER_WIDTH); //assuming red
        Pose2d subwoofer = new Pose2d(new Translation2d(0,0), new Rotation2d(0.0));
        // if (runBezier){
        //     subwoofer = new Pose2d(new Translation2d(Units.inchesToMeters(RobotMap.SUBWOOFER_DEPTH + RobotMap.AUTO_OFFSET), Units.inchesToMeters(finalPoseY)), new Rotation2d(0.0));
        // }

        double subwooferToNoteX = Units.inchesToMeters(RobotMap.SUBWOOFER_TO_NOTE - RobotMap.CHASSIS_WIDTH + 3);
        
        // Point[] path = {
        //     new Point(0, 0),
        //     new Point(Units.inchesToMeters(53.5), direction * Units.inchesToMeters(2.3)), 
        //     new Point(subwoofer.getX(), subwoofer.getY())
        // };
        // path = bezierUtil.spacedPoints(path, 25);
        DoubleSupplier angle = () -> Units.degreesToRadians(0.0); //make sure shooter is forward
        
        //start shooter speed up
        //BEZIER
        // if (runBezier){
        //     subwoofer = new Pose2d(new Translation2d(Units.inchesToMeters(RobotMap.SUBWOOFER_DEPTH + RobotMap.AUTO_OFFSET), Units.inchesToMeters(finalPoseY)), new Rotation2d(0.0));
        //     addCommands(Commands.parallel(
        //         new FlipUp(flip),
        //         new InstantCommand(()->shooter.shootSpeaker()),
        //         new driveArcLength(drivetrain, path, 3.5, angle)
        //     ));
        // }
        // else{    
        addCommands(Commands.parallel(
            new FlipUp(flip),
            new InstantCommand(()->shooter.shootSpeaker())
        ));
        //}
        
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

        //DRIVE BACK
        startPoint = subwoofer;
        angle = ()->Units.degreesToRadians(0.0);
        time = 1;
        finalPoint = new Point(Units.inchesToMeters(RobotMap.ROBOT_STARTING_ZONE_WIDTH + 5), direction*Units.inchesToMeters(63));
        double dist = Math.sqrt(Math.pow(finalPoint.x, 2)+Math.pow(finalPoint.y, 2));
        speed = dist/time;
        
        addCommands(new InstantCommand(()->shooter.end()));
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, subwoofer, true));
        //end all
    }
}
