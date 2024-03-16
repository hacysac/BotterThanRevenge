package org.team1515.BotterThanRevenge.Commands.AutoCommands;

import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.RobotContainer;
import org.team1515.BotterThanRevenge.Utils.Point;

import com.team364.swervelib.util.SwerveConstants;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;

public class driveLine extends Command {
    private final Drivetrain drivetrain;
    private Point end;
    private Point start;
    private double time; //initial time parameter
    private double speed;
    private double startTime; //actial system time
    private double i;
    private double j;

    private PIDController angleController;
    private double maxRotate;
    private double startAngle;
    private double angle;
    private double ff = 0.0; // retune
    
    public driveLine(Drivetrain drivetrain, double angle, Point end, double time) {
        this.drivetrain = drivetrain;
        this.end = end;
        this.time = time;
        this.angle = angle;

        this.maxRotate = 0.5 * SwerveConstants.Swerve.maxAngularVelocity;
        angleController = new PIDController(13.8, 55.2, 0.8625);
        // TODO retune PID
        angleController.setTolerance(Units.degreesToRadians(1));
        angleController.enableContinuousInput(-Math.PI, Math.PI);

        addRequirements(drivetrain);
    }

    @Override
    public void initialize(){
        startTime = System.currentTimeMillis(); // time when command is run
        start = new Point(drivetrain.getOdometry().getX(), drivetrain.getOdometry().getY()); 
        startAngle = RobotContainer.gyro.getGyroscopeRotation().getRadians();

        double dx = end.x-start.x;//change in x from start to end
        double dy = end.y-start.y;//change in y from start to end
        double mag = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));//magnitude of the change vector
        i = dx/mag; //unit vector i component
        j = dy/mag; //unit vector j component
        this.speed = mag/time;

        //System.out.println(speed);
        angleController.setSetpoint(MathUtil.angleModulus(startAngle + angle));
    }

    @Override
    public void execute() {
        double currentAngle = RobotContainer.gyro.getGyroscopeRotation().getRadians();
        double error = MathUtil.angleModulus(angleController.getSetpoint() - currentAngle);
        //System.out.println("error: " + Units.radiansToDegrees(error));
        double rotation = (MathUtil.clamp(angleController.calculate(error + angleController.getSetpoint(), angleController.getSetpoint()) + (ff * Math.signum(error)),
                -maxRotate, maxRotate)); //setpoint can't be zero, addsetpoint to error
        //System.out.println("rotation: " + Units.radiansToDegrees(rotation));
        drivetrain.drive(new Translation2d(speed*i,speed*j), rotation,true,true);
    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis()-startTime >= time*1000;
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
        //System.out.println("odem x: " + drivetrain.getOdometry().getX() + ", " + RobotContainer.drivetrain.getOdometry().getX()  + " odem y: " + drivetrain.getOdometry().getY() + ", " + RobotContainer.drivetrain.getOdometry().getY() + "\n");
        drivetrain.drive(new Translation2d(0,0), 0.0, true, true);
    }
}