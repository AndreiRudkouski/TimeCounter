package main.java.data.container.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import main.java.data.container.DataContainer;
import main.java.data.container.DataContainerHelper;
import main.java.initApp.annotation.Setter;
import main.java.logger.MainLogger;

public class DataContainerHelperImpl implements DataContainerHelper
{
	private static final boolean DEFAULT_AUTO_CHANGE_DATE = true;
	private static final boolean DEFAULT_RELAX_REMINDER = true;
	private static final boolean DEFAULT_IS_RUNNING_APPLICATION = true;

	@Setter
	private DataContainer container;

	@Override
	public long getCurrentTimeValue()
	{
		return container.getCurrentTimeValue();
	}

	@Override
	public long getTodayTimeValue()
	{
		return container.getTodayTimeValue();
	}

	@Override
	public long getTotalTimeValue()
	{
		return container.getTotalTimeValue();
	}

	@Override
	public void setDefaultTime()
	{
		container.setCurrentTimeValue(0);
		container.setTodayTimeValue(0);
		container.setTotalTimeValue(0);
	}

	@Override
	public void putDateAndTimeToStorage(LocalDate date, long time)
	{

		initTimeFields(date, time);
		container.putDateAndTimeToStorage(date, time);
	}

	private void initTimeFields(LocalDate date, long time)
	{
		if (container.getTodayTimeValue() != time)
		{
			if (container.getTodayDate().equals(date))
			{
				container.setTodayTimeValue(time);
			}
			container.increaseTotalTimeByDelta(time);
		}
	}

	@Override
	public Set<LocalDate> getDatesFromStorage()
	{
		container.putDateAndTimeToStorage(container.getTodayDate(), container.getTodayTimeValue());
		return container.getDatesFromStorage();
	}

	@Override
	public long getTimeFromStorageByDate(LocalDate date)
	{
		return container.getTimeFromStorageByDate(date);
	}

	@Override
	public void increaseTimeBySecond()
	{
		container.increaseCurrentTimeByDelta(1);
		container.increaseTodayTimeByDelta(1);
		container.increaseTotalTimeByDelta(1);
	}

	@Override
	public boolean isChangedTime()
	{
		return (container.getDatesFromStorage().stream().mapToLong(date ->
				container.getTimeFromStorageByDate(date)).sum() != container.getTotalTimeValue());
	}

	@Override
	public boolean isTimeToRelax(long relaxPeriod)
	{
		return container.getCurrentTimeValue() % relaxPeriod == 0 && container.getCurrentRelaxReminderFlag();
	}

	@Override
	public void eraseCurrentTime()
	{
		container.increaseTodayTimeByDelta(-container.getCurrentTimeValue());
		container.increaseTotalTimeByDelta(-container.getCurrentTimeValue());
		container.setCurrentTimeValue(0);
	}

	@Override
	public void eraseTodayTime()
	{
		container.setCurrentTimeValue(0);
		container.increaseTotalTimeByDelta(-container.getTodayTimeValue());
		container.setTodayTimeValue(0);
	}

	@Override
	public void eraseTotalTime()
	{
		container.setCurrentTimeValue(0);
		container.setTodayTimeValue(0);
		container.setTotalTimeValue(0);
		container.eraseDateAndTimeToStorage();
	}

	@Override
	public boolean isDateShouldBeChanged(LocalDate currentDate)
	{
		if (!container.getTodayDate().equals(currentDate))
		{
			if (container.getCurrentAutoChangeDateFlag())
			{
				container.putDateAndTimeToStorage(container.getTodayDate(), container.getTodayTimeValue());
				container.setCurrentTimeValue(0);
				container.setTodayTimeValue(0);
				container.setTodayDate(currentDate);
				return true;
			}
			container.setTodayDate(currentDate);
		}
		return false;
	}

