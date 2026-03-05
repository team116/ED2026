package frc.robot.commands;

import frc.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.Command;

public class RunIntake extends Command {
    private final Intake intake;
    private final boolean intaking;
    private final double speed;

    public RunIntake(Intake intake, boolean intaking, double speed) {
        this.intake = intake;
        this.intaking = intaking;
        this.speed = speed;

        super.addRequirements(intake);
    }

    public RunIntake(Intake intake, boolean goingForward) {
        this(intake, goingForward, Intake.RECOMMENDED_INTAKE_SPEED);
    }

    @Override
    public void execute() {
        if(intaking) {
            intake.run(Math.abs(speed));
        } else {
            intake.run(-Math.abs(speed));
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        intake.stop();
    }    
}
