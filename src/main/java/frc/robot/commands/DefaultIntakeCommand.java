package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;

public class DefaultIntakeCommand extends DefaultCommand{
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
        } else {
            if(thrustmaster.getRawButton(Constants.OperatorInterfaceConstants.TOGGLE_INTAKE_BUTTON)) {
                intaking = !intaking;
            }

            if(intaking) {
                intake.run(Intake.RECOMMENDED_INTAKE_SPEED);
            } else {
                intake.run(0);
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        intake.stop();
    }
}
