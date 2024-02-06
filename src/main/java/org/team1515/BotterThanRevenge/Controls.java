
package org.team1515.BotterThanRevenge;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public class Controls {
    // change button maps later
    public static final Trigger RESET_GYRO = new Trigger(RobotContainer.mainController::getBackButton);
    public static final Trigger DRIVE_ROBOT_ORIENTED = new Trigger(RobotContainer.mainController::getLeftBumper);
    public static final Trigger CANCEL_ALL = new Trigger(RobotContainer.secondController::getBackButton);

    public static final Trigger CLIMB_DOWN = new Trigger(RobotContainer.mainController::getAButton);
    public static final Trigger CLIMB_UP = new Trigger(RobotContainer.mainController::getYButton);

    public static final Trigger AUTO_INTAKE = new Trigger(RobotContainer.secondController::getYButton);
    public static final Trigger INTAKE = new Trigger(RobotContainer.secondController::getRightBumper);
    public static final Trigger OUTTAKE = new Trigger(RobotContainer.secondController::getLeftBumper);

    public static final Trigger FLIP = new Trigger(RobotContainer.secondController::getYButton);

    public static final Trigger INDEXER_UP = new Trigger(Controls::getRightTriggerSecond);
    public static final Trigger INDEXER_DOWN = new Trigger(Controls::getLeftTriggerSecond);

    public static final Trigger SHOOT_SPEAKER = new Trigger(RobotContainer.secondController::getXButton);
    public static final Trigger SHOOT_AMP = new Trigger(RobotContainer.secondController::getBButton);
    public static final Trigger SHOOTER_IN = new Trigger(RobotContainer.secondController::getAButton);

    public static boolean getLeftTriggerMain() {
        return RobotContainer.mainController.getLeftTriggerAxis() >= 0.250;
    }

    public static boolean getRightTriggerMain() {
        return RobotContainer.mainController.getRightTriggerAxis() >= 0.250;
    }

    public static boolean getRightTriggerSecond() {
        return RobotContainer.secondController.getRightTriggerAxis() >= 0.250;
    }

    public static boolean getLeftTriggerSecond() {
        return RobotContainer.secondController.getLeftTriggerAxis() >= 0.250;
    }

    public static boolean secondRightStickTrigger() {
        return RobotContainer.secondController.getRightTriggerAxis() >= 0.250;
    }

    public static boolean getLeftStickUp() {
        return RobotContainer.secondController.getLeftY() > 0;
    }

    public static boolean getLeftStickDown() {
        return RobotContainer.secondController.getLeftY() < 0;
    }

    public static boolean getRightStickUp() {
        return RobotContainer.secondController.getLeftY() > 0;
    }

    public static boolean getRightStickDown() {
        return RobotContainer.secondController.getLeftY() < 0;
    }

    public enum DPadButton {
        UP (0),
        RIGHT (90),
        DOWN (180),
        LEFT (270);

        private final int angle;
        DPadButton(int direction) {
            this.angle = direction;
        }

        public boolean getDPadButton() {
            return RobotContainer.secondController.getPOV() == angle;
        }
    }
}

// sticks for manual
// abx for set