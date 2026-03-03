package frc.robot.commands;

import static edu.wpi.first.units.Units.*;

import java.util.concurrent.atomic.AtomicBoolean;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.*;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class DefaultDrivetrainCommand extends Command {
    private final static double BLUE_ALIGNING_TAG_ID = 26;
    private final static double RED_ALIGNING_TAG_ID = 10;

    private AtomicBoolean targetingAprilTag = new AtomicBoolean(false);

    private final static double kP_YAW = 1; // FIXME: Tune
    private final static double LIMELIGHT_FOV = Math.toRadians(62.5); // in degrees

    private double MaxSpeed = (TunerConstants.kSpeedAt12Volts.in(MetersPerSecond)) / 2.0d; // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = (RotationsPerSecond.of(0.75).in(RadiansPerSecond)) / 2.0d; // 3/4 of a rotation per second max angular velocity

    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
      .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    
    private final CommandSwerveDrivetrain drivetrain;
    private final CommandXboxController controller;

    public DefaultDrivetrainCommand(CommandSwerveDrivetrain drivetrain, CommandXboxController controller) {
        this.drivetrain = drivetrain;
        this.controller = controller;
        controller.a().onChange(new InstantCommand(() -> targetingAprilTag.set(!targetingAprilTag.get())));

        super.addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        if(!targetingAprilTag.get()) {
            drivetrain.applyRequest(() -> 
                drive.withVelocityX(shape(-controller.getLeftY()) * MaxSpeed)
                    .withVelocityY(shape(-controller.getLeftX()) * MaxSpeed)
                    .withRotationalRate(shapeRotation(-controller.getRightX()) * MaxAngularRate)
            );
        } else {
            drivetrain.applyRequest(() ->
                drive.withVelocityX(shape(-controller.getLeftY()) * MaxSpeed)
                    .withVelocityY(shape(-controller.getLeftX()) * MaxSpeed)
                    .withRotationalRate(shapeRotation(getAngularOffset(Constants.HardwareIDConstants.SHOOTER_LIMELIGHT_NAME)) * MaxAngularRate)
            );
        }
    }

    // Scalar -1 -> 1 from apriltags
    // FIXME: Ensure that this is actually scalar to that range
    public static double getAngularOffset(String limelightName) {
        LimelightTarget_Fiducial[] results = LimelightHelpers.getLatestResults(limelightName).targets_Fiducials;
        LimelightTarget_Fiducial target = null;

        for(int i = 0; i < results.length; i++) {
            if(results[i].fiducialID==BLUE_ALIGNING_TAG_ID || results[i].fiducialID==RED_ALIGNING_TAG_ID) {
                target = results[i];
                break;
            }
        }

        if(target==null) {
            return 0;
        }

        Rotation3d pose = target.getCameraPose_TargetSpace().getRotation();

        double yawOffset = -pose.getZ(); // gets the yaw

        yawOffset = yawOffset/(LIMELIGHT_FOV/2); // normalizes yaw to be on a scale of -1 to 1

        return yawOffset * kP_YAW;
    }

    public static double shape(double initial) {
        return initial * Math.abs(initial);
    }

    public static double shapeRotation(double initial) {
        return initial * Math.abs(initial);
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.setControl(new SwerveRequest.Idle()); // Idle out our motors if we want to stop the command (for safety's sake)
    }
}
