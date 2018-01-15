package main.command.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

import main.command.Command;
import main.command.CommandName;
import main.logger.MainLogger;

public class CommandImpl implements Command
{
	@Override
	public boolean executeCommand(String commandName)
	{
		CommandName command = CommandName.valueOf(commandName.toUpperCase(Locale.ENGLISH));
		return executeMethod(command);

	}

	private boolean executeMethod(CommandName command)
	{
		Object executor = command.getExecutor();
		Method method = command.getMethod();
		try
		{
			if (command.isBooleanReturn())
			{
				return (boolean) method.invoke(executor);
			}
			else
			{
				method.invoke(executor);
			}
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
		}

		return false;
	}
}