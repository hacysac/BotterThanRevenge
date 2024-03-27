package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotMap;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {

    private CANSparkMax lShooter;
    private CANSparkMax rShooter;

    private boolean speaker;
    private boolean amp;

    double leftMax;
    double rightMax;
    private RelativeEncoder lEncoder;
    private SparkPIDController lPidController;

    public double kP,kI,kD,kIz,kFF,kMaxOutput,kMinOutput,maxRPM,maxVel,minVel,maxAcc,allowedErr;
    
    public Shooter(){
        lShooter = new CANSparkMax(RobotMap.L_SHOOTER_ID, MotorType.kBrushless);
        rShooter = new CANSparkMax(RobotMap.R_SHOOTER_ID, MotorType.kBrushless);

        lPidController = lShooter.getPIDController();
        lEncoder = lShooter.getEncoder();

        rShooter.follow(lShooter, true);

        lShooter.setSmartCurrentLimit(RobotMap.SHOOTER_CURRENT_LIMIT);
        rShooter.setSmartCurrentLimit(RobotMap.SHOOTER_CURRENT_LIMIT);
        lShooter.burnFlash();
        rShooter.burnFlash();
        //PID coeficients
        kP = 5e-5;
        kI = 1e-6;
        kD = 0;
        kIz = 0;
        kFF = 0.000156;
        kMaxOutput = 1;
        kMinOutput = -1;
        maxRPM = 5700;
        //SmartMotion coeficients
        maxVel = 5500;
        maxAcc = 5000;
        //set PID coefficients
        lPidController.setP(kP);
        lPidController.setI(kI);
        lPidController.setD(kD);
        lPidController.setIZone(kIz);
        lPidController.setFF(kFF);
        lPidController.setOutputRange(kMinOutput, kMaxOutput);
        //set smartmotion Constants
        int smartMotionSlot = 0;
        lPidController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
        lPidController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
        lPidController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
        lPidController.setSmartMotionMaxAccel(allowedErr, smartMotionSlot);

        SmartDashboard.putNumber("kP", kP);
        SmartDashboard.putNumber("kI", kI);
        SmartDashboard.putNumber("kD", kD);
        SmartDashboard.putNumber("kIz", kIz);
        SmartDashboard.putNumber("kFF", kFF);
        SmartDashboard.putNumber("kMax", kMaxOutput);
        SmartDashboard.putNumber("kMin", kMinOutput);

        speaker = false;
        amp = false;

        leftMax = 0.0;
        rightMax = 0.0;
    }

    public void setAmp(boolean value){
        amp = value;
    }

    public void setSpeaker(boolean value){
        speaker = value;
    }

    public boolean getAmp(){
        return amp;
    }

    public boolean getSpeaker(){
        return speaker;
    }
    
    public double getLeft(){
        return lShooter.getEncoder().getVelocity();
    }
    public double getRight(){
        return rShooter.getEncoder().getVelocity();
    }

    public void shootAmp(){
        amp = true;
        speaker = false;
        lShooter.set(-RobotMap.AMP_SPEED);
    }

    public void shootSpeaker(){
        speaker = true;
        amp = false;
        lShooter.set(-RobotMap.SPEAKER_SPEED);
    }

    public void shoot(double speed){
        lShooter.set(-speed);
    }
    //
    public void in(){
        lShooter.set(RobotMap.AMP_SPEED);
    }

    public void end(){
        lShooter.set(0);
        setAmp(false);
        setSpeaker(false);
    }
    public void resetDash(){
        leftMax = 0.0;
        rightMax = 0.0;
    }
    @Override
    public void periodic(){
        // SmartDashboard.putNumber("Shooter Left RPM", getLeft());
        // SmartDashboard.putNumber("Shooter Right RPM", getRight());
        // SmartDashboard.putNumber("Shooter Left Current", lShooter.getOutputCurrent());
        // SmartDashboard.putNumber("Shooter Right Current", rShooter.getOutputCurrent());
        // SmartDashboard.putNumber("Shooter Left Voltage", lShooter.getBusVoltage());
        // SmartDashboard.putNumber("Shooter Right Voltage", rShooter.getBusVoltage());
        SmartDashboard.putBoolean("Amp", getAmp());
        SmartDashboard.putBoolean("Speaker", getSpeaker());

        // SmartDashboard.putNumber("Left Shooter Draw", lShooter.getOutputCurrent());
        // SmartDashboard.putNumber("Right Shooter Draw", rShooter.getOutputCurrent());

    }
}
