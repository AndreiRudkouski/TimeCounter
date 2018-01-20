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
		executeCommand(event.getActionCommand());
	}

	@Override
	public boolean executeCommand(String commandName)
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
		long hour = sec / (60 * 60);
		long day = hour / 24;
		long min = (sec - hour * 60 * 60) / 60;
		sec = sec - hour * 60 * 60 - min * 60;
		hour = hour - day * 24;
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
		long[] tmp = Arrays.stream(timeStr.split(TIME_DELIMITER)).mapToLong(Long::parseLong).toArray();
		if (tmp.length == 3)
		{
			return tmp[0] * 60 * 60 + tmp[1] * 60 + tmp[2];
		}
		else
		{
			return tmp[0] * 24 * 60 * 60 + tmp[0] * 60 * 60 + tmp[1] * 60 + tmp[2];
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