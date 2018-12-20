package org.usfirst.frc.team2473.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class AutonomousTester extends CommandGroup {

    public AutonomousTester(int distanceFirst, int turnAngle, int distanceSecond) {
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

	      addSequential(new StraightDrive(distanceFirst, 0.3));
	      addSequential(new WaitCommand(wait));
	      addSequential(new PointTurn(turnAngle, 0.45));
	      addSequential(new WaitCommand(wait));
	      addSequential(new StraightDrive(distanceSecond, 0.3));
        
    	
//    	for (int i = 0; i < 4; i++) {
//    		addSequential(new StraightDrive(48, 0.5));
//            addSequential(new WaitCommand(wait));
//    		addSequential(new PointTurn(90, 0.45));
//            addSequential(new WaitCommand(wait));
//    	}
    }
}
