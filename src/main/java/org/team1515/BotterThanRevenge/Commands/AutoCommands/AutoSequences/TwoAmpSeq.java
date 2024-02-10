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

public class TwoAmpSeq extends SequentialCommandGroup{
     public TwoAmpSeq(Drivetrain drivetrain, int direction){

        double finalPoseY = direction*(RobotMap.SUBWOOFER_TO_AMP - (RobotMap.CHASSIS_WIDTH + 2*RobotMap.BUMPER_WIDTH)); //assuming red
        Pose2d amp = new Pose2d(new Translation2d(Units.inchesToMeters(RobotMap.WALL_TO_AMP + RobotMap.AUTO_OFFSET - (0.5*RobotMap.CHASSIS_WIDTH + RobotMap.BUMPER_WIDTH)), Units.inchesToMeters(finalPoseY)), new Rotation2d(180.0));
        
        double noteToAmpX = -Units.inchesToMeters(RobotMap.NOTE_TO_AMP_X);
        double noteToAmpY = direction * Units.inchesToMeters(RobotMap.NOTE_TO_AMP_Y - RobotMap.CHASSIS_WIDTH - 2*RobotMap.BUMPER_WIDTH); // TODO
        double ampToCenter = Units.inchesToMeters(RobotMap.AMP_TO_CENTER - RobotMap.CHASSIS_WIDTH - (2*RobotMap.BUMPER_WIDTH) - RobotMap.INTAKE_OFFSET); //TODO find
        
        Point[] path = {
            new Point(0, 0),
            new Point(Units.inchesToMeters(53.5), direction * Units.inchesToMeters(2.3)), 
            new Point(amp.getX(), amp.getY())
        };
        path = bezierUtil.spacedPoints(path, 25);
        DoubleSupplier angle = () -> Units.degreesToRadians(direction*-90.0); //make sure intake is forward
        
        //start shooter speed up
        //BEZIER
        addCommands(new driveArcLength(drivetrain, path, 3.5, angle));
        //run indexer into shooter
        //wait 1 seconds + flip down intake

        addCommands(Commands.waitSeconds(0.5));
        
        angle = () -> Units.degreesToRadians(-30.0*direction);
        double time = 1;
        double dist = Math.sqrt(Math.pow(noteToAmpX, 2)+Math.pow(noteToAmpY, 2));
        double speed = dist/time;

        //DRIVE BACK
        Pose2d startPoint = amp;
        Point finalPoint = new Point(-noteToAmpX, -noteToAmpY);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        //run intake+indexer


        addCommands(Commands.waitSeconds(0.5));


        //DRIVE FORWARD
        startPoint = new Pose2d(new Translation2d(amp.getX()+finalPoint.x, amp.getY()+finalPoint.y), new Rotation2d(180.0));
        finalPoint = new Point(noteToAmpX, noteToAmpY);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        //run indexer into shooter


        addCommands(Commands.waitSeconds(0.5));

        angle = ()->Units.degreesToRadians(direction*90);
        time = 3;
        speed = ampToCenter/time;

        //DRIVE TO CENTER
        startPoint = amp;
        finalPoint = new Point(ampToCenter, 0);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        //end all
    }
}
