package org.team1515.BotterThanRevenge.Utils;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.team1515.BotterThanRevenge.RobotMap;

import edu.wpi.first.math.util.Units;

public class PhotonVision {
    PhotonCamera camera;

    public PhotonVision(){
        camera = new PhotonCamera(RobotMap.CAMERA_NAME);
    }

    /* 
    public double getDist(){
        var result = camera.getLatestResult();
        if (result.hasTargets()){
            int targetID = result.getBestTarget().getFiducialId();
            if (targetID==RobotMap.L_AMP_TAG_ID || targetID==RobotMap.R_AMP_TAG_ID){
                double range = PhotonUtils.calculateDistanceToTargetMeters(
                    RobotMap.CAMERA_HEIGHT_METERS,
                    RobotMap.AMP_TARGET_HEIGHT_METERS,
                    RobotMap.CAMERA_PITCH_RADIANS,
                    Units.degreesToRadians(result.getBestTarget().getPitch()));
                    return range;
            }
            if(targetID==RobotMap.SPEAKER_ID){
                double range = PhotonUtils.calculateDistanceToTargetMeters(
                    RobotMap.CAMERA_HEIGHT_METERS,
                    RobotMap.SPEAKER_TARGET_HEIGHT_METERS,
                    RobotMap.CAMERA_PITCH_RADIANS,
                    Units.degreesToRadians(result.getBestTarget().getPitch()));
                    return range;
            }
        }
        return 0.0;
    }
    */

    public double getAngle(){
        var result = camera.getLatestResult();
        if (result.hasTargets()){
            return Units.degreesToRadians(result.getBestTarget().getYaw());
        }
        return 0.0;
    }
}
