package frc.robot.autoroutines;

import java.util.concurrent.atomic.AtomicBoolean;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import frc.robot.subsystems.*;

public class AutoRoutinesChoreo {
    private final AutoFactory autoFactory;

    // Add more subsystems here as needed, we start with the base drivetrain, but will likely need more
    public AutoRoutinesChoreo(CommandSwerveDrivetrainChoreo drivetrain) {
        autoFactory = drivetrain.createAutoFactory();
    }


    // A simple base for doing nothing in an autonomous 
    // NOTE: the routine name is what is shown to the chooser, the trajectory name has to be the name of the file in 
    public AutoRoutine NothingPath() {
        final AutoRoutine routine = autoFactory.newRoutine("Do Nothing");

        return routine;
    }

    public AutoRoutine DriveTwoFeet() {
        final AutoRoutine routine = autoFactory.newRoutine("Drive Two Feet");
        final AutoTrajectory traj = routine.trajectory("DriveTwoFeet");

        routine.active().onTrue(
            new SequentialCommandGroup(
                new InstantCommand(() -> SmartDashboard.putString("Event", "Starting to Drive Left Two Feet")),
                traj.cmd()
            )
        );

        return routine;
    }

    public AutoRoutine DriveTwoFeetLeft() {
        final AutoRoutine routine = autoFactory.newRoutine("Drive Left Two Feet");
        final AutoTrajectory traj = routine.trajectory("DriveTwoFeetLeft");

        routine.active().onTrue(
            new SequentialCommandGroup(
                new InstantCommand(() -> SmartDashboard.putString("Event", "Starting to Drive Left Two Feet")),
                traj.cmd()
            )
        );

        return routine;
    }

    public AutoRoutine DriveTwoFeetBothDirections() {
        final AutoRoutine routine = autoFactory.newRoutine("Drive Two Feet In Both Directions");
        final AutoTrajectory traj = routine.trajectory("DriveTwoFeetBoth");
        
        routine.active().onTrue(
            new SequentialCommandGroup(
                new InstantCommand(() -> SmartDashboard.putString("Event", "Starting to Drive Left Two Feet")),
                traj.cmd()
            )
        );

        return routine;
    }
}