package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants;
import frc.robot.subsystems.Deployer;

public class DefaultDeployerCommand extends DefaultCommand{
    private final Deployer deployer;
    private boolean goingFront = false;

    public DefaultDeployerCommand(Deployer deployer, Joystick thrustmaster, Joystick gunnerPad) {
        this.deployer = deployer;
        this.thrustmaster = thrustmaster;
        this.gunnerPad = gunnerPad;

        super.addRequirements(deployer);
    }

    @Override
    public void execute() { // FIXME: This may need to be updated to have a miniscule power given to the deployer motor to keep it in position
        
        if(thrustmaster.getRawButtonPressed(Constants.OperatorInterfaceConstants.SWITCH_DEPLOYING_MODE_BUTTON)) {
            goingFront = !goingFront;
        }

        if(goingFront) {
            if(!deployer.getFrontLimitSwitchPressed()) {
                deployer.run(Deployer.RECOMMENDED_DEPLOYER_SPEED);
            } else {
                deployer.run(0);
            }
        } else {
            if(!deployer.getBackLimitSwitchPressed()) {
                deployer.run(-Deployer.RECOMMENDED_DEPLOYER_SPEED);
            } else {
                deployer.run(0);
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        deployer.stop();
    }
}
