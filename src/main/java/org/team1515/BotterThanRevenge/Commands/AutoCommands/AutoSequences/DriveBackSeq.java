package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences;

import java.util.function.DoubleSupplier;
import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveSegment;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.Utils.Point;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class DriveBackSeq extends SequentialCommandGroup {
    public DriveBackSeq(Drivetrain drivetrain){
        DoubleSupplier angle = () -> Units.degreesToRadians(180.0);
        Point finalPoint = new Point(Units.inchesToMeters(RobotMap.SUBWOOFER_DEPTH+RobotMap.SUBWOOFER_TO_NOTE), 0);
        double time = 1.0;
        double speed = finalPoint.x/time;

        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, drivetrain.getOdometry(), true));
    }
}