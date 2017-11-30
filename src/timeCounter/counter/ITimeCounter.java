package timeCounter.counter;

import java.io.File;

/**
 * This interface identifies methods for working with {@link ITimeCounter}' implementation.
 */
public interface ITimeCounter
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
	 * Changes locale which is used in the time counter.
	 */
	void changeLocale();

	/**
	 * Chooses {@link File} file which is controlled.
	 */
	void chooseApplication();

	/**
	 * Erases {@link File} file which is controlled.
	 */
	void eraseApplication();

	/**
	 * Pushes 'StartStop' button and run or stop the timer.
	 */
	void pushStartStopButton();

	/**
	 * Closes the application which is chosen for controlling.
	 *
	 * @param close user choice for saving data
	 * @return true if these is data for saving otherwise false
	 */
	boolean closeTimeCounter(boolean close);
}