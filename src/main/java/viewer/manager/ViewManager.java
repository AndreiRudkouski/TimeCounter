package main.java.viewer.manager;

import java.awt.event.ActionListener;

import main.java.command.CommandName;
import main.java.observer.TimeObservable;
import main.java.observer.TimeObserver;

/**
 * This interface identifies methods for working with GUI.
 */
public interface ViewManager extends TimeObserver, TimeObservable, ActionListener
{
	/**
	 * Checks if the user has selected relaxation.
	 *
	 * @return true if the user has selected relaxation otherwise false.
	 */
	boolean isChosenRelax();

	/**
	 * Notices the user about already running application which is selected for controlling.
	 *
	 * @return true if the user agree to begin timing after restart the application which is selected for controlling
	 * otherwise false
	 */
	boolean isUserAgreeToConnectSelectedApplication();

	/**
	 * Creates implementation of this interface.
	 */
	void createView();

	/**
	 * Executes command with name witch contains in {@link CommandName}
	 *
	 * @param commandName name of command
	 * @return result of command execution
	 */
	boolean executeCommand(String commandName);
}
