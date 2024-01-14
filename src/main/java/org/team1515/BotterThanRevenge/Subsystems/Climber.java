package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotMap;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
    private CANSparkMax rClimber;
    private CANSparkMax lClimber;

    public Climber(){

        lClimber = new CANSparkMax(RobotMap.L_CLIMBER_ID, MotorType.kBrushless);
        rClimber = new CANSparkMax(RobotMap.R_CLIMBER_ID, MotorType.kBrushless);
    } 

    public void up(){

        lClimber.set(RobotMap.CLIMBER_SPEED);
        rClimber.set(RobotMap.CLIMBER_SPEED);
    }

    public void down(){

        lClimber.set(-RobotMap.CLIMBER_SPEED);
        rClimber.set(-RobotMap.CLIMBER_SPEED);
    }

    public void end() {
        lClimber.set(0);
        rClimber.set(0);
    } 
}