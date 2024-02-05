package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotMap;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Indexer extends SubsystemBase {
    
    private CANSparkMax topIndexer;

    private DigitalOutput sensor;

    public Indexer(){
        topIndexer = new CANSparkMax(RobotMap.INDEXER_ID, MotorType.kBrushless);

        //sensor = new DigitalOutput(RobotMap.INDEX_SENSOR_CHANNEL);
    }

    public boolean isBlocked(){
        return !sensor.get(); // TODO inverted, false if is blocked?
    }

    public void up(){
        topIndexer.set(RobotMap.INDEXER_SPEED);
    }

    public void down(){
        topIndexer.set(-RobotMap.INDEXER_SPEED);
    }

    public void end() {
        topIndexer.set(0);
    }
}