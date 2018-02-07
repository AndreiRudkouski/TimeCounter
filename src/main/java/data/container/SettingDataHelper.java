package main.java.data.container;

import java.io.File;

/**
 * This interface identifies methods for the helper which works with setting data from {@link DataContainer}'s container.
 */
public interface SettingDataHelper
{
	/**
	 * Sets default values to all settings.
	 */
	void setDefaultSettings();

	/**
	 * Checks if settings are changed from the last saved values.
	 *
	 * @return true if settings are changed otherwise false
	 */
	boolean isChangedSettings();

	/**
	 * Sets setting fields after saving.
	 */
	void initSettingsAfterSaving();

	/**
	 * Sets the value to the application field.
	 *
	 * @param application to set
	 */
	void initApplication(File application);

	/**
	 * Returns the value of the current application.
	 *
	 * @return the current application
	 */
	File getApplication();

	/**
	 * Changes the value to the current application.
	 *
	 * @param application to change
	 */
	void changeApplication(File application);

	/**
	 * Sets the value to the 'AutoChangeDateFlag' field.
	 *
	 * @param flag to set
	 */
	void initAutoChangeDateFlag(boolean flag);

	/**
	 * Returns the value of the current 'AutoChangeDateFlag' field.
	 *
	 * @return the current value of field
	 */
	boolean getAutoChangeDateFlag();

	/**
	 * Changes the value to the current 'AutoChangeDateFlag' field.
	 *
	 * @param flag to change
	 */
	void changeAutoChangeDateFlag(boolean flag);

	/**
	 * Sets the value to the 'RelaxReminderFlag' field.
	 *
	 * @param flag to set
	 */
	void initRelaxReminderFlag(boolean flag);

	/**
	 * Returns the value of the current 'RelaxReminderFlag' field.
	 *
	 * @return the current value of field
	 */
	boolean getRelaxReminderFlag();

	/**
	 * Changes the value to the current 'RelaxReminderFlag' field.
	 *
	 * @param flag to change
	 */
	void changeRelaxReminderFlag(boolean flag);

	/**
	 * Sets the value to the 'RunningApplicationFlag' field.
	 *
	 * @param flag to set
	 */
	void initRunningApplicationFlag(boolean flag);

	/**
	 * Returns the value of the current 'RunningApplicationFlag' field.
	 *
	 * @return the current value of field
	 */
	boolean getRunningApplicationFlag();

	/**
	 * Changes the value to the current 'RunningApplicationFlag' field.
	 *
	 * @param flag to change
	 */
	void changeRunningApplicationFlag(boolean flag);

	/**
	 * Checks if the current application should be connected for checking time.
	 *
	 * @return true if the application should be connected otherwise false
	 */
	boolean isApplicationShouldBeConnected();

	/**
	 * Checks if the current application process is alive.
	 *
	 * @return true if the application process is alive otherwise false
	 */
	boolean isApplicationProcessAlive();

	/**
	 * Creates the process for the current application.
	 */
	void createApplicationProcess();

	/**
	 * Destroys the process of the current application.
	 */
	void destroyApplicationProcess();
}
