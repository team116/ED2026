package frc.robot.subsystems;

import javax.lang.model.util.ElementScanner14;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;
import frc.robot.stubs.DummyMotorController;

public class Intake implements Subsystem {
    private final MotorController intakeMotor;
    private final SparkMaxConfig intakeMotorConfig = new  SparkMaxConfig() ; 
    
    public Intake(){}
  if(Constants.BehaviorConstants.USE_STUBS) {
            intakeMotor = new DummyMotorController();
        } else {
            SparkMax intakingMotor = new SparkMax(Constants.HardwareIDConstants.INTAKE_MOTOR_ID, MotorType.kBrushless);

            intakingMotor.configure(intakeMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

            this.intakeMotor = intakingMotor;
        }
    }
    public void run(double speed)
    {
     if (speed>=0)
     {}
     //motors go forward
     else 
    {}
     //motors go reverse   
    }
}
