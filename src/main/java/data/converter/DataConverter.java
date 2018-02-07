package main.java.data.converter;

import java.util.List;

/**
 * This interface identifies methods for data converter which converts data before and after saving.
 */
public interface DataConverter
{
	/**
	 * Converts data from the list and sets to the time container.
	 *
	 * @param loadedData list of parameters for filling
	 */
	void convertDataAndSetToContainer(List<String> loadedData);

	/**
	 * Converts data from time container to the list.
	 *
	 * @return list of parameters for saving
	 */
	List<String> convertDataFromTimeContainer();
}
