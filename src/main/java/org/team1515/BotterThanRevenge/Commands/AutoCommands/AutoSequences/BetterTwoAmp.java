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

public class BetterTwoAmp extends SequentialCommandGroup{
    public BetterTwoAmp(Drivetrain drivetrain, Shooter shooter, Indexer indexer, Intake intake, Flip flip, double direction){        
        
        double ampToNoteX = Units.inchesToMeters(RobotMap.NOTE_TO_AMP_X);
        double ampToNoteY = direction * Units.inchesToMeters(RobotMap.NOTE_TO_AMP_Y - 0.5*RobotMap.CHASSIS_WIDTH - 2*RobotMap.BUMPER_WIDTH); // TODO
        double ampX = Units.inchesToMeters(RobotMap.WALL_TO_AMP - 0.5*RobotMap.CHASSIS_WIDTH); //assuming red
        double ampY = -direction*Units.inchesToMeters(RobotMap.SUBWOOFER_TO_AMP - RobotMap.CHASSIS_WIDTH - 2*RobotMap.BUMPER_WIDTH); //assuming red

        Point pose1 = new Point(ampX, ampY);
        Point pose2 = new Point(ampX+ampToNoteX, ampY+ampToNoteY);
        Point finalPose = new Point(Units.inchesToMeters(RobotMap.ROBOT_STARTING_ZONE_WIDTH + 5), direction*Units.inchesToMeters(63));

        double firstRotation = Units.degreesToRadians(90*direction);
        double secondRotation = -(Units.degreesToRadians(50))*direction;
        
        addCommands(new FlipUp(flip).withTimeout(2));
        addCommands(new InstantCommand(()->shooter.shootAmp()));
        
        //Drive to Amp
        addCommands(new driveLine(drivetrain, firstRotation, pose1, 1).withTimeout(2));

        //Score Stoed Note
        addCommands(Commands.parallel(
                new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME),
                new FlipDown(flip)
        ).withTimeout(2));

        //Pick Up Note 1
        addCommands(Commands.parallel(
                new driveLine(drivetrain, secondRotation, pose2, 1),
                new AutoIntakeIn(intake, indexer, RobotMap.AUTO_INTAKE_TIME)
        ).withTimeout(2));

        //Score Note 1
        addCommands(new driveLine(drivetrain, -secondRotation, pose1, 1).withTimeout(2));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME).withTimeout(2));

        //Drive Back
        addCommands(new InstantCommand(()->shooter.end()));
        addCommands(new driveLine(drivetrain, -firstRotation, finalPose, 1).withTimeout(2));
    }
}
