package org.team1515.BotterThanRevenge.Utils;

import java.io.IOException;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.PhotonUtils;
import org.team1515.BotterThanRevenge.RobotMap;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;

public class PhotonVision {
    private PhotonCamera camera;
    private AprilTagFieldLayout aprilTagFieldLayout;
    public Transform3d robotToCam;
    private PhotonPoseEstimator photonPoseEstimator;
    private Pose2d prevPose;

    public PhotonVision(){
        camera = new PhotonCamera(RobotMap.CAMERA_NAME);
        prevPose = new Pose2d();



        //check that this recieves data
        try {
            aprilTagFieldLayout = AprilTagFieldLayout.loadFromResource(AprilTagFields.k2024Crescendo.m_resourceFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        robotToCam = new Transform3d(new Translation3d(0.5, 0.0, 0.5), new Rotation3d(0,0,0)); // TODO

        photonPoseEstimator = new PhotonPoseEstimator(aprilTagFieldLayout, PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, camera, robotToCam);
    }

    public Optional<EstimatedRobotPose> getEstimatedGlobalPose(Pose2d prevEstimatedRobotPose) {
        photonPoseEstimator.setReferencePose(prevEstimatedRobotPose);
        return photonPoseEstimator.update();
    }

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
            else if(targetID==RobotMap.SPEAKER_TAG_ID){
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

    public double getAngle(){
        var result = camera.getLatestResult();
        if (result.hasTargets()){
            return Units.degreesToRadians(result.getBestTarget().getYaw());
        }
        return 0.0;
    }
}
