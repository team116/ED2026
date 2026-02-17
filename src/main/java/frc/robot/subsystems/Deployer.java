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

public class Deployer implements Subsystem {
    private final MotorController deployingMotor;

    private final SparkMaxConfig deployingMotorConfig = new SparkMaxConfig();


    public Deployer() {
        if (Constants.BehaviorConstants.USE_STUBS) {
            deployingMotor = new DummyMotorController();
        } else {
            SparkMax deployingMotor = new SparkMax(Constants.HardwareIDConstants.DEPLOYING_MOTOR_ID, MotorType.kBrushless);

            deployingMotor.configure(deployingMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

            this.deployingMotor = deployingMotor;
        }
    }

    public void run(double speed) {
        deployingMotor.set(speed);
    }
}
