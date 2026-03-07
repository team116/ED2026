package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.LimitSwitchConfig.Behavior;
import com.revrobotics.spark.config.LimitSwitchConfig.Type;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;

public class Deployer implements Subsystem {
    // private final TalonFX deployingMotor;
    // private final DigitalInput frontLimitSwitch;
    // private final DigitalInput backLimitSwitch;

    private final SparkMax motor;
    private final SparkMaxConfig config = new SparkMaxConfig();

    public static final double RECOMMENDED_DEPLOYER_SPEED = 0.1; // FIXME: Get an actual speed

    public Deployer() {
        // frontLimitSwitch = new DigitalInput(Constants.HardwareIDConstants.FRONT_DEPLOYER_SWITCH_CHANNEL);
        // backLimitSwitch = new DigitalInput(Constants.HardwareIDConstants.BACK_DEPLOYER_SWITCH_CHANNEL);
        // deployingMotor = new TalonFX(Constants.HardwareIDConstants.DEPLOYING_MOTOR_CAN_ID);
        SparkMax sparkMax = new SparkMax(Constants.HardwareIDConstants.DEPLOYER_MOTOR_ID, MotorType.kBrushless);

        config
            .idleMode(IdleMode.kBrake)
            .inverted(false)
            .smartCurrentLimit(20);
        
        config.limitSwitch
            .forwardLimitSwitchType(Type.kNormallyClosed)
            .reverseLimitSwitchType(Type.kNormallyClosed)
            .forwardLimitSwitchTriggerBehavior(Behavior.kStopMovingMotor)
            .reverseLimitSwitchTriggerBehavior(Behavior.kStopMovingMotor);

        sparkMax.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        motor = sparkMax;
    }

    public void run(double speed) {
        motor.set(speed);
        // deployingMotor.set(speed);
    }

    public void stop() {
        motor.stopMotor();
        // deployingMotor.stopMotor();
    }

    public boolean getFrontLimitSwitchPressed() {
        return motor.getForwardLimitSwitch().isPressed();
        // return frontLimitSwitch.get();
    }

    public boolean getBackLimitSwitchPressed() {
        return motor.getReverseLimitSwitch().isPressed();
        // return backLimitSwitch.get();
    }
}