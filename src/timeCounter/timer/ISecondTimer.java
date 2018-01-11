package timeCounter.timer;

/**
 * This interface identifies methods for executing any {@link Runnable} command every second.
 */
public interface ISecondTimer
{
	/**
	 * Starts or resumes the execution.
	 */
	void start();

	/**
	 * Pauses the execution.
	 */
	void stop();

	/**
	 * Checks if the execution is running.
	 *
	 * @return true if the execution is running otherwise false
	 */
	boolean isRunning();

	/**
	 * Sets runnable command for executing.
	 *
	 * @param command the task to execute
	 */
	void setCommand(Runnable command);
}