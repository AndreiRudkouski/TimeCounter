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
	 * Checks if "checkDate" checkbox is selected.
	 *
	 * @return true if the checkbox is selected otherwise false.
	 */
	boolean isAutoChangeDate();

	/**
	 * Sets "checkDate" checkbox.
	 *
	 * @param check parameter for checkbox.
	 */
	void setAutoChangeDate(boolean check);

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
	 * Checks if "checkBreak" checkbox is selected.
	 *
	 * @return true if the checkbox is selected otherwise false.
	 */
	boolean isRelaxReminder();

	/**
	 * Sets "checkBreak" checkbox.
	 *
	 * @param check parameter for checkbox.
	 */
	void setRelaxReminder(boolean check);

	/**
	 * Sets {@link ITimeListener}'s listeners to GUI and create it.
	 *
	 * @param listeners {@link ITimeListener}'s implementations for add to buttons
	 */
	void setListenersAndCreate(ITimeListener... listeners);

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
	 * @param name application name or null to set in initial condition
	 */
	void setApplicationLabel(String name);

	/**
	 * Notices the user about already running application which is chosen for controlling.
	 *
	 * @return true if the user want to begin timing after restart the application which is chosen for controlling
	 * otherwise false
	 */
	boolean runningApplicationNotice();
}
