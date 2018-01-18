package main.java.loader;

import java.util.List;

/**
 * This interface identified methods for working with data.
 */
public interface LoadSaveToFile
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
}
