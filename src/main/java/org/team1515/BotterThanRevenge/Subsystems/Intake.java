package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotMap;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    
    private CANSparkMax bottomIntake;
    private CANSparkMax topIntake;

    private CANSparkMax flip;
    private CANcoder canCoder;

    private boolean down;

    public Intake(){
        bottomIntake = new CANSparkMax(RobotMap.BOTTOM_INTAKE_ID, MotorType.kBrushless);
        topIntake = new CANSparkMax(RobotMap.TOP_INTAKE_ID, MotorType.kBrushless);

        flip = new CANSparkMax(RobotMap.FLIP_INTAKE_ID, MotorType.kBrushless);
        canCoder.clearStickyFault_BadMagnet();
        canCoder.getConfigurator().apply(new CANcoderConfiguration());

        down = false;
    }

    public boolean canCoderDown(){
        return canCoder.getAbsolutePosition().getValueAsDouble() >= RobotMap.FLIP_DOWN_VALUE;
    }

    public boolean canCoderUp(){
        return canCoder.getAbsolutePosition().getValueAsDouble() >= RobotMap.FLIP_UP_VALUE;
    }

    public void in(){
        bottomIntake.set(RobotMap.INTAKE_SPEED);
        topIntake.set(RobotMap.INTAKE_SPEED);
    }

    public void out(){
        bottomIntake.set(-RobotMap.INTAKE_SPEED);
        topIntake.set(-RobotMap.INTAKE_SPEED);
    }

    public void endIntake(){
        bottomIntake.set(0);
        topIntake.set(0);
    }

    public void flipUp(){
        flip.set(-RobotMap.FLIP_SPEED);
    }

    public void flipDown(){
        flip.set(RobotMap.FLIP_SPEED);
    }

    public void endFlip(){
        flip.set(0);
    }

    public boolean getDown(){
        return down;
    }

    public void setDown(boolean down){
        this.down = down;
    }
}
