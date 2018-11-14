/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2473.robot.subsystems;

import org.usfirst.frc.team2473.framework.Devices;
import org.usfirst.frc.team2473.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class DriveSubsystem extends Subsystem {
	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	
	private static DriveSubsystem instance;
	
	static {
		instance = new DriveSubsystem();
	}
	
	public static DriveSubsystem getInstance() {
		return instance;
	}
	
	WPI_TalonSRX backLeft;
	WPI_TalonSRX backRight;
	WPI_TalonSRX frontLeft;
	WPI_TalonSRX frontRight;
	
	private DriveSubsystem() {
		backLeft = Devices.getInstance().getTalon(RobotMap.TALON_BL);
		backRight = Devices.getInstance().getTalon(RobotMap.TALON_BR);
		frontLeft = Devices.getInstance().getTalon(RobotMap.TALON_FL);
		frontRight = Devices.getInstance().getTalon(RobotMap.TALON_FR);
	}
	
	public void drive(DrivePower dp) {
		backLeft.set(dp.power_bl);
		backRight.set(dp.power_br);
		frontLeft.set(dp.power_fl);
		frontRight.set(dp.power_fr);
	}
	
	public void stopMotors() {
		drive(DrivePower.ZEROES);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
	
	public int getEncoderTicks(WPI_TalonSRX motor) {
		return motor.getSelectedSensorPosition(0);
	}
	
	public int getAverageEncoderTicks() {	
		WPI_TalonSRX[] motorsForEncoderTicks = {frontRight, backLeft};
		
		int totalClicks = 0;
		
		for (WPI_TalonSRX motor : motorsForEncoderTicks) {
			totalClicks += getEncoderTicks(motor);
		}
		
		//return totalClicks / motorsForEncoderTicks.length;
		
		return frontRight.getSelectedSensorPosition(0);
	}
	
	public void resetEncoders() {
		resetEncoderForMotor(backLeft);
		resetEncoderForMotor(backRight);
		resetEncoderForMotor(frontLeft);
		resetEncoderForMotor(frontRight);
	}
	
	public void resetEncoderForMotor(WPI_TalonSRX motor) {
		motor.setSelectedSensorPosition(0,0,5);
	}
	
	public void printEncoders() {
		System.out.println("FRONT RIGHT: " + getEncoderTicks(frontRight));
		System.out.println("BACK LEFT: " + getEncoderTicks(backLeft));
	}
	
	public int encoderDifference() {
		return Math.abs(getEncoderTicks(backLeft)) - Math.abs(getEncoderTicks(frontRight));
	}
	
	
}

