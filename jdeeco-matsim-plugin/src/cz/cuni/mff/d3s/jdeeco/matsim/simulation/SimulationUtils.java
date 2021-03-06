package cz.cuni.mff.d3s.jdeeco.matsim.simulation;

import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;

public abstract interface SimulationUtils extends CurrentTimeProvider,
		CallbackProvider {

	static final int MILLIS_IN_SECOND = 1000;
	
	public static double millisecondsToSeconds(long time) {
		return time * 1.0 / MILLIS_IN_SECOND;
	}

	public static long secondsToMilliseconds(double time) {
		return Math.round(time * MILLIS_IN_SECOND);
	}
}
