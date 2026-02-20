package frc.robot;

import frc.robot.generated.PathPlanningType;

public final class Constants {

  // Default stuff for ports of controllers
  // FIXME: Talk about adding individual button mappings to here as well
  public static class OperatorInterfaceConstants {
    public static final int driverControllerPort = 0;

    public static final int SWITCH_CHANNELING_MODE_BUTTON = -1;
    public static final int REVERSE_CHANNELING_BUTTON = -1;
  }

  // Needs to be updated for CANbus IDs
  // FIXME: Get actual IDs for each hardware component
  public static class HardwareIDConstants {
    public static final int LOADING_MOTOR_ID = -1;

    public static final int FEEDING_MOTOR_ID = -1;

    public static final int DEPLOYING_MOTOR_CAN_ID = -1;
    public static final int FRONT_DEPLOYER_SWITCH_CHANNEL = -1;
    public static final int BACK_DEPLOYER_SWITCH_CHANNEL = -1;

    public static final int INTAKE_MOTOR_ID = -1;
    
    public static final int LEFT_SHOOTER_MOTOR_ID = -1;
    public static final int RIGHT_SHOOTER_MOTOR_ID = -1;
  }
  
  // Constants for stuff like using stubs and pathplanning methods
  public static class BehaviorConstants {
    public static final boolean USE_STUBS = true;

    public static final boolean GUNNER_CONTROLS_CONNECTED = true;

    public static final PathPlanningType PATH_PLANNING_TYPE = PathPlanningType.CHOREO;
    public static final boolean USE_MANUAL_AUTO_ROUTINES = false;
  }

  // Constants for usage in SmartDashboard output
  // Use for abstracting the data keys
  public static class SmartDashboardKeys {
    public static final String AUTO_MODE_KEY = "AutoMode";
  }
}