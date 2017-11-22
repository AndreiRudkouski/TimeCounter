package timeCounter.gui;

import java.io.File;

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
	 * @param startStopTimeListener {@link ITimeListener}'s implementation for 'StartStop' button
	 * @param loadTimeListener {@link ITimeListener}'s implementation for load menu
	 * @param saveTimeListener {@link ITimeListener}'s implementation for save menu
	 * @param eraseCurrentTimeListener {@link ITimeListener}'s implementation for erase the current time menu
	 * @param eraseTodayTimeListener {@link ITimeListener}'s implementation for erase the today time menu
	 * @param eraseTotalTimeListener {@link ITimeListener}'s implementation for erase the total time menu
	 * @param applicationListener {@link ITimeListener}'s implementation for application menu
	 * @param localeListener {@link ITimeListener}'s implementation for locale menu
	 */
	void setListenersAndCreate(ITimeListener startStopTimeListener, ITimeListener loadTimeListener,
			ITimeListener saveTimeListener, ITimeListener eraseCurrentTimeListener,
			ITimeListener eraseTodayTimeListener, ITimeListener eraseTotalTimeListener,
			ITimeListener applicationListener, ITimeListener localeListener);

	/**
	 * Changes locale which is used in GUI.
	 */
	void changeLocale();

	/**
	 * Returns {@link File} file which is controlled.
	 *
	 * @return {@link File} file or null.
	 */
	File chooseApplication();

	/**
	 * Sets the label of application which is chosen.
	 *
	 * @param name application name
	 */
	void setApplicationLabel(String name);
}
