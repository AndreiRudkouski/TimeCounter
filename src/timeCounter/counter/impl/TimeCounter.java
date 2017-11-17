package timeCounter.counter.impl;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import timeCounter.counter.ITimeCounter;
import timeCounter.gui.IGUIWindow;
import timeCounter.load.ILoadSaveToFile;
import timeCounter.timing.ITimeRunning;

public class TimeCounter implements ITimeCounter
{
	private final static int MIN_TO_RELAX = 50;

	private IGUIWindow window;
	private ILoadSaveToFile saver;
	private ITimeRunning timer;

	private AtomicBoolean beginCount = new AtomicBoolean();
	private AtomicBoolean pause = new AtomicBoolean(true);
	private AtomicLong currentTime = new AtomicLong(); //The current session time
	private AtomicLong todayTime = new AtomicLong();
	private AtomicLong totalTime = new AtomicLong();
	private LocalDate currentDate;
	private LocalDate todayDate = LocalDate.now();
	private Map<LocalDate, AtomicLong> dateTimeMap = new TreeMap<>();

	@Override
	public void incrementCurrentTime()
	{
		currentTime.incrementAndGet();
		window.getCurrentTimeField().setText(printTime(currentTime));
	}

	@Override
	public void eraseCurrentTime()
	{
		setStartButton();
		currentTime.set(0);
		window.getCurrentTimeField().setText(printTime(currentTime));
	}

	@Override
	public void incrementTodayTime()
	{
		todayTime.incrementAndGet();
		window.getTodayTimeField().setText(printTime(todayTime));
	}

	@Override
	public void eraseTodayTime()
	{
		setStartButton();
		todayTime.set(0);
		window.getTodayTimeField().setText(printTime(todayTime));
	}

	@Override
	public void incrementTotalTime()
	{
		totalTime.incrementAndGet();
		window.getTotalTimeField().setText(printTime(totalTime));
	}

	@Override
	public void eraseTotalTime()
	{
		setStartButton();
		dateTimeMap.clear();
		assignTime();
	}

	@Override
	public boolean isTimeRelaxSelect()
	{
		return window.isRelaxReminder();
	}

	public void checkRelaxTime()
	{
		if (currentTime.get() % MIN_TO_RELAX == 0 && isTimeRelaxSelect() && window.timeRelaxReminder())
		{
			setStartButton();
		}
	}

	@Override
	public boolean isBeginCount()
	{
		return beginCount.get();
	}

	@Override
	public boolean isPause()
	{
		return pause.get();
	}

	@Override
	public void setStopButton()
	{
		beginCount.set(true);
		pause.set(false);
		window.setStopTextButton();
	}

	@Override
	public void setStartButton()
	{
		pause.set(true);
		window.setStartTextButton();
	}

	@Override
	public ITimeRunning getTimer()
	{
		return timer;
	}

	@Override
	public void setTimer(ITimeRunning timer)
	{
		if (this.timer != null)
		{
			this.timer.stopExecute();
		}
		this.timer = timer;
	}

	@Override
	public void checkChangeDate()
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
				saver.saveTime(dateTimeMap);
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
		long min = (sec - hour * 60 * 60) / 60;
		sec = sec - hour * 60 * 60 - min * 60;
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

	@Override
	public void loadTime()
	{
		if (!dateTimeMap.isEmpty())
		{
			beginCount.set(false);
			pause.set(true);
			window.setStartTextButton();
		}
		saver.loadTime(dateTimeMap);
		assignTime();
	}

	@Override
	public void saveTime()
	{
		if (dateTimeMap.containsKey(todayDate))
		{
			dateTimeMap.put(todayDate, todayTime);
		}
		else
		{
			dateTimeMap.put(todayDate, currentTime);
		}
		saver.saveTime(dateTimeMap);
	}

	@Override
	public void setWindow(IGUIWindow window)
	{
		this.window = window;
	}

	@Override
	public void setLoadSaveToFile(ILoadSaveToFile saver)
	{
		this.saver = saver;
	}

	@Override
	public void changeLocale()
	{
		window.changeLocale();
	}
}