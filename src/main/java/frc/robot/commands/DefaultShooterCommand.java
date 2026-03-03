package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.LimelightTarget_Fiducial;
import frc.robot.subsystems.Shooter;

public class DefaultShooterCommand extends DefaultCommand{
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

        if(!usingSensing) {
            shooter.runVoltage(Shooter.RECOMMENDED_OUTPUT_VOLTAGE * Shooter.RECOMMENDED_SHOOTING_SPEED * (1 - thrustmaster.getRawAxis(Constants.OperatorInterfaceConstants.SHOOTER_POWER_AXIS)) / 2.0);
        } else {
            shooter.runVoltage(Shooter.RECOMMENDED_OUTPUT_VOLTAGE * Shooter.RECOMMENDED_SHOOTING_SPEED * getScaleFromDistance(Constants.HardwareIDConstants.SHOOTER_LIMELIGHT_NAME));
        }
    }

    public static double getScaleFromDistance(String limelightName) {
        LimelightTarget_Fiducial[] results = LimelightHelpers.getLatestResults(limelightName).targets_Fiducials;
        LimelightTarget_Fiducial target = null;

        for(int i = 0; i < results.length; i++) {
            if(results[i].fiducialID==BLUE_ALIGNING_TAG_ID || results[i].fiducialID==RED_ALIGNING_TAG_ID) {
                target = results[i];
                break;
            }
        }

        if(target==null) {
            return 0;
        }

        return target.getCameraPose_TargetSpace().getZ() / MAXIMUM_DISTANCE; // FIXME: Fix this linear nonsense of an equation. There is no way output power will be linear. Find some polynomial or something
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
    }
}
