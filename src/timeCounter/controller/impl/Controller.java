package timeCounter.controller.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import timeCounter.controller.IController;
import timeCounter.counter.ITimeCounter;
import timeCounter.init.annotation.Setter;
import timeCounter.load.ILoadSaveToFile;
import timeCounter.view.IView;

public class Controller implements IController
{
	@Setter
	private ITimeCounter timeCounter;
	@Setter
	private IView view;
	@Setter
	private ILoadSaveToFile saver;

	@Override
	public void eraseCurrentTime()
	{
		timeCounter.eraseCurrentTime();
	}

	@Override
	public void eraseTodayTime()
	{
		timeCounter.eraseTodayTime();
	}

	@Override
	public void eraseTotalTime()
	{
		timeCounter.eraseTotalTime();
	}

	@Override
	public void loadData()
	{
		timeCounter.loadData();
	}

	@Override
	public void saveData()
	{
		timeCounter.saveData();
	}

	@Override
	public void chooseApplication()
	{
		timeCounter.chooseApplication();
	}

	@Override
	public void eraseApplication()
	{
		timeCounter.eraseApplication();
	}

	@Override
	public void startStopTimer()
	{
		timeCounter.startStopTimer();
	}

	@Override
	public boolean closeTimeCounter(boolean close)
	{
		return timeCounter.closeTimeCounter(close);
	}

	@Override
	public void setButtonTextToStop()
	{
		view.setButtonTextToStop();
	}

	@Override
	public void setButtonTextToStart()
	{
		view.setButtonTextToStart();
	}

	@Override
	public boolean isChosenRelax()
	{
		return view.isChosenRelax();
	}

	@Override
	public boolean isAutoChangeDate()
	{
		return view.isAutoChangeDate();
	}

	@Override
	public boolean isRelaxReminder()
	{
		return view.isRelaxReminder();
	}

	@Override
	public boolean runningApplicationNotice()
	{
		return view.runningApplicationNotice();
	}

	@Override
	public void createView()
	{
		view.createView();
	}

	@Override
	public void updateTimeFields(List<AtomicLong> timeList)
	{
		view.updateTimeFields(timeList);
	}

	@Override
	public void updateCheckBoxesAndAppName(boolean relaxReminder, boolean autoChangeDate, String appName)
	{
		view.setApplicationLabel(appName);
		view.setAutoChangeDate(relaxReminder);
		view.setAutoChangeDate(autoChangeDate);
	}
}
