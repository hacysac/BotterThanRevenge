package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotMap;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkBase.ControlType;

import edu.wpi.first.math.controller.BangBangController;
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
    private double bangBangSetpoint;

    public double kP,kI,kD,kIz,kFF,kMaxOutput,kMinOutput;
    
    public Shooter(){
        lShooter = new CANSparkMax(RobotMap.L_SHOOTER_ID, MotorType.kBrushless);
        rShooter = new CANSparkMax(RobotMap.R_SHOOTER_ID, MotorType.kBrushless);

        lPidController = lShooter.getPIDController();
        bangBangSetpoint = 0;
        lEncoder = lShooter.getEncoder();

        rShooter.follow(lShooter, true);

        lShooter.setSmartCurrentLimit(RobotMap.SHOOTER_CURRENT_LIMIT);
        rShooter.setSmartCurrentLimit(RobotMap.SHOOTER_CURRENT_LIMIT);
        lShooter.setIdleMode(CANSparkBase.IdleMode.kCoast);
        rShooter.setIdleMode(CANSparkBase.IdleMode.kCoast);
        lShooter.burnFlash();
        rShooter.burnFlash();
        //PID coeficients
        kP = 0.0004;//4;
        kI = 0;
        kD = 0;
        kIz = 0;
        kFF = 0.0001795;
        kMaxOutput = 1;
        kMinOutput = -1;
        //set PID coefficients
        lPidController.setP(kP);
        lPidController.setI(kI);
        lPidController.setD(kD);
        lPidController.setIZone(kIz);
        lPidController.setFF(kFF);
        lPidController.setOutputRange(kMinOutput, kMaxOutput);
        //display pid coeffs
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

    public void shootAmp(){
        amp = false;
        speaker = true;
        bangBangSetpoint = -800;
        lShooter.set(-RobotMap.AMP_SPEED);

    }

    public void shootSpeaker(){
        amp = true;
        speaker = false;
        bangBangSetpoint = -5100;
        lPidController.setReference(-5100, ControlType.kVelocity);
    }

    public void shoot(double speed){
        lShooter.set(-speed);
    }
    //
    public void in(){
        //bangBangSetpoint = 800;
        lShooter.set(RobotMap.AMP_SPEED);
    }

    public void end(){
        bangBangSetpoint = 0;
        lShooter.set(0);
        setAmp(false);
        setSpeaker(false);
    }

    @Override
    public void periodic(){

        // if(bangBangSetpoint==0){
        //     lShooter.set(0);
        // }
        // else if (lEncoder.getVelocity()<Math.abs(bangBangSetpoint)){
        //     lShooter.set(1);
        // }
        // else{
        //     lShooter.set(0);
        // }

        double p = SmartDashboard.getNumber("kP", 0);
        double i = SmartDashboard.getNumber("kI", 0);
        double d = SmartDashboard.getNumber("kD", 0);
        double iz = SmartDashboard.getNumber("kIz", 0);
        double ff = SmartDashboard.getNumber("kFF", 0);
        double max = SmartDashboard.getNumber("kMax", 0);
        double min = SmartDashboard.getNumber("kMin", 0);

        if((p != kP)) { lPidController.setP(p); kP = p; }
        if((i != kI)) { lPidController.setI(i); kI = i; }
        if((d != kD)) { lPidController.setD(d); kD = d; }
        if((iz != kIz)) { lPidController.setIZone(iz); kIz = iz; }
        if((ff != kFF)) { lPidController.setFF(ff); kFF = ff; }
        if((max != kMaxOutput) || (min != kMinOutput)) { 
            lPidController.setOutputRange(min, max); 
            kMinOutput = min; kMaxOutput = max; 
        }
        SmartDashboard.putNumber("Shooter Left RPM", lShooter.getEncoder().getVelocity());
        SmartDashboard.putNumber("Shooter Right RPM", rShooter.getEncoder().getVelocity());

        SmartDashboard.putNumber("kP", kP);
        SmartDashboard.putNumber("kI", kI);
        SmartDashboard.putNumber("kD", kD);
        SmartDashboard.putNumber("kIz", kIz);
        SmartDashboard.putNumber("kFF", kFF);
        SmartDashboard.putNumber("kMax", kMaxOutput);
        SmartDashboard.putNumber("kMin", kMinOutput);

        // SmartDashboard.putNumber("Shooter Left Current", lShooter.getOutputCurrent());
        // SmartDashboard.putNumber("Shooter Right Current", rShooter.getOutputCurrent());
        // SmartDashboard.putNumber("Shooter Left Voltage", lShooter.getBusVoltage());
        // SmartDashboard.putNumber("Shooter Right Voltage", rShooter.getBusVoltage());
        SmartDashboard.putBoolean("Amp", getAmp());
        SmartDashboard.putBoolean("Speaker", getSpeaker());

        SmartDashboard.putNumber("Left Shooter Draw", lShooter.getOutputCurrent());
        SmartDashboard.putNumber("Right Shooter Draw", rShooter.getOutputCurrent());

    }
}
