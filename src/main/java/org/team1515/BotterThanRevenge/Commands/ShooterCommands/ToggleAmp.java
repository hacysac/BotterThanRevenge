package org.team1515.BotterThanRevenge.Commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Subsystems.Shooter;

public class ToggleAmp extends Command {
    private final Shooter shooter; 
    private double speed;

    public ToggleAmp(Shooter shooter) {
        this.shooter = shooter;
        this.speed = RobotMap.AMP_SPEED;
        addRequirements(shooter);
    }

    @Override
    public void initialize(){
        shooter.setAmp(true);
        shooter.setSpeaker(false);
    }

    @Override
    public void execute() {
        shooter.shoot(speed);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        shooter.end();
    }
}
