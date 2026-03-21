package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.VelocityVoltage;

import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.Constants;
import frc.robot.LimelightHelpers;

public class Shooter implements Subsystem {
    private final TalonFX leftShooterMotor;
    private final TalonFX rightShooterMotor;
    // 0.12 on the given joystick gave us a good shot from around 77 inches, 1.9558 meters
    // 0.432365969750469 power worked from approximately 64 inches away from the front
    
    public static final double RECOMMENDED_SHOOTING_SPEED = 1.0;
    public static final double MAXIMUM_RECOMMENDED_SHOOTING_POWER = 0.77947667786;
    public static final double RECOMMENDED_OUTPUT_VOLTAGE = 12.0;

    public Shooter() {
        leftShooterMotor = new TalonFX(Constants.HardwareIDConstants.LEFT_SHOOTER_MOTOR_ID);
        rightShooterMotor = new TalonFX(Constants.HardwareIDConstants.RIGHT_SHOOTER_MOTOR_ID);

        configShooterMotor(leftShooterMotor, InvertedValue.CounterClockwise_Positive);
        configShooterMotor(rightShooterMotor, InvertedValue.Clockwise_Positive);
    }

    public void run(double speed) {
        leftShooterMotor.set(speed);
        rightShooterMotor.set(speed);
    }

    public void runVoltage(double voltage) {
        leftShooterMotor.setVoltage(voltage);
        rightShooterMotor.setVoltage(voltage);
    }

    public void runRotationalVelocity(double rotVel) {
        leftShooterMotor.setControl(new VelocityVoltage(rotVel));
        rightShooterMotor.setControl(new VelocityVoltage(rotVel));
    }

    public static double getPowerFromAxis(double origAxisVal) {
        //double pow = Math.pow(Constants.scalePowerVal(origAxisVal, 0, 1),2);
        //return pow * MAXIMUM_RECOMMENDED_SHOOTING_POWER;
        // NOTE: Used for many things, does not actually read joystick value, regardless of name (BE CAREFUL OF CHANGING!!!)
        return Constants.scalePowerVal(origAxisVal,0,MAXIMUM_RECOMMENDED_SHOOTING_POWER);
    }

    public static double getScaleFromDistance(String limelightName) {
        double dist = LimelightHelpers.getCameraPose3d_TargetSpace(limelightName).getZ();
        return Constants.getRotationalVelocityFromVelocity(Constants.getVelocityFromDistance(dist));
    }

    public void stop() {
        leftShooterMotor.stopMotor();
        rightShooterMotor.stopMotor();
    }

    private void configShooterMotor(TalonFX mot, InvertedValue motorInverted) {
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.MotorOutput
            .withNeutralMode(NeutralModeValue.Coast)
            .withInverted(motorInverted);
        
        config.Voltage
            .withPeakForwardVoltage(12)
            .withPeakReverseVoltage(-12);
        
        config.CurrentLimits
            .withSupplyCurrentLimitEnable(true)
            .withSupplyCurrentLimit(60)
            .withSupplyCurrentLowerLimit(40)
            .withSupplyCurrentLowerTime(0.5)
            .withStatorCurrentLimitEnable(true)
            .withStatorCurrentLimit(60);
        
        mot.getConfigurator().apply(config);
    }
}
