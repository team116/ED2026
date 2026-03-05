package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Feeder;

public class RunFeeder extends Command {
    private final Feeder feeder;
    private final boolean intoShooter;
    private final double speed;

    public RunFeeder(Feeder feeder, boolean intoShooter, double speed) {
        this.feeder = feeder;
        this.intoShooter = intoShooter;
        this.speed = speed;

        super.addRequirements(feeder);
    } 

    public RunFeeder(Feeder feeder, boolean intoShooter) {
        this(feeder, intoShooter, Feeder.RECOMMENDED_FEEDING_SPEED);
    }

    public RunFeeder(Feeder feeder) {
        this(feeder, true);
    }

    @Override
    public void execute() {
        if(intoShooter) {
            feeder.run(Math.abs(speed));
        } else {
            feeder.run(-Math.abs(speed));
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        feeder.stop();
    }
    
}
