/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2473.robot.subsystems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.usfirst.frc.team2473.framework.Devices;
import org.usfirst.frc.team2473.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This class contains all components of the robot necessary for driving.
 */
public class DriveSubsystem extends Subsystem {
	
	private static DriveSubsystem instance;
	
	static {
		instance = new DriveSubsystem();
	}
	
	/**
	 * Gets the current instance.
	 * @return current instance of DriveSubsystem
	 */
	public static DriveSubsystem getInstance() {
		return instance;
	}
	
	WPI_TalonSRX backLeft;
	WPI_TalonSRX backRight;
	WPI_TalonSRX frontLeft;
	WPI_TalonSRX frontRight;
	
	DifferentialDrive teleopDrive;
	
	private HashMap<Double, Double> leftTable = new HashMap<>();
	private HashMap<Double, Double> rightTable = new HashMap<>();
	
	/**
	 * minimum power for which lookup table values have been determined
	 */
	private double minTestedPower;
	
	/**
	 * maximum power for which lookup table values have been determined
	 */
	private double maxTestedPower;
	
	/*
	 * status of encoder reset
	 */
	private boolean encoderResetComplete;
	
	/**
	 * Initializes teleopDrive, motors, and lookup tables.
	 */
	private DriveSubsystem() {
		SpeedControllerGroup right = new SpeedControllerGroup(Devices.getInstance().getTalon(RobotMap.TALON_BR), Devices.getInstance().getTalon(RobotMap.TALON_FR));
		SpeedControllerGroup left = new SpeedControllerGroup(Devices.getInstance().getTalon(RobotMap.TALON_BL), Devices.getInstance().getTalon(RobotMap.TALON_FL));
		teleopDrive = new DifferentialDrive(right, left);
		
		backLeft = Devices.getInstance().getTalon(RobotMap.TALON_BL);
		backRight = Devices.getInstance().getTalon(RobotMap.TALON_BR);
		frontLeft = Devices.getInstance().getTalon(RobotMap.TALON_FL);
		frontRight = Devices.getInstance().getTalon(RobotMap.TALON_FR);
		initLookupTable();
		
		encoderResetComplete = false;
	}
	
	/**
	 * Initializes the motor power lookup tables. Inputs are raw motor powers, outputs are values that the raw powers will
	 * be divided by.
	 */
	private void initLookupTable() {
		leftTable.put(0.2, 1.035842);
		leftTable.put(0.3, 1.033883);
		leftTable.put(0.4, 1.044270);
		leftTable.put(0.5, 1.055146);
		leftTable.put(0.6, 1.052553);
		leftTable.put(0.7, 1.061291);
		leftTable.put(0.8, 1.088878);
		
		rightTable.put(0.2, 1.012183);
		rightTable.put(0.3, 1.017407);
		rightTable.put(0.4, 1.021134);
		rightTable.put(0.5, 1.027404); // was 1.025404
		rightTable.put(0.6, 1.020292);
		rightTable.put(0.7, 1.017890);
		rightTable.put(0.8, 1.033552);
		
		minTestedPower = Collections.min(leftTable.keySet());
		maxTestedPower = Collections.max(leftTable.keySet());
	}
	
	/**
	 * Transforms raw motor powers using an experimental lookup table to account for the difference in motor outputs.
	 * @param power		raw motor power
	 * @param motor		motor for which the power is being set	
	 * @return converted motor power
	 */
	public double convertPower(double power, WPI_TalonSRX motor) {
		// determine which lookup table to use
		boolean isLeft = (motor.equals(Devices.getInstance().getTalon(RobotMap.TALON_BL)) || 
						  motor.equals(Devices.getInstance().getTalon(RobotMap.TALON_FL))) ? true : false;
		HashMap<Double, Double> tempTable = isLeft ? leftTable : rightTable;
		
		if (power < minTestedPower) {
			return power/tempTable.get(minTestedPower);
		} else if (power > maxTestedPower) {
			return power/tempTable.get(maxTestedPower);
		} else {
			
			ArrayList<Double> powers = new ArrayList<>(tempTable.keySet());
			Collections.sort(powers);
			
			if (powers.contains(power)) { // the input power is one of the powers in the lookup table
				double newPower = power/tempTable.get(power);
				return newPower;
			}
			
			// linearize between the two values around the power
			int i;
			for (i = powers.size() - 1; powers.get(i) > power; i--);
		
			double lowerNearestPower = powers.get(i); //the largest power value that is lower than the power input in the lookup table
			double higherNearestPower = powers.get(i+1); //the smallest power value that is greater than the power input in the lookup table
			
			double calibrationRatioOfLowerPower = tempTable.get(lowerNearestPower); 
			double calibrationRatioOfHigherPower = tempTable.get(higherNearestPower); 	
			
			double slope = (calibrationRatioOfHigherPower-calibrationRatioOfLowerPower) / (higherNearestPower-lowerNearestPower);
			
			double deltaPower = power - lowerNearestPower; //the change of lookup ratio between the two bounds
			
			double powerCalibration = calibrationRatioOfLowerPower + slope * deltaPower;
			
			return power/powerCalibration;
		}
	}
	
