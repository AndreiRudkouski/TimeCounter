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
	 * Loads and decodes data from file.
	 *
	 * @param dateTimeMap {@link TreeMap} for data filling
	 */
	void loadData(Map<LocalDate, AtomicLong> dateTimeMap);

	/**
	 * Loads and decodes application name.
	 *
	 * @return application name or null
	 */
	String loadApplication();

	/**
	 * Encodes and saves data to file.
	 *
	 * @param dateTimeMap {@link TreeMap} for data saving
	 * @param file application name. The filename argument must be an absolute path name.
	 */
	void saveData(Map<LocalDate, AtomicLong> dateTimeMap, String file);

	/**
	 * Sets {@link File} file for working with it.
	 *
	 * @param file {@link File} file
	 */
	void setFile(File file);
}
