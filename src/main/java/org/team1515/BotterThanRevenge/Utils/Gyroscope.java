package org.team1515.BotterThanRevenge.Utils;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.SPI;

public class Gyroscope {
    private final AHRS navx;
    private double offset = 0;

    public float rollOffset = 0;
    private float pitchOffset = 0;

    public Gyroscope() {
        navx = new AHRS(SPI.Port.kMXP, (byte) 200);
    }

    /**
     * @return Rotation2d representing the angle the robot is facing
     */
    public Rotation2d getGyroscopeRotation() {
        if (navx.isMagnetometerCalibrated()) {
            // We will only get valid fused headings if the magnetometer is calibrated
            return Rotation2d.fromDegrees(navx.getFusedHeading());
        }

        // We have to invert the angle of the NavX so that rotating the robot
        // counter-clockwise makes the angle increase.
        return Rotation2d.fromDegrees(360.0 - navx.getYaw() + offset); // Add offset to make the shooter the front
                                                                       // instead of the intake
    }

    /**
     * resets gyro yaw to zero
     */
    public void zeroYaw() {
        navx.zeroYaw();
    }

    /**
     * @return float yaw of the robot in degrees
     */
    public float getYaw() {
        return navx.getYaw();
    }

    /**
     * @return float pitch of the robot in degrees
     */
    public float getPitch() {
        return navx.getPitch() - pitchOffset;
    }


    /**
     * @return float roll of the robot in degrees
     */
    public float getRoll() {
        return navx.getRoll() - rollOffset;
    }

    /**
     * Sets the robot's roll to zero
     */
    public void zeroRoll() {
        rollOffset = navx.getRoll();
    }

    public void zeroPitch(){
        pitchOffset = navx.getPitch();
    }
}
