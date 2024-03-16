package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences;

import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.DriveBackAmp;
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
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class TwoAmpSeq extends SequentialCommandGroup{
     public TwoAmpSeq(Drivetrain drivetrain, Shooter shooter, Indexer indexer, Intake intake, Flip flip, double direction){

        double finalPoseY = -direction*(RobotMap.SUBWOOFER_TO_AMP - RobotMap.CHASSIS_WIDTH - 2*RobotMap.BUMPER_WIDTH); //assuming red
        Pose2d amp = new Pose2d(new Translation2d(Units.inchesToMeters(RobotMap.WALL_TO_AMP - 0.5*RobotMap.CHASSIS_WIDTH), Units.inchesToMeters(finalPoseY)), new Rotation2d(0.0));
        
        double noteToAmpX = -Units.inchesToMeters(RobotMap.NOTE_TO_AMP_X);
        double noteToAmpY = -direction * Units.inchesToMeters(RobotMap.NOTE_TO_AMP_Y - 0.5*RobotMap.CHASSIS_WIDTH - 2*RobotMap.BUMPER_WIDTH); // TODO
        
        DoubleSupplier angle = () -> Units.degreesToRadians(direction*90.0); //make sure intake is forward
        double time = 1.5;
        double dist = Math.sqrt(Math.pow(amp.getX(), 2)+Math.pow(amp.getY(), 2));
        double speed = dist/time;
        
        //start shooter speed up
        //GO TO AMP
        Pose2d startPoint = new Pose2d(new Translation2d(0,0), new Rotation2d(0.0));
        Point finalPoint = new Point(amp.getX(), amp.getY());
        addCommands(Commands.parallel(
            new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true),
            new InstantCommand(()->shooter.shootAmp()),
            new FlipUp(flip)
        ).withTimeout(1.5));

        //FEED PIECE: run indexer 0.5 seconds?
        addCommands(Commands.parallel(
            new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME),
            new FlipDown(flip)
        ).withTimeout(2));
        //end shooter and indexer

        
        
        angle = () -> Units.degreesToRadians(-(RobotMap.AUTO_NOTE_ANGLE_OFFSET + 10)*direction);
        time = 2;
        dist = Math.sqrt(Math.pow(noteToAmpX, 2)+Math.pow(noteToAmpY, 2));
        speed = dist/time;

        //DRIVE BACK
        startPoint = amp;
        finalPoint = new Point(-noteToAmpX, -noteToAmpY);
        addCommands(Commands.parallel(
            new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true),
            new AutoIntakeIn(intake, indexer, RobotMap.AUTO_INTAKE_TIME)
        ).withTimeout(2));

        angle = () -> Units.degreesToRadians((RobotMap.AUTO_NOTE_ANGLE_OFFSET+10)*direction);
        time = 2;
        dist = Math.sqrt(Math.pow(noteToAmpX - Units.inchesToMeters(10), 2)+Math.pow(noteToAmpY, 2));
        speed = dist/time;

        //DRIVE FORWARD
        startPoint = new Pose2d(new Translation2d(startPoint.getX()+finalPoint.x, startPoint.getY()+finalPoint.y), new Rotation2d(0.0));
        finalPoint = new Point((noteToAmpX - Units.inchesToMeters(10)), noteToAmpY);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true).withTimeout(2));
        
        //FEED PIECE: run indexer 0.5 seconds?
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME).withTimeout(0.75));

        //DRIVE BACK
        new InstantCommand(()->shooter.end());

        addCommands(new InstantCommand(()->shooter.end()));

        double ampToCenter = Units.inchesToMeters(RobotMap.AMP_TO_CENTER - RobotMap.CHASSIS_WIDTH - (2*RobotMap.BUMPER_WIDTH)); //TODO find
        angle = ()->Units.degreesToRadians(0);
        time = 3;
        speed = ampToCenter/time;

        startPoint = new Pose2d(new Translation2d(startPoint.getX()+finalPoint.x, startPoint.getY()+finalPoint.y), new Rotation2d(0.0));
        finalPoint = new Point(ampToCenter/3, direction*Units.inchesToMeters(96-(0.5 * RobotMap.CHASSIS_WIDTH))/3);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true).withTimeout(2));
        
        angle = ()->Units.degreesToRadians(-direction*50);

        startPoint = new Pose2d(new Translation2d(startPoint.getX()+finalPoint.x, startPoint.getY()+finalPoint.y), new Rotation2d(0.0));
        finalPoint = new Point((2*ampToCenter)/3, direction*(2*Units.inchesToMeters(96-(0.5 * RobotMap.CHASSIS_WIDTH)))/3);
        addCommands(Commands.parallel(
            new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true),
            new AutoIntakeIn(intake, indexer, time+0.75)
        ).withTimeout(2));
        //addCommands(new DriveBackAmp(drivetrain, shooter, intake, indexer, flip, startPoint, direction));
        //end all
    }
}