	@Override
	public void setDefaultSettings()
	{
		container.setLoadedAutoChangeDateFlag(DEFAULT_AUTO_CHANGE_DATE);
		container.setLoadedRelaxReminderFlag(DEFAULT_RELAX_REMINDER);
		container.setLoadedRunningApplicationFlag(DEFAULT_IS_RUNNING_APPLICATION);
		container.setLoadedApplication(null);
	}

	@Override
	public boolean isChangedSettings()
	{
		return !(isEqualsCurrentAndLoadedApplication() && isEqualsCurrentAndLoadedSettings());
	}

	private boolean isEqualsCurrentAndLoadedApplication()
	{
		return (container.getCurrentApplication() != null && container.getCurrentApplication().equals(
				container.getLoadedApplication()))
				|| (container.getCurrentApplication() == null && container.getLoadedApplication() == null);
	}

	private boolean isEqualsCurrentAndLoadedSettings()
	{
		return container.getCurrentAutoChangeDateFlag() == container.getLoadedAutoChangeDateFlag()
				&& container.getCurrentRelaxReminderFlag() == container.getLoadedRelaxReminderFlag()
				&& container.getCurrentRunningApplicationFlag() == container.getLoadedRunningApplicationFlag();
	}

	@Override
	public void initSettingsAfterSaving()
	{
		container.setLoadedRunningApplicationFlag(container.getCurrentRunningApplicationFlag());
		container.setLoadedAutoChangeDateFlag(container.getCurrentAutoChangeDateFlag());
		container.setLoadedRelaxReminderFlag(container.getCurrentRelaxReminderFlag());
		container.setLoadedApplication(container.getCurrentApplication());
	}

	@Override
	public void initApplication(File application)
	{
		container.setLoadedApplication(application);
		container.setCurrentApplication(application);
	}

	@Override
	public File getApplication()
	{
		return container.getCurrentApplication();
	}

	@Override
	public void changeApplication(File application)
	{
		container.setCurrentApplication(application);
	}

	@Override
	public void initAutoChangeDateFlag(boolean flag)
	{
		container.setLoadedAutoChangeDateFlag(flag);
		container.setCurrentAutoChangeDateFlag(flag);
	}

	@Override
	public boolean getAutoChangeDateFlag()
	{
		return container.getCurrentAutoChangeDateFlag();
	}

	@Override
	public void changeAutoChangeDateFlag(boolean flag)
	{
		container.setCurrentAutoChangeDateFlag(flag);
	}

	@Override
	public void initRelaxReminderFlag(boolean flag)
	{
		container.setLoadedRelaxReminderFlag(flag);
		container.setCurrentRelaxReminderFlag(flag);
	}

	@Override
	public boolean getRelaxReminderFlag()
	{
		return container.getCurrentRelaxReminderFlag();
	}

	@Override
	public void changeRelaxReminderFlag(boolean flag)
	{
		container.setCurrentRelaxReminderFlag(flag);
	}

	@Override
	public void initRunningApplicationFlag(boolean flag)
	{
		container.setLoadedRunningApplicationFlag(flag);
		container.setCurrentRunningApplicationFlag(flag);
	}

	@Override
	public boolean getRunningApplicationFlag()
	{
		return container.getCurrentRunningApplicationFlag();
	}

	@Override
	public void changeRunningApplicationFlag(boolean flag)
	{
		container.setCurrentRunningApplicationFlag(flag);
	}

	@Override
	public boolean isApplicationShouldBeConnected()
	{
		return (container.getCurrentApplication() != null && container.getCurrentRunningApplicationFlag())
				&& !isApplicationProcessAlive();
	}

	@Override
	public boolean isApplicationProcessAlive()
	{
		return container.getApplicationProcess() != null && container.getApplicationProcess().isAlive();
	}

	@Override
	public void createApplicationProcess()
	{
		try
		{
			container.setApplicationProcess(
					Runtime.getRuntime().exec(container.getCurrentApplication().getAbsolutePath()));
		}
		catch (IOException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
		}
	}

	@Override
	public void destroyApplicationProcess()
	{
		if (isApplicationProcessAlive())
		{
			container.getApplicationProcess().destroy();
		}
	}
}