package frc.robot.commands;

import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Loader;
import edu.wpi.first.wpilibj2.command.Command;

public class RunChanneling extends Command {
    // Our channeling is the general name given to the combination of the feeder and loader, as they will almost always be run at the same time and rate
    // FIXME: Add individual run commands for each of the loader and feeder so that they don't HAVE to be ran together
    private final Loader loader;
    private final Feeder feeder;
    private final boolean intoShooter;
    private final double loadingSpeed;
    private final double feedingSpeed;


    // Note that our loading and feeding speeds have to be an absolute value of the power we want, otherwise it will run forwards when you mean it to run backwards
    // AKA let the intoShooter boolean handle the direction, control the speed (not velocity) with the doubles
    public RunChanneling(Loader loader, Feeder feeder, boolean intoShooter, double loadingSpeed, double feedingSpeed) {
        this.loader = loader;
        this.feeder = feeder;
        this.intoShooter = intoShooter;
        this.loadingSpeed = loadingSpeed;
        this.feedingSpeed = feedingSpeed;

        super.addRequirements(loader,feeder);
    }

    public RunChanneling(Loader loader, Feeder feeder, boolean intoShooter) {
        this(loader, feeder, intoShooter, Loader.RECOMMENDED_LOADER_SPEED, Feeder.RECOMMENDED_FEEDING_SPEED);
    }

    @Override
    public void execute() {
        if(intoShooter) {
            loader.run(Math.abs(loadingSpeed));
            feeder.run(Math.abs(feedingSpeed));
        } else {
            loader.run(-Math.abs(loadingSpeed));
            feeder.run(-Math.abs(feedingSpeed));
        }
    }

    @Override
    public boolean isFinished() {
        return false; // Note that this is faulty, but we don't want this command to tell itself when to stop, because we don't know when that is
    }

    @Override
    public void end(boolean interrupted) {
        loader.stop();
        feeder.stop();
    }
}
