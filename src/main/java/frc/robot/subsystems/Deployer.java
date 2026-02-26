package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.DigitalInput;

public class Deployer implements Subsystem {
    private final TalonFX deployingMotor;
    private final DigitalInput frontLimitSwitch;
    private final DigitalInput backLimitSwitch;

    public static final double RECOMMENDED_DEPLOYER_SPEED = 1.0; // FIXME: Get an actual speed

    public Deployer() {
        frontLimitSwitch = new DigitalInput(Constants.HardwareIDConstants.FRONT_DEPLOYER_SWITCH_CHANNEL);
        backLimitSwitch = new DigitalInput(Constants.HardwareIDConstants.BACK_DEPLOYER_SWITCH_CHANNEL);
        deployingMotor = new TalonFX(Constants.HardwareIDConstants.DEPLOYING_MOTOR_CAN_ID);
    }

    public void run(double speed) {
        deployingMotor.set(speed);
    }

    public void stop() {
        deployingMotor.stopMotor();
    }

    public boolean getFrontLimitSwitchPressed() {
        return frontLimitSwitch.get();
    }

    public boolean getBackLimitSwitchPressed() {
        return backLimitSwitch.get();
    }
}