package org.team1515.BotterThanRevenge.Utils;

public class Utilities {

    /**
     * @param value    input value
     * @param deadband amount of deadband
     * @return double value accounted for base error
     */
    public static double deadband(double value, double deadband) {
        if (Math.abs(value) > deadband) {
            if (value > 0.0) {
                return (value - deadband) / (1.0 - deadband);
            } else {
                return (value + deadband) / (1.0 - deadband);
            }
        } else {
            return 0.0;
        }
    }

    /**
     * @param counts REV Position (in rotations)
     * @param gearRatio Gear Ratio between NEO and Mechanism
     * @return Degrees of Rotation of Mechanism
     */
    public static double revToDegrees(double rotations, double gearRatio) {
        return rotations * (360.0 / (gearRatio));
    }

    /**
     * @param degrees Degrees of rotation of Mechanism
     * @param gearRatio Gear Ratio between NEO and Mechanism
     * @return REV Position (in rotations)
     */
    public static double degreesToRev(double degrees, double gearRatio) {
        return degrees / (360.0 / (gearRatio));
    }

    /*
     * Returns true if error is within epsilon
     */
    public static boolean epsilonEquals(double a, double b, double epsilon) {
        return (a - epsilon <= b) && (a + epsilon >= b);
    }
}