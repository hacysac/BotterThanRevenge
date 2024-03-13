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
import org.team1515.BotterThanRevenge.Utils.bezierUtil;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class DriveBackSubwoofer extends SequentialCommandGroup{
     public DriveBackSubwoofer(Drivetrain drivetrain, Shooter shooter, Intake intake, Indexer indexer, Flip flip, Pose2d start, double direction){
        double subwooferToCenter = Units.inchesToMeters(RobotMap.SUBWOOFER_TO_CENTER - RobotMap.CHASSIS_WIDTH - (2*RobotMap.BUMPER_WIDTH));
        
        DoubleSupplier angle = ()->Units.degreesToRadians(0.0);
        double time = 3;

        //DRIVE TO CENTER + end shooter
        addCommands(new InstantCommand(()->shooter.end()));
        Pose2d startPoint = start;
        Point finalPoint = new Point(subwooferToCenter, 0);
        Point[] centerPath = {
                new Point(0, 0),
                new Point(subwooferToCenter/2, direction * Units.inchesToMeters(24)), 
                finalPoint
        };
        centerPath = bezierUtil.spacedPoints(centerPath, 25);
        addCommands(Commands.parallel(
                new driveArcLength(drivetrain, centerPath, time, angle, startPoint),
                new AutoIntakeIn(intake, indexer, time+0.75)
        ));
     }
}