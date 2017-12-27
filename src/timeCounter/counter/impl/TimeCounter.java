package timeCounter.counter.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import javax.swing.*;

import timeCounter.counter.ITimeCounter;
import timeCounter.init.annotation.Run;
import timeCounter.init.annotation.Setter;
import timeCounter.load.ILoadSaveToFile;
import timeCounter.logger.MainLogger;
import timeCounter.view.IView;

public class TimeCounter implements ITimeCounter
{
	private final static int SEC_TO_RELAX = 3000;

	@Setter private IView window;
	@Setter private ILoadSaveToFile saver;
	private Timer timer;

	private AtomicLong currentTime = new AtomicLong(); //The current session time
	private AtomicLong todayTime = new AtomicLong();
	private AtomicLong totalTime = new AtomicLong();
	private LocalDate currentDate;
	private LocalDate todayDate = LocalDate.now();
	private Map<LocalDate, AtomicLong> dateTimeMap = new TreeMap<>();
	private File file;
	private Process process;

	// Settings for time counter
	private int timerPause = 1000;
	private int oneSecond = 1000;
	private long countTime = 0;

	private static final String DELIMITER = "/";

	public TimeCounter()
	{
		timer = new Timer(timerPause, (e) ->
		{
			correctTimeCounter();
			checkApplication();
		});
	}

	@Override
	public void eraseCurrentTime()
	{
		setButtonToStart();
		currentTime.set(0);
		window.getCurrentTimeField().setText(printTime(currentTime));
	}

	@Override
	public void eraseTodayTime()
	{
		setButtonToStart();
		eraseCurrentTime();
		todayTime.set(0);
		window.getTodayTimeField().setText(printTime(todayTime));
	}

	@Override
	public void eraseTotalTime()
	{
		setButtonToStart();
		dateTimeMap.clear();
		assignTime();
	}

	@Override
	public void loadData()
	{
		window.setButtonTextToStart();
		loadDataFromFile();
		if (file != null)
		{
			window.setApplicationLabel(file.getName());
		}
		assignTime();
		timer.restart();
		timer.stop();
	}

	@Run
	private void initLoadData()
	{
		window.createView();
		loadData();
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
	public void changeLocale()
	{
		window.changeLocale();
	}

	@Override
	public void chooseApplication()
	{
		File chosenFile = window.chooseApplication();
		if (chosenFile != null)
		{
			file = chosenFile;
			window.setApplicationLabel(file.getName());
		}
	}

	@Override
	public void pushStartStopButton()
	{
		if (timer.isRunning())
		{
			setButtonToStart();
		}
		else
		{
			setButtonToStop();
		}
	}

	@Override
	public boolean closeTimeCounter(boolean close)
	{
		setButtonToStart();
		boolean result = false;
		if ((!dateTimeMap.isEmpty() && ((dateTimeMap.containsKey(todayDate) && dateTimeMap.get(todayDate).get()
				!= todayTime.get()) || (!dateTimeMap.containsKey(todayDate) && todayTime.get() != 0))) ||
				(dateTimeMap.isEmpty() && todayTime.get() != 0))
		{
			result = true;
		}

		if (close)
		{
			saveData();
		}

		if (process != null && process.isAlive())
		{
			process.destroy();
		}
		return result;
	}

	@Override
	public void eraseApplication()
	{
		file = null;
		window.setApplicationLabel(null);
	}

	private void checkRelaxTime()
	{
		if (currentTime.get() % SEC_TO_RELAX == 0 && window.isRelaxReminder())
		{
			setButtonToStart();
			if (!window.timeRelaxReminder())
			{
				setButtonToStop();
			}
		}
	}

	private void setButtonToStop()
	{
		if ((process == null || !process.isAlive()) && file != null)
		{
			if (isRunningProcess(file.getName()))
			{
				if (window.runningApplicationNotice())
				{
					setButtonToStart();
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
		window.setButtonTextToStop();
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

	private void setButtonToStart()
	{
		window.setButtonTextToStart();
		timer.stop();
	}

	private void checkChangeDate()
	{
		currentDate = LocalDate.now();
		if (!todayDate.equals(currentDate))
		{
			if (window.isAutoChangeDate())
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
				window.getCurrentTimeField().setText(printTime(currentTime));
				window.getTodayTimeField().setText(printTime(todayTime));
			}
			todayDate = currentDate;
		}
	}

	private String printTime(AtomicLong second)
	{
		long sec = second.get();
		long hour = sec / (60 * 60);
		long day = hour / 24;
		long min = (sec - hour * 60 * 60) / 60;
		sec = sec - hour * 60 * 60 - min * 60;
		hour = hour - day * 24;
		if (day != 0)
		{
			return String.format("%1$02d-%2$02d:%3$02d:%4$02d", day, hour, min, sec);
		}
		return String.format("%1$02d:%2$02d:%3$02d", hour, min, sec);
	}

	private void assignTime()
	{
		currentTime.set(0);
		todayTime = dateTimeMap.containsKey(todayDate) ? new AtomicLong(dateTimeMap.get(todayDate).get()) :
				new AtomicLong(0);
		totalTime.set(0);
		dateTimeMap.forEach((date, time) -> totalTime.getAndAdd(time.get()));
		window.getCurrentTimeField().setText(printTime(currentTime));
		window.getTodayTimeField().setText(printTime(todayTime));
		window.getTotalTimeField().setText(printTime(totalTime));
	}

	private void incrementTime()
	{
		currentTime.incrementAndGet();
		window.getCurrentTimeField().setText(printTime(currentTime));
		todayTime.incrementAndGet();
		window.getTodayTimeField().setText(printTime(todayTime));
		totalTime.incrementAndGet();
		window.getTotalTimeField().setText(printTime(totalTime));
	}

	private void correctTimeCounter()
	{
		// Correct the delay of time counter
		if (countTime != 0 && timer.isRunning())
		{
			timerPause = timerPause - (int) Math.round((System.nanoTime() - countTime) / 1000000d - oneSecond);
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
			setButtonToStart();
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
		dataToSave.add(fileName + DELIMITER + window.isRelaxReminder() + DELIMITER + window.isAutoChangeDate());
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
				window.setRelaxReminder(Boolean.parseBoolean(stringTmp[1]));
				window.setAutoChangeDate(Boolean.parseBoolean(stringTmp[2]));
			}
		}
	}
}