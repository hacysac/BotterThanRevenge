package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotMap;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
    private CANSparkMax rClimber;
    private CANSparkMax lClimber;

    private DigitalInput lSensor;
    private DigitalInput rSensor;

    private RelativeEncoder rEncoder;
    private RelativeEncoder lEncoder;

    public Climber(){
        lClimber = new CANSparkMax(RobotMap.L_CLIMBER_ID, MotorType.kBrushless);
        rClimber = new CANSparkMax(RobotMap.R_CLIMBER_ID, MotorType.kBrushless);

        lSensor = new DigitalInput(RobotMap.L_CLIMBER_SENSOR_CHANNEL);
        rSensor = new DigitalInput(RobotMap.R_CLIMBER_SENSOR_CHANNEL);

        lEncoder = lClimber.getEncoder();
        rEncoder = rClimber.getEncoder();

    } 

    public void up(){
        if (lEncoder.getPosition() < RobotMap.CLIMBER_EXTENTION_LIMIT){
            lClimber.set(RobotMap.CLIMBER_SPEED);
        }
        else{
            lClimber.set(0);
        }
        if (rEncoder.getPosition() < RobotMap.CLIMBER_EXTENTION_LIMIT){
            rClimber.set(RobotMap.CLIMBER_SPEED);
        }
        else{
            rClimber.set(0);
        }
    }

    public void down(){
        if (!lSensor.get()){
            lClimber.set(-RobotMap.CLIMBER_SPEED);
        }
        else{
            lClimber.set(0);
            lEncoder.setPosition(0);
        }
        if(!rSensor.get()){
            rClimber.set(-RobotMap.CLIMBER_SPEED);
        }
        else{
            rClimber.set(0);
            rEncoder.setPosition(0);
        }
    }

    public void lDown(){
        if (!lSensor.get()){
            lClimber.set(-RobotMap.CLIMBER_SPEED);
        }
        else{
            lClimber.set(0);
            lEncoder.setPosition(0);
        }
    }

    public void rDown(){
        if (!rSensor.get()){
            rClimber.set(-RobotMap.CLIMBER_SPEED);
        }
        else{
            rClimber.set(0);
            rEncoder.setPosition(0);
        }
    }

    public void end() {
        lClimber.set(0);
        rClimber.set(0);
    } 

    public void zeroEncoders() {
        lEncoder.setPosition(0);
        rEncoder.setPosition(0);
    } 

    public boolean getDown() {
        return (lSensor.get() && rSensor.get());
    }

    @Override
    public void periodic(){
        SmartDashboard.putNumber("Left Climber Extenion", lEncoder.getPosition());
        SmartDashboard.putNumber("Right Climber Extenion", rEncoder.getPosition());
    }
}