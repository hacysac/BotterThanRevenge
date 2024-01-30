package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences;

import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveArcLength;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveSegment;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.Utils.Point;
import org.team1515.BotterThanRevenge.Utils.bezierUtil;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class TwoNoteSeq extends SequentialCommandGroup{
     public TwoNoteSeq(Drivetrain drivetrain, int direction){
        
        double finalPoseY = direction*(0.5*RobotMap.CHASSIS_WIDTH + 0.5*RobotMap.SUBWOOFER_LONG_WIDTH + RobotMap.BUMPER_WIDTH); //assuming red
        Pose2d subwoofer = new Pose2d(new Translation2d(Units.inchesToMeters(RobotMap.SUBWOOFER_DEPTH + RobotMap.AUTO_OFFSET), Units.inchesToMeters(finalPoseY)), new Rotation2d(180.0));
        
        double subwooferToNoteX = Units.inchesToMeters(RobotMap.SUBWOOFER_TO_NOTE - RobotMap.CHASSIS_WIDTH - (2*RobotMap.BUMPER_WIDTH) - RobotMap.INTAKE_OFFSET);
        double subwooferToCenter = Units.inchesToMeters(RobotMap.SUBWOOFER_TO_CENTER - RobotMap.CHASSIS_WIDTH - (2*RobotMap.BUMPER_WIDTH) - RobotMap.INTAKE_OFFSET);
        
        Point[] path = {
            new Point(0, 0),
            new Point(Units.inchesToMeters(53.5), direction * Units.inchesToMeters(2.3)), 
            new Point(subwoofer.getX(), subwoofer.getY())
        };
        path = bezierUtil.spacedPoints(path, 25);
        DoubleSupplier angle = () -> Units.degreesToRadians(180); //make sure shooter is forward
        
        //start shooter speed up
        //BEZIER
        addCommands(new driveArcLength(drivetrain, path, 3.5, angle));
        //run indexer into shooter
        //wait 1 seconds + flip down intake
        
        angle = () -> Units.degreesToRadians(0);
        double time = 1;
        double speed = subwooferToNoteX/time;

        //DRIVE BACK
        Pose2d startPoint = subwoofer;
        Point finalPoint = new Point(subwooferToNoteX, 0);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        //run intake+indexer


        addCommands(Commands.waitSeconds(0.5));


        //DRIVE FORWARD
        startPoint = new Pose2d(new Translation2d(subwoofer.getX()+finalPoint.x, subwoofer.getY()+finalPoint.y), new Rotation2d(180.0));
        finalPoint = new Point(-subwooferToNoteX, 0);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        //run indexer into shooter


        addCommands(Commands.waitSeconds(0.5));

        
        angle = ()->Units.degreesToRadians(0.0);
        time = 3;
        speed = subwooferToCenter/time;

        //DRIVE TO CENTER
        startPoint = subwoofer;
        finalPoint = new Point(subwooferToCenter, 0);
        addCommands(Commands.waitSeconds(0.5));
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        //end all
    }
}
