package timeCounter.observer;

/**
 * This interface can be implemented by class if it wants to inform of changes to all observe objects.
 */
public interface ITimeObservable
{
	/**
	 * Adds {@link ITimeObserver}'s instance.
	 *
	 * @param observer for addition
	 */
	void addTimeObserver(ITimeObserver observer);

	/**
	 * Notifies about time changes to all {@link ITimeObserver}s.
	 */
	void notifyTimeObserversAboutTime();

	/**
	 * Notifies about setting changes to all {@link ITimeObserver}s.
	 */
	void notifyTimeObserversAboutSettings();

	/**
	 * Notifies about time counting state changes to all {@link ITimeObserver}s.
	 */
	void notifyTimeObserversAboutTiming();
}