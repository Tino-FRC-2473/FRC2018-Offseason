/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2473.robot;

public class RobotMap {
	public static int TALON_BL = 3;
	public static int TALON_BR = 6;
	public static int TALON_FL = 2;
	public static int TALON_FR = 7;
	
	public static double K_TICKS_PER_INCH = 55;
	public static double K_ENCODER_THRESHOLD = 1500;
	public static double K_DEGREE_THRESHOLD = 70;
	public static double K_DEGREE_THRESHOLD_CRITICAL = 30;
	public static double K_ANGLE_DAMPEN = 0.95;
	public static double K_ANGLE_DAMPEN_CRITICAL = 0.6;
}
