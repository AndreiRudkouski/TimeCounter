package timeCounter.counter;

import timeCounter.observer.ITimeObservable;
import timeCounter.observer.ITimeObserver;

/**
 * This interface identifies methods for working with {@link ITimeCounter}' implementation.
 */
public interface ITimeCounter extends ITimeObserver, ITimeObservable
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
	 * Closes the application which is chosen for controlling.
	 *
	 * @param saveData user choice for saving data
	 * @param closeApp close or not the connected application
	 * @return true if these is data for saving otherwise false
	 */
	boolean closeTimeCounter(boolean saveData, boolean closeApp);
}