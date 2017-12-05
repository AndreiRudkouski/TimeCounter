package timeCounter.listener.impl;

import java.awt.event.ActionEvent;

import timeCounter.listener.AbstractTimeListener;

public class ApplicationListener extends AbstractTimeListener
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		timeCounter.chooseApplication();
	}
}