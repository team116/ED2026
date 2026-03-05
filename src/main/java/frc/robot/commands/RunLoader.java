package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Loader;

public class RunLoader extends Command {
    private final Loader loader;
    private final boolean intoShooter;
    private final double loadingSpeed;

    public RunLoader(Loader loader, boolean intoShooter, double loadingSpeed) {
        this.loader = loader;
        this.intoShooter = intoShooter;
        this.loadingSpeed = loadingSpeed;
        
        super.addRequirements(loader);
    }

    public RunLoader(Loader loader, boolean intoShooter) {
        this(loader, intoShooter, Loader.RECOMMENDED_LOADER_SPEED);
    }

    public RunLoader(Loader loader) {
        this(loader, true);
    }

    @Override
    public void execute() {
        if(intoShooter) {
            loader.run(Math.abs(loadingSpeed));
        } else {
            loader.run(-Math.abs(loadingSpeed));
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        loader.stop();
    }

    
}
