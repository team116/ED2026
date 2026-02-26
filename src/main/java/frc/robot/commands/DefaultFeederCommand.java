package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants;
import frc.robot.subsystems.Feeder;

public class DefaultFeederCommand extends DefaultCommand{
    private final Feeder feeder;
    private boolean feeding = false;

    public DefaultFeederCommand(Feeder feeder, Joystick thrustmaster, Joystick gunnerPad) {
        this.feeder = feeder;
        this.thrustmaster = thrustmaster;
        this.gunnerPad = gunnerPad;

        super.addRequirements(feeder);
    }

    @Override
    public void execute() {
        if(thrustmaster.getRawButton(Constants.OperatorInterfaceConstants.REVERSE_CHANNELING_BUTTON)) {
            feeder.run(Feeder.RECOMMENDED_FEEDING_SPEED);
        } else {
            if(thrustmaster.getRawButtonPressed(Constants.OperatorInterfaceConstants.SWITCH_CHANNELING_MODE_BUTTON)) {
                feeding = !feeding;
            }

            if(feeding) {
                feeder.run(Feeder.RECOMMENDED_FEEDING_SPEED);
            } else {
                feeder.run(0);
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        feeder.stop();
    }
}
