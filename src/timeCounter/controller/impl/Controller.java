package timeCounter.controller.impl;

import timeCounter.controller.IController;
import timeCounter.counter.ITimeCounter;
import timeCounter.init.annotation.Setter;

public class Controller implements IController
{
	@Setter
	private ITimeCounter timeCounter;

	@Override
	public void startStopTimer()
	{
		timeCounter.pushStartStopButton();
	}
}
