package timeCounter.gui;

import javax.swing.*;

import timeCounter.listener.ITimeListener;

public interface IGUIWindow
{
	void setStopTextButton();

	void setStartTextButton();

	boolean timeRelaxReminder();

	boolean changeDate();

	JTextField getCurrentTimeField();

	JTextField getTodayTimeField();

	JTextField getTotalTimeField();

	boolean isRelaxReminder();

	void setListenersAndCreate(ITimeListener startStopTimeListener, ITimeListener loadTimeListener,
			ITimeListener saveTimeListener, ITimeListener eraseCurrentTimeListener,
			ITimeListener eraseTodayTimeListener, ITimeListener eraseTotalTimeListener);
}
