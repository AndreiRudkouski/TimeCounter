package timeCounter.listener.impl;

import static timeCounter.main.Main.TIME_COUNTER;

import java.awt.event.ActionEvent;

import timeCounter.listener.ITimeListener;

public class EraseTodayTimeListener implements ITimeListener
{
	@Override
	public void actionPerformed(ActionEvent a)
	{
		TIME_COUNTER.eraseCurrentTime();
		TIME_COUNTER.eraseTodayTime();
	}
}