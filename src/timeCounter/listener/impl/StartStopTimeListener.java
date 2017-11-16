package timeCounter.listener.impl;

import static timeCounter.main.Main.TIME_COUNTER;

import java.awt.event.ActionEvent;

import timeCounter.listener.ITimeListener;
import timeCounter.timing.impl.TimeRunning;

public class StartStopTimeListener implements ITimeListener
{
	@Override
	public void actionPerformed(ActionEvent a)
	{
		if (!TIME_COUNTER.isBeginCount() || TIME_COUNTER.isPause())
		{
			if (!TIME_COUNTER.isBeginCount())
			{
				TIME_COUNTER.setTimer(new TimeRunning());
				new Thread(TIME_COUNTER.getTimer()).start();
			}
			TIME_COUNTER.setStopButton();
		}
		else
		{
			TIME_COUNTER.setStartButton();
		}
	}
}