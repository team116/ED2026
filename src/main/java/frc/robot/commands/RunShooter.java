package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.Shooter;

public class RunShooter extends Command{
    private final Shooter shooter;
    private final double speed;

    public RunShooter(Shooter shooter, double speed) {
        this.shooter = shooter;
        this.speed = speed;

        super.addRequirements(shooter);
    }

    public RunShooter(Shooter shooter) {
        this(shooter, Shooter.RECOMMENDED_SHOOTING_SPEED);
    }

    @Override
    public void execute() {
        shooter.run(speed);
    }

    @Override
    public boolean isFinished() {
        return false; // Note that this is somewhat dangerous, but we don't really want a default reason to stop
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
    }
}
