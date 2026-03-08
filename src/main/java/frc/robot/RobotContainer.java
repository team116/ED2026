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
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.OperatorInterfaceConstants;
import frc.robot.autoroutines.AutoRoutinesChoreo;
import frc.robot.commands.DefaultDeployerCommand;
import frc.robot.commands.DefaultDrivetrainCommand;
import frc.robot.commands.DefaultLoaderCommand;
import frc.robot.commands.DefaultShooterCommand;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.CommandSwerveDrivetrainChoreo;
import frc.robot.subsystems.CommandSwerveDrivetrainPathPlanner;
import frc.robot.subsystems.Deployer;
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

  public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
// Uncomment the following lines when we can confirm connectivity to each of the subsystems
  public final Shooter shooter = new Shooter();
  public final Loader loader = new Loader();
  // public final Intake intake = new Intake();
  public final Deployer deployer = new Deployer();

  private SendableChooser<Command> autoChooserPathPlanner;
  private AutoChooser autoChooserChoreo;
  public AutoRoutinesChoreo autoRoutinesChoreo;

  private final CommandXboxController controller = new CommandXboxController(OperatorInterfaceConstants.driverControllerPort);
  private final Joystick thrustmaster = new Joystick(OperatorInterfaceConstants.thrustmasterPort);
  private final Joystick gunnerPad = null;// new Joystick(OperatorInterfaceConstants.gunnerPadPort);
  // FIXME: Uncomment whenever we can confirm connectivity

  public RobotContainer() {
    // LimelightHelpers.SetFiducialIDFiltersOverride(Constants.HardwareIDConstants.SHOOTER_LIMELIGHT_NAME,Constants.getGoodIdsForShooter());

    if(drivetrain instanceof CommandSwerveDrivetrainChoreo) {
      autoChooserChoreo = new AutoChooser();
      autoRoutinesChoreo = new AutoRoutinesChoreo((CommandSwerveDrivetrainChoreo)(drivetrain), null, loader, null, null);

      autoChooserChoreo.addRoutine("Do Nothing", autoRoutinesChoreo::NothingPath);
      // autoChooserChoreo.addRoutine("Drive Two Feet", autoRoutinesChoreo::DriveTwoFeet);
      // autoChooserChoreo.addRoutine("Drive Left Two Feet", autoRoutinesChoreo::DriveTwoFeetLeft);
      // autoChooserChoreo.addRoutine("Drive Two Feet in Both Directions", autoRoutinesChoreo::DriveTwoFeetBothDirections);
      // autoChooserChoreo.addRoutine("Shoot Initial Fuel", autoRoutinesChoreo::ShootInitialFuel);
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

    shooter.setDefaultCommand(new DefaultShooterCommand(shooter, thrustmaster, gunnerPad));
    loader.setDefaultCommand(new DefaultLoaderCommand(loader, thrustmaster, gunnerPad));
    //intake.setDefaultCommand(new DefaultIntakeCommand(intake, thrustmaster, gunnerPad));
    deployer.setDefaultCommand(new DefaultDeployerCommand(deployer, thrustmaster, gunnerPad));
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
