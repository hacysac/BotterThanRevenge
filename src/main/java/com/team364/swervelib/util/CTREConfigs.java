package com.team364.swervelib.util;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.configs.OpenLoopRampsConfigs;
import com.ctre.phoenix6.configs.ClosedLoopRampsConfigs;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;

public final class CTREConfigs {
    public TalonFXConfiguration swerveAngleFXConfig;
    public TalonFXConfiguration swerveDriveFXConfig;
    public CANcoderConfiguration swerveCanCoderConfig;

    public CTREConfigs(){
        swerveAngleFXConfig = new TalonFXConfiguration();
        swerveDriveFXConfig = new TalonFXConfiguration();
        swerveCanCoderConfig = new CANcoderConfiguration();

        /* Swerve Angle Motor Configurations */
        // SupplyCurrentLimitConfiguration angleSupplyLimit = new SupplyCurrentLimitConfiguration(
        //     SwerveConstants.Swerve.angleEnableCurrentLimit, 
        //     SwerveConstants.Swerve.angleContinuousCurrentLimit, 
        //     SwerveConstants.Swerve.anglePeakCurrentLimit, 
        //     SwerveConstants.Swerve.anglePeakCurrentDuration);
        
        CurrentLimitsConfigs angleSupplyLimit = new CurrentLimitsConfigs();
        angleSupplyLimit.SupplyCurrentLimit =  SwerveConstants.Swerve.angleContinuousCurrentLimit;
        angleSupplyLimit.SupplyCurrentLimitEnable = SwerveConstants.Swerve.angleEnableCurrentLimit;
        angleSupplyLimit.SupplyTimeThreshold = SwerveConstants.Swerve.anglePeakCurrentDuration;
        angleSupplyLimit.SupplyCurrentThreshold = SwerveConstants.Swerve.anglePeakCurrentLimit;

        swerveAngleFXConfig.Slot0.kP = SwerveConstants.Swerve.angleKP;
        swerveAngleFXConfig.Slot0.kI = SwerveConstants.Swerve.angleKI;
        swerveAngleFXConfig.Slot0.kD = SwerveConstants.Swerve.angleKD;
        swerveAngleFXConfig.Slot0.kV = SwerveConstants.Swerve.angleKF * 2048.0*(1.0/1023.0)*(0.1);
        swerveAngleFXConfig.withCurrentLimits(angleSupplyLimit);

        /* Swerve Drive Motor Configuration */

        // SupplyCurrentLimitConfiguration driveSupplyLimit = new SupplyCurrentLimitConfiguration(
        //     SwerveConstants.Swerve.driveEnableCurrentLimit, 
        //     SwerveConstants.Swerve.driveContinuousCurrentLimit, 
        //     SwerveConstants.Swerve.drivePeakCurrentLimit, 
        //     SwerveConstants.Swerve.drivePeakCurrentDuration);
        CurrentLimitsConfigs driveSupplyLimit = new CurrentLimitsConfigs();
        driveSupplyLimit.SupplyCurrentLimit =  SwerveConstants.Swerve.driveContinuousCurrentLimit;
        driveSupplyLimit.SupplyCurrentLimitEnable = SwerveConstants.Swerve.driveEnableCurrentLimit;
        driveSupplyLimit.SupplyTimeThreshold = SwerveConstants.Swerve.drivePeakCurrentDuration;
        driveSupplyLimit.SupplyCurrentThreshold = SwerveConstants.Swerve.drivePeakCurrentLimit;

        swerveDriveFXConfig.Slot0.kP = SwerveConstants.Swerve.driveKP;
        swerveDriveFXConfig.Slot0.kI = SwerveConstants.Swerve.driveKI;
        swerveDriveFXConfig.Slot0.kD = SwerveConstants.Swerve.driveKD;
        swerveDriveFXConfig.Slot0.kV = SwerveConstants.Swerve.driveKF * 2048.0*(1.0/1023.0)*(0.1);   

        swerveDriveFXConfig.withCurrentLimits(driveSupplyLimit);
        OpenLoopRampsConfigs oRamp = new OpenLoopRampsConfigs();
        oRamp.DutyCycleOpenLoopRampPeriod = SwerveConstants.Swerve.openLoopRamp; 
        swerveDriveFXConfig.withOpenLoopRamps(oRamp);
        ClosedLoopRampsConfigs cRamp = new ClosedLoopRampsConfigs();
        cRamp.DutyCycleClosedLoopRampPeriod = SwerveConstants.Swerve.closedLoopRamp; 
        swerveDriveFXConfig.withClosedLoopRamps(cRamp);
        
        /* Swerve CANCoder Configuration */

        MagnetSensorConfigs mag = new MagnetSensorConfigs();
        mag.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        mag.SensorDirection = SwerveConstants.Swerve.canCoderInvert ? SensorDirectionValue.Clockwise_Positive : SensorDirectionValue.CounterClockwise_Positive; //TODO: check this

        swerveCanCoderConfig.withMagnetSensor(mag);

        //swerveCanCoderConfig.sensorTimeBase = SensorTimeBase.PerSecond;
    }
}