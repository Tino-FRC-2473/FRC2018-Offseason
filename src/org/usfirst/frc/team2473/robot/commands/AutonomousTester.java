package org.usfirst.frc.team2473.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class AutonomousTester extends CommandGroup {

    public AutonomousTester() {
    	double wait = 0.1;
    	
        
        //Lightning bolt
//    	addSequential(new WaitCommand(wait));
//    	
//        addSequential(new StraightDrive(60, 0.5));
//        addSequential(new WaitCommand(wait));
//        
//        addSequential(new PointTurn(45, 0.45));
//        addSequential(new WaitCommand(wait));
//        
//        addSequential(new StraightDrive(30, 0.5));
//        addSequential(new WaitCommand(wait));
//        
//        addSequential(new PointTurn(-45, 0.45));
//        addSequential(new WaitCommand(wait));
//        
//        addSequential(new StraightDrive(30, 0.5));
        
	      addSequential(new PointTurn(-180, 0.45));
	      addSequential(new WaitCommand(wait));
	      addSequential(new StraightDrive(48, 0.5));
        
    	
//    	for (int i = 0; i < 4; i++) {
//    		addSequential(new StraightDrive(48, 0.5));
//            addSequential(new WaitCommand(wait));
//    		addSequential(new PointTurn(90, 0.45));
//            addSequential(new WaitCommand(wait));
//    	}
    }
}
