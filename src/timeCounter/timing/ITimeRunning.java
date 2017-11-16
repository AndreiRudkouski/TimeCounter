package timeCounter.timing;

/**
 * This interface allow to operate with {@link ITimeRunning}'s implementations.
 */
public interface ITimeRunning extends Runnable
{
	/**
	 * Stops executing of {@link ITimeRunning} implementation.
	 */
	void stopExecute();
}
