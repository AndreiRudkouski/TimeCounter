package main.command;

/**
 * This interface identifies methods for commands execution
 */
public interface Command
{
	/**
	 * Executes command with name witch contains in {@link CommandName}
	 *
	 * @param commandName name of command
	 * @return result of command execution
	 */
	boolean executeCommand(String commandName);
}
