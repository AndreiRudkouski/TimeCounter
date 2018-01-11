package timeCounter.observer;

/**
 * This interface can be implemented by class if it wants to inform of changes to all observe objects.
 */
public interface TimeObservable
{
	/**
	 * Adds {@link TimeObserver}'s instance.
	 *
	 * @param observer for addition
	 */
	void addTimeObserver(TimeObserver observer);

	/**
	 * Notifies about time changes to all {@link TimeObserver}s.
	 */
	void notifyTimeObserversAboutTime();

	/**
	 * Notifies about setting changes to all {@link TimeObserver}s.
	 */
	void notifyTimeObserversAboutSettings();

	/**
	 * Notifies about time counting state changes to all {@link TimeObserver}s.
	 */
	void notifyTimeObserversAboutTiming();
}