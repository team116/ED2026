package frc.robot.commands;

import java.util.concurrent.atomic.AtomicBoolean;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;

public class DefaultIntakeCommand extends DefaultCommand {
    private static final String intakingKey = "Intaking Mode";
    private final Intake intake;
    
    public DefaultIntakeCommand(Intake intake, Joystick thrustmaster, Joystick gunnerPad) {
        this.intake = intake;
        this.thrustmaster = thrustmaster;
        this.gunnerPad = gunnerPad;
        
        new JoystickButton(thrustmaster, Constants.OperatorInterfaceConstants.TOGGLE_INTAKE_BUTTON).toggleOnTrue(
            new StartEndCommand(() -> {
                    intake.run(Intake.RECOMMENDED_INTAKE_SPEED);
                    SmartDashboard.putString(intakingKey, "Intaking");
                }, () -> {
                    intake.stop();
                    SmartDashboard.putString(intakingKey, "Neutral");
                },
                intake
            )
        );

        Trigger outtake = new Trigger(() -> thrustmaster.getRawButton(Constants.OperatorInterfaceConstants.OUTTAKE_BUTTON));

        outtake.onTrue(new InstantCommand(() -> {
            intake.run(-Intake.RECOMMENDED_INTAKE_SPEED);
            SmartDashboard.putString(intakingKey, "Outtaking");
        }));

        outtake.onFalse(new InstantCommand(() -> {
            intake.stop();
            SmartDashboard.putString(intakingKey, "Neutral");
        }));

        super.addRequirements(intake);
    }

    @Override
    public void execute() {
        //Note we do not need this body due to the bindings being set in the init()
    }

    @Override
    public void end(boolean interrupted) {
        intake.stop();
    }
}
