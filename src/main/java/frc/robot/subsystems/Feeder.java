package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;
import frc.robot.stubs.DummyMotorController;

public class Feeder implements Subsystem {
    private final MotorController feederMotor;

    private final SparkMaxConfig feederMotorConfig = new SparkMaxConfig();

    public static final double RECOMMENDED_FEEDING_SPEED = 1.0;

    public Feeder() {

        if(Constants.BehaviorConstants.USE_STUBS) {
            feederMotor = new DummyMotorController();
        } else {
            SparkMax feederMotor = new SparkMax(Constants.HardwareIDConstants.FEEDING_MOTOR_ID, MotorType.kBrushless);
            
            feederMotor.configure(feederMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

            this.feederMotor = feederMotor;
        }
    }

    public void run(double speed) {
        feederMotor.set(speed);
    }

    public void stop() {
        feederMotor.stopMotor();
    }
} 
