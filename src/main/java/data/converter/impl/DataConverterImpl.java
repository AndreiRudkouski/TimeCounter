package main.java.data.converter.impl;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import main.java.data.container.DataContainer;
import main.java.data.converter.DataConverter;
import main.java.initApp.annotation.Setter;

public class DataConverterImpl implements DataConverter
{
	private static final boolean DEFAULT_AUTO_CHANGE_DATE = true;
	private static final boolean DEFAULT_RELAX_REMINDER = true;
	private static final boolean DEFAULT_IS_RUNNING_APPLICATION = true;

	private static final String DELIMITER_SLASH = "/";

	private static final int QTY_OF_SETTING_PARAMETERS_IN_LINE = 3;

	@Setter
	private DataContainer container;

	@Override
	public void convertDataAndInitTimeContainer(List<String> loadedData)
	{
		setDefaultTime();
		setDefaultSettings();

		convertAndSetDataFromList(loadedData);
	}

	private void setDefaultTime()
	{
		container.setCurrentTimeValue(0);
		container.setTodayTimeValue(0);
		container.setTotalTimeValue(0);
	}

	private void setDefaultSettings()
	{
		container.setLoadedAutoChangeDateFlag(DEFAULT_AUTO_CHANGE_DATE);
		container.setLoadedRelaxReminderFlag(DEFAULT_RELAX_REMINDER);
		container.setLoadedRunningApplicationFlag(DEFAULT_IS_RUNNING_APPLICATION);
		container.setLoadedApplication(null);
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
		container.putDateAndTimeToStorage(
				LocalDate.of(Integer.parseInt(strings[2]), Integer.parseInt(strings[1]), Integer.parseInt(strings[0])),
				Long.parseLong(strings[3]));
	}

	private void convertDataToSettingsAndAddToContainer(String[] strings)
	{
		container.setCurrentApplication(convertDataToFile(strings));
		container.setCurrentAutoChangeDateFlag(convertDataToAutoChangeDateSetting(strings));
		container.setCurrentRelaxReminderFlag(convertDataToRelaxReminderSetting(strings));
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
		container.putDateAndTimeToStorage(container.getTodayDate(), container.getTodayTimeValue());
		List<String> dataToSave = new ArrayList<>();
		convertSettingsAndAddToList(dataToSave);
		convertTimeAndAddToList(dataToSave);
		return dataToSave;
	}

	private void convertSettingsAndAddToList(List<String> dataToSave)
	{
		String fileName =
				container.getCurrentApplication() != null ? container.getCurrentApplication().getAbsolutePath() : "";
		dataToSave.add(fileName + DELIMITER_SLASH + container.getCurrentAutoChangeDateFlag() + DELIMITER_SLASH
				+ container.getCurrentRelaxReminderFlag());
	}

	private void convertTimeAndAddToList(List<String> dataToSave)
	{
		container.getDatesFromStorage().forEach(date -> dataToSave
				.add(date.getDayOfMonth() + DELIMITER_SLASH + date.getMonthValue() + DELIMITER_SLASH + date.getYear()
						+ DELIMITER_SLASH + container.getTimeFromStorageByDate(date)));
	}
}
