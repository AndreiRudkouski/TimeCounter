package timeCounter.counter;

import timeCounter.gui.IGUIWindow;
import timeCounter.load.ILoadSaveToFile;
import timeCounter.timing.ITimeRunning;

/**
 * This interface identifies methods for working with {@link ITimeCounter}' implementation.
 */
public interface ITimeCounter
{
	/**
	 * Increments the current time by one second and passes a new value to GUI.
	 */
	void incrementCurrentTime();

	/**
	 * Erases the current time by zero and passes a new value to GUI.
	 */
	void eraseCurrentTime();

	/**
	 * Increments the today time by one second and passes a new value to GUI.
	 */
	void incrementTodayTime();

	/**
	 * Erases the today time by zero and passes a new value to GUI.
	 */
	void eraseTodayTime();

	/**
	 * Increments the total time by one second and passes a new value to GUI.
	 */
	void incrementTotalTime();

	/**
	 * Erases the total time by zero and passes a new value to GUI.
	 */
	void eraseTotalTime();

	/**
	 * Checks if "RelaxReminder" checkbox is selected.
	 *
	 * @return true if the checkbox is selected otherwise false.
	 */
	boolean isTimeRelaxSelect();

	/**
	 * Checks if the time to relaxation.
	 */
	void checkRelaxTime();

	/**
	 * Checks if the time counting was begun.
	 *
	 * @return true if the time counting was begun otherwise false.
	 */
	boolean isBeginCount();

	/**
	 * Checks if the time was paused.
	 *
	 * @return true if the time was paused otherwise false.
	 */
	boolean isPause();

	/**
	 * Sets the text of "StartStop" button to "Stop".
	 */
	void setStopButton();

	/**
	 * Sets the text of "StartStop" button to "Start".
	 */
	void setStartButton();

	/**
	 * Returns t{@link ITimeRunning}'s implementation which is used for time counting.
	 *
	 * @return {@link ITimeRunning}'s implementation or null.
	 */
	ITimeRunning getTimer();

	/**
	 * Sets {@link ITimeRunning}'s implementation which is used for time counting. If other implementation was already
	 * used then it will be stopped.
	 *
	 * @param timer {@link ITimeRunning}'s implementation
	 */
	void setTimer(ITimeRunning timer);

	/**
	 * Checks if date was changed after time counting start.
	 */
	void checkChangeDate();

	/**
	 * Loads time data to the time counter.
	 */
	void loadTime();

	/**
	 * Saves time data from the time counter.
	 */
	void saveTime();

	/**
	 * Sets {@link IGUIWindow}'s view for display data by the time counter.
	 */
	void setWindow(IGUIWindow window);

	/**
	 * Sets {@link ILoadSaveToFile}'s implementation for load and save data by the time counter.
	 */
	void setLoadSaveToFile(ILoadSaveToFile saver);

	/**
	 * Changes locale which is used in the time counter.
	 */
	void changeLocale();
}