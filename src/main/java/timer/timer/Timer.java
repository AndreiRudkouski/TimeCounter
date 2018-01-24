package main.java.timer.timer;

/**
 * This interface identifies methods for executing any {@link Runnable} command.
 */
public interface Timer
{
	/**
	 * Starts or resumes the command execution.
	 */
	void start();

	/**
	 * Pauses the command execution.
	 */
	void stop();

	/**
	 * Checks if the execution of command is running.
	 *
	 * @return true if the command execution is running otherwise false
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