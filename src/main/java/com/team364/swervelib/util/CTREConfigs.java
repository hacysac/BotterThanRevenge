package com.team364.swervelib.util;

import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.ctre.phoenix.sensors.SensorTimeBase;

public final class CTREConfigs {
    public TalonFXConfiguration swerveAngleFXConfig;
    public TalonFXConfiguration swerveDriveFXConfig;
    public CANcoderConfiguration swerveCanCoderConfig;

    public CTREConfigs(){
        swerveAngleFXConfig = new TalonFXConfiguration();
        swerveDriveFXConfig = new TalonFXConfiguration();
        swerveCanCoderConfig = new CANcoderConfiguration();

        /* Swerve Angle Motor Configurations */
        SupplyCurrentLimitConfiguration angleSupplyLimit = new SupplyCurrentLimitConfiguration(
            SwerveConstants.Swerve.angleEnableCurrentLimit, 
            SwerveConstants.Swerve.angleContinuousCurrentLimit, 
            SwerveConstants.Swerve.anglePeakCurrentLimit, 
            SwerveConstants.Swerve.anglePeakCurrentDuration);

        swerveAngleFXConfig.Slot0.kP = SwerveConstants.Swerve.angleKP;
        swerveAngleFXConfig.Slot0.kI = SwerveConstants.Swerve.angleKI;
        swerveAngleFXConfig.Slot0.kD = SwerveConstants.Swerve.angleKD;
        swerveAngleFXConfig.Slot0.kF = SwerveConstants.Swerve.angleKF;
        swerveAngleFXConfig.supplyCurrLimit = angleSupplyLimit;

        /* Swerve Drive Motor Configuration */
        SupplyCurrentLimitConfiguration driveSupplyLimit = new SupplyCurrentLimitConfiguration(
            SwerveConstants.Swerve.driveEnableCurrentLimit, 
            SwerveConstants.Swerve.driveContinuousCurrentLimit, 
            SwerveConstants.Swerve.drivePeakCurrentLimit, 
            SwerveConstants.Swerve.drivePeakCurrentDuration);

        swerveDriveFXConfig.Slot0.kP = SwerveConstants.Swerve.driveKP;
        swerveDriveFXConfig.Slot0.kI = SwerveConstants.Swerve.driveKI;
        swerveDriveFXConfig.Slot0.kD = SwerveConstants.Swerve.driveKD;
        swerveDriveFXConfig.Slot0.kF = SwerveConstants.Swerve.driveKF;        
        swerveDriveFXConfig.supplyCurrLimit = driveSupplyLimit;
        swerveDriveFXConfig.openloopRamp = SwerveConstants.Swerve.openLoopRamp;
        swerveDriveFXConfig.closedloopRamp = SwerveConstants.Swerve.closedLoopRamp;
        
        /* Swerve CANCoder Configuration */
        swerveCanCoderConfig.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
        swerveCanCoderConfig.sensorDirection = SwerveConstants.Swerve.canCoderInvert;
        swerveCanCoderConfig.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition;
        swerveCanCoderConfig.sensorTimeBase = SensorTimeBase.PerSecond;
    }
}