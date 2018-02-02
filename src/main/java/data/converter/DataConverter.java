package main.java.data.converter;

import java.util.List;

public interface DataConverter
{
	/**
	 * Converts data from the list and filling the time container.
	 *
	 * @param loadedData list of parameters for filling
	 */
	void convertDataAndInitTimeContainer(List<String> loadedData);

	/**
	 * Converts data from time container to the list.
	 *
	 * @return list of parameters for saving
	 */
	List<String> convertDataFromTimeContainer();
}
