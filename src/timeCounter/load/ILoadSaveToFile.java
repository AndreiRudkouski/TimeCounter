package timeCounter.load;

import java.io.File;
import java.util.List;

/**
 * This interface identified methods for working with data.
 */
public interface ILoadSaveToFile
{
	/**
	 * Loads and decodes data from file.
	 *
	 * @return list of parameters for filling
	 */
	List<String> loadData();

	/**
	 * Encodes and saves data to file.
	 *
	 * @param dataToSave list of parameter for saving
	 */
	void saveData(List<String> dataToSave);

	/**
	 * Sets {@link File} file for working with it.
	 *
	 * @param file {@link File} file
	 */
	void setFile(File file);
}
