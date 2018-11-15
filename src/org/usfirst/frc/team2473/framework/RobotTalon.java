package org.usfirst.frc.team2473.framework;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.RobotController;

public class RobotTalon extends WPI_TalonSRX {

	final static double PREFERRED_VOLTAGE = 12.25;
	
	public RobotTalon(int deviceNumber) {
		super(deviceNumber);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void set(double pow) {
		super.set(pow*PREFERRED_VOLTAGE/RobotController.getBatteryVoltage());
	}

}