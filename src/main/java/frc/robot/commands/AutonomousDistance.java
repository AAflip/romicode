// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutonomousDistance extends SequentialCommandGroup {
  /**
   * Creates a new Autonomous Drive based on distance. This will drive out for a specified distance,
   * turn around and drive back.
   *
   * @param drivetrain The drivetrain subsystem on which this command will run
   */
  public AutonomousDistance(Drivetrain drivetrain) {
    addCommands(
        new DriveDistance(-2, 29, drivetrain),
        new TurnDegrees(-1.5,  245, drivetrain)
        /*new DriveDistance(-1.5, 7, drivetrain),
        new TurnDegrees(-1.5, 265, drivetrain),
        new DriveDistance(-1.5, 23, drivetrain),
        new TurnDegrees(-1.5, 90, drivetrain),
        new DriveDistance(-1.5, 30, drivetrain),
        new TurnDegrees(-1.5, 90, drivetrain),
        new DriveDistance(-1.5, 23, drivetrain)*/);
  }
}
