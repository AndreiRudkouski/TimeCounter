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

import javax.swing.*;

import timeCounter.command.CommandName;
import timeCounter.command.ICommand;
import timeCounter.counter.ITimeCounter;
import timeCounter.init.annotation.Setter;
import timeCounter.load.ILoadSaveToFile;
import timeCounter.logger.MainLogger;
import timeCounter.observer.ITimeObserver;

public class TimeCounter implements ITimeCounter
{
	private final static int SEC_TO_RELAX = 3000;
	private final static boolean DEFAULT_AUTO_CHANGE_DATE = true;
	private final static boolean DEFAULT_RELAX_REMINDER = true;

	@Setter
	private ICommand command;
	@Setter
	private ILoadSaveToFile saver;
	private Timer timer;

	private AtomicLong currentTime = new AtomicLong();
	private AtomicLong todayTime = new AtomicLong();
	private AtomicLong totalTime = new AtomicLong();
	private LocalDate todayDate = LocalDate.now();
	private Map<LocalDate, AtomicLong> dateTimeMap = new TreeMap<>();
	private boolean autoChangeDate;
	private boolean relaxReminder;
	private File file;
	private Process process;

	private List<ITimeObserver> observers = new ArrayList<>();

	// Settings for time counter
	private int timerPause = 1000;
	private long countTime = 0;

	private static final String DELIMITER = "/";

	public TimeCounter()
	{
		timer = new Timer(timerPause, (e) ->
		{
			correctTimeCounter();
			checkApplication();
		});
		autoChangeDate = DEFAULT_AUTO_CHANGE_DATE;
		relaxReminder = DEFAULT_RELAX_REMINDER;
	}

	@Override
	public void loadData()
	{
		timer.restart();
		stopTimer();
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

		if (closeApp && process != null && process.isAlive())
		{
			process.destroy();
		}

		return (dateTimeMap.containsKey(todayDate) && dateTimeMap.get(todayDate).get() != todayTime.get())
				|| (dateTimeMap.values().stream().mapToLong(AtomicLong::get).sum() != totalTime.get())
				|| isChangedSettings();
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
		if ((process == null || !process.isAlive()) && file != null)
		{
			if (isRunningProcess(file.getName()))
			{
				if (command.executeCommand(CommandName.VIEW_RUNNING_APPLICATION_NOTICE.name()))
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
					MainLogger.getLogger().severe(e.toString());
				}
			}
		}

		correctTimeCounter();
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
		// Determine OS type (Windows or Linux)
		String delimiter = " ";
		String env = Stream.of(System.getProperties().getProperty("os.name").split(delimiter)).anyMatch(
				s -> s.equalsIgnoreCase("Windows")) ?
				System.getenv("windir") + "\\system32\\" + "tasklist.exe" : "ps -e";

		try
		{
			String line;
			Process p = Runtime.getRuntime().exec(env);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null)
			{
				if (Stream.of(line.split(delimiter)).anyMatch(n -> n.equalsIgnoreCase(name)))
				{
					return true;
				}
			}
			input.close();
		}
		catch (IOException e)
		{
			MainLogger.getLogger().severe(e.toString());
		}
		return false;
	}

	private void checkChangeDate()
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

	private void correctTimeCounter()
	{
		// Correct the delay of time counter
		if (countTime != 0 && timer.isRunning())
		{
			timerPause = timerPause - (int) Math.round((System.nanoTime() - countTime) / 1000000d - 1000);
			countTime = System.nanoTime();
			timer.setDelay(timerPause);
		}
		else
		{
			countTime = System.nanoTime();
		}
	}

	private void checkApplication()
	{
		if (process != null && !process.isAlive())
		{
			stopTimer();
		}
		else
		{
			incrementTime();
			checkRelaxTime();
			checkChangeDate();
		}
	}

	private void saveDataToFile()
	{
		List<String> dataToSave = new ArrayList<>();
		String fileName = file != null ? file.getAbsolutePath() : "";
		dataToSave.add(fileName + DELIMITER + autoChangeDate + DELIMITER + relaxReminder);
		for (Map.Entry<LocalDate, AtomicLong> tmp : dateTimeMap.entrySet())
		{
			dataToSave.add(tmp.getKey().getDayOfMonth() + DELIMITER + tmp.getKey().getMonthValue() + DELIMITER +
					tmp.getKey().getYear() + DELIMITER + tmp.getValue());
		}
		saver.saveData(dataToSave);
	}

	private void loadDataFromFile()
	{
		List<String> loadData = saver.loadData();
		for (String tmp : loadData)
		{
			String[] stringTmp = tmp.split(DELIMITER);
			if (stringTmp.length != 3)
			{
				dateTimeMap.put(LocalDate.of(Integer.parseInt(stringTmp[2]),
						Integer.parseInt(stringTmp[1]), Integer.parseInt(stringTmp[0])),
						new AtomicLong(Long.parseLong(stringTmp[3])));
			}
			else
			{
				if (stringTmp[0] != null && !stringTmp[0].isEmpty())
				{
					file = new File(stringTmp[0]);
				}
				autoChangeDate = Boolean.parseBoolean(stringTmp[1]);
				relaxReminder = Boolean.parseBoolean(stringTmp[2]);
			}
		}
		notifyTimeObserversAboutSettings();
	}

	private boolean isChangedSettings()
	{
		File savedFile = null;
		boolean savedAutoChangeDate = DEFAULT_AUTO_CHANGE_DATE;
		boolean savedRelaxReminder = DEFAULT_RELAX_REMINDER;
		List<String> loadData = saver.loadData();
		for (String tmp : loadData)
		{
			String[] stringTmp = tmp.split(DELIMITER);
			if (stringTmp.length == 3)
			{

				if (stringTmp[0] != null && !stringTmp[0].isEmpty())
				{
					savedFile = new File(stringTmp[0]);
				}
				savedAutoChangeDate = Boolean.parseBoolean(stringTmp[1]);
				savedRelaxReminder = Boolean.parseBoolean(stringTmp[2]);
				break;
			}
		}
		return (file != null && !file.equals(savedFile)) || (file == null && savedFile != null)
				|| autoChangeDate != savedAutoChangeDate || relaxReminder != savedRelaxReminder;
	}

	@Override
	public void addTimeObserver(ITimeObserver observer)
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