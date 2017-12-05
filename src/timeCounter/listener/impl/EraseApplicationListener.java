package timeCounter.listener.impl;

import java.awt.event.ActionEvent;

import timeCounter.listener.AbstractTimeListener;

public class EraseApplicationListener extends AbstractTimeListener
{
	@Override
	public void actionPerformed(ActionEvent a)
	{
		timeCounter.eraseApplication();
	}
}