	/**
	 * Moves the robot with the given speed and rotation values in teleop mode.
	 * @param speed		the robot's speed along the x-axis. Forward is positive.
	 * @param rotation	the robot's rotation rate along the z-axis. Clockwise is positive.
	 */
	public void teleopDrive(double speed, double rotation) {
		teleopDrive.arcadeDrive(speed, rotation);
	}
	
	/**
	 * Sets motor powers using the lookup table.
	 * @param bl 	back left motor power
	 * @param fl 	front left motor power
	 * @param br 	back right motor power
	 * @param fr 	front right motor power
	 */
	public void drive(double bl, double fl, double br, double fr) {
		backLeft.set(convertPower(bl, backLeft));
		backRight.set(convertPower(-br, backRight));
		frontLeft.set(convertPower(fl, frontLeft));
		frontRight.set(convertPower(-fr, frontRight));
	}
	
	/**
	 * Sets motor powers without using the lookup table.
	 * @param bl 	back left motor power
	 * @param fl 	front left motor power
	 * @param br 	back right motor power
	 * @param fr 	front right motor power
	 */
	public void driveRawPower(double bl, double fl, double br, double fr) {
		backLeft.set(bl);
		backRight.set(-br);
		frontLeft.set(fl);
		frontRight.set(-fr);
	}
	
	/**
	 * Sets all motor powers to 0.
	 */
	public void stopMotors() {
		drive(0, 0, 0, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initDefaultCommand() {}
	
	/**
	 * Gets the gyro heading.
	 * @return heading of the robot (degrees). Initial position is 0, clockwise is positive.
	 */
	public double getGyroAngle() {
		return Devices.getInstance().getNavXGyro().getAngle();
	}
	
	/**
	 * Gets the encoder ticks of a given Talon based on id. Forward is positive.
	 * @param id Talon ID
	 * @return encoder ticks
	 */
	public synchronized int getEncoderTicks(int id) {
		// flip sign of the ticks if the motor is on the right
		if (id == RobotMap.TALON_FR || id == RobotMap.TALON_BR) return -Devices.getInstance().getTalon(id).getSelectedSensorPosition(0);
		return Devices.getInstance().getTalon(id).getSelectedSensorPosition(0);
	}
	
	/**
	 * Sets all encoder values to 0.
	 */
	public synchronized void resetEncoders() {
		frontRight.setSelectedSensorPosition(0, 0, 0);
		backLeft.setSelectedSensorPosition(0, 0, 0);
		backRight.setSelectedSensorPosition(0, 0, 0);
		frontLeft.setSelectedSensorPosition(0, 0, 0);
		
		encoderResetComplete = true;
	}
	
	/**
	 * Returns if encoder reset has been completed or not.
	 * @return if encoder reset has been completed
	 */
	public synchronized boolean isEncoderResetComplete() {
		return encoderResetComplete;
	}
	
	/**
	 * Prints current encoder ticks of front right and back left motors.
	 */
	public synchronized void printEncoders() {
		System.out.println(String.format("FR: %7d       BL: %7d", getEncoderTicks(RobotMap.TALON_FR), getEncoderTicks(RobotMap.TALON_BL)));
	}
	
	/**
	 * Calculates the difference in encoder ticks between the left and right.
	 * @return absolute value of back left ticks minus absolute value of front right ticks
	 */
	public synchronized int encoderDifference() {
		return Math.abs(getEncoderTicks(RobotMap.TALON_BL)) - Math.abs(getEncoderTicks(RobotMap.TALON_FR));
	}
	
	
}
