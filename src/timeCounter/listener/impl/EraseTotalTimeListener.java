package timeCounter.listener.impl;

import java.awt.event.ActionEvent;

import timeCounter.listener.AbstractTimeListener;

public class EraseTotalTimeListener extends AbstractTimeListener
{
	@Override
	public void actionPerformed(ActionEvent a)
	{
		timeCounter.eraseTotalTime();
	}
}