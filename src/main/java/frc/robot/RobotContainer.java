// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.*;
import frc.robot.Constants.OperatorInterfaceConstants;
import frc.robot.commands.Autos;
import edu.wpi.first.wpilibj2.command.*;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;

import choreo.auto.AutoChooser;
import frc.robot.commands.ExampleCommand;
import frc.robot.generated.TunerConstants;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.*;
import frc.robot.autoroutines.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private double MaxSpeed = (TunerConstants.kSpeedAt12Volts.in(MetersPerSecond)) / 2.0d; // kSpeedAt12Volts desired top speed
  private double MaxAngularRate = (RotationsPerSecond.of(0.75).in(RadiansPerSecond)) / 2.0d; // 3/4 of a rotation per second max angular velocity


  private final String DEFAULT_PATHPLANNER_AUTO = "default";

  public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain(); //FIXME: Update for actual drivetrain (2026 drivetrain)

  private SendableChooser<Command> autoChooserPathPlanner;
  private AutoChooser autoChooserChoreo;
  public AutoRoutinesChoreo autoRoutinesChoreo;

  private final CommandXboxController controller = new CommandXboxController(OperatorInterfaceConstants.driverControllerPort);
  // private final Joystick thrustmaster = new Joystick(OperatorInterfaceConstants.thrustmasterPort);
  // private final Joystick gunnerPad = new Joystick(OperatorInterfaceConstants.gunnerPadPort);
  // FIXME: Uncomment whenever we can confirm connectivity

  private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
      .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors


  public RobotContainer() {

    if(drivetrain instanceof CommandSwerveDrivetrainChoreo) {
      autoChooserChoreo = new AutoChooser();
      autoRoutinesChoreo = new AutoRoutinesChoreo((CommandSwerveDrivetrainChoreo)(drivetrain));

      autoChooserChoreo.addRoutine("Do Nothing", autoRoutinesChoreo::NothingPath);
      autoChooserChoreo.addRoutine("Drive Two Feet", autoRoutinesChoreo::DriveTwoFeet);
      autoChooserChoreo.addRoutine("Drive Left Two Feet", autoRoutinesChoreo::DriveTwoFeetLeft);
      autoChooserChoreo.addRoutine("Drive Two Feet in Both Directions", autoRoutinesChoreo::DriveTwoFeetBothDirections);
      
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

    drivetrain.setDefaultCommand(
      // Drivetrain will execute this command periodically
      Commands.sequence(
        Commands.runOnce(() -> SmartDashboard.putString("Drive Mode", "Field Centric")),
        drivetrain.applyRequest(() ->
          drive.withVelocityX(shape(-controller.getLeftY()) * MaxSpeed) // Drive forward with negative Y (forward)
            .withVelocityY(shape(-controller.getLeftX()) * MaxSpeed) // Drive left with negative X (left)
              .withRotationalRate(shapeRotation(-controller.getRightX()) * MaxAngularRate) // Drive counterclockwise with negative X (left)
        )
      )
    );
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

  // These are here as a holdover from last year, can be used later on, need to update for curbing
  public double shape(double initial) {
    return initial * Math.abs(initial);
  }

  public double shapeRotation(double initial) {
    return initial * Math.abs(initial);
  }
}
