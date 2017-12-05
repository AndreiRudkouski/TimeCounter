package timeCounter.listener.impl;

import java.awt.event.ActionEvent;

import timeCounter.listener.AbstractTimeListener;

public class SaveTimeListener extends AbstractTimeListener
{
	@Override
	public void actionPerformed(ActionEvent a)
	{
		timeCounter.saveData();
	}
}