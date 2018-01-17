package main.java.counter.bean;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

public class TimeAndSettingsContainer
{
	private AtomicLong currentTime = new AtomicLong();
	private AtomicLong todayTime = new AtomicLong();
	private AtomicLong totalTime = new AtomicLong();
	private LocalDate todayDate = LocalDate.now();
	private Map<LocalDate, AtomicLong> dateTimeMap = new TreeMap<>();
	private boolean autoChangeDate;
	private boolean relaxReminder;
	private boolean isRunningApplication = true;
	private File application;
	private Process applicationProcess;

	public AtomicLong getCurrentTime()
	{
		return currentTime;
	}

	public void setCurrentTime(AtomicLong currentTime)
	{
		this.currentTime = currentTime;
	}

	public AtomicLong getTodayTime()
	{
		return todayTime;
	}

	public void setTodayTime(AtomicLong todayTime)
	{
		this.todayTime = todayTime;
	}

	public AtomicLong getTotalTime()
	{
		return totalTime;
	}

	public void setTotalTime(AtomicLong totalTime)
	{
		this.totalTime = totalTime;
	}

	public LocalDate getTodayDate()
	{
		return todayDate;
	}

	public void setTodayDate(LocalDate todayDate)
	{
		this.todayDate = todayDate;
	}

	public Map<LocalDate, AtomicLong> getDateTimeMap()
	{
		return dateTimeMap;
	}

	public void setDateTimeMap(Map<LocalDate, AtomicLong> dateTimeMap)
	{
		this.dateTimeMap = dateTimeMap;
	}

	public boolean isAutoChangeDate()
	{
		return autoChangeDate;
	}

	public void setAutoChangeDate(boolean autoChangeDate)
	{
		this.autoChangeDate = autoChangeDate;
	}

	public boolean isRelaxReminder()
	{
		return relaxReminder;
	}

	public void setRelaxReminder(boolean relaxReminder)
	{
		this.relaxReminder = relaxReminder;
	}

	public boolean isRunningApplication()
	{
		return isRunningApplication;
	}

	public void setIsRunningApplication(boolean isRunningApplication)
	{
		this.isRunningApplication = isRunningApplication;
	}

	public File getApplication()
	{
		return application;
	}

	public void setApplication(File application)
	{
		this.application = application;
	}

	public Process getApplicationProcess()
	{
		return applicationProcess;
	}

	public void setApplicationProcess(Process applicationProcess)
	{
		this.applicationProcess = applicationProcess;
	}
}