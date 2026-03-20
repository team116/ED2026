package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;

public class Climber implements Subsystem {
    private TalonFX climbingMotor;

    private DigitalInput limitSwitch;

    private static final double CLIMBING_SPEED = 0.25; // FIXME: Tune

    private static final double EXTENDED_POSITION_ROTATIONS = 10;

    public Climber() {
        this.climbingMotor = new TalonFX(Constants.HardwareIDConstants.CLIMBER_MOTOR_CAN_ID);

        TalonFXConfiguration config = new TalonFXConfiguration();

        config.MotorOutput
            .withNeutralMode(NeutralModeValue.Brake)
            .withInverted(InvertedValue.Clockwise_Positive);

        climbingMotor.getConfigurator().apply(config);
        // NOTE: We aren't using the current and voltage limits because we need the motor running strong
        
        limitSwitch = new DigitalInput(Constants.HardwareIDConstants.CLIMBER_LIMIT_SWITCH_CHANNEL);
    }

    public void extend() {
        climbingMotor.set(CLIMBING_SPEED);
    }

    public void retract() {
        climbingMotor.set(-CLIMBING_SPEED);
    }

    // FIXME: I have no idea if this will work
    public double getCurrentRotations() {
        return climbingMotor.getPosition().getValueAsDouble();
    }

    public boolean getLimitSwitchPressed() {
        return limitSwitch.get();
    }

    public boolean pastExtendedPosition() {
        return getCurrentRotations() >= EXTENDED_POSITION_ROTATIONS;
    }

    public void stop() {
        climbingMotor.stopMotor();
    }

    public Command ExtendCommand() {
        Command c = new Command() {
            @Override
            public void initialize() {
                extend();
            }

            @Override
            public boolean isFinished() {
                return pastExtendedPosition();
            }

            @Override
            public void end(boolean interrupted) {
                stop();
            }
        };

        c.addRequirements(this);
        
        return c;
    }

    public Command RetractCommand() {
        Command c = new Command() {
            @Override
            public void initialize() {
                retract();
            }

            @Override
            public boolean isFinished() {
                return getLimitSwitchPressed();
            }

            @Override
            public void end(boolean interrupted) {
                stop();
            }
        };

        c.addRequirements(this);

        return c;
    }
}
