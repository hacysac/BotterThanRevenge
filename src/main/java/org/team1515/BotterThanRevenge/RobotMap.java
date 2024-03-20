package org.team1515.BotterThanRevenge;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class RobotMap {

    // PDH
    public static final int PDH_ID = -1; // TODO

    //field and robot measurements
    public static final double CHASSIS_WIDTH = 28;
    public static final double BUMPER_WIDTH = 2;

    public static final double AUTO_OFFSET = 3; //TODO check robot
    public static final double INTAKE_OFFSET = 4; // TODO check robot
    public static final double AUTO_NOTE_ANGLE_OFFSET = Units.degreesToRadians(40.0); //TODO check robot
    public static final double AUTO_AMP_ANGLE_OFFSET = Units.degreesToRadians(120.0); //TODO check robot
    public static final double AUTO_INTAKE_TIME = 1.75; //TODO check robot
    public static final double AUTO_FEED_TIME = 0.75; //TODO check robot

    public static final double SUBWOOFER_DEPTH = 37;
    public static final double SUBWOOFER_LONG_WIDTH = 80;
    public static final double SUBWOOFER_TO_NOTE = 78;
    public static final double SUBWOOFER_TO_AMP = 64;
    public static final double NOTE_TO_NOTE = 57;
    public static final double CENTER_NOTE_TO_NOTE = 66;
    public static final double WALL_TO_AMP = 73;
    public static final double WALL_TO_CENTER = 325.5;
    public static final double ROBOT_STARTING_ZONE_WIDTH = 76;
    public static final double HALF_FIELD_WIDTH = 324.6;
    public static final double SUBWOOFER_TO_CENTER = (WALL_TO_CENTER-SUBWOOFER_DEPTH) - 7; // -7 for note TODO check
    public static final double AMP_TO_CENTER = (WALL_TO_CENTER-WALL_TO_AMP) - 7; // -7 for note TODO check
    public static final double NOTE_TO_AMP_Y = 47.64;
    public static final double NOTE_TO_AMP_X = 40;
    public static final double PASS_SPEAKER_OFFSET = -20;


    //Vision
    public static final String CAMERA_NAME = "camera3";
    public static final double CAMERA_HEIGHT_METERS = 0.07; //TODO
    public static final double AMP_TARGET_HEIGHT_METERS = -1.0; //TODO: to center or bottom?
    public static final double SPEAKER_TARGET_HEIGHT_METERS = 0.85; //TODO: to center or bottom?
    public static final double CAMERA_PITCH_RADIANS = Units.degreesToRadians(18.0); //TODO
    public static final Translation2d CAMERA_TO_BUMPER = new Translation2d(0.1146, 0);


    public static final Translation2d TAG_TO_AMP = new Translation2d(0.22, 0);
    public static final Translation2d CTR_TAG_TO_SUBWOOFER = new Translation2d(0.1146, 0);
    public static final Translation2d LEFT_TAG_TO_SUBWOOFER = new Translation2d(0.1146, 0);
    public static int CTR_SPEAKER_TAG_ID = DriverStation.getAlliance().get() == Alliance.Red ? 4 : 7;
    public static int RIGHT_SPEAKER_TAG_ID = DriverStation.getAlliance().get() == Alliance.Red ? 3 : 8;
    public static int AMP_TAG_ID = DriverStation.getAlliance().get() == Alliance.Red ? 5 : 6;
    /* Subsystems */

    // Shooter
    public static final int L_SHOOTER_ID = 20;
    public static final int R_SHOOTER_ID = 21;
    public static final int SHOOTER_CURRENT_LIMIT = 35;
    public static final double SPEAKER_SPEED = 0.875; // TODO
    public static final double PASS_SPEED = 0.3; // TODO
    public static final double AMP_SPEED = 0.15; // TODO

    // Indexer
    public static final int INDEXER_ID = 22;
    public static final int INDEX_SENSOR_CHANNEL = 2; // TODO
    public static final double INDEXER_SPEED = 0.7;// TODO 
    public static final int INDEXER_CURRENT_LIMIT = 25;

    //Intake
    public static final int TOP_INTAKE_ID = 23;
    public static final int BOTTOM_INTAKE_ID = 24;
    public static final double UPPER_INTAKE_SPEED = 0.7;
    public static final double LOWER_INTAKE_SPEED = 0.55;
    public static final int INTAKE_CURRENT_LIMIT = 20;

    //Flip
    public static final int FLIP_INTAKE_ID = 25;
    public static final int FLIP_CANCODER_ID = 30; 
    public static final double FLIP_UP_SPEED = 0.4; //TODO
    public static final double FLIP_DOWN_SPEED = 0.15; //TODO should be less than ff value
    public static final double FLIP_MID_OFFSET = 0.24; //TODO
    public static final double FLIP_DOWN_OFFSET = 0.51; //TODO
    public static final int FLIP_CURRENT_LIMIT = 30;

    //Climber
    public static final int L_CLIMBER_ID = 26;
    public static final int R_CLIMBER_ID = 27;
    public static final double CLIMBER_UP_SPEED = 0.7;
    public static final double CLIMBER_DOWN_SPEED = 0.7;
    public static final double CLIMBER_EXTENTION_LIMIT = 65; //TODO
    public static final int CLIMBER_CURRENT_LIMIT = 30;
}