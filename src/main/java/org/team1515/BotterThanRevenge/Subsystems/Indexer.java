package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotMap;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Indexer extends SubsystemBase {
    
    private CANSparkMax indexer;

    public Indexer(){
        indexer = new CANSparkMax(RobotMap.INDEXER_ID, MotorType.kBrushless);
    }

    public void up(){
        indexer.set(RobotMap.INDEXER_SPEED);
    }

    public void down(){
        indexer.set(-RobotMap.INDEXER_SPEED);
    }

    public void end() {
        indexer.set(0);
    }
}