package timeCounter.controller;

import java.io.File;

public interface IController
{
	/**
	 * Erases the current time by zero and passes a new value to GUI.
	 */
	void eraseCurrentTime();

	/**
	 * Erases the today time by zero and passes a new value to GUI.
	 */
	void eraseTodayTime();

	/**
	 * Erases the total time by zero and passes a new value to GUI.
	 */
	void eraseTotalTime();

	/**
	 * Loads data to the time counter.
	 */
	void loadData();

	/**
	 * Saves data from the time counter.
	 */
	void saveData();

	/**
	 * Chooses {@link File} file which is controlled.
	 */
	void chooseApplication();

	/**
	 * Erases {@link File} file which is controlled.
	 */
	void eraseApplication();

	/**
	 * Runs or stops the timer.
	 */
	void startStopTimer();

	/**
	 * Closes the application which is chosen for controlling.
	 *
	 * @param close user choice for saving data
	 * @return true if these is data for saving otherwise false
	 */
	boolean closeTimeCounter(boolean close);
}
