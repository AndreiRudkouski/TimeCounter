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
	private static final String ENV_VARIABLE_FOR_WINDOWS = "windir";
	private static final String COMMAND_FOR_LINUX = "ps -e";
	private static final String COMMAND_FOR_WINDOWS = "\\system32\\tasklist.exe";
	private static final String PROPERTY_OS_NAME = "os.name";
	private static final String WINDOWS_OS_NAME = "Windows";
	private static final String TASK_KILL_COMMAND_FORMAT = "taskkill /IM %s /F";

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
	private boolean checkRunningApplication = true;
	private File file;
	private Process process;

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
			timer.setCommand(this::checkIfApplicationProcessIsRunning);
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

	@Override
	public void closeApplication()
	{
		stopTimer();
		if (isProcessAlive())
		{
			process.destroy();
		}
	}

	@Override
	public boolean isChangedTimeOrSettings()
	{
		stopTimer();
		return isChangedTime() || isChangedSettings();
	}

	private void checkRelaxTimeAndWaitEndOfRelax()
	{
		if (currentTime.get() % SEC_TO_RELAX == 0 && relaxReminder)
		{
			stopTimer();
			if (!command.executeCommand(CommandName.VIEW_CHOSEN_RELAX.name()))
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
		if (!isProcessAlive() && file != null)
		{
			if (isProcessAlreadyRun(file.getName()))
			{
				checkRunningApplication = command.executeCommand(
						CommandName.VIEW_IS_USER_AGREE_TO_CONNECT_SELECTED_APP.name());
				if (checkRunningApplication)
				{
					closeProcessesWithNamesEqualsApplication();
					startProcessForChosenApplication();
				}
			}
			else
			{
				startProcessForChosenApplication();
			}
		}
	}

	private void closeProcessesWithNamesEqualsApplication()
	{
		try
		{
			while (isProcessAlreadyRun(file.getName()))
			{
				Process process = Runtime.getRuntime().exec(String.format(TASK_KILL_COMMAND_FORMAT, file.getName()));
				while (process.isAlive())
				{
				}
			}
		}
		catch (IOException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
		}
	}

	private void startProcessForChosenApplication()
	{
		try
		{
			process = Runtime.getRuntime().exec(file.getAbsolutePath());
			while (!process.isAlive())
			{
			}
		}
		catch (IOException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
		}
	}

	private void stopTimer()
	{
		timer.stop();
		notifyTimeObserversAboutTiming();
	}

	private boolean isProcessAlreadyRun(String name)
	{
		try (BufferedReader input = new BufferedReader(
				new InputStreamReader(Runtime.getRuntime().exec(getEnvironmentCommand()).getInputStream())))
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

	private String getEnvironmentCommand()
	{
		// Determine OS type (Windows or Linux)
		return Stream.of(System.getProperties().getProperty(PROPERTY_OS_NAME).split(DELIMITER_SPACE)).anyMatch(
				s -> s.equalsIgnoreCase(WINDOWS_OS_NAME)) ?
				System.getenv(ENV_VARIABLE_FOR_WINDOWS) + COMMAND_FOR_WINDOWS : COMMAND_FOR_LINUX;
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

	private void setTimeFromMap()
	{
		currentTime.set(0);
		todayTime = dateTimeMap.containsKey(todayDate) ? new AtomicLong(dateTimeMap.get(todayDate).get()) :
				new AtomicLong(0);
		totalTime.set(0);
		dateTimeMap.forEach((date, time) -> totalTime.getAndAdd(time.get()));
		notifyTimeObserversAboutTime();
	}

	private void incrementAllTimes()
	{
		currentTime.incrementAndGet();
		todayTime.incrementAndGet();
		totalTime.incrementAndGet();
		notifyTimeObserversAboutTime();
	}

	private void checkIfApplicationProcessIsRunning()
	{
		if (!isProcessAlive() && file != null && checkRunningApplication)
		{
			stopTimer();
		}
		else
		{
			incrementAllTimes();
			checkRelaxTimeAndWaitEndOfRelax();
			checkChangingDate();
		}
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
		String fileName = file != null ? file.getAbsolutePath() : "";
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
				file = convertDataToFile(strings);
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
		return ((file != null && file.equals(loadedFile)) || (file == null && loadedFile == null))
				&& autoChangeDate == loadedAutoChangeDate && relaxReminder == loadedRelaxReminder;
	}

	private boolean isEqualsCurrentAndDefaultSettings()
	{
		return file != null || autoChangeDate != DEFAULT_AUTO_CHANGE_DATE || relaxReminder != DEFAULT_RELAX_REMINDER;
	}

	private boolean isProcessAlive()
	{
		return process != null && process.isAlive();
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
		observers.forEach(obs -> obs.updateSettings(autoChangeDate, relaxReminder, file));
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
		this.file = file;
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