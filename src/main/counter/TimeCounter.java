package main.counter;

import main.observer.TimeObservable;
import main.observer.TimeObserver;

/**
 * This interface identifies methods for working with {@link TimeCounter}' implementation.
 */
public interface TimeCounter extends TimeObserver, TimeObservable
{
	/**
	 * Loads data from the saved file.
	 */
	void loadDataAndInitTimer();

	/**
	 * Saves data to the saved file.
	 */
	void saveData();

	/**
	 * Closes the controlled application.
	 */
	void closeApplication();

	/**
	 * Checks if time or settings were changed.
	 *
	 * @return true if time or settings were changed otherwise false
	 */
	boolean isChangedTimeOrSettings();
}