// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.*;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;

import com.pathplanner.lib.auto.AutoBuilder;

import choreo.auto.AutoChooser;
import frc.robot.commands.ExampleCommand;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.autoroutines.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private final String DEFAULT_PATHPLANNER_AUTO = "default";

  public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain(); //FIXME: Update for actual drivetrain (2026 drivetrain)

  private SendableChooser<Command> autoChooserPathPlanner;
  private AutoChooser autoChooserChoreo;
  public AutoRoutinesChoreo autoRoutinesChoreo;


  public RobotContainer() {

    if(drivetrain instanceof CommandSwerveDrivetrainChoreo) {
      autoChooserChoreo = new AutoChooser();
      autoRoutinesChoreo = new AutoRoutinesChoreo((CommandSwerveDrivetrainChoreo)(drivetrain));

      autoChooserChoreo.addRoutine("Do Nothing",autoRoutinesChoreo::NothingPath);
      
      SmartDashboard.putData("Choreo Auto", autoChooserChoreo);
    }

    if(drivetrain instanceof CommandSwerveDrivetrainPathPlanner) {
      autoChooserPathPlanner = AutoBuilder.buildAutoChooser(DEFAULT_PATHPLANNER_AUTO);
      SmartDashboard.putData("PathPlanner Auto",autoChooserPathPlanner);
    }

    configureBindings();
  }

  // Define triggers and their respective commands
  private void configureBindings() {}

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
