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

    private PIDController angleController;
    private double maxRotate;
    private double ff = -0.05; // flip motor is inverted, negative goes up
    private double offset = 0.0; //this is how u apply additional speed to added to the ff

    public Flip(){

        flip = new CANSparkMax(RobotMap.FLIP_INTAKE_ID, MotorType.kBrushless);
        // canCoder = new CANcoder(RobotMap.FLIP_CANCODER_ID);
        // canCoder.clearStickyFault_BadMagnet();
        // canCoder.getConfigurator().apply(new CANcoderConfiguration());

        // upperSensor = new DigitalInput(RobotMap.FLIP_UPPER_SENSOR_CHANNEL);
        // lowerSensor = new DigitalInput(RobotMap.FLIP_LOWER_SENSOR_CHANNEL);

        //down = false;

        // this.maxRotate = 0.5;
        // angleController = new PIDController(2, 1.5, 0);
        // // TODO retune PID
        // angleController.setTolerance(Units.degreesToRadians(1));
        // angleController.enableContinuousInput(-Math.PI, Math.PI); // TODO
        // angleController.setSetpoint(Units.rotationsToRadians(canCoder.getAbsolutePosition().getValueAsDouble()));

    }

    // public boolean canCoderDown(){
    //     return canCoder.getAbsolutePosition().getValueAsDouble() >= RobotMap.FLIP_DOWN_VALUE;// || lowerSensor.get();
    // }

    // public boolean canCoderUp(){
    //     return canCoder.getAbsolutePosition().getValueAsDouble() >= RobotMap.FLIP_UP_VALUE;// || upperSensor.get();
    // }

    public void flipUp(){
        offset = -RobotMap.FLIP_UP_SPEED;
    }

    public void flipDown(){
        offset = RobotMap.FLIP_DOWN_SPEED;
    }

    // public void setFlipDown(){
    //     angleController.setSetpoint(Units.rotationsToRadians(RobotMap.FLIP_DOWN_VALUE));
    // }

    // public void setFlipUp(){
    //     angleController.setSetpoint(Units.rotationsToRadians(RobotMap.FLIP_UP_VALUE));
    // }

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
        // double currentAngle = canCoder.getAbsolutePosition().getValueAsDouble();
        // double error = MathUtil.angleModulus(currentAngle - angleController.getSetpoint());
        // double rotation = (MathUtil.clamp(angleController.calculate(error + angleController.getSetpoint(), angleController.getSetpoint()) + (ff * Math.signum(-error)),
        //             -maxRotate, maxRotate)); // change setpoint?

        //if (upperSensor.get() || lowerSensor.get()){
        //flip.set(offset+0.0);
        //}
        // else{
        flip.set(offset+ff);
        // }

        //SmartDashboard.putBoolean("Intake Down?", getDown());
        //SmartDashboard.putNumber("Intake Angle", canCoder.getAbsolutePosition().getValueAsDouble());
    }
}
