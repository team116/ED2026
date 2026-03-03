package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.subsystems.Feeder;

public class DefaultFeederCommand extends DefaultCommand {
    private static final String feedingKey = "Feeding Mode";
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
            SmartDashboard.putString(feedingKey, "Reverse");
        } else {
            if(thrustmaster.getRawButtonPressed(Constants.OperatorInterfaceConstants.SWITCH_CHANNELING_MODE_BUTTON)) {
                feeding = !feeding;
            }

            if(feeding) {
                feeder.run(Feeder.RECOMMENDED_FEEDING_SPEED);
                SmartDashboard.putString(feedingKey, "Forward");
            } else {
                feeder.run(0);
                SmartDashboard.putString(feedingKey, "Neutral");
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        feeder.stop();
    }
}
