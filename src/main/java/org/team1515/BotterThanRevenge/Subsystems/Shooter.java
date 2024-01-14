package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotMap;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {

    private CANSparkMax lShooter;
    private CANSparkMax rShooter;
    
    public Shooter(){
        lShooter = new CANSparkMax(RobotMap.L_SHOOTER_ID, MotorType.kBrushless);
        rShooter = new CANSparkMax(RobotMap.R_SHOOTER_ID, MotorType.kBrushless);
        rShooter.follow(lShooter, true);
    }

    public void shoot(){
        lShooter.set(RobotMap.SHOOTER_SPEED);
    }
    
    public void in(){
        lShooter.set(-RobotMap.SHOOTER_SPEED);
    }

    public void end(){
        lShooter.set(0);
        
    }
}
