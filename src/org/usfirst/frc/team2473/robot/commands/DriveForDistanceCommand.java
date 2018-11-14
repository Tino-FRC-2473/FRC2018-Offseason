/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2473.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2473.robot.Robot;
import org.usfirst.frc.team2473.robot.RobotMap;
import org.usfirst.frc.team2473.robot.subsystems.DrivePower;
import org.usfirst.frc.team2473.robot.subsystems.DriveSubsystem;

public class DriveForDistanceCommand extends Command {
	
	private static DrivePower SLOW_POWER = new DrivePower(0.1, 0.1, 0.1, 0.1);
	
	private double inches;
	private double ticks; // should not be set directly
	
	private int prevTicks;
	
	private DrivePower power;

	public boolean finished;
	
	public DriveForDistanceCommand(double inches, DrivePower power) {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.driveSubsystem);
		
		setDistance(inches);
		this.power = power;
	}
	
	public void setPower(DrivePower power) {
		this.power = power;
	}

	private void setDistance(double inches) {
		this.inches = inches;
		this.ticks = this.inches * RobotMap.K_TICKS_PER_INCH;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		Robot.driveSubsystem.drive(power);
		prevTicks = 0;
		finished = false;
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		DrivePower tempPower = power;
		int currTicks = Math.abs(Robot.driveSubsystem.getAverageEncoderTicks());
		
		int delta = currTicks - prevTicks;
		if (ticks - (currTicks + delta) < RobotMap.K_ENCODER_THRESHOLD) {
			tempPower = SLOW_POWER;
		}
		Robot.driveSubsystem.drive(tempPower);
		prevTicks = currTicks;
		
		
	}


	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		int currTicks = Math.abs(Robot.driveSubsystem.getAverageEncoderTicks());
		return (ticks < currTicks);		
	}

	// Called once after isFinished returns true
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

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		Robot.driveSubsystem.stopMotors();
	}
}
