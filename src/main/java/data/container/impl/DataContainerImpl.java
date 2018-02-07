package main.java.data.container.impl;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import main.java.data.container.DataContainer;

public class DataContainerImpl implements DataContainer
{
	private AtomicLong currentTime = new AtomicLong();
	private AtomicLong todayTime = new AtomicLong();
	private AtomicLong totalTime = new AtomicLong();
	private LocalDate todayDate = LocalDate.now();
	private Map<LocalDate, AtomicLong> dateTimeStorage = new HashMap<>();
	private AtomicBoolean currentAutoChangeDateFlag = new AtomicBoolean();
	private AtomicBoolean currentRelaxReminderFlag = new AtomicBoolean();
	private AtomicBoolean currentRunningApplicationFlag = new AtomicBoolean();
	private AtomicReference<File> currentApplication = new AtomicReference<>();
	private AtomicBoolean loadedAutoChangeDateFlag = new AtomicBoolean();
	private AtomicBoolean loadedRelaxReminderFlag = new AtomicBoolean();
	private AtomicBoolean loadedRunningApplicationFlag = new AtomicBoolean();
	private AtomicReference<File> loadedApplication = new AtomicReference<>();
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
	public void increaseCurrentTimeByDelta(long delta)
	{
		currentTime.getAndAdd(delta);
	}

	@Override
	public void increaseTodayTimeByDelta(long delta)
	{
		todayTime.getAndAdd(delta);
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
	public long getTimeFromStorageByDate(LocalDate date)
	{
		return dateTimeStorage.get(date).get();
	}

	@Override
	public Set<LocalDate> getDatesFromStorage()
	{
		return dateTimeStorage.keySet();
	}

	@Override
	public void eraseDateAndTimeToStorage()
	{
		dateTimeStorage.clear();
	}

	@Override
	public boolean getCurrentAutoChangeDateFlag()
	{
		return currentAutoChangeDateFlag.get();
	}

	@Override
	public void setCurrentAutoChangeDateFlag(boolean flag)
	{
		this.currentAutoChangeDateFlag.set(flag);
	}

	@Override
	public boolean getCurrentRelaxReminderFlag()
	{
		return currentRelaxReminderFlag.get();
	}

	@Override
	public void setCurrentRelaxReminderFlag(boolean flag)
	{
		this.currentRelaxReminderFlag.set(flag);
	}

	@Override
	public boolean getCurrentRunningApplicationFlag()
	{
		return currentRunningApplicationFlag.get();
	}

	@Override
	public void setCurrentRunningApplicationFlag(boolean flag)
	{
		this.currentRunningApplicationFlag.set(flag);
	}

	@Override
	public File getCurrentApplication()
	{
		return currentApplication.get();
	}

	@Override
	public void setCurrentApplication(File currentApplication)
	{
		this.currentApplication.set(currentApplication);
	}

	@Override
	public boolean getLoadedAutoChangeDateFlag()
	{
		return loadedAutoChangeDateFlag.get();
	}

	@Override
	public void setLoadedAutoChangeDateFlag(boolean flag)
	{
		loadedAutoChangeDateFlag.set(flag);
	}

	@Override
	public boolean getLoadedRelaxReminderFlag()
	{
		return loadedRelaxReminderFlag.get();
	}

	@Override
	public void setLoadedRelaxReminderFlag(boolean flag)
	{
		loadedRelaxReminderFlag.set(flag);
	}

	@Override
	public boolean getLoadedRunningApplicationFlag()
	{
		return loadedRunningApplicationFlag.get();
	}

	@Override
	public void setLoadedRunningApplicationFlag(boolean flag)
	{
		loadedRunningApplicationFlag.set(flag);
		setCurrentRunningApplicationFlag(flag);
	}

	@Override
	public File getLoadedApplication()
	{
		return loadedApplication.get();
	}

	@Override
	public void setLoadedApplication(File loadedApplication)
	{
		this.loadedApplication.set(loadedApplication);
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