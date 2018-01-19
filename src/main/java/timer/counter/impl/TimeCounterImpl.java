package main.java.timer.counter.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import main.java.command.Command;
import main.java.command.CommandName;
import main.java.initApp.annotation.Setter;
import main.java.loader.LoadSaveToFile;
import main.java.logger.MainLogger;
import main.java.observer.TimeObserver;
import main.java.timer.container.TimeAndSettingsContainer;
import main.java.timer.counter.TimeCounter;
import main.java.timer.timer.Timer;

public class TimeCounterImpl implements TimeCounter
{
	private static final int SEC_TO_RELAX = 3000;
	private static final int QTY_OF_SETTING_PARAMETERS_IN_LINE = 3;

	private static final boolean DEFAULT_AUTO_CHANGE_DATE = true;
	private static final boolean DEFAULT_RELAX_REMINDER = true;
	private static final boolean DEFAULT_IS_RUNNING_APPLICATION = true;

	private static final String DELIMITER_SLASH = "/";
	private static final String DELIMITER_SPACE = " ";
	private static final String TASK_LIST_COMMAND = "tasklist";
	private static final String TASK_KILL_COMMAND = "taskkill /IM ";

	@Setter
	private TimeAndSettingsContainer container;
	@Setter
	private Command command;
	@Setter
	private LoadSaveToFile saver;
	@Setter
	private Timer timer;

	private List<TimeObserver> observers = new ArrayList<>();

	@Override
	public void loadDataAndInitTimer()
	{
		loadDataFromFile();
		if (!timer.hasCommand())
		{
			timer.setCommand(this::stopOrIncreaseTime);
		}
	}

	private void loadDataFromFile()
	{
		setDefaultTime();
		setDefaultSettings();

		List<String> loadData = saver.loadData();
		convertAndSetDataFromList(loadData);

		notifyTimeObserversAboutTime();
		notifyTimeObserversAboutSettings();
	}

	private void setDefaultTime()
	{
		container.setCurrentTimeValue(0);
		container.setTodayTimeValue(0);
		container.setTotalTimeValue(0);
	}

	private void setDefaultSettings()
	{
		container.setAutoChangeDate(DEFAULT_AUTO_CHANGE_DATE);
		container.setRelaxReminder(DEFAULT_RELAX_REMINDER);
		container.setIsRunningApplication(DEFAULT_IS_RUNNING_APPLICATION);
	}

	private void convertAndSetDataFromList(List<String> loadedData)
	{
		for (String tmp : loadedData)
		{
			String[] strings = tmp.split(DELIMITER_SLASH);
			if (strings.length != QTY_OF_SETTING_PARAMETERS_IN_LINE)
			{
				convertDataAndAddToContainerStorage(strings);
			}
			else
			{
				container.setApplication(convertDataToFile(strings));
				container.setAutoChangeDate(convertDataToAutoChangeDateSetting(strings));
				container.setRelaxReminder(convertDataToRelaxReminderSetting(strings));
			}
		}
	}

