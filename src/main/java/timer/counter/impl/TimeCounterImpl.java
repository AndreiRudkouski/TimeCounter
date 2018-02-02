package main.java.timer.counter.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import main.java.command.Command;
import main.java.data.container.DataContainer;
import main.java.data.loadSave.LoadSaveToFile;
import main.java.initApp.annotation.Setter;
import main.java.logger.MainLogger;
import main.java.observer.TimeObserver;
import main.java.timer.counter.TimeCounter;
import main.java.timer.timer.Timer;

public class TimeCounterImpl implements TimeCounter
{
	private static final int SEC_TO_RELAX = 3000;

	private static final String DELIMITER_SPACE = " ";
	private static final String TASK_LIST_COMMAND = "tasklist";
	private static final String TASK_KILL_COMMAND = "taskkill /IM ";

	@Setter
	private DataContainer container;
	@Setter
	private Command command;
	@Setter
	private LoadSaveToFile saver;
	@Setter
	private Timer timer;

	private List<TimeObserver> observers = new ArrayList<>();

	@Override
	public void loadData()
	{
		saver.loadAndInitData();
		if (!timer.hasCommand())
		{
			timer.setCommand(this::stopOrIncreaseTime);
		}
		notifyTimeObserversAboutTime();
		notifyTimeObserversAboutSettings();
	}

	private void stopOrIncreaseTime()
	{
		if (shouldTimerStop())
		{
			stopTimer();
		}
		else
		{
			increaseAllTimes();
			checkRelaxTimeAndStopTimerIfNeeded();
			checkChangingDate();
		}
	}

	private boolean shouldTimerStop()
	{
		return (container.getCurrentApplication() != null && container.getCurrentRunningApplicationFlag())
				&& !isApplicationProcessAlive();
	}

	private boolean isApplicationProcessAlive()
	{
		return container.getApplicationProcess() != null && container.getApplicationProcess().isAlive();
	}

	private void stopTimer()
	{
		timer.stop();
		notifyTimeObserversAboutTiming();
	}

	private void increaseAllTimes()
	{
		container.increaseTimeBySecond();
		notifyTimeObserversAboutTime();
	}

	private void checkRelaxTimeAndStopTimerIfNeeded()
	{
		if (container.getCurrentTimeValue() % SEC_TO_RELAX == 0 && container.getCurrentRelaxReminderFlag())
		{
			stopTimer();
			if (!command.executeCommand(Command.Name.VIEW_IS_CHOSEN_RELAX))
			{
				startTimer();
			}
		}
	}

	private void startTimer()
	{
		startApplicationProcess();
		timer.start();
		notifyTimeObserversAboutTiming();
	}

	private void startApplicationProcess()
	{
		if (!isApplicationProcessAlive() && container.getCurrentApplication() != null)
		{
			String applicationName = container.getCurrentApplication().getName();
			if (isProcessWithSameNameAlreadyRun(applicationName))
			{
				container.setCurrentRunningApplicationFlag(command.executeCommand(
						Command.Name.VIEW_IS_USER_AGREE_TO_CONNECT_SELECTED_APP));
				if (container.getCurrentRunningApplicationFlag())
				{
					closeAllProcessesWithSameNames(applicationName);
				}
				else
				{
					return;
				}
			}
			startProcessForApplication();
		}
	}

	private void closeAllProcessesWithSameNames(String name)
	{
		try
		{
			while (isProcessWithSameNameAlreadyRun(name))
			{
				Process process = Runtime.getRuntime().exec(TASK_KILL_COMMAND + name);
				process.waitFor();
			}
		}
		catch (InterruptedException | IOException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
		}
	}

	private boolean isProcessWithSameNameAlreadyRun(String name)
	{
		try (BufferedReader input = new BufferedReader(
				new InputStreamReader(Runtime.getRuntime().exec(TASK_LIST_COMMAND).getInputStream())))
		{
			String line;
			while ((line = input.readLine()) != null)
			{
				if (Stream.of(line.split(DELIMITER_SPACE)).anyMatch(n -> n.equalsIgnoreCase(name)))
				{
					return true;
				}
			}
		}
		catch (IOException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
		}
		return false;
	}

	private void startProcessForApplication()
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

	private void checkChangingDate()
	{
		LocalDate currentDate = LocalDate.now();
		if (!container.getTodayDate().equals(currentDate))
		{
			changeDate();
			container.setTodayDate(currentDate);
		}
	}

	private void changeDate()
	{
		if (container.getCurrentAutoChangeDateFlag())
		{
			saveData();
			container.setCurrentTimeValue(0);
			container.setTodayTimeValue(0);
			notifyTimeObserversAboutTime();
		}
	}

	@Override
	public void saveData()
	{
		saver.saveData();
	}

	@Override
	public void closeApplication()
	{
		stopTimer();
		if (isApplicationProcessAlive())
		{
			container.getApplicationProcess().destroy();
		}
	}

	@Override
	public boolean isChangedTimeOrSettings()
	{
		stopTimer();
		return isChangedTime() || isChangedSettings();
	}

	private boolean isChangedTime()
	{
		return (container.getDatesFromStorage().stream().mapToLong(date ->
				container.getTimeFromStorageByDate(date)).sum() != container.getTotalTimeValue());
	}

	private boolean isChangedSettings()
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
		return container.getCurrentAutoChangeDateFlag() != container.getLoadedAutoChangeDateFlag()
				|| container.getCurrentRelaxReminderFlag() != container.getLoadedRelaxReminderFlag()
				|| container.getCurrentRunningApplicationFlag() != container.getLoadedRunningApplicationFlag();
	}

	@Override
	public void addTimeObserver(TimeObserver observer)
	{
		observers.add(observer);
	}

	@Override
	public void notifyTimeObserversAboutTime()
	{
		observers.forEach(obs -> obs.updateTime(
				Arrays.asList(container.getCurrentTimeValue(), container.getTodayTimeValue(),
						container.getTotalTimeValue())));
	}

	@Override
	public void notifyTimeObserversAboutSettings()
	{
		observers.forEach(obs -> obs
				.updateSettings(container.getCurrentAutoChangeDateFlag(), container.getCurrentRelaxReminderFlag(),
						container.getCurrentApplication()));
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
		container.increaseTodayTimeByDelta(-container.getCurrentTimeValue());
		container.increaseTotalTimeByDelta(-container.getCurrentTimeValue());
		container.setCurrentTimeValue(0);
		notifyTimeObserversAboutTime();
	}

	private void eraseTodayTime()
	{
		stopTimer();
		container.setCurrentTimeValue(0);
		container.increaseTotalTimeByDelta(-container.getTodayTimeValue());
		container.setTodayTimeValue(0);
		notifyTimeObserversAboutTime();
	}

	private void eraseTotalTime()
	{
		stopTimer();
		container.setCurrentTimeValue(0);
		container.setTodayTimeValue(0);
		container.setTotalTimeValue(0);
		notifyTimeObserversAboutTime();
	}

	@Override
	public void updateSettings(boolean autoChangeDate, boolean relaxReminder, File file)
	{
		container.setCurrentAutoChangeDateFlag(autoChangeDate);
		container.setCurrentRelaxReminderFlag(relaxReminder);
		container.setCurrentApplication(file);
		notifyTimeObserversAboutSettings();
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