package timeCounter.controller;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This interface identifies methods for working with {@link IController}' implementation.
 */
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

	/**
	 * Sets the text of "StartStop" button to "Stop".
	 */
	void setButtonTextToStop();

	/**
	 * Sets the text of "StartStop" button to "Start".
	 */
	void setButtonTextToStart();

	/**
	 * Checks if the user has selected relaxation.
	 *
	 * @return true if the user has selected relaxation otherwise false.
	 */
	boolean isChosenRelax();

	/**
	 * Checks if "checkDate" checkbox is selected.
	 *
	 * @return true if the checkbox is selected otherwise false.
	 */
	boolean isAutoChangeDate();

	/**
	 * Checks if "checkBreak" checkbox is selected.
	 *
	 * @return true if the checkbox is selected otherwise false.
	 */
	boolean isRelaxReminder();

	/**
	 * Notices the user about already running application which is chosen for controlling.
	 *
	 * @return true if the user want to begin timing after restart the application which is chosen for controlling
	 * otherwise false
	 */
	boolean runningApplicationNotice();

	/**
	 * Creates implementation of this interface.
	 */
	void createView();

	/**
	 * Updates time fields of view.
	 *
	 * @param timeList list of new time values (index 0 - the current time, 1 - the today time, 2 - the total time)
	 */
	void updateTimeFields(List<AtomicLong> timeList);

	void updateCheckBoxesAndAppName(boolean relaxReminder, boolean autoChangeDate, String appName);
}
