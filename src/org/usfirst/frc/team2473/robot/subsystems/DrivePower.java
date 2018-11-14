package org.usfirst.frc.team2473.robot.subsystems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/*
 * Instead of always inputting doubles for motor powers, a DrivePower object can be passed
 * to multiply drive-tuning constants automatically.
 */
public class DrivePower {
	
	public final static DrivePower ZEROES = new DrivePower(0,0,0,0);
	
	double power_bl;
	double power_br;
	double power_fl;
	double power_fr;
	
	double K_COEFF_BL = 1.000;
	double K_COEFF_BR = 1.000;
	double K_COEFF_FL = 1.000;
	double K_COEFF_FR = 1.000;
	
	double K_COEFF_L = 1.000;
	double K_COEFF_R = 0.98;
	
	HashMap<Double, Double> lookupTable = new HashMap<>();
	HashMap<Double, Double> leftTable = new HashMap<>();
	HashMap<Double, Double> rightTable = new HashMap<>();
	
	
	double minTestedPower, maxTestedPower;
	
	public DrivePower(double power) {
		this(power, power, power, power);
	}
	
	public DrivePower(double power_bl, double power_br, double power_fl, double power_fr) {
//		this.power_bl = power_bl * K_COEFF_L;
//		this.power_br = power_br * K_COEFF_R;
//		this.power_fl = power_fl * K_COEFF_L;
//		this.power_fr = power_fr * K_COEFF_R;

		initLookupTable();
		
		this.power_bl = convertPowerLeft(power_bl);
		this.power_br = -1 * convertPowerRight(power_br);
		this.power_fl = convertPowerLeft(power_fl);
		this.power_fr = -1 * convertPowerRight(power_fr);
		
	}
	
	private void initLookupTable() {
		lookupTable.put(0.2, 0.967679);
		lookupTable.put(0.3, 0.974425);
		lookupTable.put(0.4, 0.968279);
		lookupTable.put(0.5, 0.962537);
		lookupTable.put(0.6, 0.960015);
		lookupTable.put(0.7, 0.949875);
		lookupTable.put(0.8, 0.940507);
		
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
		
		minTestedPower = Collections.min(lookupTable.keySet());
		maxTestedPower = Collections.max(lookupTable.keySet());
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Power: " + power_fr;
	}
	
	public double convertPower(double power) {
		if (power < minTestedPower) {
			return power * lookupTable.get(minTestedPower);
		} else if (power > maxTestedPower) {
			return power * lookupTable.get(maxTestedPower);
		} else {
			// linearize between the two values around it
			ArrayList<Double> powers = new ArrayList<>(lookupTable.keySet());
			Collections.sort(powers);
			
			if (powers.contains(power)) { // the input power is one of the powers in the lookup table
				double newPower = power * lookupTable.get(power);
				System.out.println("Power is exact! No linearization required. Old power: " + power + " ||| New Power: " + newPower);
				return newPower;
			}
			
			int i = 0;
			while(powers.get(i) < power) i++;
			i--;
			double lowerBound = powers.get(i); //the largest power value that is lower than the power input in the lookup table
			double upperBound = powers.get(i+1); //the smallest power value that is greater than the power input in the lookup table
			
			double lowerValue = lookupTable.get(lowerBound); 
			double upperValue = lookupTable.get(upperBound); 
			
			double ratio = (power - lowerBound) / (upperBound - lowerBound); // where the input power is relative to the tested power bounds
			
			double difference = (upperValue - lowerValue); //the change of lookup ratio between the two bounds
			
			double multiplier = lowerValue + difference * ratio;
			
			return power * multiplier;
		}
	}
	
	public double convertPowerLeft(double power) {
		if (power < minTestedPower) {
			return power * 1/leftTable.get(minTestedPower);
		} else if (power > maxTestedPower) {
			return power * 1/leftTable.get(maxTestedPower);
		} else {
			// linearize between the two values around it
			ArrayList<Double> powers = new ArrayList<>(leftTable.keySet());
			Collections.sort(powers);
			
			if (powers.contains(power)) { // the input power is one of the powers in the lookup table
				double newPower = power * 1/leftTable.get(power);
				System.out.println("Power is exact! No linearization required. Old power: " + power + " ||| New Power: " + newPower);
				return newPower;
			}
			
			int i = 0;
			while(powers.get(i) < power) i++;
			i--;
			double lowerBound = powers.get(i); //the largest power value that is lower than the power input in the lookup table
			double upperBound = powers.get(i+1); //the smallest power value that is greater than the power input in the lookup table
			
			double lowerValue = leftTable.get(lowerBound); 
			double upperValue = leftTable.get(upperBound); 
			
			// NOT FINISHED
			double ratio = (power - lowerBound) / (upperBound - lowerBound); // where the input power is relative to the tested power bounds
			
			
			double difference = 1/(upperValue - lowerValue); //the change of lookup ratio between the two bounds
			
			double multiplier = lowerValue + difference * ratio;
			
			return power * multiplier;
		}
	}
		
	public double convertPowerRight(double power) {
		if (power < minTestedPower) {
			return power * 1/rightTable.get(minTestedPower);
		} else if (power > maxTestedPower) {
			return power * 1/rightTable.get(maxTestedPower);
		} else {
			// linearize between the two values around it
			ArrayList<Double> powers = new ArrayList<>(rightTable.keySet());
			Collections.sort(powers);
			
			if (powers.contains(power)) { // the input power is one of the powers in the lookup table
				double newPower = power * 1/rightTable.get(power);
				System.out.println("Power is exact! No linearization required. Old power: " + power + " ||| New Power: " + newPower);
				return newPower;
			}
			
			int i = 0;
			while(powers.get(i) < power) i++;
			i--;
			double lowerBound = powers.get(i); //the largest power value that is lower than the power input in the lookup table
			double upperBound = powers.get(i+1); //the smallest power value that is greater than the power input in the lookup table
			
			double lowerValue = rightTable.get(lowerBound); 
			double upperValue = rightTable.get(upperBound); 
			
			// NOT FINISHED
			
			double ratio = (power - lowerBound) / (upperBound - lowerBound); // where the input power is relative to the tested power bounds
			
			double difference = 1/(upperValue - lowerValue); //the change of lookup ratio between the two bounds
			
			double multiplier = lowerValue + difference * ratio;
			
			return power * multiplier;
		}
	}
}