package org.team1515.BotterThanRevenge;

import edu.wpi.first.math.util.Units;

public class RobotMap {

    // PDH
    public static final int PDH_ID = -1; // TODO

    //field and robot measurements
    public static final double CHASSIS_WIDTH = 28;
    public static final double BUMPER_WIDTH = 2;

    public static final double AUTO_OFFSET = 3; //TODO check robot
    public static final double INTAKE_OFFSET = 4; // TODO check robot

    public static final double SUBWOOFER_DEPTH = 37;
    public static final double SUBWOOFER_LONG_WIDTH = 80;
    public static final double SUBWOOFER_TO_NOTE = 68.5;
    public static final double SUBWOOFER_TO_AMP = 63.5;
    public static final double NOTE_TO_NOTE = 57;
    public static final double WALL_TO_AMP = 76.1;
    public static final double WALL_TO_CENTER = 325.5;
    public static final double SUBWOOFER_TO_CENTER = (WALL_TO_CENTER-SUBWOOFER_DEPTH) - 7; // -7 for note TODO check
    public static final double AMP_TO_CENTER = (WALL_TO_CENTER-WALL_TO_AMP) - 7; // -7 for note TODO check
    public static final double NOTE_TO_AMP_Y = (SUBWOOFER_TO_AMP + (0.5 * SUBWOOFER_LONG_WIDTH)) - NOTE_TO_NOTE;
    public static final double NOTE_TO_AMP_X = (SUBWOOFER_DEPTH + SUBWOOFER_TO_NOTE) - WALL_TO_AMP;

    //Vision
    public static final String CAMERA_NAME = "camera3";
    public static final double CAMERA_HEIGHT_METERS = 0.07; //TODO
    public static final double AMP_TARGET_HEIGHT_METERS = -1.0; //TODO: to center or bottom?
    public static final double SPEAKER_TARGET_HEIGHT_METERS = 0.85; //TODO: to center or bottom?
    public static final double CAMERA_PITCH_RADIANS = Units.degreesToRadians(18.0); //TODO
    
    public static int SPEAKER_TAG_ID = 3;
    public static int L_AMP_TAG_ID = 2;
    public static int R_AMP_TAG_ID = 2;
    
    /* Subsystems */

    // Shooter
    public static final int L_SHOOTER_ID = 20;
    public static final int R_SHOOTER_ID = 21;
    public static final double SPEAKER_SPEED = 0.7; // TODO
    public static final double AMP_SPEED = 0.175; // TODO

    // Indexer
    public static final int INDEXER_ID = 22;
    public static final int INDEX_SENSOR_CHANNEL = -1; // TODO
    public static final double INDEXER_SPEED = 0.5;// TODO 

    //Intake
    public static final int TOP_INTAKE_ID = 23;
    public static final int BOTTOM_INTAKE_ID = 24;
    public static final double INTAKE_SPEED = 0.5; //TODO

    public static final int FLIP_INTAKE_ID = 25;
    public static final int FLIP_UPPER_SENSOR_CHANNEL = -1; //TODO
    public static final int FLIP_LOWER_SENSOR_CHANNEL = -1; // TODO
    public static final int FLIP_CANCODER_ID = 30; //TODO
    public static final double FLIP_SPEED = 0.3; //TODO
    public static final double FLIP_DOWN_VALUE = -1; //TODO
    public static final double FLIP_UP_VALUE = -1; //TODO

    //Climber
    public static final int L_CLIMBER_ID = 26;
    public static final int R_CLIMBER_ID = 27;
    public static final int L_CLIMBER_SENSOR_CHANNEL = -1; //TODO
    public static final int R_CLIMBER_SENSOR_CHANNEL = -1; //TODO
    public static final double CLIMBER_SPEED = -1; // TODO
    public static final double CLIMBER_EXTENTION_LIMIT = 2000; //TODO
}