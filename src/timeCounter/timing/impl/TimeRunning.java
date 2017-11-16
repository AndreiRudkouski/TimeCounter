package timeCounter.timing.impl;

import static timeCounter.main.Main.TIME_COUNTER;

import javax.swing.*;

import timeCounter.timing.ITimeRunning;

public class TimeRunning implements ITimeRunning
{
	private volatile boolean execute = true;
	private Timer timer = new Timer(1000, (e) -> {
		TIME_COUNTER.incrementCurrentTime();
		TIME_COUNTER.incrementTodayTime();
		TIME_COUNTER.incrementTotalTime();
		TIME_COUNTER.checkRelaxTime();
		TIME_COUNTER.checkChangeDate();
	});

	public void run()
	{
		timer.start();
		while (execute)
		{
			if (TIME_COUNTER.isPause())
			{
				timer.stop();
			}
			else
			{
				timer.start();
			}
		}
		timer.stop();
	}

	@Override
	public void stopExecute()
	{
		execute = false;
	}
}