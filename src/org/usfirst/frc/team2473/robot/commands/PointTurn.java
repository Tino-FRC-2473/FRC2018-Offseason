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
	
	private double leftPower;
	private double rightPower;
	
	private boolean isClockwise;
	private double prevAngle;
	private double angleGoal;
	private double initialAngle;
	private double initialPower;
	
	
	public PointTurn(double degrees, double power) {
		requires(Robot.driveSubsystem);
		if (power < 0) throw new IllegalArgumentException("Power must be a positive scalar for point turn!");
		
		if(Math.abs(degrees) < 45) power = RobotMap.K_START_STALL_POWER;
		
		this.initialPower = power;
		
		isClockwise = degrees > 0;
		setPower(power);
		
		prevAngle = Devices.getInstance().getNavXGyro().getAngle();
		this.initialAngle = prevAngle;
		

		this.angleGoal = prevAngle + degrees;
		//if (Math.abs(degrees) > 20) angleGoal -= (isClockwise) ? 10 : -10;
	}
	
	private void setPower(double power) {
		if (power < 0) throw new IllegalArgumentException("Power must be a positive scalar for point turn!");
		if (power < RobotMap.K_RUNNING_STALL_POWER) power = RobotMap.K_RUNNING_STALL_POWER;
		this.leftPower = isClockwise ? power : -power;
		this.rightPower = -leftPower;
	}
	
	public double getPower(){
		return isClockwise ? leftPower : rightPower;
	}
	
	@Override
	protected void initialize() {
		Robot.driveSubsystem.drive(leftPower, leftPower, rightPower, rightPower);
	}

	@Override
	protected void execute() {
		double currDegrees = Devices.getInstance().getNavXGyro().getAngle();
		double degreesToGoal = isClockwise ? angleGoal-currDegrees : currDegrees-angleGoal;
		
		if (degreesToGoal < 90) {
			double settingPower = RobotMap.K_TURN*initialPower*(degreesToGoal/(Math.abs(angleGoal-initialAngle)));
			setPower(Math.abs(settingPower));
		}
		
		
		prevAngle = currDegrees;
		
		if (degreesToGoal <= 10) {
			Robot.driveSubsystem.driveRawPower(-RobotMap.K_OPPOSITE_POWER, -RobotMap.K_OPPOSITE_POWER, RobotMap.K_OPPOSITE_POWER, RobotMap.K_OPPOSITE_POWER);
		}else {
			Robot.driveSubsystem.driveRawPower(leftPower, leftPower, rightPower, rightPower);
		}
		
		System.out.printf("Power: %-5.3f | DTG: %.3f \n", Devices.getInstance().getTalon(RobotMap.TALON_BL).get(), degreesToGoal);
		
	}

	@Override
	protected boolean isFinished() {
		double currAngle = Devices.getInstance().getNavXGyro().getAngle();
		return isClockwise ? currAngle > angleGoal : currAngle < angleGoal;
	}

	@Override
	protected void end() {
		System.out.println("Absolute Angle: "+Devices.getInstance().getNavXGyro().getAngle());
		Robot.driveSubsystem.stopMotors();
		System.out.println("Relative Angle: " + Math.abs(initialAngle-Devices.getInstance().getNavXGyro().getAngle()));
		System.out.println("Turn Speed: " + this.initialPower);
		System.out.println("Current speed: " + this.leftPower);
	}

	@Override
	protected void interrupted() {
		Robot.driveSubsystem.stopMotors();
	}
}
