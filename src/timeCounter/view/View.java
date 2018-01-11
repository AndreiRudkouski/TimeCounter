package timeCounter.view;

import timeCounter.observer.TimeObservable;
import timeCounter.observer.TimeObserver;

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
	 * Notices the user about already running application which is chosen for controlling.
	 *
	 * @return true if the user want to begin timing after restart the application which is chosen for controlling
	 * otherwise false
	 */
	boolean runningApplicationNotice();

	/**
	 * Creates implementation of this interface.
	 */
	void createView();
}
