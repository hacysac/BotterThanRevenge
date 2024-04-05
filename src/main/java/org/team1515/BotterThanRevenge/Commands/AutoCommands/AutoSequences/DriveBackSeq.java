package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences;

import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveLine;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.TriggeredFlipDown;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.Subsystems.Flip;
import org.team1515.BotterThanRevenge.Utils.Point;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class DriveBackSeq extends SequentialCommandGroup {
    public DriveBackSeq(Drivetrain drivetrain, Flip flip){
        Point finalPose = new Point(Units.inchesToMeters(RobotMap.ROBOT_STARTING_ZONE_WIDTH + 5), 0);
        addCommands(new TriggeredFlipDown(flip));
        addCommands(new driveLine(drivetrain, 0, finalPose, 3).withTimeout(2));
    }
}