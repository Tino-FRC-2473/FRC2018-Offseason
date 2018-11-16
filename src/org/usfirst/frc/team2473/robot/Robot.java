/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2473.robot;

import org.usfirst.frc.team2473.framework.Devices;
import org.usfirst.frc.team2473.robot.commands.PointTurn;
import org.usfirst.frc.team2473.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
	
	// Subsystems
	public static DriveSubsystem driveSubsystem = DriveSubsystem.getInstance();
		
	public static OI oi;

	Command m_autonomousCommand;
	SendableChooser<Command> m_chooser = new SendableChooser<>();
	SendableChooser<Double> powerChooser = new SendableChooser<>();
	SendableChooser<Integer> distanceChooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		// m_chooser.addDefault("Default Auto", new DriveForDistanceCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		
		powerChooser.addDefault("0.1", 0.1);
		powerChooser.addObject("0.2", 0.2);
		powerChooser.addObject("0.3", 0.3);
		powerChooser.addObject("0.4", 0.4);
		powerChooser.addObject("0.5", 0.5);
		powerChooser.addObject("0.6", 0.6);
		powerChooser.addObject("0.7", 0.7);
		powerChooser.addObject("0.8", 0.8);
		powerChooser.addObject("0.9", 0.9);
		powerChooser.addObject("1.0", 1.0);
		
		distanceChooser.addDefault("60 inches", 60);
		distanceChooser.addObject("120 inches", 120);
		distanceChooser.addObject("180 inches", 180);
		distanceChooser.addObject("210 inches", 210);

		
		SmartDashboard.putData("Power", powerChooser);
		SmartDashboard.putData("Distance", distanceChooser);
		
		Devices.getInstance().getNavXGyro().reset();
	}
	

	@Override
	public void disabledInit() {
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		//double power = powerChooser.getSelected();
		//int distance = distanceChooser.getSelected();
		driveSubsystem.resetEncoders();
		//new DriveForDistanceCommand(distance, new DrivePower(-power)).start();
		new PointTurn(270, 0.3).start();

		
	}


	@Override
	public void autonomousPeriodic() {		
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		
	}


	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
	}
	
}
