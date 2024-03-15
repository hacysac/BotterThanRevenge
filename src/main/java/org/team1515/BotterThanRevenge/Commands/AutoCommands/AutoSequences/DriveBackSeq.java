package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences;

import java.util.function.DoubleSupplier;
import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveSegment;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.FlipUp;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.Subsystems.Flip;
import org.team1515.BotterThanRevenge.Utils.Point;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class DriveBackSeq extends SequentialCommandGroup {
    public DriveBackSeq(Drivetrain drivetrain, Flip flip){
        DoubleSupplier angle = () -> Units.degreesToRadians(0.0);
        Point finalPoint = new Point(Units.inchesToMeters(RobotMap.ROBOT_STARTING_ZONE_WIDTH + 5), 0);
        double time = 3.0;
        double speed = finalPoint.x/time;
        addCommands(new FlipUp(flip));
        //addCommands(new InstantCommand(()->System.out.print(drivetrain.getOdometry().getX())));
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, new Pose2d(), true));
    }
}