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

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveSubsystem extends Subsystem {
	
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
	
	DifferentialDrive teleopDrive;
	
	private HashMap<Double, Double> leftTable = new HashMap<>();
	private HashMap<Double, Double> rightTable = new HashMap<>();
	private double minTestedPower, maxTestedPower;
	
	private DriveSubsystem() {
		SpeedControllerGroup right = new SpeedControllerGroup(Devices.getInstance().getTalon(RobotMap.TALON_BR), Devices.getInstance().getTalon(RobotMap.TALON_FR));
		SpeedControllerGroup left = new SpeedControllerGroup(Devices.getInstance().getTalon(RobotMap.TALON_BL), Devices.getInstance().getTalon(RobotMap.TALON_FL));
		teleopDrive = new DifferentialDrive(right, left);
		
		backLeft = Devices.getInstance().getTalon(RobotMap.TALON_BL);
		backRight = Devices.getInstance().getTalon(RobotMap.TALON_BR);
		frontLeft = Devices.getInstance().getTalon(RobotMap.TALON_FL);
		frontRight = Devices.getInstance().getTalon(RobotMap.TALON_FR);
		initLookupTable();
	}
	
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
	
	public double convertPower(double power, boolean isLeft) {
		HashMap<Double, Double> tempTable = isLeft ? leftTable : rightTable;
		if (power < minTestedPower) {
			return power * 1/tempTable.get(minTestedPower);
		} else if (power > maxTestedPower) {
			return power * 1/tempTable.get(maxTestedPower);
		} else {
			// linearize between the two values around it
			ArrayList<Double> powers = new ArrayList<>(tempTable.keySet());
			Collections.sort(powers);
			
			if (powers.contains(power)) { // the input power is one of the powers in the lookup table
				double newPower = power * 1/tempTable.get(power);
				return newPower;
			}
			
			int i = 0;
			while(powers.get(i) < power) i++;
			i--;
			double lowerNearestPower = powers.get(i); //the largest power value that is lower than the power input in the lookup table
			double higherNearestPower = powers.get(i+1); //the smallest power value that is greater than the power input in the lookup table
			
			double calibrationRatioOfLowerPower = tempTable.get(lowerNearestPower); 
			double calibrationRatioOfHigherPower = tempTable.get(higherNearestPower); 
			
			//return power*(1/calibrationRatioOfLowerPower);
			
			
			double slope = (calibrationRatioOfHigherPower-calibrationRatioOfLowerPower) / (higherNearestPower-lowerNearestPower);
			
			double deltaPower = power - lowerNearestPower; //the change of lookup ratio between the two bounds
			
			double powerCalibration = calibrationRatioOfLowerPower + slope * deltaPower;
			
			return power * 1/powerCalibration;
		}
	}
	
	public void teleopDrive(double speed, double rotation) {
		teleopDrive.arcadeDrive(speed, rotation);
	}
	
	public void drive(double bl, double fl, double br, double fr) {
		backLeft.set(convertPower(bl, true));
		backRight.set(convertPower(-br, false));
		frontLeft.set(convertPower(fl, true));
		frontRight.set(convertPower(-fr, false));
	}
	
	public void driveRawPower(double bl, double fl, double br, double fr) {
		backLeft.set(bl);
		backRight.set(-br);
		frontLeft.set(fl);
		frontRight.set(-fr);
	}
	
	public void stopMotors() {
		drive(0, 0, 0, 0);
	}

	public void initDefaultCommand() {
	}
	
	/**
	 *	Gets the encoder ticks of a given Talon based on id. Positive means robot is going forward.
	 *  @param id Talon ID
	 *  @return Encoder ticks
	 */
	public int getEncoderTicks(int id) {
		if (id == RobotMap.TALON_FR || id == RobotMap.TALON_BR) return -Devices.getInstance().getTalon(id).getSelectedSensorPosition(0);
		return Devices.getInstance().getTalon(id).getSelectedSensorPosition(0);
	}
	
	public void resetEncoders() {
		resetEncoderForMotor(backLeft);
		System.out.println("Back left reset");
		
		resetEncoderForMotor(backRight);
		System.out.println("Back right reset");
		
		resetEncoderForMotor(frontLeft);
		System.out.println("Front left reset");
		
		resetEncoderForMotor(frontRight);
		System.out.println("Front right reset"); 
	}
	
	public void resetEncoderForMotor(WPI_TalonSRX motor) {
		ErrorCode c = motor.setSelectedSensorPosition(0,0,5000);
		if (c.value != 0) {
			throw new IllegalArgumentException(c.toString());
		}
		
		while (motor.getSelectedSensorPosition() != 0);
	}
	
	public void printEncoders() {
		System.out.println(String.format("FR: %7d       BL: %7d", getEncoderTicks(RobotMap.TALON_FR), getEncoderTicks(RobotMap.TALON_BL)));
	}
	
	public int encoderDifference() {
		return Math.abs(getEncoderTicks(RobotMap.TALON_BL)) - Math.abs(getEncoderTicks(RobotMap.TALON_FR));
	}
	
	
}

