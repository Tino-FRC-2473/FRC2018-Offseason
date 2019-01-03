/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2473.robot;

import java.util.ArrayList;

import org.usfirst.frc.team2473.framework.Devices;
import org.usfirst.frc.team2473.robot.commands.AutonomousTester;
import org.usfirst.frc.team2473.robot.commands.PointTurn;
import org.usfirst.frc.team2473.robot.commands.TeleopDrive;
import org.usfirst.frc.team2473.robot.commands.StallExperiment;
import org.usfirst.frc.team2473.robot.commands.StraightDrive;
import org.usfirst.frc.team2473.robot.subsystems.DriveSubsystem;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
	
	public static DriveSubsystem driveSubsystem = DriveSubsystem.getInstance();
		
	public static OI oi;
	

	Preferences prefs;


	@Override
	public void robotInit() {
		oi = new OI();
		
		prefs = Preferences.getInstance();
		
		UsbCamera cubeCam = CameraServer.getInstance().startAutomaticCapture("Cube View", 0);
		cubeCam.setBrightness(75);
		cubeCam.setResolution(640, 480);
		UsbCamera driveCam = CameraServer.getInstance().startAutomaticCapture("Front View", 1);
		driveCam.setBrightness(75);
		driveCam.setResolution(640, 480);
		
		
		Devices.getInstance().getNavXGyro().reset();
	}
	

	@Override
	public void disabledInit() {
		System.out.println("AFTER DISABLED: " + Devices.getInstance().getNavXGyro().getAngle());
		Scheduler.getInstance().removeAll();
		
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		driveSubsystem.resetEncoders();
//	
		int distanceFirst  = prefs.getInt("1. First Distance", 48);
		int degrees  = prefs.getInt("2. Turn Degrees", 180);
		int distanceSecond  = prefs.getInt("3. Second Distance", 48);
				
		AutonomousTester tester = new AutonomousTester();
		tester.addDriveTurnDrive(distanceFirst, degrees, distanceSecond);
		tester.start();

	}

	@Override
	public void autonomousPeriodic() {		
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		(new TeleopDrive()).start();
	}


	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
	}
	
}