package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;

public class Loader implements Subsystem {
    private final TalonFX motor;

    private final TalonFXConfiguration config = new TalonFXConfiguration();

    public static final double RECOMMENDED_LOADER_SPEED = 1.0;

    public Loader() {
        motor = new TalonFX(Constants.HardwareIDConstants.LOADING_MOTOR_CAN_ID);

        config.MotorOutput
                .withNeutralMode(NeutralModeValue.Brake)
                .withInverted(InvertedValue.Clockwise_Positive);

        motor.getConfigurator().apply(config);
    }

    public void run(double speed) {

    }

    public void stop() {
        motor.stopMotor();
    }
    
}