	private void convertDataAndAddToContainerStorage(String[] strings)
	{
		container.putDateAndTimeToStorage(
				LocalDate.of(Integer.parseInt(strings[2]), Integer.parseInt(strings[1]), Integer.parseInt(strings[0])),
				Long.parseLong(strings[3]));
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

	private void stopOrIncreaseTime()
	{
		if (isApplicationProcessRunning())
		{
			increaseAllTimes();
			checkRelaxTimeAndStopTimerIfNeeded();
			checkChangingDate();
		}
		else
		{
			stopTimer();
		}
	}

	private boolean isApplicationProcessRunning()
	{
		return isApplicationProcessAlive() && container.getApplication() != null && container.isRunningApplication();
	}

	private boolean isApplicationProcessAlive()
	{
		return container.getApplicationProcess() != null && container.getApplicationProcess().isAlive();
	}

	private void stopTimer()
	{
		timer.stop();
		notifyTimeObserversAboutTiming();
	}

	private void increaseAllTimes()
	{
		container.increaseTimeBySecond();
		notifyTimeObserversAboutTime();
	}

	private void checkRelaxTimeAndStopTimerIfNeeded()
	{
		if (container.getCurrentTimeValue() % SEC_TO_RELAX == 0 && container.isRelaxReminder())
		{
			stopTimer();
			if (!command.executeCommand(CommandName.VIEW_IS_CHOSEN_RELAX.name()))
			{
				startTimer();
			}
		}
	}

	private void startTimer()
	{
		startApplicationProcess();
		timer.start();
		notifyTimeObserversAboutTiming();
	}

	private void startApplicationProcess()
	{
		if (!isApplicationProcessAlive() && container.getApplication() != null)
		{
			String applicationName = container.getApplication().getName();
			if (isProcessWithSameNameAlreadyRun(applicationName))
			{
				container.setIsRunningApplication(command.executeCommand(
						CommandName.VIEW_IS_USER_AGREE_TO_CONNECT_SELECTED_APP.name()));
				if (container.isRunningApplication())
				{
					closeAllProcessesWithSameNames(applicationName);
				}
				else
				{
					return;
				}
			}
			startProcessForApplication();
		}
	}

	private void closeAllProcessesWithSameNames(String name)
	{
		try
		{
			while (isProcessWithSameNameAlreadyRun(name))
			{
				Process process = Runtime.getRuntime().exec(TASK_KILL_COMMAND + name);
				process.waitFor();
			}
		}
		catch (InterruptedException | IOException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
		}
	}

	private boolean isProcessWithSameNameAlreadyRun(String name)
	{
		try (BufferedReader input = new BufferedReader(
				new InputStreamReader(Runtime.getRuntime().exec(TASK_LIST_COMMAND).getInputStream())))
		{
			String line;
			while ((line = input.readLine()) != null)
			{
				if (Stream.of(line.split(DELIMITER_SPACE)).anyMatch(n -> n.equalsIgnoreCase(name)))
				{
					return true;
				}
			}
		}
		catch (IOException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
		}
		return false;
	}

	private void startProcessForApplication()
	{
		try
		{
			container.setApplicationProcess(Runtime.getRuntime().exec(container.getApplication().getAbsolutePath()));
		}
		catch (IOException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
		}
	}

	private void checkChangingDate()
	{
		LocalDate currentDate = LocalDate.now();
		if (!container.getTodayDate().equals(currentDate))
		{
			changeDate();
			container.setTodayDate(currentDate);
		}
	}

	private void changeDate()
	{
		if (container.isAutoChangeDate())
		{
			saveData();
			container.setCurrentTimeValue(0);
			container.setTodayTimeValue(0);
			notifyTimeObserversAboutTime();
		}
	}

	@Override
	public void saveData()
	{
		container.putDateAndTimeToStorage(container.getTodayDate(), container.getTodayTimeValue());
		saveDataToFile();
	}

	private void saveDataToFile()
	{
		List<String> dataToSave = new ArrayList<>();
		convertSettingsAndAddToList(dataToSave);
		convertTimeAndAddToList(dataToSave);
		saver.saveData(dataToSave);
	}

	private void convertSettingsAndAddToList(List<String> dataToSave)
	{
		String fileName = container.getApplication() != null ? container.getApplication().getAbsolutePath() : "";
		dataToSave.add(fileName + DELIMITER_SLASH + container.isAutoChangeDate() + DELIMITER_SLASH
				+ container.isRelaxReminder());
	}

	private void convertTimeAndAddToList(List<String> dataToSave)
	{
		container.getDatesFromStorage().forEach(date -> dataToSave
				.add(date.getDayOfMonth() + DELIMITER_SLASH + date.getMonthValue() + DELIMITER_SLASH + date.getYear()
						+ DELIMITER_SLASH + container.getTimeFromStorageByDate(date)));
	}

	@Override
	public void closeApplication()
	{
		stopTimer();
		if (isApplicationProcessAlive())
		{
			container.getApplicationProcess().destroy();
		}
	}

	@Override
	public boolean isChangedTimeOrSettings()
	{
		stopTimer();
		return isChangedTime() || isChangedSettings();
	}

	private boolean isChangedTime()
	{
		return (container.getDatesFromStorage().stream().mapToLong(date ->
				container.getTimeFromStorageByDate(date)).sum() != container.getTotalTimeValue());
	}

	private boolean isChangedSettings()
	{
		List<String> loadData = saver.loadData();
		for (String tmp : loadData)
		{
			String[] strings = tmp.split(DELIMITER_SLASH);
			if (strings.length == QTY_OF_SETTING_PARAMETERS_IN_LINE)
			{
				File loadedFile = convertDataToFile(strings);
				boolean loadedAutoChangeDate = convertDataToAutoChangeDateSetting(strings);
				boolean loadedRelaxReminder = convertDataToRelaxReminderSetting(strings);
				return !isEqualsCurrentAndLoadedSettings(loadedFile, loadedAutoChangeDate, loadedRelaxReminder);
			}
		}
		return isEqualsCurrentAndDefaultSettings();
	}

	private boolean isEqualsCurrentAndLoadedSettings(File loadedFile, boolean loadedAutoChangeDate,
			boolean loadedRelaxReminder)
	{
		return ((container.getApplication() != null && container.getApplication().equals(loadedFile))
				|| (container.getApplication() == null && loadedFile == null))
				&& container.isAutoChangeDate() == loadedAutoChangeDate
				&& container.isRelaxReminder() == loadedRelaxReminder;
	}

	private boolean isEqualsCurrentAndDefaultSettings()
	{
		return container.getApplication() != null || container.isAutoChangeDate() != DEFAULT_AUTO_CHANGE_DATE
				|| container.isRelaxReminder() != DEFAULT_RELAX_REMINDER;
	}

	@Override
	public void addTimeObserver(TimeObserver observer)
	{
		observers.add(observer);
	}

	@Override
	public void notifyTimeObserversAboutTime()
	{
		observers.forEach(obs -> obs.updateTime(
				Arrays.asList(container.getCurrentTimeValue(), container.getTodayTimeValue(),
						container.getTotalTimeValue())));
	}

	@Override
	public void notifyTimeObserversAboutSettings()
	{
		observers.forEach(obs -> obs
				.updateSettings(container.isAutoChangeDate(), container.isRelaxReminder(), container.getApplication()));
	}

	@Override
	public void notifyTimeObserversAboutTiming()
	{
		observers.forEach(obs -> obs.updateTiming(timer.isRunning()));
	}

	@Override
	public void updateTime(List<Long> timeList)
	{
		if (timeList != null && timeList.size() == 3)
		{
			if (timeList.get(0) != null && timeList.get(0) == 0)
			{
				eraseCurrentTime();
			}
			if (timeList.get(1) != null && timeList.get(1) == 0)
			{
				eraseTodayTime();
			}
			if (timeList.get(2) != null && timeList.get(2) == 0)
			{
				eraseTotalTime();
			}
		}
	}

	private void eraseCurrentTime()
	{
		stopTimer();
		container.increaseTodayTimeByDelta(-container.getCurrentTimeValue());
		container.increaseTotalTimeByDelta(-container.getCurrentTimeValue());
		container.setCurrentTimeValue(0);
		notifyTimeObserversAboutTime();
	}

	private void eraseTodayTime()
	{
		stopTimer();
		container.setCurrentTimeValue(0);
		container.increaseTotalTimeByDelta(-container.getTodayTimeValue());
		container.setTodayTimeValue(0);
		notifyTimeObserversAboutTime();
	}

	private void eraseTotalTime()
	{
		stopTimer();
		setDefaultTime();
		notifyTimeObserversAboutTime();
	}

	@Override
	public void updateSettings(boolean autoChangeDate, boolean relaxReminder, File file)
	{
		container.setAutoChangeDate(autoChangeDate);
		container.setRelaxReminder(relaxReminder);
		container.setApplication(file);
	}

	@Override
	public void updateTiming(boolean isStart)
	{
		if (isStart)
		{
			startTimer();
		}
		else
		{
			stopTimer();
		}
	}
}