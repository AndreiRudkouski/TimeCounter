package main.java.timer.timer;

/**
 * This interface identifies methods for executing any {@link Runnable} command every second.
 */
public interface Timer
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

	/**
	 * Checks if the executor has command.
	 *
	 * @return true if the executor has command otherwise false
	 */
	boolean hasCommand();
}