package timeCounter.load;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This interface identified methods for working with data.
 */
public interface ILoadSaveToFile
{
	/**
	 * Loads and decodes data from file and fills specified map.
	 *
	 * @param dateTimeMap {@link TreeMap} for data filling
	 */
	void loadTime(Map<LocalDate, AtomicLong> dateTimeMap);

	/**
	 * Encodes and saves data to file from specified map.
	 *
	 * @param dateTimeMap {@link TreeMap} for data saving
	 */
	void saveTime(Map<LocalDate, AtomicLong> dateTimeMap);

	/**
	 * Sets {@link File} file for working with it.
	 *
	 * @param file {@link File} file
	 */
	void setFile(File file);
}
