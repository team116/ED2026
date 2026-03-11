package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.Shooter;

public class DefaultShooterCommand extends DefaultCommand {
    private final static String shootingKey = "Shooting Power";
    
    private final Shooter shooter;
    private boolean usingSensing = false;

    public DefaultShooterCommand(Shooter shooter, Joystick thrustmaster, Joystick gunnerPad) {
        this.shooter = shooter;
        this.thrustmaster = thrustmaster;
        this.gunnerPad = gunnerPad;

        super.addRequirements(shooter);
    }

    @Override
    public void execute() {
        if(thrustmaster.getRawButtonPressed(Constants.OperatorInterfaceConstants.TOGGLE_SHOOTING_MODE_BUTTON)) {
            usingSensing = !usingSensing;
        }

        double pow = Shooter.getPowerFromAxis(thrustmaster.getRawAxis(Constants.OperatorInterfaceConstants.SHOOTER_POWER_AXIS));
        double powerFromAxis = pow;

        if(!usingSensing) {
            shooter.runVoltage(Shooter.RECOMMENDED_OUTPUT_VOLTAGE * pow);
        } else {
            pow = getScaleFromDistance(Constants.HardwareIDConstants.SHOOTER_LIMELIGHT_NAME);
            shooter.runRotationalVelocity(pow);
        }

        SmartDashboard.putNumber(shootingKey, pow);
        SmartDashboard.putNumber("Axis Shooting Power", powerFromAxis);
    }

    public static double getScaleFromDistance(String limelightName) {
        double dist = LimelightHelpers.getCameraPose3d_TargetSpace(limelightName).getZ();
        return Constants.getRotationalVelocityFromVelocity(Constants.getVelocityFromDistance(dist));
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
    }
}
