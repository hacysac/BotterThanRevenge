package org.team1515.BotterThanRevenge.Commands.AutoCommands;

import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.Robot;
import org.team1515.BotterThanRevenge.RobotContainer;
import org.team1515.BotterThanRevenge.Utils.Point;

import com.team364.swervelib.util.SwerveConstants;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;

public class driveSegment extends Command {
    private final Drivetrain drivetrain;
    private Point start;
    private Point end;
    private double t; //initial time parameter
    private double speed;
    private double startTime; //actial system time
    private double i;
    private double j;
    private Pose2d originalPose;
    private boolean endSegment;

    private PIDController angleController;
    private double maxRotate;
    private DoubleSupplier startAngle;
    private DoubleSupplier angle;
    private double ff = 0.0; // retune
    
    public driveSegment(Drivetrain drivetrain, DoubleSupplier theta, Point end, double speed, Pose2d startPose) {
        this.drivetrain = drivetrain;
        this.originalPose = startPose;
        this.end = end;

        this.speed = speed;

        this.angle = theta;
        this.maxRotate = 0.5 * SwerveConstants.Swerve.maxAngularVelocity;
        this.startAngle = () -> RobotContainer.gyro.getGyroscopeRotation().getRadians();
        angleController = new PIDController(2, 1, 0);
        this.speed = speed;
        // TODO retune PID
        angleController.setTolerance(Units.degreesToRadians(3));
        angleController.enableContinuousInput(-Math.PI, Math.PI);

        addRequirements(drivetrain);
    }

    public driveSegment(Drivetrain drivetrain, DoubleSupplier theta, Point end, double speed, Pose2d startPose, boolean endSegment) {
        this.drivetrain = drivetrain;
        this.end = end;
        this.originalPose = startPose;
        this.endSegment = endSegment;

        this.speed = speed;

        this.angle = theta;
        this.maxRotate = 0.5 * SwerveConstants.Swerve.maxAngularVelocity;
        this.startAngle = () -> RobotContainer.gyro.getGyroscopeRotation().getRadians();
        angleController = new PIDController(2, 1, 0);
        this.speed = speed;
        // TODO retune PID
        angleController.setTolerance(Units.degreesToRadians(3));
        angleController.enableContinuousInput(-Math.PI, Math.PI);

        addRequirements(drivetrain);
    }

    private double getAngle() {
        return startAngle.getAsDouble() + angle.getAsDouble();
    }

    @Override
    public void initialize(){
        startTime = System.currentTimeMillis(); // time when command is run

        //reset the start pose to the current odometry and calculate speed based on the distance from the setpoint
        start = new Point(drivetrain.getOdometry().getX() - originalPose.getX(), 
                          drivetrain.getOdometry().getY() - originalPose.getY()); 
                          // starting position relative to the original position of the bezier
        double dx = end.x-start.x;//change in x from start to end
        double dy = end.y-start.y;//change in y from start to end
        double mag = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));//magnitude of the change vector
        i = dx/mag; //unit vector i component
        j = dy/mag; //unit vector j component

        this.t = (mag/speed) * 1000;
        angleController.setSetpoint(MathUtil.angleModulus(getAngle()));
        //System.out.println("Start: " + MathUtil.angleModulus(getAngle()));
    }

    @Override
    public void execute() {
        double currentAngle = RobotContainer.gyro.getGyroscopeRotation().getRadians();
        double error = MathUtil.angleModulus(currentAngle - angleController.getSetpoint());
        //System.out.println("error: " + Units.radiansToDegrees(error));
        double rotation = (MathUtil.clamp(angleController.calculate(error + angleController.getSetpoint(), angleController.getSetpoint()) + (ff * Math.signum(-error)),
                -maxRotate, maxRotate)); //setpoint can't be zero, addsetpoint to error
        //System.out.println("rotation: " + Units.radiansToDegrees(rotation));
        drivetrain.drive(new Translation2d(speed*i,speed*j), rotation,true,true);
    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis()-startTime >= t;
    }

    @Override
    public void end(boolean interrupted) {
        //System.out.println(RobotContainer.gyro.getGyroscopeRotation().getRadians() + " " + getAngle());
        //double xoff = drivetrain.getOdometry().getX()-originalPose.getX();
        //double yoff = drivetrain.getOdometry().getY()-originalPose.getY();
        //System.out.println("x: " + xoff + " y:" + yoff);
        //System.out.println("t: " + (System.currentTimeMillis()-startTime));
        //System.out.println(" Speed: " + speed);
        //System.out.println("start pose: " + start.x + ", " + start.y + " end pose: " + end.x + ", " + end.y + "\n");
        //System.out.println("odem x: " + drivetrain.getOdometry().getX() + " odem y: " + drivetrain.getOdometry().getY() + "\n");
        if (endSegment){
            drivetrain.drive(new Translation2d(0,0), 0.0, true, true);
        }
    }
}