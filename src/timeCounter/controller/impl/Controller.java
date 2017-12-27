package timeCounter.controller.impl;

import timeCounter.controller.IController;
import timeCounter.counter.ITimeCounter;
import timeCounter.init.annotation.Setter;

public class Controller implements IController
{
	@Setter
	private ITimeCounter timeCounter;

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
		return false;
	}
}
