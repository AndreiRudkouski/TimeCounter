package main.java.counter.container.impl;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import main.java.counter.container.TimeAndSettingsContainer;

public class TimeAndSettingsContainerImpl implements TimeAndSettingsContainer
{
	private AtomicLong currentTime = new AtomicLong();
	private AtomicLong todayTime = new AtomicLong();
	private AtomicLong totalTime = new AtomicLong();
	private LocalDate todayDate = LocalDate.now();
	private Map<LocalDate, AtomicLong> dateTimeStorage = new HashMap<>();
	private AtomicBoolean autoChangeDate = new AtomicBoolean();
	private AtomicBoolean relaxReminder = new AtomicBoolean();
	private AtomicBoolean isRunningApplication = new AtomicBoolean();
	private AtomicReference<File> application = new AtomicReference<>();
	private AtomicReference<Process> applicationProcess = new AtomicReference<>();

	@Override
	public long getCurrentTimeValue()
	{
		return currentTime.get();
	}

	@Override
	public void setCurrentTimeValue(long value)
	{
		this.currentTime.set(value);
	}

	@Override
	public long getTodayTimeValue()
	{
		return todayTime.get();
	}

	@Override
	public void setTodayTimeValue(long value)
	{
		this.todayTime.set(value);
	}

	@Override
	public long getTotalTimeValue()
	{
		return totalTime.get();
	}

	@Override
	public void setTotalTimeValue(long value)
	{
		this.totalTime.set(value);
	}

	@Override
	public void increaseTimeBySecond()
	{
		currentTime.incrementAndGet();
		todayTime.incrementAndGet();
		totalTime.incrementAndGet();
	}

	@Override
	public void increaseTodayTimeByDelta(long delta)
	{

	}

	@Override
	public void increaseTotalTimeByDelta(long delta)
	{
		totalTime.getAndAdd(delta);
	}

	@Override
	public LocalDate getTodayDate()
	{
		return todayDate;
	}

	public void setTodayDate(LocalDate todayDate)
	{
		this.todayDate = todayDate;
	}

	@Override
	public void putDateAndTimeToStorage(LocalDate date, long time)
	{
		dateTimeStorage.put(date, new AtomicLong(time));
	}

	@Override
	public void setDateTimeStorage(Map<LocalDate, Long> map)
	{
		dateTimeStorage = map.entrySet().stream().collect(
				Collectors.toMap(Map.Entry::getKey, e -> new AtomicLong(e.getValue())));
		initTimeValuesAfterSetToStorage();
	}

	private void initTimeValuesAfterSetToStorage()
	{
		currentTime.set(0);
		todayTime = dateTimeStorage.containsKey(todayDate) ?
				new AtomicLong(dateTimeStorage.get(todayDate).get()) : new AtomicLong(0);
		totalTime.set(0);
		dateTimeStorage.forEach((date, time) -> totalTime.getAndAdd(time.get()));
	}

	@Override
	public Map<LocalDate, Long> getDateTimeStorage()
	{
		return dateTimeStorage.entrySet().stream().collect(
				Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
	}

	@Override
	public boolean isAutoChangeDate()
	{
		return autoChangeDate.get();
	}

	@Override
	public void setAutoChangeDate(boolean autoChangeDate)
	{
		this.autoChangeDate.set(autoChangeDate);
	}

	@Override
	public boolean isRelaxReminder()
	{
		return relaxReminder.get();
	}

	@Override
	public void setRelaxReminder(boolean relaxReminder)
	{
		this.relaxReminder.set(relaxReminder);
	}

	@Override
	public boolean isRunningApplication()
	{
		return isRunningApplication.get();
	}

	@Override
	public void setIsRunningApplication(boolean isRunningApplication)
	{
		this.isRunningApplication.set(isRunningApplication);
	}

	@Override
	public File getApplication()
	{
		return application.get();
	}

	@Override
	public void setApplication(File application)
	{
		this.application.set(application);
	}

	@Override
	public Process getApplicationProcess()
	{
		return applicationProcess.get();
	}

	@Override
	public void setApplicationProcess(Process applicationProcess)
	{
		this.applicationProcess.set(applicationProcess);
	}
}