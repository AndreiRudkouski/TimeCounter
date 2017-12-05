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

	//////////////////////////////////////////////
	//
	// Getters & Setters
	//
	//////////////////////////////////////////////

	public ITimeCounter getTimeCounter()
	{
		return timeCounter;
	}

	@Setter(name = "timeCounter")
	public void setTimeCounter(ITimeCounter timeCounter)
	{
		this.timeCounter = timeCounter;
	}
}