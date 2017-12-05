package timeCounter.listener.impl;

import java.awt.event.ActionEvent;

import timeCounter.listener.AbstractTimeListener;

public class LocaleListener extends AbstractTimeListener
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		timeCounter.changeLocale();
	}
}