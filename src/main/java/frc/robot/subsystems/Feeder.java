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
    private final MotorController FeederMotor;

    private final SparkMaxConfig FeederMotorConfig = new SparkMaxConfig();

    public Feeder () {

  if(Constants.BehaviorConstants.USE_STUBS) {
         FeederMotor = new DummyMotorController();
        } else {
            SparkMax FeederMotor = new SparkMax(Constants.HardwareIDConstants.FEEDING_MOTOR_ID, MotorType.kBrushless);

            FeederMotor.configure(FeederMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

            this.FeederMotor = FeederMotor;

    }
}
public void run(Double speed) {
// 1 moter run in likly both directions and possidle varibal speed.
// moter - runs- both directions and maybe varibal speed

FeederMoter.set.(speed);

}
} 
