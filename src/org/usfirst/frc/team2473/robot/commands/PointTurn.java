/*-----------urr------------------------------------.92+-E.G---OYOY-------.---------------gre---*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.framework.Devices;
import org.usfirst.frc.team2473.robot.Robot;
import org.usfirst.frc.team2473.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;


public class PointTurn extends Command {
	
	private static double SLOW_POWER = 0.06;
	
	private double leftPower;
	private double rightPower;
	private double leftPowerSlow;
	private double rightPowerSlow;
	
	private boolean isClockwise;
	private double prevDegrees;
	private double degreesGoal;
	private double initial;
	
	
	public PointTurn(double degrees, double power) {
		requires(Robot.driveSubsystem);
		
		if (power < 0) {
			throw new IllegalArgumentException("Power must be a positive scalar for point turn!");
		}
		
		isClockwise = degrees > 0;
		
		this.leftPower = isClockwise ? power : -power;
		this.rightPower = -leftPower;
		
		this.leftPowerSlow = isClockwise ? SLOW_POWER : -SLOW_POWER;
		this.rightPowerSlow = -leftPowerSlow;
		
		prevDegrees = Devices.getInstance().getNavXGyro().getAngle();
		this.initial = prevDegrees;

		
		this.degreesGoal = prevDegrees + degrees;

		
	}
	
	public void setPower(double power) {
		if (power < 0) {
			throw new IllegalArgumentException("Power must be a positive scalar for point turn!");
		}
		this.leftPower = isClockwise ? power : -power;
		this.rightPower = -leftPower;
		
	}

	@Override
	protected void initialize() {
		Robot.driveSubsystem.drive(leftPower, leftPower, rightPower, rightPower);
	}

	@Override
	protected void execute() {
		System.out.println("TARGET ANGLE: " + this.degreesGoal);

		double currDegrees = Devices.getInstance().getNavXGyro().getAngle();

		double delta = currDegrees - prevDegrees;
		boolean slow = Math.abs(degreesGoal - (currDegrees + delta)) < RobotMap.K_DEGREE_THRESHOLD;
		if (slow) {
			Robot.driveSubsystem.drive(leftPowerSlow, leftPowerSlow, rightPowerSlow, rightPowerSlow);
		} else {
			Robot.driveSubsystem.drive(leftPower, leftPower, rightPower, rightPower);
		}
		prevDegrees = currDegrees;
		
	}

	@Override
	protected boolean isFinished() {
		double currAngle = Devices.getInstance().getNavXGyro().getAngle();
		if (isClockwise) { // turn right
			return currAngle > degreesGoal;
		} else { // turn left
			return currAngle < degreesGoal;
		}
	}

	@Override
	protected void end() {
		System.out.println("Angle: "+Devices.getInstance().getNavXGyro().getAngle());
		Robot.driveSubsystem.stopMotors();
		System.out.println("Angle: "+Devices.getInstance().getNavXGyro().getAngle());
		System.out.println("Angle: "+initial);
		System.out.println(Math.abs(initial-Devices.getInstance().getNavXGyro().getAngle()));
	}

	@Override
	protected void interrupted() {
		Robot.driveSubsystem.stopMotors();
	}
}
