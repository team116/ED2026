package frc.robot.subsystems;

import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.stubs.DummyMotorController;
import frc.robot.Constants;

public class Shooter implements Subsystem {
    private final MotorController leftShooterMotor;
    private final MotorController rightShooterMotor;

    private final SparkMaxConfig leftShooterConfig = new SparkMaxConfig();
    private final SparkMaxConfig rightShooterConfig = new SparkMaxConfig();

    public static final double RECOMMENDED_SHOOTING_SPEED = 1.0;

    public Shooter() {
        if(Constants.BehaviorConstants.USE_STUBS) {
            leftShooterMotor = new DummyMotorController();
            rightShooterMotor = new DummyMotorController();
        } else {
            SparkMax leftShooterMotor = new SparkMax(Constants.HardwareIDConstants.LEFT_SHOOTER_MOTOR_ID, MotorType.kBrushless);
            SparkMax rightShooterMotor = new SparkMax(Constants.HardwareIDConstants.RIGHT_SHOOTER_MOTOR_ID, MotorType.kBrushless);

            leftShooterMotor.configure(leftShooterConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
            rightShooterMotor.configure(rightShooterConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

            this.leftShooterMotor = leftShooterMotor;
            this.rightShooterMotor = rightShooterMotor;
        }
    }

    public void run(double speed) {
        leftShooterMotor.set(speed);
        rightShooterMotor.set(speed);
    }

    public void stop() {
        leftShooterMotor.stopMotor();
        rightShooterMotor.stopMotor();
    }
}
