/*----------------------------------------------------------------------------*/
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
	
	private static double SLOW_POWER = 0.1;
	
	private double leftPower;
	private double rightPower;
	private double leftPowerSlow;
	private double rightPowerSlow;
	
	private double prevDegrees;
	private double degreesGoal;
	
	public PointTurn(double degrees, double power) {
		requires(Robot.driveSubsystem);
		
		if (power < 0) {
			throw new IllegalArgumentException("Power must be a positive scalar for point turn!");
		}
		
		this.leftPower = degrees < 0 ? -power : power;
		this.rightPower = -leftPower;
		
		this.leftPowerSlow = degrees < 0 ? -SLOW_POWER : SLOW_POWER;
		this.rightPowerSlow = -leftPowerSlow;
		
		prevDegrees = Devices.getInstance().getNavXGyro().getAngle();
		this.degreesGoal = prevDegrees + degrees;
		
	}
	
	public void setPower(double power) {
		if (power < 0) {
			throw new IllegalArgumentException("Power must be a positive scalar for point turn!");
		}
		this.leftPower = leftPower < 0 ? -power : power;
		this.rightPower = -leftPower;
		
	}

	@Override
	protected void initialize() {
		Robot.driveSubsystem.drive(leftPower, leftPower, rightPower, rightPower);
	}

	@Override
	protected void execute() {
		double currDegrees = Devices.getInstance().getNavXGyro().getAngle();
		double delta = currDegrees - prevDegrees;
		boolean slow = degreesGoal - (currDegrees + delta) < RobotMap.K_DEGREE_THRESHOLD;
		if (slow) {
			Robot.driveSubsystem.drive(leftPower, leftPower, rightPower, rightPower);
		} else {
			Robot.driveSubsystem.drive(leftPowerSlow, leftPowerSlow, rightPowerSlow, rightPowerSlow);
		}
		prevDegrees = currDegrees;
		
	}

	@Override
	protected boolean isFinished() {
		return (Devices.getInstance().getNavXGyro().getAngle() > degreesGoal);		
	}

	@Override
	protected void end() {
		Robot.driveSubsystem.stopMotors();
	}

	@Override
	protected void interrupted() {
		Robot.driveSubsystem.stopMotors();
	}
}
