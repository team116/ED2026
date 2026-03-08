package frc.robot.subsystems;

// import com.ctre.phoenix6.configs.TalonFXConfiguration;
// import com.ctre.phoenix6.hardware.TalonFX;
// import com.ctre.phoenix6.signals.InvertedValue;
// import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.LimitSwitchConfig.Behavior;
import com.revrobotics.spark.config.LimitSwitchConfig.Type;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;

public class Loader implements Subsystem {
    // private final TalonFX motor;
    private final SparkMax motor;

    // private final TalonFXConfiguration config = new TalonFXConfiguration();
    private final SparkMaxConfig config = new SparkMaxConfig();

    public static final double RECOMMENDED_LOADER_SPEED = 0.5;

    public Loader() {
        // motor = new TalonFX(Constants.HardwareIDConstants.LOADING_MOTOR_CAN_ID);

        // config.MotorOutput
        //     .withNeutralMode(NeutralModeValue.Brake)
        //     .withInverted(InvertedValue.Clockwise_Positive);
        
        // config.Voltage
        //     .withPeakForwardVoltage(12)
        //     .withPeakReverseVoltage(-12);
        
        // config.CurrentLimits
        //     .withSupplyCurrentLimitEnable(true)
        //     .withSupplyCurrentLimit(60)
        //     .withSupplyCurrentLowerLimit(40)
        //     .withSupplyCurrentLowerTime(0.5)
        //     .withStatorCurrentLimitEnable(true)
        //     .withStatorCurrentLimit(60);

        // motor.getConfigurator().apply(config);
        SparkMax sparkMax = new SparkMax(Constants.HardwareIDConstants.LOADER_MOTOR_ID, MotorType.kBrushless);

        config
            .idleMode(IdleMode.kBrake)
            .inverted(true)
            .smartCurrentLimit(20);

        sparkMax.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        motor = sparkMax;
    }

    public void run(double speed) {
        motor.set(speed);
    }

    public void stop() {
        motor.stopMotor();
    }
    
}
