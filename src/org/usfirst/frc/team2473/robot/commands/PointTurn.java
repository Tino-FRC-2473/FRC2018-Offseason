/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2473.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team2473.framework.Devices;
import org.usfirst.frc.team2473.robot.Robot;
import org.usfirst.frc.team2473.robot.RobotMap;
import org.usfirst.frc.team2473.robot.subsystems.DrivePower;


public class PointTurn extends Command {
	
	private static double SLOW_POWER = 0.1;
	private DrivePower slowDrivePower;
	
	private DrivePower power;
	
	private double prevDegrees;
	private double degreesGoal;
	
	public PointTurn(double degrees, double power) {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.driveSubsystem);
		
		power = Math.abs(power);
		
		this.power = degrees < 0 ? new DrivePower(-power, power, -power, power) :
			new DrivePower(power, -power, power, -power);
		
		prevDegrees = Devices.getInstance().getNavXGyro().getAngle();
		this.degreesGoal = prevDegrees + degrees;
		
		slowDrivePower = degrees < 0 ? new DrivePower(-SLOW_POWER, SLOW_POWER, -SLOW_POWER, SLOW_POWER): 
			new DrivePower(SLOW_POWER, -SLOW_POWER, SLOW_POWER, -SLOW_POWER);
		
	}
	
	public void setPower(DrivePower power) {
		this.power = power;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		Robot.driveSubsystem.drive(power);
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		DrivePower tempPower = power;
		double currDegrees = Devices.getInstance().getNavXGyro().getAngle();
		
		double delta = currDegrees - prevDegrees;
		if (degreesGoal - (currDegrees + delta) < RobotMap.K_DEGREE_THRESHOLD) {
			tempPower = slowDrivePower;
		}
		Robot.driveSubsystem.drive(tempPower);
		prevDegrees = currDegrees;
		
		
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return (Devices.getInstance().getNavXGyro().getAngle() > degreesGoal);		
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		Robot.driveSubsystem.stopMotors();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		Robot.driveSubsystem.stopMotors();
	}
}
