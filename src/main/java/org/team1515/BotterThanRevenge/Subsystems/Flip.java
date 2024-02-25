package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotContainer;
import org.team1515.BotterThanRevenge.RobotMap;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.team364.swervelib.util.SwerveConstants;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Flip extends SubsystemBase{
    private CANSparkMax flip;
    private CANcoder canCoder;

    // private DigitalInput upperSensor;
    // private DigitalInput lowerSensor;

    private boolean down;

    private double constantSpeed = -0.05; // flip motor is inverted, negative goes up
    private double offset = 0.0; //this is how u apply speed

    public Flip(){

        flip = new CANSparkMax(RobotMap.FLIP_INTAKE_ID, MotorType.kBrushless);
        canCoder = new CANcoder(RobotMap.FLIP_CANCODER_ID);
        canCoder.clearStickyFault_BadMagnet();
        canCoder.getConfigurator().apply(new CANcoderConfiguration());

        // upperSensor = new DigitalInput(RobotMap.FLIP_UPPER_SENSOR_CHANNEL);
        // lowerSensor = new DigitalInput(RobotMap.FLIP_LOWER_SENSOR_CHANNEL);

        //down = false;

    }

    // public boolean canCoderDown(){
    //     return canCoder.getAbsolutePosition().getValueAsDouble() >= RobotMap.FLIP_DOWN_VALUE;// || lowerSensor.get();
    // }

    // public boolean canCoderUp(){
    //     return canCoder.getAbsolutePosition().getValueAsDouble() >= RobotMap.FLIP_UP_VALUE;// || upperSensor.get();
    // }

    public boolean getDown(){
        //return lowerSensor.get();
        //return getCANCoderValue() < RobotMap.FLIP_DOWN_VALUE;
        return false;
    }

    public boolean getUp(){
        //return upperSensor.get();
        //return getCANCoderValue() > RobotMap.FLIP_TOP_VALUE;
        return false;
    }

    public void flipUp(){
        offset = -RobotMap.FLIP_UP_SPEED;
    }

    public void flipDown(){
        offset = RobotMap.FLIP_DOWN_SPEED;
    }

    public double getCANCoderValue(){
        // if (canCoder.getAbsolutePosition().getValueAsDouble() < 0){
            return 0.5+canCoder.getAbsolutePosition().getValueAsDouble();
        // }
        // else{
        //     return canCoder.getAbsolutePosition().getValueAsDouble();
        // }
    }

    public void end(){
        offset = 0;
    }

    // public boolean getDown(){
    //     return down;
    // }

    // public void setDown(boolean down){
    //     this.down = down;
    // }

    @Override
    public void periodic(){

        if (getCANCoderValue()<RobotMap.FLIP_MID_VALUE && offset==0){
            flip.set(constantSpeed);
        }
        else{
            flip.set(offset);
        }

        //SmartDashboard.putBoolean("Intake Down?", getDown());
        SmartDashboard.putNumber("Calulated Intake Angle", getCANCoderValue());
        SmartDashboard.putNumber("Absolute Intake Angle", canCoder.getAbsolutePosition().getValueAsDouble());
    }
}
