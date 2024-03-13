package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotMap;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    
    private CANSparkMax bottomIntake;
    private CANSparkMax topIntake;


    public Intake(){
        bottomIntake = new CANSparkMax(RobotMap.BOTTOM_INTAKE_ID, MotorType.kBrushless);
        topIntake = new CANSparkMax(RobotMap.TOP_INTAKE_ID, MotorType.kBrushless);
        topIntake.setSmartCurrentLimit(RobotMap.INTAKE_CURRENT_LIMIT);
        bottomIntake.setSmartCurrentLimit(RobotMap.INTAKE_CURRENT_LIMIT);
        topIntake.burnFlash();
        bottomIntake.burnFlash();

    }

    public void in(){
        bottomIntake.set(RobotMap.LOWER_INTAKE_SPEED);
        topIntake.set(-RobotMap.UPPER_INTAKE_SPEED);
    }

    public void out(){
        bottomIntake.set(-RobotMap.LOWER_INTAKE_SPEED);
        topIntake.set(RobotMap.UPPER_INTAKE_SPEED);
    }

    public void end(){
        bottomIntake.set(0);
        topIntake.set(0);
    }

    @Override
    public void periodic(){
        // SmartDashboard.putNumber("Top Intake RPM", topIntake.getEncoder().getVelocity());
        // SmartDashboard.putNumber("Bottom Intake RPM", bottomIntake.getEncoder().getVelocity());
        SmartDashboard.putNumber("Top Intake Draw", topIntake.getOutputCurrent());
        SmartDashboard.putNumber("Bottom Intake Draw", bottomIntake.getOutputCurrent());
        // SmartDashboard.putNumber("Top Intake Voltage", topIntake.getBusVoltage());
        // SmartDashboard.putNumber("Bottom Intake Voltage", bottomIntake.getBusVoltage());
        // SmartDashboard.putBoolean("Intake Down?", getDown());
        // SmartDashboard.putNumber("Intake Angle", canCoder.getAbsolutePosition().getValueAsDouble());
        // double currentUp = topIntake.getOutputCurrent();
        // double currentDown = bottomIntake.getOutputCurrent();
        // if (currentDown>downMax){
        //     downMax = currentDown;
        // }
        // if (currentUp>upMax){
        //     upMax = currentUp;
        // }
    }
}
