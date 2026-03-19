package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Deployer;

public class RunDeployer extends Command {
    private final Deployer deployer;
    private final boolean goingForward;

    public RunDeployer(Deployer deployer, boolean forward) {
        this.deployer = deployer;
        goingForward = forward;

        super.addRequirements(deployer);
    }

    @Override
    public void execute() {
        if(!isFinished()) {
            // Note that we don't allow for custom speeds to be used here, as we should have one set speed for the deployer (make sure it's safe)
            if(goingForward) {
                deployer.run(Deployer.RECOMMENDED_DEPLOYING_SPEED);
            } else {
                deployer.run(-Deployer.RECOMMENDED_DEPLOYING_SPEED);
            }
        } else {
            deployer.stop();
        }
    }

    @Override
    public boolean isFinished() {
        return (goingForward && deployer.getFrontLimitSwitchPressed()) || (!goingForward && deployer.getBackLimitSwitchPressed());
    }

    @Override
    public void end(boolean interrupted) {
        deployer.stop();
    }
}
