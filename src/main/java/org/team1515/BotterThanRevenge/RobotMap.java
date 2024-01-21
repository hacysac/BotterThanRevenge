package org.team1515.BotterThanRevenge;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;

public class RobotMap {

    // Conversion factors
    public final static double FALCON_SENSOR_UNITS = 4096.0;

    // PDH
    public static final int PDH_ID = -1; // replace

    //Vision
    public static final String CAMERA_NAME = "camera3"; //TODO
    public static final double CAMERA_HEIGHT_METERS = 0.07; //TODO
    public static final double AMP_TARGET_HEIGHT_METERS = -1.0; //TODO: to center or bottom?
    public static final double SPEAKER_TARGET_HEIGHT_METERS = 0.85; //TODO: to center or bottom?
    public static final double CAMERA_PITCH_RADIANS = Units.degreesToRadians(18.0); //TODO
    public static int SPEAKER_TAG_ID = 3;
    public static int L_AMP_TAG_ID = 2;
    public static int R_AMP_TAG_ID = 2;

    // Subsystems

}