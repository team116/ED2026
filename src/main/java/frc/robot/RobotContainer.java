// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.concurrent.atomic.AtomicBoolean;

import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;

import choreo.auto.AutoChooser;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.OperatorInterfaceConstants;
import frc.robot.autoroutines.AutoRoutinesChoreo;
import frc.robot.commands.DefaultDrivetrainCommand;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.CommandSwerveDrivetrainChoreo;
import frc.robot.subsystems.CommandSwerveDrivetrainPathPlanner;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Loader;
import frc.robot.subsystems.Shooter;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private final String DEFAULT_PATHPLANNER_AUTO = "default";

  private AtomicBoolean targetingAprilTag = new AtomicBoolean(false);
  private AtomicBoolean drivingRobotCentric = new AtomicBoolean(false);

  private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();

  private final static double presetPower1 = Shooter.MAXIMUM_RECOMMENDED_SHOOTING_POWER * Shooter.getPowerFromAxis(-0.52);

  public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
// Uncomment the following lines when we can confirm connectivity to each of the subsystems
  public final Shooter shooter = new Shooter();
  public final Loader loader = new Loader();
  public final Intake intake = new Intake();
  public final Deployer deployer = new Deployer();
  public final Climber climber = new Climber();

  private SendableChooser<Command> autoChooserPathPlanner;
  private AutoChooser autoChooserChoreo;
  public AutoRoutinesChoreo autoRoutinesChoreo;

  public final CommandXboxController controller = new CommandXboxController(OperatorInterfaceConstants.driverControllerPort);
  public final Joystick thrustmaster = new Joystick(OperatorInterfaceConstants.thrustmasterPort);
  public final Joystick gunnerPad = null;// new Joystick(OperatorInterfaceConstants.gunnerPadPort);
  // FIXME: Uncomment whenever we can confirm connectivity

  public RobotContainer() {
    // LimelightHelpers.SetFiducialIDFiltersOverride(Constants.HardwareIDConstants.SHOOTER_LIMELIGHT_NAME,Constants.getGoodIdsForShooter());

    if(drivetrain instanceof CommandSwerveDrivetrainChoreo) {
      autoChooserChoreo = new AutoChooser();
      autoRoutinesChoreo = new AutoRoutinesChoreo((CommandSwerveDrivetrainChoreo)(drivetrain), deployer, loader, shooter, intake, climber);

      autoChooserChoreo.addRoutine("Do Nothing", autoRoutinesChoreo::NothingPath);
      // autoChooserChoreo.addRoutine("Drive Two Feet", autoRoutinesChoreo::DriveTwoFeet);
      // autoChooserChoreo.addRoutine("Drive Left Two Feet", autoRoutinesChoreo::DriveTwoFeetLeft);
      // autoChooserChoreo.addRoutine("Drive Two Feet in Both Directions", autoRoutinesChoreo::DriveTwoFeetBothDirections);
      autoChooserChoreo.addRoutine("Shoot Initial Fuel Left Side", autoRoutinesChoreo::ShootInitialFuelLeft);
      autoChooserChoreo.addRoutine("Shoot Initial Fuel Right Side", autoRoutinesChoreo::ShootInitialFuelRight);
      // autoChooserChoreo.addRoutine("Shoot Init, Get Human Player - Center", autoRoutinesChoreo::DumpHumanPlayerCenter);
      //autoChooserChoreo.addRoutine("Center Shoot Depot", autoRoutinesChoreo::CenterShootDepot); // uncomment when we use the subsystems.
      
      SmartDashboard.putData("Choreo Auto", autoChooserChoreo);
    }

    if(drivetrain instanceof CommandSwerveDrivetrainPathPlanner) {
      autoChooserPathPlanner = AutoBuilder.buildAutoChooser(DEFAULT_PATHPLANNER_AUTO);
      SmartDashboard.putData("PathPlanner Auto",autoChooserPathPlanner);
    }

    configureBindings();
  }

  // Define triggers and their respective commands
  private void configureBindings() {
    controller.b().onTrue(new InstantCommand(() -> drivingRobotCentric.set(!drivingRobotCentric.get())));
    controller.leftBumper().onTrue(new InstantCommand(() -> {drivetrain.seedFieldCentric();}));
    controller.x().whileTrue(drivetrain.applyRequest(() -> brake));

    drivetrain.setDefaultCommand(
      // Drivetrain will execute this command periodically
      new DefaultDrivetrainCommand(drivetrain, controller, targetingAprilTag, drivingRobotCentric)
    );

    JoystickButton toggleShootingPowerButton = new JoystickButton(thrustmaster, Constants.OperatorInterfaceConstants.TOGGLE_SHOOTER_POWER_BUTTON);
    // JoystickButton toggleShootingDefinitionButton = new JoystickButton(thrustmaster, Constants.OperatorInterfaceConstants.TOGGLE_SHOOTING_MODE_BUTTON);

    shooter.setDefaultCommand(shooter.runOnce(() -> {
      shooter.stop();
      SmartDashboard.putBoolean("Shooter Enabled", false);
    }));

    Command shootWithAxis = new Command() {
      public void initialize() {
        SmartDashboard.putBoolean("Shooter Enabled", true);
      }

      public void execute() {
        shooter.runVoltage(Shooter.RECOMMENDED_OUTPUT_VOLTAGE * Shooter.getPowerFromAxis(thrustmaster.getRawAxis(Constants.OperatorInterfaceConstants.SHOOTER_POWER_AXIS)));
      }

      public void end(boolean interrupted) {
        shooter.stop();
      }
    };
    
    shootWithAxis.addRequirements(shooter);

    toggleShootingPowerButton.toggleOnTrue(shootWithAxis);

    // Command shootWithLimelight = new Command() {
    //   public void execute() {
    //     shooter.runRotationalVelocity(Shooter.getScaleFromDistance(Constants.HardwareIDConstants.SHOOTER_LIMELIGHT_NAME));
    //   }

    //   public void end(boolean interrupted) {
    //     shooter.stop();
    //   }
    // };

    // shootWithLimelight.addRequirements(shooter);

    // toggleShootingDefinitionButton.toggleOnTrue(shootWithLimelight);

    JoystickButton toggleLoaderButton = new JoystickButton(thrustmaster, Constants.OperatorInterfaceConstants.SWITCH_CHANNELING_MODE_BUTTON);
    JoystickButton reverseLoaderButton = new JoystickButton(thrustmaster, Constants.OperatorInterfaceConstants.REVERSE_CHANNELING_BUTTON);

    Command toggleLoader = new Command() {
      public void execute() {
        loader.load();
      }

      public void end(boolean interrupted) {
        loader.stop();
      }
    };

    toggleLoader.addRequirements(loader);

    toggleLoaderButton.toggleOnTrue(toggleLoader);

    reverseLoaderButton.onTrue(
      Commands.sequence(
      new InstantCommand(() -> toggleLoader.cancel()),
      new InstantCommand(() -> loader.deload())
      )
    ).onFalse(
      new InstantCommand(() -> loader.stop())
    );

    JoystickButton toggleIntakeButton = new JoystickButton(thrustmaster, Constants.OperatorInterfaceConstants.TOGGLE_INTAKE_BUTTON);
    JoystickButton runIntakeReverseButton = new JoystickButton(thrustmaster, Constants.OperatorInterfaceConstants.OUTTAKE_BUTTON);

    Command toggleIntake = new Command() {
        public void execute() {intake.intake();}

        public void end(boolean interrupted) {intake.stop();}
    };
    
    toggleIntake.addRequirements(intake);

    toggleIntakeButton.toggleOnTrue(toggleIntake);

    runIntakeReverseButton.onTrue(
      Commands.sequence(
        new InstantCommand(() -> toggleIntake.cancel()),
        new InstantCommand(() -> intake.outtake()))
      ).onFalse(new InstantCommand(() -> {
      intake.stop();
    }));
    
    JoystickButton deployButton = new JoystickButton(thrustmaster, Constants.OperatorInterfaceConstants.DEPLOY_BUTTON);
    JoystickButton retractDeployerButton = new JoystickButton(thrustmaster, Constants.OperatorInterfaceConstants.RETRACT_DEPLOYER_BUTTON);

    Command deploy = deployer.runDeployerForwardCommand();

    deployButton.onTrue(deploy).onFalse(
      new InstantCommand(() -> deploy.cancel()));

    Command retract = deployer.runDeployerBackwardCommand();

    retractDeployerButton.onTrue(retract).onFalse(
      new InstantCommand(() -> retract.cancel()));

    climber.setDefaultCommand(new RunCommand(() -> climber.stop(), climber));

    JoystickButton extendButton = new JoystickButton(thrustmaster, Constants.OperatorInterfaceConstants.EXTEND_BUTTON);
    JoystickButton retractButton = new JoystickButton(thrustmaster, Constants.OperatorInterfaceConstants.RETRACT_BUTTON);

    Command extend = new Command() {
      public void initialize() {
        climber.extend();
      }

      public void end(boolean interrupted) {
        climber.stop();
      }
    };

    extend.addRequirements(climber);

    Command retractClimber = new Command() {
      public void initialize() {
        climber.retract();
      }

      public void end(boolean interrupted) {
        climber.stop();
      }
    };

    retractClimber.addRequirements(climber);

    extendButton.whileTrue(extend);
    retractButton.whileTrue(retractClimber);
    
    // extendButton.onTrue(climber.ExtendCommand());
    // retractButton.onTrue(climber.RetractCommand());


  }

  public Command getAutonomousCommand() {
    if(autoChooserChoreo!=null) {
      return autoChooserChoreo.selectedCommand();
    } else if(autoChooserPathPlanner!=null) {
      return autoChooserPathPlanner.getSelected();
    } else {
      return new InstantCommand();
    }
  }
}
