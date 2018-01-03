package timeCounter.command;

public interface ICommand
{
	void executeCommand(String commandName);

	boolean executeBooleanCommand(String commandName);

	boolean executeBooleanCommandWithParameters(String commandName, Boolean saveData, Boolean closeApp);
}
