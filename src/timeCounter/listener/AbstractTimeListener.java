package timeCounter.listener;

import java.awt.event.ActionListener;

import timeCounter.counter.ITimeCounter;
import timeCounter.init.annotation.Setter;

/**
 * This interface marks all listeners.
 */
public abstract class AbstractTimeListener implements ActionListener
{
	protected ITimeCounter timeCounter;

	public ITimeCounter getTimeCounter()
	{
		return timeCounter;
	}

	@Setter
	public void setTimeCounter(ITimeCounter timeCounter)
	{
		this.timeCounter = timeCounter;
	}
}