package main.java.viewer.gui;

import java.io.File;
import java.util.List;

/**
 * This interface identifies methods for GUI view management.
 */
public interface GuiView
{
	/**
	 * Creates implementation of this interface.
	 */
	void createGuiView();

	/**
	 * Returns the CheckBreak Box value.
	 *
	 * @return true if the box is selected otherwise false
	 */
	boolean isCheckBreakBoxSelected();

	/**
	 * Sets the value to the CheckBreak Box.
	 *
	 * @param value to set
	 */
	void setCheckBreakBoxSelectedValue(boolean value);

	/**
	 * Returns the CheckDate Box value.
	 *
	 * @return true if the box is selected otherwise false
	 */
	boolean isCheckDateBoxSelected();

	/**
	 * Sets the value to the CheckDate Box.
	 *
	 * @param value to set
	 */
	void setCheckDateBoxSelectedValue(boolean value);

	/**
	 * Returns the selected application.
	 *
	 * @return selected application
	 */
	File getSelectedFile();

	/**
	 * Sets the label to the selected application field.
	 *
	 * @param label to set
	 */
	void setSelectedFileLabel(String label);

	/**
	 * Returns the value of the current time field.
	 *
	 * @return value of the current time field
	 */
	String getCurrentTimeFieldValue();

	/**
	 * Returns the value of the today time field.
	 *
	 * @return value of the today time field
	 */
	String getTodayTimeFieldValue();

	/**
	 * Returns the value of the total time field.
	 *
	 * @return value of the total time field
	 */
	String getTotalTimeFieldValue();

	/**
	 * Updates time fields of GUI view.
	 *
	 * @param timeList list of time values (index 0 - the current time, 2 - the today time and 3 - the total time)
	 */
	void updateTimeFields(List<String> timeList);

	/**
	 * Returns the position of the StartStop button.
	 *
	 * @return true if timer is running and the button has stop position otherwise false
	 */
	boolean isStartStopButtonInStartPosition();

	/**
	 * Sets the position to the StartStop button
	 *
	 * @param position to set
	 */
	void setStartStopButtonToStartPosition(boolean position);

	/**
	 * Calls the reminder window about relax time.
	 *
	 * @return true if the user has selected relaxation otherwise false.
	 */
	boolean callReminderWindowAboutRelax();

	/**
	 * Calls the reminder window about wiring of selected application.
	 *
	 * @return true if the user agree to begin timing after restart the application which is selected for controlling
	 * otherwise false
	 */
	boolean callReminderWindowAboutSelectApplication();
}
