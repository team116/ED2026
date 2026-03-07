package frc.robot.autoroutines;

import java.util.concurrent.atomic.AtomicBoolean;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.RunDeployer;
import frc.robot.commands.RunShooter;
import frc.robot.subsystems.*;

public class AutoRoutinesChoreo {
    private final AutoFactory autoFactory;

    private final Intake intake;
    private final Shooter shooter;
    private final Loader loader;
    private final Deployer deployer;

    private AtomicBoolean b1 = new AtomicBoolean(false);
    private Trigger done1 = new Trigger(() -> b1.get());
    private AtomicBoolean b2 = new AtomicBoolean(false);
    private Trigger done2 = new Trigger(() -> b2.get());

    // Add more subsystems here as needed, we start with the base drivetrain, but will likely need more
    public AutoRoutinesChoreo(CommandSwerveDrivetrainChoreo drivetrain, Deployer deployer, Loader loader, Shooter shooter, Intake intake) {
        autoFactory = drivetrain.createAutoFactory();
        this.deployer = deployer;
        this.loader = loader;
        this.shooter = shooter;
        this.intake = intake;
    }

    // This really shouldn't be used, it's just here until we actually add the subsystems to RobotContainer.java and can confirm that they work.
    public AutoRoutinesChoreo(CommandSwerveDrivetrainChoreo drivetrain) {
        this(drivetrain, null, null, null, null);
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

    public AutoRoutine CenterShootDepot() {
        final AutoRoutine routine = autoFactory.newRoutine("Center Shoot Depot");
        final AutoTrajectory immediateMotion = routine.trajectory("CenterShootDepot1");
        final AutoTrajectory intakeFuel = routine.trajectory("CenterShootDepot2");
        final AutoTrajectory getInPosition = routine.trajectory("CenterShootDepot3");

        routine.active().onTrue(
            Commands.sequence(
                new InstantCommand(() -> SmartDashboard.putString("Event", "Shooting initial fuel")),
                new InstantCommand(() -> loader.run(Loader.RECOMMENDED_LOADER_SPEED)),
                new RunShooter(shooter).withTimeout(3),
                new InstantCommand(() -> loader.stop()),
                new InstantCommand(() -> SmartDashboard.putString("Event", "Starting CenterShootDepot1 - ImmediateMotion")),
                immediateMotion.cmd()
            )
        );

        immediateMotion.done().onTrue(
            Commands.sequence(
                new InstantCommand(() -> SmartDashboard.putString("Event", "Deploying intake")),
                new RunDeployer(deployer, true),
                new InstantCommand(() -> b1.set(true))
            )
        );

        routine.observe(done1).onTrue(
            Commands.sequence(
                new InstantCommand(() -> SmartDashboard.putString("Event", "Starting CenterShootDepot2 - IntakeFuel")),
                new InstantCommand(() -> intake.run(Intake.RECOMMENDED_INTAKE_SPEED)),
                intakeFuel.cmd()
            )
        );

        intakeFuel.done().onTrue(
            Commands.sequence(
                new InstantCommand(() -> SmartDashboard.putString("Event", "Undeploying intake")),
                new InstantCommand(() -> intake.stop()),
                new RunDeployer(deployer, false),
                new InstantCommand(() -> b2.set(true))
            )
        );

        routine.observe(done2).onTrue(
            Commands.sequence(
                new InstantCommand(() -> SmartDashboard.putString("Event", "Starting CenterShootDepot3 - GetInPosition")),
                getInPosition.cmd()
            )
        );

        getInPosition.done().onTrue(
            Commands.sequence(
                new InstantCommand(() -> SmartDashboard.putString("Event", "Shooting final fuel")),
                new InstantCommand(() -> loader.run(Loader.RECOMMENDED_LOADER_SPEED)),
                new RunShooter(shooter).withTimeout(3),
                new InstantCommand(() -> loader.stop()),
                new InstantCommand(() -> SmartDashboard.putString("Event", "Finished"))
            )
        );

        return autoFactory.newRoutine("Unusable"); // return routine;
    }

    public AutoRoutine ShootInitialFuel() {
        final AutoRoutine routine = autoFactory.newRoutine("Shoot Initial Fuel");
        routine.active().onTrue(
            new InstantCommand(() -> {
                loader.run(Loader.RECOMMENDED_LOADER_SPEED);
                shooter.run(Shooter.RECOMMENDED_SHOOTING_SPEED);
            })
        );
        
        return routine;
    }

    public AutoRoutine DumpThenGetHumanPlayer() {
        final AutoRoutine routine = autoFactory.newRoutine("Dump and then get more fuel from Human Player");
        final AutoTrajectory traj1 = routine.trajectory("DumpHumanPlayer1");
        final AutoTrajectory traj2 = routine.trajectory("DumpHumanPlayer2");

        routine.active().onTrue(
            Commands.sequence(
                new InstantCommand(() -> {
                    SmartDashboard.putString("Event", "Dumping Fuel");
                    loader.run(Loader.RECOMMENDED_LOADER_SPEED);
                }), 
                new RunShooter(shooter).withTimeout(3),
                new InstantCommand(() -> {
                    loader.stop();
                    b1.set(true);
                })
            )
        );

        routine.observe(done1).onTrue(
            Commands.sequence(
                new InstantCommand(() -> SmartDashboard.putString("Event", "Starting DumpHumanPlayer1 - Moving to position for fuel")),
                traj1.cmd()
            )
        );

        traj1.done().onTrue(
            Commands.sequence(
                new InstantCommand(() -> SmartDashboard.putString("Event", "Waiting for fuel...")),
                new WaitCommand(5),
                new InstantCommand(() -> b2.set(true))
            )
        );

        routine.observe(done2).onTrue(
            Commands.sequence(
                new InstantCommand(() -> SmartDashboard.putString("Event", "Starting DumpHumanPlayer2 - Moving to position to shoot")),
                traj2.cmd()
            )
        );

        traj2.done().onTrue(
            Commands.sequence(
                new InstantCommand(() -> {
                    SmartDashboard.putString("Event", "Firing...");
                    loader.run(Loader.RECOMMENDED_LOADER_SPEED);
            }),
                new RunShooter(shooter).withTimeout(3),
                new InstantCommand(() -> {
                    SmartDashboard.putString("Event", "Finished with DumpHumanPlayer");
                    loader.stop();
                })
            )
        );

        return routine;
    }
}