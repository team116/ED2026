package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.LimelightTarget_Fiducial;
import frc.robot.subsystems.Shooter;

public class DefaultShooterCommand extends DefaultCommand {
    private final static String shootingKey = "Shooting Power";

    private final static double BLUE_ALIGNING_TAG_ID = 26;
    private final static double RED_ALIGNING_TAG_ID = 10;

    private final static double MAXIMUM_DISTANCE = 1.0; // FIXME: Get the distance from which the shooter hits while going at maximum power
    
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

        double pow = 0;

        if(!usingSensing) {
            pow = ((1 - thrustmaster.getRawAxis(Constants.OperatorInterfaceConstants.SHOOTER_POWER_AXIS)) / 2.0);
        } else {
            pow = getScaleFromDistance(Constants.HardwareIDConstants.SHOOTER_LIMELIGHT_NAME);
        }
        
        shooter.runVoltage(Shooter.RECOMMENDED_OUTPUT_VOLTAGE * Shooter.RECOMMENDED_SHOOTING_SPEED * pow);

        SmartDashboard.putNumber(shootingKey, pow);
    }

    public static double getScaleFromDistance(String limelightName) {
        return LimelightHelpers.getCameraPose3d_TargetSpace(limelightName).getZ() / MAXIMUM_DISTANCE; // FIXME: Fix this linear nonsense of an equation. There is no way output power will be linear. Find some polynomial or something
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
    }
}
