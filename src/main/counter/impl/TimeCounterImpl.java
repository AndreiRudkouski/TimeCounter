package main.counter.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import main.command.Command;
import main.command.CommandName;
import main.counter.TimeCounter;
import main.init.annotation.Setter;
import main.load.LoadSaveToFile;
import main.logger.MainLogger;
import main.observer.TimeObserver;
import main.timer.Timer;

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
	private Command command;
	@Setter
	private LoadSaveToFile saver;
	@Setter
	private Timer timer;

	private AtomicLong currentTime = new AtomicLong();
	private AtomicLong todayTime = new AtomicLong();
	private AtomicLong totalTime = new AtomicLong();
	private LocalDate todayDate = LocalDate.now();
	private Map<LocalDate, AtomicLong> dateTimeMap = new TreeMap<>();
	private boolean autoChangeDate;
	private boolean relaxReminder;
	private boolean checkIsRunningApplication = true;
	private File application;
	private Process applicationProcess;

	private List<TimeObserver> observers = new ArrayList<>();

	public TimeCounterImpl()
	{
		autoChangeDate = DEFAULT_AUTO_CHANGE_DATE;
		relaxReminder = DEFAULT_RELAX_REMINDER;
	}

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
		convertDataFromListAndSetThemAsCurrent(loadData);
		notifyTimeObserversAboutSettings();
	}

	private void convertDataFromListAndSetThemAsCurrent(List<String> loadedData)
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
				application = convertDataToFile(strings);
				autoChangeDate = convertDataToAutoChangeDateSetting(strings);
				relaxReminder = convertDataToRelaxReminderSetting(strings);
			}
		}
	}

	private void convertDataToTimeAndAddToMap(String[] strings)
	{
		dateTimeMap.put(LocalDate.of(Integer.parseInt(strings[2]),
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
		currentTime.set(0);
		todayTime = dateTimeMap.containsKey(todayDate) ? new AtomicLong(dateTimeMap.get(todayDate).get()) :
				new AtomicLong(0);
		totalTime.set(0);
		dateTimeMap.forEach((date, time) -> totalTime.getAndAdd(time.get()));
		notifyTimeObserversAboutTime();
	}

	private void stopTimerOrIncrementTime()
	{
		if (!isApplicationProcessAlive() && application != null && checkIsRunningApplication)
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
		return applicationProcess != null && applicationProcess.isAlive();
	}

	private void stopTimer()
	{
		timer.stop();
		notifyTimeObserversAboutTiming();
	}

	private void incrementAllTimes()
	{
		currentTime.incrementAndGet();
		todayTime.incrementAndGet();
		totalTime.incrementAndGet();
		notifyTimeObserversAboutTime();
	}

	private void checkRelaxTimeAndStopTimerIfNeeded()
	{
		if (currentTime.get() % SEC_TO_RELAX == 0 && relaxReminder)
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
		if (!isApplicationProcessAlive() && application != null)
		{
			String applicationName = application.getName();
			if (isProcessWithSameNameAlreadyRun(applicationName))
			{
				checkIsRunningApplication = command.executeCommand(
						CommandName.VIEW_IS_USER_AGREE_TO_CONNECT_SELECTED_APP.name());
				if (checkIsRunningApplication)
				{
					closeAllProcessesWithSameNames(applicationName);
				}
				else
				{
					return;
				}
			}
			startProcessForCurrentApplication();
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

	private void startProcessForCurrentApplication()
	{
		try
		{
			applicationProcess = Runtime.getRuntime().exec(application.getAbsolutePath());
		}
		catch (IOException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
		}
	}

	private void checkChangingDate()
	{
		LocalDate currentDate = LocalDate.now();
		if (!todayDate.equals(currentDate))
		{
			changeDate();
			todayDate = currentDate;
		}
	}

	private void changeDate()
	{
		if (autoChangeDate)
		{
			if (dateTimeMap.containsKey(todayDate))
			{
				dateTimeMap.put(todayDate, new AtomicLong(dateTimeMap.get(todayDate).getAndAdd(currentTime.get())));
			}
			else
			{
				dateTimeMap.put(todayDate, new AtomicLong(currentTime.get()));
			}
			saveDataToFile();
			currentTime.set(0);
			todayTime.set(0);
			notifyTimeObserversAboutTime();
		}
	}

	@Override
	public void saveData()
	{
		if (dateTimeMap.containsKey(todayDate))
		{
			dateTimeMap.put(todayDate, todayTime);
		}
		else
		{
			dateTimeMap.put(todayDate, currentTime);
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
		String fileName = application != null ? application.getAbsolutePath() : "";
		dataToSave.add(fileName + DELIMITER_SLASH + autoChangeDate + DELIMITER_SLASH + relaxReminder);
	}

	private void convertTimeAndAddToList(List<String> dataToSave)
	{
		for (Map.Entry<LocalDate, AtomicLong> tmp : dateTimeMap.entrySet())
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
			applicationProcess.destroy();
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
		return (dateTimeMap.containsKey(todayDate) && dateTimeMap.get(todayDate).get() != todayTime.get())
				|| (dateTimeMap.values().stream().mapToLong(AtomicLong::get).sum() != totalTime.get());
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
		return ((application != null && application.equals(loadedFile)) || (application == null && loadedFile == null))
				&& autoChangeDate == loadedAutoChangeDate && relaxReminder == loadedRelaxReminder;
	}

	private boolean isEqualsCurrentAndDefaultSettings()
	{
		return application != null || autoChangeDate != DEFAULT_AUTO_CHANGE_DATE
				|| relaxReminder != DEFAULT_RELAX_REMINDER;
	}

	@Override
	public void addTimeObserver(TimeObserver observer)
	{
		observers.add(observer);
	}

	@Override
	public void notifyTimeObserversAboutTime()
	{
		observers.forEach(obs -> obs.updateTime(Arrays.asList(currentTime.get(), todayTime.get(), totalTime.get())));
	}

	@Override
	public void notifyTimeObserversAboutSettings()
	{
		observers.forEach(obs -> obs.updateSettings(autoChangeDate, relaxReminder, application));
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
		currentTime.set(0);
		notifyTimeObserversAboutTime();
	}

	private void eraseTodayTime()
	{
		stopTimer();
		currentTime.set(0);
		todayTime.set(0);
		notifyTimeObserversAboutTime();
	}

	private void eraseTotalTime()
	{
		stopTimer();
		currentTime.set(0);
		todayTime.set(0);
		totalTime.set(0);
		notifyTimeObserversAboutTime();
	}

	@Override
	public void updateSettings(boolean autoChangeDate, boolean relaxReminder, File file)
	{
		this.autoChangeDate = autoChangeDate;
		this.relaxReminder = relaxReminder;
		this.application = file;
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