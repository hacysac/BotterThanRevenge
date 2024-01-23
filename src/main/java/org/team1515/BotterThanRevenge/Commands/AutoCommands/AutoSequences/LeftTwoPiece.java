package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences;

import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveArcLength;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.Utils.Point;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class LeftTwoPiece extends SequentialCommandGroup{
     public LeftTwoPiece(Drivetrain drivetrain){
        double finalPose = -0.5*RobotMap.CHASSIS_WIDTH - 0.5*RobotMap.SUBWOOFER_LONG_WIDTH; 
        //go negative direction
        Point[] path = {
            new Point(0, 0),
            new Point(Units.inchesToMeters(133), Units.inchesToMeters(finalPose/2)), 
            // 
            new Point(Units.inchesToMeters(37), Units.inchesToMeters(finalPose)) 
            // subwoffer extends 3 ft 1/8 in, a little more to be safe

        };
        DoubleSupplier angle = () -> Units.degreesToRadians(180); //make sure shooter is forward
        //start shooter speed up
        addCommands(new driveArcLength(drivetrain, path, 5, angle));
        //run indexer to feed note to shooter
        //wait 2 seconds + flip down intake
        //back up to center note
        //run intake+indexer
        //end all
    }
}
