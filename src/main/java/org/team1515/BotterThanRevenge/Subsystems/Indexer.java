package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotMap;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Indexer extends SubsystemBase {
    
    private CANSparkMax topIndexer;
    private RelativeEncoder encoder;

    private DigitalInput sensor;

    public Indexer(){
        topIndexer = new CANSparkMax(RobotMap.INDEXER_ID, MotorType.kBrushless);

        encoder = topIndexer.getEncoder();

        //sensor = new DigitalInput(RobotMap.INDEX_SENSOR_CHANNEL);
    }

    public boolean isBlocked(){
        //return !sensor.get(); // TODO inverted, false if is blocked?
        return false;
    }

    public void up(){
        topIndexer.set(-RobotMap.INDEXER_SPEED);
    }

    public void down(){
        topIndexer.set(RobotMap.INDEXER_SPEED);
    }

    public void end() {
        topIndexer.set(0);
    }
    @Override
    public void periodic(){
        SmartDashboard.putNumber("Indexer RPM", encoder.getVelocity());
    }
}