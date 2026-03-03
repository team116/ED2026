package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;

public class DefaultIntakeCommand extends DefaultCommand {
    private static final String intakingKey = "Intaking Mode";
    private final Intake intake;
    private boolean intaking = false;
    
    public DefaultIntakeCommand(Intake intake, Joystick thrustmaster, Joystick gunnerPad) {
        this.intake = intake;
        this.thrustmaster = thrustmaster;
        this.gunnerPad = gunnerPad;

        super.addRequirements(intake);
    }

    @Override
    public void execute() {
        if(thrustmaster.getRawButton(Constants.OperatorInterfaceConstants.OUTTAKE_BUTTON)) {
            intake.run(-Intake.RECOMMENDED_INTAKE_SPEED);
            SmartDashboard.putString(intakingKey, "Outtaking");
        } else {
            if(thrustmaster.getRawButtonPressed(Constants.OperatorInterfaceConstants.TOGGLE_INTAKE_BUTTON)) {
                intaking = !intaking;
            }

            if(intaking) {
                intake.run(Intake.RECOMMENDED_INTAKE_SPEED);
                SmartDashboard.putString(intakingKey, "Intaking");
            } else {
                intake.run(0);
                SmartDashboard.putString(intakingKey, "Neutral");
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        intake.stop();
    }
}
