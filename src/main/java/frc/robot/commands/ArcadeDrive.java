// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import java.util.function.Supplier;
import edu.wpi.first.wpilibj.Joystick;
public class ArcadeDrive extends CommandBase {
  private final Drivetrain m_drivetrain;
  private final Supplier<Double> m_xaxisSpeedSupplier;
  private final Supplier<Double> m_zaxisRotateSupplier;
  private final Joystick m_controller = new Joystick(0);
  /**
   * Creates a new ArcadeDrive. This command will drive your robot according to the speed supplier
   * lambdas. This command does not terminate.
   *
   * @param drivetrain The drivetrain subsystem on which this command will run
   * @param xaxisSpeedSupplier Lambda supplier of forward/backward speed
   * @param zaxisRotateSupplier Lambda supplier of rotational speed
   */
  public ArcadeDrive(
      Drivetrain drivetrain,
      Supplier<Double> xaxisSpeedSupplier,
      Supplier<Double> zaxisRotateSupplier) {
    m_drivetrain = drivetrain;
    m_xaxisSpeedSupplier = xaxisSpeedSupplier;
    m_zaxisRotateSupplier = zaxisRotateSupplier;
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}
  int timer;
  int wiggle = -1;
  int wiggleTime;
  int autoTimer;
  // Called every time the scheduler runs while the command is scheduled.
  static double trunc(double number, int place){
    number = number * Math.pow(10, place); 
    number = Math.floor(number); 
    number = number / Math.pow(10, place); 
    return number;
  }
  @Override
  public void execute() {
    double gas = -m_controller.getRawAxis(5)+1;
    gas = gas>1 ? gas*2 : 1;
    
    double steer = trunc(m_controller.getRawAxis(0),1);
    if (Math.abs(steer) < 0.1) steer = trunc(m_controller.getRawAxis(3),1)*2-trunc(m_controller.getRawAxis(2),1)*2;
    if(m_controller.getRawButton(6)) steer = 1;
    if(m_controller.getRawButton(5)) steer = -1;
    if(m_controller.getRawButton(3)){
      timer = 32;
    }else if(timer > 0 && !m_controller.getRawButton(3)){
      steer = 1; 
      timer--;
    }
    double move = trunc(m_controller.getRawAxis(1), 1);
    if(m_controller.getRawButton(1) && !m_controller.getRawButton(8)){
      autoTimer = 800000000;
    }else if(autoTimer > 0 && !m_controller.getRawButton(1) && !m_controller.getRawButton(8)){
      autoTimer--;
      if(Math.random() > 0.6){
        steer = Math.random();
        move = 0;
      }else{
        move= Math.random()>0.5 ? 1 : -1; //originally 0.6
      }
    }else if(m_controller.getRawButton(8)){
      autoTimer = 0;
    }
    if(m_controller.getRawButton(4)){
      wiggleTime++;
      wiggle = wiggleTime%2;
      steer = wiggle == 1 ? 1:-1;
    }
    int brake = m_controller.getRawButton(2) ? 0 : 1;
    m_drivetrain.arcadeDrive(move*brake*gas, -steer*brake); //original (Math.round(m_zaxisRotateSupplier.get()*100)/100)*1.8, -(Math.round(m_xaxisSpeedSupplier.get()*100)/100)
    if(m_controller.getRawButton(3)){
      new AutonomousDistance(m_drivetrain);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
