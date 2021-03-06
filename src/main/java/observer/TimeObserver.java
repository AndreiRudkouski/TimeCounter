package main.java.observer;

import java.io.File;
import java.util.List;

/**
 * This interface can be implemented by class if it wants to be informed of changes in observable objects.
 */
public interface TimeObserver
{
	/**
	 * Updates time of observer.
	 *
	 * @param timeList list of time values (index 0 - the current time, 1 - the today time and 2 - the total time)
	 */
	void updateTime(List<Long> timeList);

	/**
	 * Updates settings of observer.
	 *
	 * @param autoChangeDate the current value of this field
	 * @param relaxReminder the current value of this field
	 * @param file the current file
	 */
	void updateSettings(boolean autoChangeDate, boolean relaxReminder, File file);

	/**
	 * Updates state of time timer (run or stop).
	 *
	 * @param isStart the current state
	 */
	void updateTiming(boolean isStart);
}