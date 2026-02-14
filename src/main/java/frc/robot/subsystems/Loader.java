package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.Subsystem;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.Constants;
import frc.robot.stubs.*;

public class Loader implements Subsystem {
    private final MotorController loadingMotor;

    private final SparkMaxConfig loadingMotorConfig = new SparkMaxConfig();
    
    
    public Loader() {
        if(Constants.BehaviorConstants.USE_STUBS) {
            loadingMotor = new DummyMotorController();
        } else {
            SparkMax loadingMotor = new SparkMax(Constants.HardwareIDConstants.LOADING_MOTOR_ID, MotorType.kBrushless);

            loadingMotor.configure(loadingMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

            this.loadingMotor = loadingMotor;
        }
    }
}
