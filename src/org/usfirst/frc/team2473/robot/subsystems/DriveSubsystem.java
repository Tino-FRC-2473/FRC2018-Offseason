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

import edu.wpi.first.wpilibj.command.Subsystem;

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
	
	private HashMap<Double, Double> leftTable = new HashMap<>();
	private HashMap<Double, Double> rightTable = new HashMap<>();
	private double minTestedPower, maxTestedPower;
	
	private DriveSubsystem() {
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
			double lowerBound = powers.get(i); //the largest power value that is lower than the power input in the lookup table
			double upperBound = powers.get(i+1); //the smallest power value that is greater than the power input in the lookup table
			
			double lowerValue = tempTable.get(lowerBound); 
			double upperValue = tempTable.get(upperBound); 
			
			// NOT FINISHED
			double ratio = (power - lowerBound) / (upperBound - lowerBound); // where the input power is relative to the tested power bounds
			
			
			double difference = 1/(upperValue - lowerValue); //the change of lookup ratio between the two bounds
			
			double multiplier = lowerValue + difference * ratio;
			
			return power * multiplier;
		}
	}
	
	public void drive(double bl, double fl, double br, double fr) {
		backLeft.set(convertPower(bl, true));
		backRight.set(convertPower(-br, false));
		frontLeft.set(convertPower(fl, true));
		frontRight.set(convertPower(-fr, false));
		System.out.println(backLeft.get()+" "+backRight.get());
	}
	
	public void stopMotors() {
		drive(0, 0, 0, 0);
	}

	public void initDefaultCommand() {
	}
	
	public int getEncoderTicks(int id) {
		return Devices.getInstance().getTalon(id).getSelectedSensorPosition(0);
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
		System.out.println("FRONT RIGHT: " + getEncoderTicks(RobotMap.TALON_FR));
		System.out.println("BACK LEFT: " + getEncoderTicks(RobotMap.TALON_BL));
	}
	
	public int encoderDifference() {
		return Math.abs(getEncoderTicks(RobotMap.TALON_BL)) - Math.abs(getEncoderTicks(RobotMap.TALON_FR));
	}
	
	
}

