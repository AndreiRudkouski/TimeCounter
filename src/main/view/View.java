package main.view;

import main.observer.TimeObservable;
import main.observer.TimeObserver;

/**
 * This interface identifies methods for working with GUI.
 */
public interface View extends TimeObserver, TimeObservable
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
}
