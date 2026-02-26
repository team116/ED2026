package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;

public class DefaultShooterCommand extends DefaultCommand{
    private final Shooter shooter;

    public DefaultShooterCommand(Shooter shooter, Joystick thrustmaster, Joystick gunnerPad) {
        this.shooter = shooter;
        this.thrustmaster = thrustmaster;
        this.gunnerPad = gunnerPad;

        super.addRequirements(shooter);
    }

    @Override
    public void execute() {
        shooter.run(Shooter.RECOMMENDED_SHOOTING_SPEED * (1 - thrustmaster.getRawAxis(Constants.OperatorInterfaceConstants.SHOOTER_POWER_AXIS)) / 2.0);
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
    }
}
