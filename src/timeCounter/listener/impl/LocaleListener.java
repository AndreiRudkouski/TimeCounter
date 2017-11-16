package timeCounter.listener.impl;

import static timeCounter.main.Main.TIME_COUNTER;

import java.awt.event.ActionEvent;

import timeCounter.listener.ITimeListener;

public class LocaleListener implements ITimeListener
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		TIME_COUNTER.changeLocale();
	}
}