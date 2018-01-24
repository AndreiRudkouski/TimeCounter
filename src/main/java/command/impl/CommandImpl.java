package main.java.command.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import main.java.command.Command;
import main.java.logger.MainLogger;

public class CommandImpl implements Command
{
	@Override
	public boolean executeCommand(Command.Name command)
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