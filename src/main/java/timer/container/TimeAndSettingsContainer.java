package main.java.timer.container;

import java.io.File;
import java.time.LocalDate;
import java.util.Set;

/**
 * This interface identifies methods for any container which contains time and settings.
 */
public interface TimeAndSettingsContainer
{
	/**
	 * Returns the value of the current time.
	 *
	 * @return value in seconds
	 */
	long getCurrentTimeValue();

	/**
	 * Sets the value to the current time.
	 *
	 * @param value in seconds
	 */
	void setCurrentTimeValue(long value);

	/**
	 * Returns the value of the today time.
	 *
	 * @return value in seconds
	 */
	long getTodayTimeValue();

	/**
	 * Sets the value to the today time.
	 *
	 * @param value in seconds
	 */
	void setTodayTimeValue(long value);

	/**
	 * Returns the value of the total time.
	 *
	 * @return value in seconds
	 */
	long getTotalTimeValue();

	/**
	 * Sets the value to the total time.
	 *
	 * @param value in seconds
	 */
	void setTotalTimeValue(long value);

	/**
	 * Adds one second to the time.
	 */
	void increaseTimeBySecond();

	/**
	 * Adds the given delta to the today time.
	 *
	 * @param delta the value to add in seconds
	 */
	void increaseTodayTimeByDelta(long delta);

	/**
	 * Adds the given delta to the total time.
	 *
	 * @param delta the value to add in seconds
	 */
	void increaseTotalTimeByDelta(long delta);

	/**
	 * Returns the value of the today date.
	 *
	 * @return today date
	 */
	LocalDate getTodayDate();

	/**
	 * Sets the value to the today date.
	 *
	 * @param todayDate value
	 */
	void setTodayDate(LocalDate todayDate);

	/**
	 * Puts pair of of date and appropriate time to the storage of date and time.
	 *
	 * @param date value
	 * @param time appropriate time value in seconds
	 */
	void putDateAndTimeToStorage(LocalDate date, long time);

	/**
	 * Returns stored time from the storage by given date.
	 *
	 * @return time value in seconds
	 */
	long getTimeFromStorageByDate(LocalDate date);

	/**
	 * Returns set of dates from storage.
	 *
	 * @return set of dates
	 */
	Set<LocalDate> getDatesFromStorage();

	/**
	 * Returns the value of 'AutoChangeDate' flag.
	 *
	 * @return value of the flag
	 */
	boolean isAutoChangeDate();

	/**
	 * Sets the value to 'AutoChangeDate' flag.
	 *
	 * @param autoChangeDate value to set
	 */
	void setAutoChangeDate(boolean autoChangeDate);

	/**
	 * Returns the value of 'RelaxReminder' flag.
	 *
	 * @return value of the flag
	 */
	boolean isRelaxReminder();

	/**
	 * Sets the value to 'RelaxReminder' flag.
	 *
	 * @param relaxReminder value to set
	 */
	void setRelaxReminder(boolean relaxReminder);

	/**
	 * Returns the value of 'RunningApplication' flag.
	 *
	 * @return value of the flag
	 */
	boolean isRunningApplication();

	/**
	 * Sets the value to 'RunningApplication' flag.
	 *
	 * @param isRunningApplication value to set
	 */
	void setIsRunningApplication(boolean isRunningApplication);

	/**
	 * Returns the application from the container.
	 *
	 * @return application
	 */
	File getApplication();

	/**
	 * Sets the application to the container.
	 *
	 * @param application to sets
	 */
	void setApplication(File application);

	/**
	 * Returns the application process from the container.
	 *
	 * @return application process
	 */
	Process getApplicationProcess();

	/**
	 * Sets the application process to the container.
	 *
	 * @param applicationProcess to sets
	 */
	void setApplicationProcess(Process applicationProcess);
}