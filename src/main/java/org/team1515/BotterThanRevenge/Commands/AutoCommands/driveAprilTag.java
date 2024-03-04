package org.team1515.BotterThanRevenge.Commands.AutoCommands;

import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.Utils.PhotonVision;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;

public class driveAprilTag extends Command{

    private Drivetrain drivetrain;
    private PhotonVision photon;
    private final double tolerance = 0.10; //ion meters
    private final double speed; //ion meters

    public driveAprilTag(Drivetrain drivetrain, PhotonVision photon, double time){
        this.drivetrain = drivetrain;
        this.photon = photon;
        Translation2d translation = photon.getTrans();
        double x = translation.getX();
        double y = translation.getY();
        double length = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
        this.speed = length/time;
    }
    @Override
    public void execute(){
        Translation2d trans = photon.getTrans();
        double rotation = photon.getAngle();
        double x = trans.getX();
        double y = trans.getY();
        double length = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));   
        double i = x/length;
        double j = y/length;
        Translation2d vector = new Translation2d(i*speed,j*speed);
        drivetrain.drive(vector, rotation, true, false);
    }
    @Override
    public boolean isFinished(){
        Translation2d transToTag = photon.getTrans();
        double x = transToTag.getX();
        double y = transToTag.getY();
        double length = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
        return length <= tolerance;
    }
    @Override
    public void end(boolean interrupted){
        //drivetrain.setOdometry();
        drivetrain.drive(new Translation2d(0,0), 0, true, false);
    }
}
