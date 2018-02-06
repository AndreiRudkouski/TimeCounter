package main.java.data.container;

import java.io.File;
import java.time.LocalDate;
import java.util.Set;

/**
 * This interface identifies methods for any container which contains time and settings.
 */
public interface DataContainer
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
	 * Returns the value of 'CurrentAutoChangeDate' flag.
	 *
	 * @return value of the flag
	 */
	boolean getCurrentAutoChangeDateFlag();

	/**
	 * Sets the value to 'CurrentAutoChangeDate' flag.
	 *
	 * @param currentAutoChangeDate value to set
	 */
	void setCurrentAutoChangeDateFlag(boolean currentAutoChangeDate);

	/**
	 * Returns the value of 'CurrentRelaxReminder' flag.
	 *
	 * @return value of the flag
	 */
	boolean getCurrentRelaxReminderFlag();

	/**
	 * Sets the value to 'CurrentRelaxReminder' flag.
	 *
	 * @param currentRelaxReminder value to set
	 */
	void setCurrentRelaxReminderFlag(boolean currentRelaxReminder);

	/**
	 * Returns the value of 'CurrentRunningApplication' flag.
	 *
	 * @return value of the flag
	 */
	boolean getCurrentRunningApplicationFlag();

	/**
	 * Sets the value to 'CurrentRunningApplication' flag.
	 *
	 * @param currentRunningApplication value to set
	 */
	void setCurrentRunningApplicationFlag(boolean currentRunningApplication);

	/**
	 * Returns the current application from the container.
	 *
	 * @return application
	 */
	File getCurrentApplication();

	/**
	 * Sets the current application to the container.
	 *
	 * @param currentApplication to sets
	 */
	void setCurrentApplication(File currentApplication);

	/**
	 * Returns the value of 'LoadedAutoChangeDate' flag.
	 *
	 * @return value of the flag
	 */
	boolean getLoadedAutoChangeDateFlag();

	/**
	 * Sets the value to 'LoadedAutoChangeDate' flag and sets the same value to 'CurrentAutoChangeDate' flag too.
	 *
	 * @param loadedAutoChangeDate value to set
	 */
	void setLoadedAutoChangeDateFlag(boolean loadedAutoChangeDate);

	/**
	 * Returns the value of 'LoadedRelaxReminder' flag.
	 *
	 * @return value of the flag
	 */
	boolean getLoadedRelaxReminderFlag();

	/**
	 * Sets the value to 'LoadedRelaxReminder' flag and sets the same value to 'CurrentRelaxReminder' flag too.
	 *
	 * @param loadedRelaxReminder value to set
	 */
	void setLoadedRelaxReminderFlag(boolean loadedRelaxReminder);

	/**
	 * Returns the value of 'LoadedRunningApplication' flag.
	 *
	 * @return value of the flag
	 */
	boolean getLoadedRunningApplicationFlag();

	/**
	 * Sets the value to 'LoadedRunningApplication' flag and sets the same value to 'CurrentRunningApplication' flag too.
	 *
	 * @param loadedRunningApplication value to set
	 */
	void setLoadedRunningApplicationFlag(boolean loadedRunningApplication);

	/**
	 * Returns the loaded application from the container.
	 *
	 * @return application
	 */
	File getLoadedApplication();

	/**
	 * Sets the loaded application to the container and sets the same value to the current application.
	 *
	 * @param loadedApplication to sets
	 */
	void setLoadedApplication(File loadedApplication);

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