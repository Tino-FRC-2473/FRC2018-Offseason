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
	
	
	public PointTurn(double degrees, double power) {
		requires(Robot.driveSubsystem);
		
		if (power < 0) throw new IllegalArgumentException("Power must be a positive scalar for point turn!");
		
		isClockwise = degrees > 0;
		
//		this.leftPower = isClockwise ? power : -power;
//		this.rightPower = -leftPower;
		setPower(power);
		
		prevAngle = Devices.getInstance().getNavXGyro().getAngle();
		this.initialAngle = prevAngle;
		this.angleGoal = prevAngle + degrees;
	}
	
	/**
	 * 
	 * @param power
	 * 
	 * Doesn't actually change the motor power
	 */
	public void setPower(double power) {
		if (power < 0) throw new IllegalArgumentException("Power must be a positive scalar for point turn!");
		if (power < 0.15) {
			System.out.println("FSFDFSFD" + power);
			power = 0.15;
		}
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
//		System.out.println("TARGET ANGLE: " + this.angleGoal);
		double currDegrees = Devices.getInstance().getNavXGyro().getAngle();
		double angleDiff = isClockwise ? angleGoal-currDegrees : currDegrees-angleGoal;
		/*if (angleDiff < RobotMap.K_DEGREE_THRESHOLD) {
			double newPower = getPower()*RobotMap.K_ANGLE_DAMPEN;
			System.out.println("NEW POWER: " + newPower);
			setPower(newPower);
			
			System.out.println("LEFT POWER: " + leftPower);
			System.out.println("RIGHT POWER: " + rightPower);
		}*/
		System.out.println("adfsfsd POWER: " + getPower());
		if(angleDiff < RobotMap.K_DEGREE_THRESHOLD) {
			setPower(Math.floor((getPower()*RobotMap.K_ANGLE_DAMPEN)*100)/100);
		}
		Robot.driveSubsystem.drive(leftPower, leftPower, rightPower, rightPower);
		prevAngle = currDegrees;
		
	}

	@Override
	protected boolean isFinished() {
		double currAngle = Devices.getInstance().getNavXGyro().getAngle();
		return isClockwise ? currAngle > angleGoal : currAngle < angleGoal;
	}

	@Override
	protected void end() {
		System.out.println("Angle: "+Devices.getInstance().getNavXGyro().getAngle());
		Robot.driveSubsystem.stopMotors();
		System.out.println("Angle: "+Devices.getInstance().getNavXGyro().getAngle());
		System.out.println("Angle: "+initialAngle);
		System.out.println(Math.abs(initialAngle-Devices.getInstance().getNavXGyro().getAngle()));
	}

	@Override
	protected void interrupted() {
		Robot.driveSubsystem.stopMotors();
	}
}
