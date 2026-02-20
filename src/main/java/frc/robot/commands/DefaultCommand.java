package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;

public abstract class DefaultCommand extends Command{
    public Joystick thrustmaster;
    public Joystick gunnerPad;
}
