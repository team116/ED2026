package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.subsystems.Loader;

public class DefaultLoaderCommand extends DefaultCommand {
    private static final String loadingKey = "Loading Mode";
    private final Loader loader;
    private boolean loading = false;

    public DefaultLoaderCommand(Loader loader, Joystick thrustmaster, Joystick gunnerPad) {
        this.loader = loader;
        this.thrustmaster = thrustmaster;
        this.gunnerPad = gunnerPad;
        
        super.addRequirements(loader);
    }

    @Override
    public void execute() {
        if(thrustmaster.getRawButton(Constants.OperatorInterfaceConstants.REVERSE_CHANNELING_BUTTON)) {
            loader.run(-Loader.RECOMMENDED_LOADER_SPEED);
            SmartDashboard.putString(loadingKey, "Reverse");
        } else {
            if(thrustmaster.getRawButtonPressed(Constants.OperatorInterfaceConstants.SWITCH_CHANNELING_MODE_BUTTON)) {
                loading = !loading;
            }

            if(loading) {
                loader.run(Loader.RECOMMENDED_LOADER_SPEED);
                SmartDashboard.putString(loadingKey, "Forward");
            } else {
                loader.run(0);
                SmartDashboard.putString(loadingKey, "Neutral");
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        loader.stop();
    }
}
