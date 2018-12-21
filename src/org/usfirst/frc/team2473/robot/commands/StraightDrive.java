/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.Robot;
import org.usfirst.frc.team2473.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class StraightDrive extends Command {
	
	private static double SLOW_POWER = 0.1;
	
	private double inches;
	private double ticks;
	
	private int prevTicks;
	
	private double power;

	public boolean finished;
	
	public StraightDrive(double inches, double power) {
		requires(Robot.driveSubsystem);
		
		this.inches = inches;
		this.power = (inches < 0) ? -power : power;
	}
	
	public void setPower(double power) {
		this.power = power;
	}
	/**
	 * Set the target distance
	 */
	private void setDistance(double inches) {
		System.out.println("TICKSSSSS: " + Robot.driveSubsystem.getEncoderTicks(RobotMap.TALON_FR));
		this.ticks = Robot.driveSubsystem.getEncoderTicks(RobotMap.TALON_FR) + (this.inches * RobotMap.K_TICKS_PER_INCH);
	}

	@Override
	protected void initialize() {
		setDistance(inches);
		prevTicks = Robot.driveSubsystem.getEncoderTicks(RobotMap.TALON_FR);
		finished = false;
		
		System.out.println("ANGLE: " + Robot.driveSubsystem.getGyroAngle());

		System.out.println("REQUIRED TICKS: " + ticks);
		Robot.driveSubsystem.drive(power, power, power, power);
	}

	@Override
	protected void execute() {
		double tempPower = power;
		int currTicks = Robot.driveSubsystem.getEncoderTicks(RobotMap.TALON_FR);
		
		int delta = currTicks - prevTicks;
		if (Math.abs(ticks - (currTicks + delta)) < RobotMap.K_ENCODER_THRESHOLD) { // Math.abs() allows this to work regardless of driving direction (forwards or backwards)
			if (inches > 0) tempPower = SLOW_POWER;
			else tempPower = -SLOW_POWER;
		}
		Robot.driveSubsystem.drive(tempPower,tempPower,tempPower,tempPower);
				
		prevTicks = currTicks;
		
		Robot.driveSubsystem.printEncoders();
		
	}

	@Override
	protected boolean isFinished() {
		int currTicks = Robot.driveSubsystem.getEncoderTicks(RobotMap.TALON_FR);
		if (this.inches > 0) return (ticks < currTicks);
		else return (ticks > currTicks);
	}

	@Override
	protected void end() {
		System.out.println(power);
		System.out.println("----------------");
		System.out.println("REQUIRED TICKS: " + ticks);
		Robot.driveSubsystem.printEncoders();		
		System.out.println("Difference: " + Robot.driveSubsystem.encoderDifference());
		
		System.out.println();
		
		Robot.driveSubsystem.stopMotors();
	}

	@Override
	protected void interrupted() {
		Robot.driveSubsystem.stopMotors();
	}
}