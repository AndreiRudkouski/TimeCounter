package main.java.viewer.manager.impl;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import main.java.command.Command;
import main.java.initApp.annotation.Setter;
import main.java.observer.TimeObserver;
import main.java.viewer.gui.GuiView;
import main.java.viewer.manager.ViewManager;

public class ViewManagerImpl implements ViewManager
{
	public static final String TIME_DELIMITER = ":";
	public static final String TIME_FORMAT_WITHOUT_DAY =
			"%1$02d" + TIME_DELIMITER + "%2$02d" + TIME_DELIMITER + "%3$02d";
	public static final String TIME_FORMAT_WITH_DAY =
			"%1$02d" + TIME_DELIMITER + "%2$02d" + TIME_DELIMITER + "%3$02d" + TIME_DELIMITER + "%4$02d";

	private static final int SECONDS_PER_MINUTE = 60;
	private static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * 60;
	private static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * 24;

	@Setter
	private Command command;
	@Setter
	private GuiView guiView;

	private List<TimeObserver> observers = new ArrayList<>();

	@Override
	public void createView()
	{
		guiView.createGuiView();
	}

	@Override
	public boolean isChosenRelax()
	{
		return guiView.callReminderWindowAboutRelax();
	}

	@Override
	public boolean isUserAgreeToConnectSelectedApplication()
	{
		return guiView.callReminderWindowAboutSelectApplication();
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		executeCommand(Command.Name.valueOf(event.getActionCommand()));
	}

	@Override
	public boolean executeCommand(Command.Name commandName)
	{
		return command.executeCommand(commandName);
	}

	@Override
	public void updateTime(List<Long> timeList)
	{
		if (timeList != null && timeList.size() == 3)
		{
			guiView.updateTimeFields(timeList.stream().map(this::convertTimeToViewFormat).collect(Collectors.toList()));
		}
	}

	private String convertTimeToViewFormat(long sec)
	{
		long day = sec / SECONDS_PER_DAY;
		sec = sec - day * SECONDS_PER_DAY;
		long hour = sec / SECONDS_PER_HOUR;
		sec = sec - hour * SECONDS_PER_HOUR;
		long min = sec / SECONDS_PER_MINUTE;
		sec = sec - min * SECONDS_PER_MINUTE;
		if (day != 0)
		{
			return String.format(TIME_FORMAT_WITH_DAY, day, hour, min, sec);
		}
		return String.format(TIME_FORMAT_WITHOUT_DAY, hour, min, sec);
	}

	@Override
	public void updateSettings(boolean autoChangeDate, boolean relaxReminder, File file)
	{
		guiView.setCheckDateBoxSelectedValue(autoChangeDate);
		guiView.setCheckBreakBoxSelectedValue(relaxReminder);
		guiView.setSelectedFileLabel(file != null ? file.getName() : null);
	}

	@Override
	public void updateTiming(boolean isStart)
	{
		guiView.setStartStopButtonToStartPosition(isStart);
	}

	@Override
	public void addTimeObserver(TimeObserver observer)
	{
		observers.add(observer);
	}

	@Override
	public void notifyTimeObserversAboutTime()
	{
		observers.forEach(
				obs -> obs.updateTime(Arrays.asList(convertViewFormatToTime(guiView.getCurrentTimeFieldValue()),
						convertViewFormatToTime(guiView.getTodayTimeFieldValue()),
						convertViewFormatToTime(guiView.getTotalTimeFieldValue()))));
	}

	private long convertViewFormatToTime(String timeStr)
	{
		long[] seconds = Arrays.stream(timeStr.split(TIME_DELIMITER)).mapToLong(Long::parseLong).toArray();
		if (seconds.length == 3)
		{
			return seconds[0] * SECONDS_PER_HOUR + seconds[1] * SECONDS_PER_MINUTE + seconds[2];
		}
		else
		{
			return seconds[0] * SECONDS_PER_DAY + seconds[0] * SECONDS_PER_HOUR + seconds[1] * SECONDS_PER_MINUTE + seconds[2];
		}
	}

	@Override
	public void notifyTimeObserversAboutSettings()
	{
		observers.forEach(obs -> obs.updateSettings(guiView.isCheckDateBoxSelected(), guiView.isCheckBreakBoxSelected(),
				guiView.getSelectedFile()));
	}

	@Override
	public void notifyTimeObserversAboutTiming()
	{
		observers.forEach(obs -> obs
				.updateTiming(guiView.isStartStopButtonInStartPosition()));
	}
}