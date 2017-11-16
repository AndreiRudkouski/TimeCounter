package timeCounter.gui;

import javax.swing.*;

import timeCounter.listener.ITimeListener;

/**
 * This interface identifies methods for working with GUI.
 */
public interface IGUIWindow
{
	/**
	 * Sets the text of "StartStop" button to "Stop".
	 */
	void setStopTextButton();

	/**
	 * Sets the text of "StartStop" button to "Start".
	 */
	void setStartTextButton();

	/**
	 * Checks if the user has selected relaxation.
	 *
	 * @return true if the user has selected relaxation otherwise false.
	 */
	boolean timeRelaxReminder();

	/**
	 * Checks if the user has selected date changing.
	 *
	 * @return true if the user has selected date changing otherwise false.
	 */
	boolean changeDate();

	/**
	 * Returns {@link JTextField}'s field of the current time from GUI.
	 *
	 * @return {@link JTextField}'s field of the current time.
	 */
	JTextField getCurrentTimeField();

	/**
	 * Returns {@link JTextField}'s field of the today time from GUI.
	 *
	 * @return {@link JTextField}'s field of the today time.
	 */
	JTextField getTodayTimeField();

	/**
	 * Returns {@link JTextField}'s field of the total time from GUI.
	 *
	 * @return {@link JTextField}'s field of the total time.
	 */
	JTextField getTotalTimeField();

	/**
	 * Checks if "RelaxReminder" checkbox is selected.
	 *
	 * @return true if the checkbox is selected otherwise false.
	 */
	boolean isRelaxReminder();

	/**
	 * Sets {@link ITimeListener}'s listeners to GUI and create it.
	 *
	 */
	void setListenersAndCreate(ITimeListener startStopTimeListener, ITimeListener loadTimeListener,
			ITimeListener saveTimeListener, ITimeListener eraseCurrentTimeListener,
			ITimeListener eraseTodayTimeListener, ITimeListener eraseTotalTimeListener);
}
