package main.java.data.converter.impl;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import main.java.data.container.DataContainerHelper;
import main.java.data.converter.DataConverter;
import main.java.initApp.annotation.Setter;

public class DataConverterImpl implements DataConverter
{
	private static final String DELIMITER_SLASH = "/";

	private static final int QTY_OF_SETTING_PARAMETERS_IN_LINE = 3;

	@Setter
	private DataContainerHelper containerHelper;

	@Override
	public void convertDataAndSetToContainer(List<String> loadedData)
	{
		containerHelper.setDefaultTime();
		containerHelper.setDefaultSettings();
		convertAndSetDataFromList(loadedData);
	}

	private void convertAndSetDataFromList(List<String> loadedData)
	{
		for (String tmp : loadedData)
		{
			String[] strings = tmp.split(DELIMITER_SLASH);
			if (strings.length != QTY_OF_SETTING_PARAMETERS_IN_LINE)
			{
				convertDataToTimeAndAddToContainer(strings);
			}
			else
			{
				convertDataToSettingsAndAddToContainer(strings);
			}
		}
	}

	private void convertDataToTimeAndAddToContainer(String[] strings)
	{
		containerHelper.putDateAndTimeToStorage(
				LocalDate.of(Integer.parseInt(strings[2]), Integer.parseInt(strings[1]), Integer.parseInt(strings[0])),
				Long.parseLong(strings[3]));
	}

	private void convertDataToSettingsAndAddToContainer(String[] strings)
	{
		containerHelper.initApplication(convertDataToFile(strings));
		containerHelper.initAutoChangeDateFlag(convertDataToAutoChangeDateSetting(strings));
		containerHelper.initRelaxReminderFlag(convertDataToRelaxReminderSetting(strings));
	}

	private File convertDataToFile(String[] strings)
	{
		File loadedFile = null;
		if (strings[0] != null && !strings[0].isEmpty())
		{
			loadedFile = new File(strings[0]);
		}
		return loadedFile;
	}

	private boolean convertDataToAutoChangeDateSetting(String[] strings)
	{
		return Boolean.parseBoolean(strings[1]);
	}

	private boolean convertDataToRelaxReminderSetting(String[] strings)
	{
		return Boolean.parseBoolean(strings[2]);
	}

	@Override
	public List<String> convertDataFromTimeContainer()
	{
		List<String> dataToSave = new ArrayList<>();
		convertSettingsAndAddToList(dataToSave);
		convertTimeAndAddToList(dataToSave);
		return dataToSave;
	}

	private void convertSettingsAndAddToList(List<String> dataToSave)
	{
		String fileName =
				containerHelper.getApplication() != null ? containerHelper.getApplication().getAbsolutePath() : "";
		dataToSave.add(fileName + DELIMITER_SLASH + containerHelper.getAutoChangeDateFlag() + DELIMITER_SLASH
				+ containerHelper.getRelaxReminderFlag());
	}

	private void convertTimeAndAddToList(List<String> dataToSave)
	{
		containerHelper.getDatesFromStorage().forEach(date -> dataToSave
				.add(date.getDayOfMonth() + DELIMITER_SLASH + date.getMonthValue() + DELIMITER_SLASH + date.getYear()
						+ DELIMITER_SLASH + containerHelper.getTimeFromStorageByDate(date)));
	}
}
