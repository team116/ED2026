package frc.robot;

import frc.robot.generated.PathPlanningType;

import edu.wpi.first.math.geometry.*;

public final class Constants {

  // Default stuff for ports of controllers
  // FIXME: Talk about adding individual button mappings to here as well
  // Button mappings for reference:

  // 4 on top of thrustmaster: 0-3 (priority 0 for controls, these should be the most common, like toggling shooting or intaking)
  // 6 left of thrustmaster: 4-9 (priority 1 for controls, these should be useful, but not too specific, like reversing systems)
  // 6 right of thrustmaster: 10-15 (priority 2 for controls, these should almost never be used, as they are super uncomfortable for the gunner to hit)

  // Get individual mappings for commands on the gunnerPad, they're a little too specific to list
  // For their mappings, they should be mapped to common commands. Something like an alignment command or similar
  public static class OperatorInterfaceConstants {
    public static final int driverControllerPort = 0;
    public static final int thrustmasterPort = 1;
    public static final int gunnerPadPort = 2;

    public static final int SWITCH_CHANNELING_MODE_BUTTON = 8;
    public static final int REVERSE_CHANNELING_BUTTON = 1;

    public static final int SWITCH_DEPLOYING_MODE_BUTTON = 2;

    public static final int SHOOTER_POWER_AXIS = 3;

    public static final int OUTTAKE_BUTTON = 3;
    public static final int TOGGLE_INTAKE_BUTTON = 7;

    public static final int TOGGLE_SHOOTING_MODE_BUTTON = 0;
  }

  // Needs to be updated for CANbus IDs
  // FIXME: Get actual IDs for each hardware component
  public static class HardwareIDConstants {
    public static final int LOADER_MOTOR_ID = 54;

    // public static final int DEPLOYING_MOTOR_CAN_ID = -1;
    // public static final int FRONT_DEPLOYER_SWITCH_CHANNEL = -1;
    // public static final int BACK_DEPLOYER_SWITCH_CHANNEL = -1;
    public static final int DEPLOYER_MOTOR_ID = 61;

    public static final int INTAKE_MOTOR_CAN_ID = 60;
    
    public static final int LEFT_SHOOTER_MOTOR_ID = 55;
    public static final int RIGHT_SHOOTER_MOTOR_ID = 56;

    // 15 & 31 are the middle april tags
    // 16 & 32 are the right-based april tags

    public static final String SHOOTER_LIMELIGHT_NAME = "limelight-shooter";
    // Current static IP Address is 10.1.16.11
    // Current hostname is http://limelight-shooter.local:5801
    public static final String CLIMBER_LIMELIGHT_NAME = "limelight-climber"; // FIXME: Get actual names for each of the limelights
  }
  
  // Constants for stuff like using stubs and pathplanning methods
  public static class BehaviorConstants {
    public static final boolean USE_STUBS = true;

    public static final boolean GUNNER_CONTROLS_CONNECTED = true;

    public static final PathPlanningType PATH_PLANNING_TYPE = PathPlanningType.CHOREO;
    public static final boolean USE_MANUAL_AUTO_ROUTINES = false;

    public static final boolean TESTING_HARDWARE = true;
  }

  // Constants for usage in SmartDashboard output
  // Use for abstracting the data keys
  public static class SmartDashboardKeys {
    public static final String AUTO_MODE_KEY = "AutoMode";
  }

  public static int[] getGoodIdsForShooter() {
    int[] list = new int[2];
    list[0] = 10; // Red Hub Center
    list[1] = 26; // Blue Hub Center
    return list;
  }

  public static int[] getGoodIdsForClimber() {
    int[] list = new int[2];
    list[0] = 15; // Red Climber Center
    list[1] = 31; // Blue Climber Center
    return list;
  }

  public static String getCoordinatesFromPose(Pose3d pose) {
    Rotation3d rotate = pose.getRotation();
    return ("Translation: (" + pose.getX() + ", " + pose.getY() + ", " + pose.getZ() + "), Rotation: (" + rotate.getX() + ", " + rotate.getY() + ", " + rotate.getZ() + ")");
  }

  public static double getVelocityFromDistance(double horizontalDist) {
    horizontalDist += 0.6096; // add 2 feet to our distance
    final double g = 9.806625; // gravity in meters per second squared
    final double dY = 2; // 2 meters of vertical movement
    final double theta = 72; // our degree from the horizontal

    return Math.sqrt(g * Math.pow(horizontalDist, 2) / (2 * Math.pow(Math.cos(theta), 2) * (horizontalDist * Math.tan(theta) - dY)));
  }

  public static double getRotationalVelocityFromVelocity(double velocity) {
    final double radius = 0.0508; // radius of the flywheel in meters
    
    return (velocity / radius) * (30 / Math.PI); // funny math
  }
}