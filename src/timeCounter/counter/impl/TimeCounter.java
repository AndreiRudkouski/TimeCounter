package timeCounter.counter.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import javax.swing.*;

import timeCounter.counter.ITimeCounter;
import timeCounter.gui.IGUIWindow;
import timeCounter.load.ILoadSaveToFile;

public class TimeCounter implements ITimeCounter
{
	private final static int MIN_TO_RELAX = 50;

	private IGUIWindow window;
	private ILoadSaveToFile saver;
	private Timer timer = new Timer(1000, (e) -> checkApplication());

	private AtomicBoolean beginCount = new AtomicBoolean();
	private AtomicBoolean pause = new AtomicBoolean(true);
	private AtomicLong currentTime = new AtomicLong(); //The current session time
	private AtomicLong todayTime = new AtomicLong();
	private AtomicLong totalTime = new AtomicLong();
	private LocalDate currentDate;
	private LocalDate todayDate = LocalDate.now();
	private Map<LocalDate, AtomicLong> dateTimeMap = new TreeMap<>();
	private File file;
	private Process process;

	public TimeCounter()
	{
	}

	public TimeCounter(IGUIWindow window, ILoadSaveToFile saver)
	{
		this.window = window;
		this.saver = saver;
	}

	@Override
	public void eraseCurrentTime()
	{
		setStartButton();
		currentTime.set(0);
		window.getCurrentTimeField().setText(printTime(currentTime));
	}

	@Override
	public void eraseTodayTime()
	{
		setStartButton();
		eraseCurrentTime();
		todayTime.set(0);
		window.getTodayTimeField().setText(printTime(todayTime));
	}

	@Override
	public void eraseTotalTime()
	{
		setStartButton();
		dateTimeMap.clear();
		assignTime();
	}

	@Override
	public void loadData()
	{
		if (!dateTimeMap.isEmpty())
		{
			beginCount.set(false);
			pause.set(true);
			window.setStartTextButton();
		}
		saver.loadData(dateTimeMap);
		String name = saver.loadApplication();
		if (name != null)
		{
			file = new File(name);
			window.setApplicationLabel(file.getName());
		}
		assignTime();
		timer.restart();
		timer.stop();
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
		saver.saveData(dateTimeMap, file.getAbsolutePath());
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
		if (file == null || chosenFile != null)
		{
			file = chosenFile;
			window.setApplicationLabel(file.getName());
		}
	}

	@Override
	public void pushStartStopButton()
	{
		if (!beginCount.get() || pause.get())
		{
			setStopButton();
		}
		else
		{
			setStartButton();
		}
	}

	@Override
	public void closeApplication()
	{
		if (process != null && process.isAlive())
		{
			process.destroy();
		}
	}

	private void checkRelaxTime()
	{
		if (currentTime.get() % MIN_TO_RELAX == 0 && window.isRelaxReminder() && window.timeRelaxReminder())
		{
			setStartButton();
		}
	}

	private void setStopButton()
	{
		if ((process == null || !process.isAlive()) && file != null)
		{
			if (isRunningProcess(file.getName()))
			{
				if (window.runningApplicationNotice())
				{
					setStartButton();
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
					/*NOP*/
				}
			}
		}

		beginCount.set(true);
		pause.set(false);
		timer.start();
		window.setStopTextButton();
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
			/*NOP*/
		}
		return false;
	}

	private void setStartButton()
	{
		pause.set(true);
		window.setStartTextButton();
		timer.stop();
	}

	private void checkChangeDate()
	{
		currentDate = LocalDate.now();
		if (!todayDate.equals(currentDate))
		{
			if (window.changeDate())
			{
				if (dateTimeMap.containsKey(todayDate))
				{
					dateTimeMap.put(todayDate, new AtomicLong(dateTimeMap.get(todayDate).getAndAdd(currentTime.get())));
				}
				else
				{
					dateTimeMap.put(todayDate, currentTime);
				}
				saver.saveData(dateTimeMap, file.getAbsolutePath());
				setStartButton();
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
		todayTime = dateTimeMap.containsKey(todayDate) ? dateTimeMap.get(todayDate) : new AtomicLong(0);
		totalTime.set(0);
		dateTimeMap.forEach((date, time) -> totalTime.getAndAdd(time.get()));
		window.getCurrentTimeField().setText(printTime(currentTime));
		window.getTodayTimeField().setText(printTime(todayTime));
		window.getTotalTimeField().setText(printTime(totalTime));
	}

	private void incrementCurrentTime()
	{
		currentTime.incrementAndGet();
		window.getCurrentTimeField().setText(printTime(currentTime));
	}

	private void incrementTodayTime()
	{
		todayTime.incrementAndGet();
		window.getTodayTimeField().setText(printTime(todayTime));
	}

	private void incrementTotalTime()
	{
		totalTime.incrementAndGet();
		window.getTotalTimeField().setText(printTime(totalTime));
	}

	private void checkApplication()
	{
		if (process != null && !process.isAlive())
		{
			setStartButton();
		}
		else
		{
			incrementCurrentTime();
			incrementTodayTime();
			incrementTotalTime();
			checkRelaxTime();
			checkChangeDate();
		}
	}
}