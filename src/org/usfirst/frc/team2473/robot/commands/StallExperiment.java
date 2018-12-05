/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2473.robot.commands;

import java.util.ArrayList;

import org.usfirst.frc.team2473.framework.Devices;
import org.usfirst.frc.team2473.robot.Robot;
import org.usfirst.frc.team2473.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class StallExperiment extends Command {
    
    private double power;
    private double change;
    
    private double prevAngle;
    private double currAngle;
    
    private final int[] MOTORS = {RobotMap.TALON_FR, RobotMap.TALON_BR, 
                                  RobotMap.TALON_FL, RobotMap.TALON_BL};
    

    public StallExperiment(double power, double change) {
        requires(Robot.driveSubsystem);
        
        if (power < 0) throw new IllegalArgumentException("Power must be a positive scalar!");
        if (change < 0) throw new IllegalArgumentException("Change must be a positive scalar!");
        this.power = power;
        this.change = change;
        
    }
    
    @Override
    protected void initialize() {
        prevAngle = Devices.getInstance().getNavXGyro().getAngle();
        Robot.driveSubsystem.driveRawPower(power, power, -power, -power);
    }
    
    @Override
    protected void execute() {
        currAngle = Devices.getInstance().getNavXGyro().getAngle();
        
        if (currAngle - prevAngle > 360) {
            power -= change;
            prevAngle = currAngle;
            System.out.println("New power: " + power);
        }
        
        Robot.driveSubsystem.driveRawPower(power, power, -power, -power);
        
    }

    @Override
    protected boolean isFinished() {
        return power<=0;
    }

    @Override
    protected void end() {
        System.out.println("FINAL POWER: " + power);
        Robot.driveSubsystem.stopMotors();
        
    }

    @Override
    protected void interrupted() {
        Robot.driveSubsystem.stopMotors();
    }
} 