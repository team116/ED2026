package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;

public class Intake implements Subsystem {
    private final TalonFX intakeMotor;
    private final TalonFXConfiguration intakeMotorConfig = new TalonFXConfiguration();

    public static final double RECOMMENDED_INTAKE_SPEED = 1;
    
    public Intake() {
        intakeMotor = new TalonFX(Constants.HardwareIDConstants.INTAKE_MOTOR_CAN_ID);

        intakeMotorConfig.MotorOutput
            .withNeutralMode(NeutralModeValue.Brake)
            .withInverted(InvertedValue.Clockwise_Positive);
        
        intakeMotorConfig.Voltage
            .withPeakForwardVoltage(12)
            .withPeakReverseVoltage(-12);
        
        intakeMotorConfig.CurrentLimits
            .withSupplyCurrentLimitEnable(true)
            .withSupplyCurrentLimit(60)
            .withSupplyCurrentLowerLimit(40)
            .withSupplyCurrentLowerTime(0.5)
            .withStatorCurrentLimitEnable(true)
            .withStatorCurrentLimit(60);
        
        intakeMotor.getConfigurator().apply(intakeMotorConfig);
    }

    public void run(double speed) {
        intakeMotor.set(speed);
    }

    public void intake() {
        run(RECOMMENDED_INTAKE_SPEED);
    }

    public void outtake() {
        run(-RECOMMENDED_INTAKE_SPEED);
    }

    public void stop() {
        intakeMotor.stopMotor();
    }
}
