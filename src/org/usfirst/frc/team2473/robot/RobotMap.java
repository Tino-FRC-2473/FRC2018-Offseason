/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2473.robot;

public class RobotMap {
	public static final int TALON_BL = 3;
	public static final int TALON_BR = 6;
	public static final int TALON_FL = 2;
	public static final int TALON_FR = 7;
	
	public static double K_TICKS_PER_INCH = 55;
	public static double K_ENCODER_THRESHOLD = 750;
	
	public static double K_START_STALL_POWER = 0.19;
	public static double K_RUNNING_STALL_POWER = 0.2;
	public static double K_OPPOSITE_POWER = 0.2;
	public static double K_TURN = 0.95;
}
