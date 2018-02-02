package main.java.data.loadSave;

/**
 * This interface identified methods for working with data.
 */
public interface LoadSaveToFile
{
	/**
	 * Loads and decodes data from file, also initializes the time container by data.
	 */
	void loadAndInitData();

	/**
	 * Encodes and saves data from the time container to file.
	 */
	void saveData();
}
