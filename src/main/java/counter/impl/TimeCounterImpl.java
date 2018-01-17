package main.java.counter.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import main.java.command.Command;
import main.java.command.CommandName;
import main.java.counter.TimeCounter;
import main.java.counter.bean.TimeAndSettingsContainer;
import main.java.init.annotation.Setter;
import main.java.load.LoadSaveToFile;
import main.java.logger.MainLogger;
import main.java.observer.TimeObserver;
import main.java.timer.Timer;

public class TimeCounterImpl implements TimeCounter
{
	private static final int SEC_TO_RELAX = 3000;
	private static final int QTY_OF_SETTING_PARAMETERS_IN_LINE = 3;

	private static final boolean DEFAULT_AUTO_CHANGE_DATE = true;
	private static final boolean DEFAULT_RELAX_REMINDER = true;

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
		setTimeFromMap();
		if (!timer.hasCommand())
		{
			timer.setCommand(this::stopTimerOrIncrementTime);
		}
	}

	private void loadDataFromFile()
	{
		List<String> loadData = saver.loadData();
		if (!loadData.isEmpty())
		{
			convertAndSetDataFromList(loadData);
		}
		else
		{
			setDefaultSettings();
		}
		notifyTimeObserversAboutSettings();
	}

	private void setDefaultSettings()
	{
		container.setAutoChangeDate(DEFAULT_AUTO_CHANGE_DATE);
		container.setRelaxReminder(DEFAULT_RELAX_REMINDER);
	}

	private void convertAndSetDataFromList(List<String> loadedData)
	{
		for (String tmp : loadedData)
		{
			String[] strings = tmp.split(DELIMITER_SLASH);
			if (strings.length != QTY_OF_SETTING_PARAMETERS_IN_LINE)
			{
				convertDataToTimeAndAddToMap(strings);
			}
			else
			{
				container.setApplication(convertDataToFile(strings));
				container.setAutoChangeDate(convertDataToAutoChangeDateSetting(strings));
				container.setRelaxReminder(convertDataToRelaxReminderSetting(strings));
			}
		}
	}

	private void convertDataToTimeAndAddToMap(String[] strings)
	{
		container.getDateTimeMap().put(LocalDate.of(Integer.parseInt(strings[2]),
				Integer.parseInt(strings[1]), Integer.parseInt(strings[0])),
				new AtomicLong(Long.parseLong(strings[3])));
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

	private void setTimeFromMap()
	{
		container.getCurrentTime().set(0);
		container.setTodayTime(container.getDateTimeMap().containsKey(container.getTodayDate()) ?
				new AtomicLong(container.getDateTimeMap().get(container.getTodayDate()).get()) :
				new AtomicLong(0));
		container.getTotalTime().set(0);
		container.getDateTimeMap().forEach((date, time) -> container.getTotalTime().getAndAdd(time.get()));
		notifyTimeObserversAboutTime();
	}

	private void stopTimerOrIncrementTime()
	{
		if (!isApplicationProcessAlive() && container.getApplication() != null && container.isRunningApplication())
		{
			stopTimer();
		}
		else
		{
			incrementAllTimes();
			checkRelaxTimeAndStopTimerIfNeeded();
			checkChangingDate();
		}
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

	private void incrementAllTimes()
	{
		container.getCurrentTime().incrementAndGet();
		container.getTodayTime().incrementAndGet();
		container.getTotalTime().incrementAndGet();
		notifyTimeObserversAboutTime();
	}

	private void checkRelaxTimeAndStopTimerIfNeeded()
	{
		if (container.getCurrentTime().get() % SEC_TO_RELAX == 0 && container.isRelaxReminder())
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
			if (container.getDateTimeMap().containsKey(container.getTodayDate()))
			{
				container.getDateTimeMap().put(container.getTodayDate(), new AtomicLong(
						container.getDateTimeMap().get(container.getTodayDate())
								.getAndAdd(container.getCurrentTime().get())));
			}
			else
			{
				container.getDateTimeMap().put(container.getTodayDate(),
						new AtomicLong(container.getCurrentTime().get()));
			}
			saveDataToFile();
			container.getCurrentTime().set(0);
			container.getTodayTime().set(0);
			notifyTimeObserversAboutTime();
		}
	}

	@Override
	public void saveData()
	{
		if (container.getDateTimeMap().containsKey(container.getTodayDate()))
		{
			container.getDateTimeMap().put(container.getTodayDate(), container.getTodayTime());
		}
		else
		{
			container.getDateTimeMap().put(container.getTodayDate(), container.getCurrentTime());
		}
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
		for (Map.Entry<LocalDate, AtomicLong> tmp : container.getDateTimeMap().entrySet())
		{
			dataToSave.add(tmp.getKey().getDayOfMonth() + DELIMITER_SLASH
					+ tmp.getKey().getMonthValue() + DELIMITER_SLASH +
					tmp.getKey().getYear() + DELIMITER_SLASH + tmp.getValue());
		}
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
		return (container.getDateTimeMap().containsKey(container.getTodayDate()) && container.getDateTimeMap().get(
				container.getTodayDate()).get() != container.getTodayTime().get()) || (
				container.getDateTimeMap().values().stream().mapToLong(AtomicLong::get).sum() != container
						.getTotalTime().get());
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
				Arrays.asList(container.getCurrentTime().get(), container.getTodayTime().get(),
						container.getTotalTime().get())));
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
		container.getCurrentTime().set(0);
		notifyTimeObserversAboutTime();
	}

	private void eraseTodayTime()
	{
		stopTimer();
		container.getCurrentTime().set(0);
		container.getTodayTime().set(0);
		notifyTimeObserversAboutTime();
	}

	private void eraseTotalTime()
	{
		stopTimer();
		container.getCurrentTime().set(0);
		container.getTodayTime().set(0);
		container.getTotalTime().set(0);
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