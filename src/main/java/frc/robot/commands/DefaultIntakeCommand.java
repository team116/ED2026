package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

public class DefaultIntakeCommand extends Command {
    private final static String key = "Intake";
    private final Intake intake;

    private boolean intaking = false;
    private boolean outtaking = false;

    public DefaultIntakeCommand(Intake intake) {
        this.intake = intake;

        super.addRequirements(intake);
    }

    public void execute() {
        if(outtaking) {
            intake.outtake();
            SmartDashboard.putBoolean(key, true);
        } else if(intaking) {
            intake.intake();
            SmartDashboard.putBoolean(key, true);
        } else {
            intake.stop();
            SmartDashboard.putBoolean(key, false);
        }
    }

    public void toggleIntake() {
        intaking = !intaking;
    }

    public void setOuttaking(boolean setVal) {
        outtaking = setVal;
    }

    public void killIntake() {
        intaking = false;
        outtaking = false;
    }

    public void end(boolean interrupted) {
        intake.stop();
    }
}