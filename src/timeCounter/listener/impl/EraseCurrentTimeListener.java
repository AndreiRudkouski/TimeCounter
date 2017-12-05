package timeCounter.listener.impl;

import java.awt.event.ActionEvent;

import timeCounter.listener.AbstractTimeListener;

public class EraseCurrentTimeListener extends AbstractTimeListener
{
	@Override
	public void actionPerformed(ActionEvent a)
	{
		timeCounter.eraseCurrentTime();
	}
}