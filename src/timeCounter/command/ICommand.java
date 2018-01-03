package timeCounter.command;

/**
 * This interface identifies methods for commands execution
 */
public interface ICommand
{
	/**
	 * Executes command with name witch contains in {@link CommandName}
	 *
	 * @param commandName name of command
	 * @return result of command execution
	 */
	boolean executeCommand(String commandName);

	/**
	 * Executes command with name witch contains in {@link CommandName} and two parameters
	 *
	 * @param commandName name of command
	 * @param param1 the first parameter
	 * @param param2 the second parameter
	 * @return result of command execution
	 */
	boolean executeCommand(String commandName, Boolean param1, Boolean param2);
}
