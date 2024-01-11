package org.team1515.BotterThanRevenge.Utils;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.team1515.BotterThanRevenge.RobotMap;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;

public class PhotonVision {
    private PhotonCamera camera;
    private AprilTagFieldLayout aprilTagFieldLayout;
    public Transform3d robotToCam;
    // private PhotonPoseEstimator photonPoseEstimator;
    private Pose2d prevPose;

    public PhotonVision(){
        //camera = new PhotonCamera(RobotMap.CAMERA_NAME);

        
        // // prevPose = new Pose2d();

        // //check that this recieves data
        // try {
        //     aprilTagFieldLayout = AprilTagFieldLayout.loadFromResource(AprilTagFields.k2024Crescendo.m_resourceFile);
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }

        robotToCam = new Transform3d(new Translation3d(Units.inchesToMeters(28*-0.5), 0.0, Units.inchesToMeters(15.5)), new Rotation3d(0,50.0,0)); // TODO

        // photonPoseEstimator = new PhotonPoseEstimator(aprilTagFieldLayout, PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, camera, robotToCam);
    }

    // public Optional<EstimatedRobotPose> getEstimatedGlobalPose(Pose2d prevEstimatedRobotPose) {
    //     photonPoseEstimator.setReferencePose(prevEstimatedRobotPose);
    //     return photonPoseEstimator.update();
    // }

    public Translation2d getTrans(){
        var result = camera.getLatestResult();
        if (result.hasTargets()){
            PhotonTrackedTarget target = result.getBestTarget();
            int targetID = target.getFiducialId();
            double targetPitch = target.getPitch();
            Rotation2d targetYaw = new Rotation2d(target.getYaw());

            if (targetID==RobotMap.AMP_TAG_ID){
                double distance = PhotonUtils.calculateDistanceToTargetMeters(RobotMap.CAMERA_HEIGHT_METERS, RobotMap.AMP_TARGET_HEIGHT_METERS, RobotMap.CAMERA_PITCH_RADIANS, targetPitch);
                Translation2d cameraToTarget = PhotonUtils.estimateCameraToTargetTranslation(distance, targetYaw);
                return cameraToTarget.minus(RobotMap.TAG_TO_AMP);
            }
            else if(targetID==RobotMap.CTR_SPEAKER_TAG_ID){
                double distance = PhotonUtils.calculateDistanceToTargetMeters(RobotMap.CAMERA_HEIGHT_METERS, RobotMap.SPEAKER_TARGET_HEIGHT_METERS, RobotMap.CAMERA_PITCH_RADIANS, targetPitch);
                Translation2d cameraToTarget = PhotonUtils.estimateCameraToTargetTranslation(distance, targetYaw);
                return cameraToTarget.minus(RobotMap.CTR_TAG_TO_SUBWOOFER);
            }
            else if(targetID==RobotMap.RIGHT_SPEAKER_TAG_ID){
                double distance = PhotonUtils.calculateDistanceToTargetMeters(RobotMap.CAMERA_HEIGHT_METERS, RobotMap.SPEAKER_TARGET_HEIGHT_METERS, RobotMap.CAMERA_PITCH_RADIANS, targetPitch);
                Translation2d cameraToTarget = PhotonUtils.estimateCameraToTargetTranslation(distance, targetYaw);
                return cameraToTarget.minus(RobotMap.LEFT_TAG_TO_SUBWOOFER);
            }
        }
        return new Translation2d(0,0);
    }
    // public double getDist(){
    //     var result = camera.getLatestResult();
    //     if (result.hasTargets()){
    //         int targetID = result.getBestTarget().getFiducialId();
    //         if (targetID==RobotMap.AMP_TAG_ID){
    //             double range = PhotonUtils.calculateDistanceToTargetMeters(
    //                 RobotMap.CAMERA_HEIGHT_METERS,
    //                 RobotMap.AMP_TARGET_HEIGHT_METERS,
    //                 RobotMap.CAMERA_PITCH_RADIANS,
    //                 Units.degreesToRadians(result.getBestTarget().getPitch()));
    //                 return range;
    //         }
    //         else if(targetID==RobotMap.CTR_SPEAKER_TAG_ID){
    //             double range = PhotonUtils.calculateDistanceToTargetMeters(
    //                 RobotMap.CAMERA_HEIGHT_METERS,
    //                 RobotMap.SPEAKER_TARGET_HEIGHT_METERS,
    //                 RobotMap.CAMERA_PITCH_RADIANS,
    //                 Units.degreesToRadians(result.getBestTarget().getPitch()));
    //                 return range;
    //         }
    //         else if(targetID==RobotMap.RIGHT_SPEAKER_TAG_ID){
    //             double range = PhotonUtils.calculateDistanceToTargetMeters(
    //                 RobotMap.CAMERA_HEIGHT_METERS,
    //                 RobotMap.SPEAKER_TARGET_HEIGHT_METERS,
    //                 RobotMap.CAMERA_PITCH_RADIANS,
    //                 Units.degreesToRadians(result.getBestTarget().getPitch()));
    //                 return range;
    //         }
    //     }
    //     return 0.0;
    // }

    public double getAngle(){
        var result = camera.getLatestResult();
        if (result.hasTargets()){
            return result.getBestTarget().getYaw();
        }
        return 0.0;
    }
}
