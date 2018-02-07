package main.java.data.container;

import java.time.LocalDate;
import java.util.Set;

/**
 * This interface identifies methods for the helper which works with time data from {@link DataContainer}'s container.
 */
public interface TimeDataHelper
{
	/**
	 * Returns the value of the current time.
	 *
	 * @return value in seconds
	 */
	long getCurrentTimeValue();

	/**
	 * Returns the value of the today time.
	 *
	 * @return value in seconds
	 */
	long getTodayTimeValue();

	/**
	 * Returns the value of the total time.
	 *
	 * @return value in seconds
	 */
	long getTotalTimeValue();

	/**
	 * Sets default values to all time fields.
	 */
	void setDefaultTime();

	/**
	 * Puts pair of of date and appropriate time to the storage of date and time.
	 *
	 * @param date value
	 * @param time appropriate time value in seconds
	 */
	void putDateAndTimeToStorage(LocalDate date, long time);

	/**
	 * Returns set of dates from storage.
	 *
	 * @return set of dates
	 */
	Set<LocalDate> getDatesFromStorage();

	/**
	 * Returns stored time from the storage by given date.
	 *
	 * @return time value in seconds
	 */
	long getTimeFromStorageByDate(LocalDate date);

	/**
	 * Increases all time fields by one second.
	 */
	void increaseTimeBySecond();

	/**
	 * Checks if time is changed from the last saved value.
	 *
	 * @return true if time is changed otherwise false
	 */
	boolean isChangedTime();

	/**
	 * Checks if time is for relaxing appropriate the relax period.
	 *
	 * @param relaxPeriod time between relaxes
	 * @return true if time is for relaxing otherwise false
	 */
	boolean isTimeToRelax(long relaxPeriod);

	/**
	 * Erases the current time field and fields which are coupling with it.
	 */
	void eraseCurrentTime();

	/**
	 * Erases the today time field and fields which are coupling with it.
	 */
	void eraseTodayTime();

	/**
	 * Erases the total time field and fields which are coupling with it.
	 */
	void eraseTotalTime();

	/**
	 * Checks if date should be changed and if it is true changes date and time.
	 *
	 * @param currentDate the current date
	 * @return true if date should be changed otherwise false
	 */
	boolean isDateShouldBeChanged(LocalDate currentDate);
}
