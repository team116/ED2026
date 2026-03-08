package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.subsystems.Deployer;

public class DefaultDeployerCommand extends DefaultCommand {
    private final static String deployingKey = "Deployer Going Forward";
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
        
        if (thrustmaster.getRawButtonPressed(Constants.OperatorInterfaceConstants.SWITCH_DEPLOYING_MODE_BUTTON)) {
            deployer.run(Deployer.RECOMMENDED_DEPLOYER_SPEED);
        }
        
        if (thrustmaster.getRawButtonPressed(4)) {
            deployer.run(0.0);
        }
        // if(thrustmaster.getRawButtonPressed(Constants.OperatorInterfaceConstants.SWITCH_DEPLOYING_MODE_BUTTON)) {
        //     goingFront = !goingFront;
        // }

        // if(goingFront) {
        //     if(!deployer.getFrontLimitSwitchPressed()) {
        //         deployer.run(Deployer.RECOMMENDED_DEPLOYER_SPEED);
        //     } else {
        //         deployer.stop();
        //     }
        // } else {
        //     if(!deployer.getBackLimitSwitchPressed()) {
        //         deployer.run(-Deployer.RECOMMENDED_DEPLOYER_SPEED);
        //     } else {
        //         deployer.stop();
        //     }
        // }

        // if(Constants.BehaviorConstants.TESTING_HARDWARE) {
        //     if(deployer.getFrontLimitSwitchPressed() || deployer.getBackLimitSwitchPressed()) {
        //         deployer.stop(); // while we don't know which limit switch is which logically, both will turn off the power of the robot.
        //     }
        // }

        SmartDashboard.putBoolean("Front Deployer Limit Switch Pressed", deployer.getFrontLimitSwitchPressed());
        SmartDashboard.putBoolean("Back Deployer Limit Switch Pressed", deployer.getBackLimitSwitchPressed());
        SmartDashboard.putBoolean(deployingKey, goingFront);
    }

    @Override
    public void end(boolean interrupted) {
        deployer.stop();
    }
}
