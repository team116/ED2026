package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;

public class EnabledShooterCommand extends Command {
    private final Shooter shooter;
    private final Joystick thrustmaster;
    private Mode curMode;

    private double pow = 0;

    public static enum Mode {
        MANUAL, TOWERONE, MEDIUM, TOWERTWO
    }

    public EnabledShooterCommand(Shooter shooter, Joystick thrustmaster) {
        this.shooter = shooter;
        this.thrustmaster = thrustmaster;
        addRequirements(shooter);
    }

    public void setMode(Mode mode){
        this.curMode = mode;
    }

    public void initialize() {
        curMode = Mode.MANUAL;
        SmartDashboard.putBoolean("Shooter Enabled", true);
    }

    /**
     * The main body of a command. Called repeatedly while the command is scheduled.
     */
    public void execute() {
        SmartDashboard.putString("Shooter Mode", curMode.name());

        switch (curMode) {
            case MANUAL:
                pow = Shooter.getPowerFromAxis(thrustmaster.getRawAxis(Constants.OperatorInterfaceConstants.SHOOTER_POWER_AXIS));
                break;

            case TOWERONE:
                pow = 0.481;
                break;

            case MEDIUM:
                pow = Shooter.getPowerFromAxis(-0.24);
                break;

            case TOWERTWO:

                pow = 0.481;
                break;

            default:
                break;
        }

        shooter.runVoltage(Shooter.RECOMMENDED_OUTPUT_VOLTAGE * pow);
    }

    /**
     * The action to take when the command ends. Called when either the command
     * finishes normally, or
     * when it interrupted/canceled.
     *
     * <p>
     * Do not schedule commands here that share requirements with this command. Use
     * {@link
     * #andThen(Command...)} instead.
     *
     * @param interrupted whether the command was interrupted/canceled
     */
    public void end(boolean interrupted) {
        shooter.stop();
        SmartDashboard.putBoolean("Shooter Enabled", false);
    }

    public double getPower() {
        return pow;
    }

    public boolean isFinished() {
        return false;
    }
}
