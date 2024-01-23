package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences;

import java.util.ArrayList;
import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveArcLength;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.Utils.Point;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class LeftOneNoteSeq extends SequentialCommandGroup {
    public LeftOneNoteSeq(Drivetrain drivetrain){
        double finalPose = 0.5*RobotMap.CHASSIS_WIDTH + 0.5*RobotMap.SUBWOOFER_LONG_WIDTH;
        Point[] path = {
            new Point(0, 0),
            new Point(Units.inchesToMeters(133), Units.inchesToMeters(finalPose/2)), 
            //boundary line at 6 ft 4 1/8 in, go a little farther to be safe, 133 hits just above 77in
            new Point(Units.inchesToMeters(37), Units.inchesToMeters(finalPose)) 
            // subwoffer extends 3 ft 1/8 in, a little more to be safe
        };
        DoubleSupplier angle = () -> Units.degreesToRadians(180);
        //start shooter speed up
        addCommands(new driveArcLength(drivetrain, path, 5, angle));
        //run indexer to feed note to shooter
        //wait 2 seconds
        //end all
    }
    
}