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
import main.java.data.container.DataContainerHelper;
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
	private DataContainerHelper containerHelper;
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
		if (containerHelper.isApplicationShouldBeConnected())
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

	private void stopTimer()
	{
		timer.stop();
		notifyTimeObserversAboutTiming();
	}

	private void increaseAllTimes()
	{
		containerHelper.increaseTimeBySecond();
		notifyTimeObserversAboutTime();
	}

	private void checkRelaxTimeAndStopTimerIfNeeded()
	{
		if (containerHelper.isTimeToRelax(SEC_TO_RELAX))
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
		if (!containerHelper.isApplicationProcessAlive() && containerHelper.getApplication() != null)
		{
			String applicationName = containerHelper.getApplication().getName();
			if (isProcessWithSameNameAlreadyRun(applicationName))
			{
				containerHelper.changeRunningApplicationFlag(command.executeCommand(
						Command.Name.VIEW_IS_USER_AGREE_TO_CONNECT_SELECTED_APP));
				if (containerHelper.getRunningApplicationFlag())
				{
					closeAllProcessesWithSameNames(applicationName);
				}
				else
				{
					return;
				}
			}
			containerHelper.createApplicationProcess();
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

	private void checkChangingDate()
	{
		LocalDate currentDate = LocalDate.now();
		if (containerHelper.isDateShouldBeChanged(currentDate))
		{
			saveData();
			notifyTimeObserversAboutTime();
		}
	}

	@Override
	public void saveData()
	{
		saver.saveData();
		containerHelper.initSettingsAfterSaving();
	}

	@Override
	public void closeApplication()
	{
		stopTimer();
		containerHelper.destroyApplicationProcess();
	}

	@Override
	public boolean isChangedTimeOrSettings()
	{
		stopTimer();
		return containerHelper.isChangedTime() || containerHelper.isChangedSettings();
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
				Arrays.asList(containerHelper.getCurrentTimeValue(), containerHelper.getTodayTimeValue(),
						containerHelper.getTotalTimeValue())));
	}

	@Override
	public void notifyTimeObserversAboutSettings()
	{
		observers.forEach(obs -> obs
				.updateSettings(containerHelper.getAutoChangeDateFlag(), containerHelper.getRelaxReminderFlag(),
						containerHelper.getApplication()));
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
		containerHelper.eraseCurrentTime();
		notifyTimeObserversAboutTime();
	}

	private void eraseTodayTime()
	{
		stopTimer();
		containerHelper.eraseTodayTime();
		notifyTimeObserversAboutTime();
	}

	private void eraseTotalTime()
	{
		stopTimer();
		containerHelper.eraseTotalTime();
		notifyTimeObserversAboutTime();
	}

	@Override
	public void updateSettings(boolean autoChangeDate, boolean relaxReminder, File file)
	{
		containerHelper.changeAutoChangeDateFlag(autoChangeDate);
		containerHelper.changeRelaxReminderFlag(relaxReminder);
		containerHelper.changeApplication(file);
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