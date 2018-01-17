package main.java.counter.bean;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class TimeAndSettingsContainer
{
	private AtomicLong currentTime = new AtomicLong();
	private AtomicLong todayTime = new AtomicLong();
	private AtomicLong totalTime = new AtomicLong();
	private LocalDate todayDate = LocalDate.now();
	private ConcurrentMap<LocalDate, AtomicLong> dateTimeMap = new ConcurrentHashMap<>();
	private AtomicBoolean autoChangeDate = new AtomicBoolean();
	private AtomicBoolean relaxReminder = new AtomicBoolean();
	private AtomicBoolean isRunningApplication = new AtomicBoolean(true);
	private AtomicReference<File> application = new AtomicReference<>();
	private AtomicReference<Process> applicationProcess = new AtomicReference<>();

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

	public void setDateTimeMap(ConcurrentMap<LocalDate, AtomicLong> dateTimeMap)
	{
		this.dateTimeMap = dateTimeMap;
	}

	public boolean isAutoChangeDate()
	{
		return autoChangeDate.get();
	}

	public void setAutoChangeDate(boolean autoChangeDate)
	{
		this.autoChangeDate.set(autoChangeDate);
	}

	public boolean isRelaxReminder()
	{
		return relaxReminder.get();
	}

	public void setRelaxReminder(boolean relaxReminder)
	{
		this.relaxReminder.set(relaxReminder);
	}

	public boolean isRunningApplication()
	{
		return isRunningApplication.get();
	}

	public void setIsRunningApplication(boolean isRunningApplication)
	{
		this.isRunningApplication.set(isRunningApplication);
	}

	public File getApplication()
	{
		return application.get();
	}

	public void setApplication(File application)
	{
		this.application.set(application);
	}

	public Process getApplicationProcess()
	{
		return applicationProcess.get();
	}

	public void setApplicationProcess(Process applicationProcess)
	{
		this.applicationProcess.set(applicationProcess);
	}
}