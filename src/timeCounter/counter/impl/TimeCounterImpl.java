package timeCounter.counter.impl;

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

import timeCounter.command.Command;
import timeCounter.command.CommandName;
import timeCounter.init.annotation.Setter;
import timeCounter.load.LoadSaveToFile;
import timeCounter.logger.MainLogger;
import timeCounter.observer.TimeObserver;
import timeCounter.timer.Timer;

public class TimeCounterImpl implements timeCounter.counter.TimeCounter
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
	private boolean checkApplication = true;
	private File file;
	private Process process;

	private List<TimeObserver> observers = new ArrayList<>();

	public TimeCounterImpl()
	{
		autoChangeDate = DEFAULT_AUTO_CHANGE_DATE;
		relaxReminder = DEFAULT_RELAX_REMINDER;
	}

	@Override
	public void loadData()
	{
		if (!timer.hasCommand())
		{
			timer.setCommand(this::checkApplication);
		}
		loadDataFromFile();
		assignTime();
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
	public boolean closeTimeCounter(Boolean saveData, Boolean closeApp)
	{
		stopTimer();

		if (saveData)
		{
			saveData();
		}

		if (closeApp && isProcessAlive())
		{
			process.destroy();
		}

		return isChangedTime() || isChangedSettings();
	}

	private void checkRelaxTime()
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
		if (!isProcessAlive() && file != null)
		{
			if (isRunningProcess(file.getName()))
			{
				checkApplication = command.executeCommand(CommandName.VIEW_RUNNING_APPLICATION_NOTICE.name());
				if (checkApplication)
				{
					stopTimer();
					return;
				}
			}
			else
			{
				try
				{
					process = Runtime.getRuntime().exec(file.getAbsolutePath());
				}
				catch (IOException e)
				{
					MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
				}
			}
		}

		timer.start();
		notifyTimeObserversAboutTiming();
	}

	private void stopTimer()
	{
		timer.stop();
		notifyTimeObserversAboutTiming();
	}

	private boolean isRunningProcess(String name)
	{
		try
		{
			String line;
			Process p = Runtime.getRuntime().exec(getEnvironmentCommand());
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null)
			{
				if (Stream.of(line.split(DELIMITER_SPACE)).anyMatch(n -> n.equalsIgnoreCase(name)))
				{
					return true;
				}
			}
			input.close();
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
			todayDate = currentDate;
		}
	}

	private void assignTime()
	{
		currentTime.set(0);
		todayTime = dateTimeMap.containsKey(todayDate) ? new AtomicLong(dateTimeMap.get(todayDate).get()) :
				new AtomicLong(0);
		totalTime.set(0);
		dateTimeMap.forEach((date, time) -> totalTime.getAndAdd(time.get()));
		notifyTimeObserversAboutTime();
	}

	private void incrementTime()
	{
		currentTime.incrementAndGet();
		todayTime.incrementAndGet();
		totalTime.incrementAndGet();
		notifyTimeObserversAboutTime();
	}

	private void checkApplication()
	{
		if (!isProcessAlive() && file != null && checkApplication)
		{
			stopTimer();
		}
		else
		{
			incrementTime();
			checkRelaxTime();
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
		convertDataFromList(loadData);
		notifyTimeObserversAboutSettings();
	}

	private void convertDataFromList(List<String> loadData)
	{
		for (String tmp : loadData)
		{
			String[] strings = tmp.split(DELIMITER_SLASH);
			if (strings.length != QTY_OF_SETTING_PARAMETERS_IN_LINE)
			{
				convertDataToTime(strings);
			}
			else
			{
				convertDataToSettings(strings);
			}
		}
	}
	private void convertDataToTime(String[] strings)
	{
		dateTimeMap.put(LocalDate.of(Integer.parseInt(strings[2]),
				Integer.parseInt(strings[1]), Integer.parseInt(strings[0])),
				new AtomicLong(Long.parseLong(strings[3])));
	}

	/**
	 * Converts gotten array to the counter settings.
	 *
	 * @return true if existing settings equals loaded ones otherwise false
	 */
	private boolean convertDataToSettings(String[] strings)
	{

		File loadedFile = null;
		if (strings[0] != null && !strings[0].isEmpty())
		{
			loadedFile = new File(strings[0]);
		}
		boolean loadedAutoChangeDate = Boolean.parseBoolean(strings[1]);
		boolean loadedRelaxReminder = Boolean.parseBoolean(strings[2]);

        boolean isEquals = isEqualsExistedAndLoadedSettings(loadedFile, loadedAutoChangeDate, loadedRelaxReminder);

		file = loadedFile;
		autoChangeDate = Boolean.parseBoolean(strings[1]);
		relaxReminder = Boolean.parseBoolean(strings[2]);

		return isEquals;
	}

	private boolean isEqualsExistedAndLoadedSettings(File loadedFile, boolean loadedAutoChangeDate, boolean loadedRelaxReminder)
    {
        return ((file != null && file.equals(loadedFile)) || (file == null && loadedFile == null))
                && autoChangeDate == loadedAutoChangeDate && relaxReminder == loadedRelaxReminder;
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
			String[] stringTmp = tmp.split(DELIMITER_SLASH);
			if (stringTmp.length == QTY_OF_SETTING_PARAMETERS_IN_LINE)
			{
				return !convertDataToSettings(stringTmp);
			}
		}
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