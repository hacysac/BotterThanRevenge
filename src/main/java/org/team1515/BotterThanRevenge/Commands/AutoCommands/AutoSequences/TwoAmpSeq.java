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

public class TwoAmpSeq extends SequentialCommandGroup{
     public TwoAmpSeq(Drivetrain drivetrain, Shooter shooter, Indexer indexer, Intake intake, Flip flip, double direction){

        double finalPoseY = direction*(RobotMap.SUBWOOFER_TO_AMP - (RobotMap.CHASSIS_WIDTH + 2*RobotMap.BUMPER_WIDTH)); //assuming red
        Pose2d amp = new Pose2d(new Translation2d(Units.inchesToMeters(RobotMap.WALL_TO_AMP + RobotMap.AUTO_OFFSET - (0.5*RobotMap.CHASSIS_WIDTH + RobotMap.BUMPER_WIDTH)), Units.inchesToMeters(finalPoseY)), new Rotation2d(0.0));
        
        double noteToAmpX = -(Units.inchesToMeters(RobotMap.NOTE_TO_AMP_X) - 0.5*RobotMap.CHASSIS_WIDTH);
        double noteToAmpY = -direction * (Units.inchesToMeters(RobotMap.NOTE_TO_AMP_Y - 0.5*RobotMap.CHASSIS_WIDTH - 2*RobotMap.BUMPER_WIDTH)+10); // TODO
        double ampToCenter = Units.inchesToMeters(RobotMap.AMP_TO_CENTER - RobotMap.CHASSIS_WIDTH - (2*RobotMap.BUMPER_WIDTH) - RobotMap.INTAKE_OFFSET); //TODO find
        
        Point[] path = {
            new Point(0, 0),
            new Point(Units.inchesToMeters(53.5), direction * Units.inchesToMeters(2.3)), 
            new Point(amp.getX(), amp.getY())
        };
        path = bezierUtil.spacedPoints(path, 25);
        DoubleSupplier angle = () -> Units.degreesToRadians(direction*90.0); //make sure intake is forward
        
        //start shooter speed up
        //BEZIER
        addCommands(Commands.parallel(
            new driveArcLength(drivetrain, path, 3.5, angle),
            new InstantCommand(()->shooter.shoot(RobotMap.AMP_SPEED)),
            new FlipUp(flip)
        ));

        //FEED PIECE: run indexer 0.5 seconds?
        addCommands(Commands.parallel(
            new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME),
            new FlipDown(flip)
        ));
        //end shooter and indexer

        
        
        angle = () -> Units.degreesToRadians(-RobotMap.AUTO_NOTE_ANGLE_OFFSET*direction);
        double time = 1;
        double dist = Math.sqrt(Math.pow(noteToAmpX, 2)+Math.pow(noteToAmpY, 2));
        double speed = dist/time;

        //DRIVE BACK
        Pose2d startPoint = amp;
        Point finalPoint = new Point(-noteToAmpX, -noteToAmpY);
        addCommands(Commands.parallel(
            new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true),
            new AutoIntakeIn(intake, indexer, RobotMap.AUTO_INTAKE_TIME)
        ));

        //DRIVE FORWARD
        startPoint = new Pose2d(new Translation2d(amp.getX()+finalPoint.x, amp.getY()+finalPoint.y), new Rotation2d(180.0));
        finalPoint = new Point(noteToAmpX, noteToAmpY);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        
        //FEED PIECE: run indexer 0.5 seconds?
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME));
        //end shooter and indexer

        angle = ()->Units.degreesToRadians(direction*-90);
        time = 3;
        speed = ampToCenter/time;

        //DRIVE TO CENTER
        startPoint = amp;
        finalPoint = new Point(ampToCenter, 0);
        //addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        //end all
    }
}
