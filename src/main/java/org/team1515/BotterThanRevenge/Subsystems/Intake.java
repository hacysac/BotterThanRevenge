package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotMap;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    
    private CANSparkMax intake;

    public Intake(){
        intake = new CANSparkMax(RobotMap.INTAKE_ID, MotorType.kBrushless);
    }

    public void in(){
        intake.set(RobotMap.INTAKE_SPEED);

    }

    public void out(){
        intake.set(-RobotMap.INTAKE_SPEED);

    }

    public void end(){
        intake.set(0);
    }
}
