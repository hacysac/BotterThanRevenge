package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences;

import java.util.function.DoubleSupplier;
import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveSegment;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.Utils.Point;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class DriveBackSeq extends SequentialCommandGroup {
    public DriveBackSeq(Drivetrain drivetrain){
        DoubleSupplier angle = () -> Units.degreesToRadians(0.0);
        Point finalPoint = new Point(Units.inchesToMeters(RobotMap.SUBWOOFER_DEPTH + RobotMap.SUBWOOFER_TO_CENTER - RobotMap.CHASSIS_WIDTH - (2 * RobotMap.BUMPER_WIDTH) - RobotMap.INTAKE_OFFSET), 0);
        double time = 4.0;
        double speed = finalPoint.x/time;
        //addCommands(new InstantCommand(()->System.out.print(drivetrain.getOdometry().getX())));
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, new Pose2d(), true));
    }
}