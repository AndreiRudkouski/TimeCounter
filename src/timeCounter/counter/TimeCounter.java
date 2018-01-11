package timeCounter.counter;

import timeCounter.observer.TimeObservable;
import timeCounter.observer.TimeObserver;

/**
 * This interface identifies methods for working with {@link TimeCounter}' implementation.
 */
public interface TimeCounter extends TimeObserver, TimeObservable
{
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
	boolean closeTimeCounter(Boolean saveData, Boolean closeApp);
}