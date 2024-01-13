package com.team364.swervelib.util;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;

import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.team364.swervelib.math.Conversions;

import org.team1515.BotterThanRevenge.Robot;

public class SwerveModule {
    
    public int moduleNumber;
    private Rotation2d angleOffset;
    private Rotation2d lastAngle;

    private TalonFX mAngleMotor;
    private TalonFX mDriveMotor;
    private CANcoder angleEncoder;

    SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(SwerveConstants.Swerve.driveKS,
            SwerveConstants.Swerve.driveKV, SwerveConstants.Swerve.driveKA);

    public SwerveModule(int moduleNumber, SwerveModuleConstants moduleConstants) {
        this.moduleNumber = moduleNumber;
        this.angleOffset = moduleConstants.angleOffset;

        /* Angle Encoder Config */
        angleEncoder = new CANcoder(moduleConstants.cancoderID, "rio");
        configAngleEncoder();

        /* Angle Motor Config */
        mAngleMotor = new TalonFX(moduleConstants.angleMotorID, "rio");
        configAngleMotor();

        /* Drive Motor Config */
        mDriveMotor = new TalonFX(moduleConstants.driveMotorID, "rio");
        configDriveMotor();

        lastAngle = getState().angle;
    }

    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {
        /*
         * This is a custom optimize function, since default WPILib optimize assumes
         * continuous controller which CTRE and Rev onboard is not
         */
        desiredState = CTREModuleState.optimize(desiredState, getState().angle);
        setAngle(desiredState);
        setSpeed(desiredState, isOpenLoop);
    }

    private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop) {
        if (isOpenLoop) {
            double percentOutput = desiredState.speedMetersPerSecond / SwerveConstants.Swerve.maxSpeed;
            mDriveMotor.setControl(new DutyCycleOut(percentOutput));
        } else {
            double velocity = Conversions.falconToRPM(Conversions.MPSToFalcon(desiredState.speedMetersPerSecond,
                    SwerveConstants.Swerve.wheelCircumference, SwerveConstants.Swerve.driveGearRatio), SwerveConstants.Swerve.driveGearRatio)/60.0;
            mDriveMotor.setControl(new VelocityDutyCycle(velocity).withFeedForward(feedforward.calculate(desiredState.speedMetersPerSecond)));
        }
    }

    private void setAngle(SwerveModuleState desiredState) {
        Rotation2d angle = (Math.abs(desiredState.speedMetersPerSecond) <= (SwerveConstants.Swerve.maxSpeed * 0.01))
                ? lastAngle
                : desiredState.angle; // Prevent rotating module if speed is less then 1%. Prevents Jittering.

        mAngleMotor.setControl(new PositionDutyCycle(Conversions.degreesToRotations(angle.getDegrees(), SwerveConstants.Swerve.angleGearRatio)));
        lastAngle = angle;
    }

    private Rotation2d getAngle() {
        return Rotation2d.fromRotations(mAngleMotor.getPosition().getValueAsDouble());
    }

    public Rotation2d getCanCoder() {
        return Rotation2d.fromRotations(angleEncoder.getAbsolutePosition().getValueAsDouble());
    }

    public void resetToAbsolute() {
        double absolutePosition = Units.degreesToRotations(getCanCoder().getDegrees() - angleOffset.getDegrees());
        mAngleMotor.setPosition(absolutePosition);
    }

    private void configAngleEncoder() {
        angleEncoder.getConfigurator().apply(new CANcoderConfiguration());
        angleEncoder.getConfigurator().apply(Robot.config.swerveCanCoderConfig);
        //angleEncoder.configAllSettings(Robot.config.swerveCanCoderConfig);
    }

    private void configAngleMotor() {
        mAngleMotor.getConfigurator().apply(new TalonFXConfiguration());
        mAngleMotor.getConfigurator().apply(Robot.config.swerveAngleFXConfig);;
        //mAngleMotor.configAllSettings(Robot.config.swerveAngleFXConfig);
        mAngleMotor.setInverted(SwerveConstants.Swerve.angleMotorInvert);
        mAngleMotor.setNeutralMode(SwerveConstants.Swerve.angleNeutralMode);
        resetToAbsolute();
    }

    private void configDriveMotor() {
        mDriveMotor.getConfigurator().apply(new TalonFXConfiguration());
        mDriveMotor.getConfigurator().apply(Robot.config.swerveDriveFXConfig);
        //mDriveMotor.configAllSettings(Robot.config.swerveDriveFXConfig);
        mDriveMotor.setInverted(SwerveConstants.Swerve.driveMotorInvert);
        mDriveMotor.setNeutralMode(SwerveConstants.Swerve.driveNeutralMode);
        mDriveMotor.setPosition(0);
    }

    public SwerveModuleState getState() {
        return new SwerveModuleState(
                Conversions.falconToMPS(Conversions.RPMToFalcon(mDriveMotor.getVelocity().getValueAsDouble(), SwerveConstants.Swerve.driveGearRatio) ,
                        SwerveConstants.Swerve.wheelCircumference, SwerveConstants.Swerve.driveGearRatio),
                getAngle());
    }

    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(
                Conversions.falconToMeters(Conversions.RPMToFalcon(mDriveMotor.getVelocity().getValueAsDouble(), SwerveConstants.Swerve.driveGearRatio),
                        SwerveConstants.Swerve.wheelCircumference, SwerveConstants.Swerve.driveGearRatio),
                getAngle());
    }
}