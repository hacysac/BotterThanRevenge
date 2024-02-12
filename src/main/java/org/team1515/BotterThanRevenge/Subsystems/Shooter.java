package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotMap;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {

    private CANSparkMax lShooter;
    private CANSparkMax rShooter;

    private boolean speaker;
    private boolean amp;


    private RelativeEncoder encoder;
    
    public Shooter(){
        lShooter = new CANSparkMax(RobotMap.L_SHOOTER_ID, MotorType.kBrushless);
        rShooter = new CANSparkMax(RobotMap.R_SHOOTER_ID, MotorType.kBrushless);
        rShooter.follow(lShooter, true);
        speaker = false;
        amp = false;

        encoder = lShooter.getEncoder();
    }
    public void setAmp(boolean value){
        amp = value;
    }
    public void setSpeaker(boolean value){
        speaker = value;
    }
    public boolean getAmp(){
        return amp;
    }
    public boolean getSpeaker(){
        return speaker;
    }
    public void shoot(double speed){
        lShooter.set(-speed);
    }
    
    public void in(){
        lShooter.set(RobotMap.AMP_SPEED);
    }

    public void end(){
        lShooter.set(0);
    }

    @Override
    public void periodic(){
        SmartDashboard.putNumber("Shooter RPM", encoder.getVelocity());
        SmartDashboard.putBoolean("Amp", getAmp());
        SmartDashboard.putBoolean("Speaker", getSpeaker());
    }
}
