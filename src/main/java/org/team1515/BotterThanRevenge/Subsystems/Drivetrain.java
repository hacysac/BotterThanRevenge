package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotContainer;
import org.team1515.BotterThanRevenge.Utils.LimelightHelpers;

import com.team364.swervelib.util.SwerveConstants;
import com.team364.swervelib.util.SwerveModule;

import edu.wpi.first.math.VecBuilder;
//import edu.wpi.first.math.MathUtil;
//import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
    public SwerveModule[] mSwerveMods;

    private Rotation2d realZero;
    private double gyroOffset = 0;

    private Pose2d m_pose;
    private SwerveDrivePoseEstimator estimator;
    private Pose2d initialTagOffset;
    //private PhotonVision photonVision;


    public Drivetrain(Pose2d initialPos) {
        realZero = Rotation2d.fromDegrees(RobotContainer.gyro.getYaw());
        initialTagOffset = new Pose2d();

        //photonVision = photon;
        
        zeroGyro();

        mSwerveMods = new SwerveModule[] {
                new SwerveModule(0, SwerveConstants.Swerve.Mod0.constants),
                new SwerveModule(1, SwerveConstants.Swerve.Mod1.constants),
                new SwerveModule(2, SwerveConstants.Swerve.Mod2.constants),
                new SwerveModule(3, SwerveConstants.Swerve.Mod3.constants)
        };

        estimator = new SwerveDrivePoseEstimator(
            SwerveConstants.Swerve.swerveKinematics, RobotContainer.gyro.getGyroscopeRotation(),
            new SwerveModulePosition[] {
            mSwerveMods[0].getPosition(),
            mSwerveMods[1].getPosition(),
            mSwerveMods[2].getPosition(),
            mSwerveMods[3].getPosition()
        }, initialPos, 
          //tune
          VecBuilder.fill(0.05, 0.05, Units.degreesToRadians(5)),
          VecBuilder.fill(0.5, 0.5, Units.degreesToRadians(30)));
        


        /*
         * By pausing init for a second before setting module offsets, we avoid a bug
         * with inverting motors.
         * See https://github.com/Team364/BaseFalconSwerve/issues/8 for more info.
         */
        Timer.delay(1.0);
        resetModulesToAbsolute();
    }

    /**
     * 
     * @param translation   speed translation in m/s
     * @param rotation      in r/s
     * @param fieldRelative if robot is robot or field reletive
     * @param isOpenLoop    does not use velocity pid?
     */

    public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
        SwerveModuleState[] swerveModuleStates = SwerveConstants.Swerve.swerveKinematics.toSwerveModuleStates(
                fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
                        translation.getX(),
                        translation.getY(),
                        rotation,
                        getYaw())
                        : new ChassisSpeeds(
                                translation.getX(),
                                translation.getY(),
                                rotation));
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, SwerveConstants.Swerve.maxSpeed);

        for (SwerveModule mod : mSwerveMods) {
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop);
        }
    }

    /* Used by SwerveControllerCommand in Auto */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, SwerveConstants.Swerve.maxSpeed);

        for (SwerveModule mod : mSwerveMods) {
            mod.setDesiredState(desiredStates[mod.moduleNumber], false);
        }
    }

    public SwerveModuleState[] getModuleStates() {
        SwerveModuleState[] states = new SwerveModuleState[4];
        for (SwerveModule mod : mSwerveMods) {
            states[mod.moduleNumber] = mod.getState();
        }
        return states;
    }

    public SwerveModulePosition[] getModulePositions() {
        SwerveModulePosition[] positions = new SwerveModulePosition[4];
        for (SwerveModule mod : mSwerveMods) {
            positions[mod.moduleNumber] = mod.getPosition();
        }
        return positions;
    }

    /**
     * resets robot yaw to zero
     */
    public void zeroGyro() {
        realZero = realZero.minus(RobotContainer.gyro.getGyroscopeRotation());
        RobotContainer.gyro.zeroYaw();
    }

    /**
     * @return Rotation2d yaw of the robot in radians
     */
    public Rotation2d getYaw() {
        return (SwerveConstants.Swerve.invertGyro) ? Rotation2d.fromDegrees(360 - RobotContainer.gyro.getYaw() + gyroOffset)
                : Rotation2d.fromDegrees(RobotContainer.gyro.getYaw());
    }

    public void flipGyro() {
        if(gyroOffset == 0) {
            gyroOffset = 180;
        }
        else {
            gyroOffset = 0;
        }
    }

    public void resetModulesToAbsolute() {
        for (SwerveModule mod : mSwerveMods) {
            mod.resetToAbsolute();
        }
    }

    @Override
    public void periodic() {
        for (SwerveModule mod : mSwerveMods) {
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Cancoder", mod.getCANcoder().getDegrees());
            //SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Integrated", mod.getPosition().angle.getDegrees());
            //SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Velocity", mod.getState().speedMetersPerSecond);
            
        }
        m_pose = estimator.update(
            RobotContainer.gyro.getGyroscopeRotation(),
            new SwerveModulePosition[] {
                mSwerveMods[0].getPosition(),
                mSwerveMods[1].getPosition(),
                mSwerveMods[2].getPosition(),
                mSwerveMods[3].getPosition()
            }
        );
        estimator.update(
            RobotContainer.gyro.getGyroscopeRotation(),
            new SwerveModulePosition[] {
                mSwerveMods[0].getPosition(),
                mSwerveMods[1].getPosition(),
                mSwerveMods[2].getPosition(),
                mSwerveMods[3].getPosition()
            }
        );

        LimelightHelpers.PoseEstimate limelightMeasurement = LimelightHelpers.getBotPoseEstimate_wpiBlue("limelight");
        if(limelightMeasurement.tagCount >= 2){
            estimator.setVisionMeasurementStdDevs(VecBuilder.fill(.7,.7,9999999));
            estimator.addVisionMeasurement(
                limelightMeasurement.pose,
                limelightMeasurement.timestampSeconds);
        }

        SmartDashboard.putNumber("Odem Pose X: ", getOdometry().getX());
        SmartDashboard.putNumber("Odem Pose Y: ", getOdometry().getY());
        SmartDashboard.putNumber("Estimated Pose X: ", getEstimator().getX());
        SmartDashboard.putNumber("Estimated Pose Y: ", getEstimator().getY());
    }

    /**
     * @return Rotation2d offset from starting zero to currect zero
     */
    public Rotation2d getRealZero() {
        return realZero;
    }
    public Pose2d getOdometry(){
        return m_pose;
    }
    public void setOdometry(Pose2d pose){
        m_pose = pose;
    }
    public void zeroEstimatorOffset(){
        initialTagOffset = getEstimator();
    }
    public void resetOdometry(){
        zeroGyro();
        for (SwerveModule mod : mSwerveMods){
            mod.zeroInternalEncoder();
        }
        setOdometry(new Pose2d(new Translation2d(0,0), new Rotation2d(0.0)));
    }
    public Pose2d getEstimator(){
        return estimator.getEstimatedPosition();
    }
    public Transform2d getRelativeEstimator(){
        return getEstimator().minus(initialTagOffset);
    }
}