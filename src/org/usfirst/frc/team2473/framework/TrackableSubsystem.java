package org.usfirst.frc.team2473.framework;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class TrackableSubsystem extends Subsystem {
    public abstract void stop();
    public abstract String getState();
    
    public void logCurrentState() {
		System.out.println("\t" + this.getClass().getSimpleName() + ": " + this.getState());
	}
}
