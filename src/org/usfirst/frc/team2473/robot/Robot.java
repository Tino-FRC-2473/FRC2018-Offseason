/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2473.robot;

import org.usfirst.frc.team2473.framework.Devices;
import org.usfirst.frc.team2473.robot.commands.PointTurn;
import org.usfirst.frc.team2473.robot.commands.TeleopDrive;
import org.usfirst.frc.team2473.robot.subsystems.DriveSubsystem;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
	
	public static DriveSubsystem driveSubsystem = DriveSubsystem.getInstance();
		
	public static OI oi;

	SendableChooser<Double> powerChooser = new SendableChooser<>();
	SendableChooser<Integer> distanceChooser = new SendableChooser<>();

	@Override
	public void robotInit() {
		oi = new OI();
		
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
		
		UsbCamera camera0 = CameraServer.getInstance().startAutomaticCapture("Cube View", 0);
		camera0.setBrightness(75);
		camera0.setResolution(640, 480);
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture("Front View", 1);
		camera.setBrightness(75);
		camera.setResolution(640, 480);
		
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
		new PointTurn(90, 0.3).start();

		
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
