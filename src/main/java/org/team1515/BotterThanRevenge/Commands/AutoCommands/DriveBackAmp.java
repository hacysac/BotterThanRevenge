package org.team1515.BotterThanRevenge.Commands.AutoCommands;

import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.AutoIntakeIn;
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

public class DriveBackAmp extends SequentialCommandGroup{
     public DriveBackAmp(Drivetrain drivetrain, Shooter shooter, Intake intake, Indexer indexer, Flip flip, Pose2d start, double direction){
        double ampToCenter = Units.inchesToMeters(RobotMap.AMP_TO_CENTER - RobotMap.CHASSIS_WIDTH - (2*RobotMap.BUMPER_WIDTH)); //TODO find
        
        DoubleSupplier angle = ()->Units.degreesToRadians(0);
        double time = 1.5;
        double speed = ampToCenter/time;

        //DRIVE TO CENTER
        new InstantCommand(()->shooter.end());
        Pose2d startPoint = start;
        Point finalPoint = new Point((ampToCenter/2), direction*Units.inchesToMeters(96-(0.5 * RobotMap.CHASSIS_WIDTH))/2);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        
        angle = ()->Units.degreesToRadians(-direction*50);

        startPoint = new Pose2d(new Translation2d(startPoint.getX()+finalPoint.x, startPoint.getY()+finalPoint.y), new Rotation2d(0.0));
        finalPoint = new Point(ampToCenter/2, Units.inchesToMeters(direction*Units.inchesToMeters(96-(0.5 * RobotMap.CHASSIS_WIDTH))/2));
        addCommands(Commands.parallel(
            new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true),
            new AutoIntakeIn(intake, indexer, time+0.75)
        ));
     }
}