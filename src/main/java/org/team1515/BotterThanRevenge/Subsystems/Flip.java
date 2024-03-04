package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotContainer;
import org.team1515.BotterThanRevenge.RobotMap;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import com.team364.swervelib.util.SwerveConstants;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.SparkLimitSwitch.Type;

public class Flip extends SubsystemBase{
    private CANSparkMax flip;
    private CANcoder canCoder;

    private SparkLimitSwitch upperSensor;
    //private SparkLimitSwitch lowerSensor;

    private boolean up;

    private double topValue;
    private double midValue;
    private double lowValue;

    private double midOffset;
    private double lowOffset;

    private double constantSpeed = -0.025; // flip motor is inverted, negative goes up
    private double offset = 0.0; //this is how u apply speed

    public Flip(){

        flip = new CANSparkMax(RobotMap.FLIP_INTAKE_ID, MotorType.kBrushless);
        canCoder = new CANcoder(RobotMap.FLIP_CANCODER_ID);
        canCoder.clearStickyFault_BadMagnet();
        canCoder.getConfigurator().apply(new CANcoderConfiguration());

        topValue = 0.76;
        midValue = -1000;
        lowValue = -1000;

        lowOffset = RobotMap.FLIP_DOWN_OFFSET;
        midOffset = RobotMap.FLIP_MID_OFFSET;

        upperSensor = flip.getReverseLimitSwitch(Type.kNormallyOpen);
        // lowerSensor = flip.getForwardLimitSwitch(Type.kNormallyOpen);

        up = false;

    }

    public boolean belowMid(){
        return getCANCoderValue() < midValue;
    }

    public boolean canCoderDown(){
        return getCANCoderValue() < lowValue;
    }

    public boolean canCoderUp(){
        return getCANCoderValue() > topValue;
    }

    public boolean getDown(){
        //return lowerSensor.isPressed();
        return canCoderDown();
        //return false;
    }

    public boolean getUp(){
        return upperSensor.isPressed();
        //return canCoderUp();
        //return false;
    }

    public void flipUp(){
        if (belowMid()){
            offset = -RobotMap.FLIP_UP_SPEED;
        }
        else{
            offset = -RobotMap.FLIP_UP_SPEED/2;
        }
    }

    public void flipDown(){
        offset = RobotMap.FLIP_DOWN_SPEED;
    }

    public double getCANCoderValue(){
        //looping
        //return 0.5+canCoder.getAbsolutePosition().getValueAsDouble();

        //no looping
        if (canCoder.getAbsolutePosition().getValueAsDouble() < 0){
            return 1+canCoder.getAbsolutePosition().getValueAsDouble();
        }
        else{
            return canCoder.getAbsolutePosition().getValueAsDouble();
        }
    }

    public void end(){
        offset = 0;
    }

    public void setCurrentLowValue(){
        lowValue = getCANCoderValue();
    }

    public void setCurrentTopValue(){
        topValue = getCANCoderValue();
    }

    public void setUp(boolean up){
        this.up = up;
    }

    public boolean getUpState(){
        return up;
    }

    @Override
    public void periodic(){

        //topValue = SmartDashboard.getNumber("Intake TopValue", 0.76);
        midValue = topValue - midOffset;
        lowValue = topValue - lowOffset;

        //midOffset = SmartDashboard.getNumber("Intake MidOffset", 0.24);
        //lowOffset = SmartDashboard.getNumber("Intake LowOffset", 0.51);

        if (belowMid() && offset==0){
            flip.set(constantSpeed);
        }
        else{
            flip.set(offset);
        }

        SmartDashboard.putNumber("Calulated Intake Angle", getCANCoderValue());
        //SmartDashboard.putNumber("Absolute Intake Angle", canCoder.getAbsolutePosition().getValueAsDouble());

        SmartDashboard.putNumber("Intake TopValue", topValue);
        SmartDashboard.putNumber("Intake MidValue", midValue);
        SmartDashboard.putNumber("Intake LowValue", lowValue);

        // SmartDashboard.putNumber("CurrentOffset", topValue - getCANCoderValue());
        // SmartDashboard.putNumber("LowOffset", lowOffset);
        // SmartDashboard.putNumber("MidOffset", midOffset);

        //SmartDashboard.putBoolean("Intake Down?", getDown());
        SmartDashboard.putBoolean("Intake Up?", up);
    }
}