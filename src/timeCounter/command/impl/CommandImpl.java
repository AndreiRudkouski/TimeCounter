package timeCounter.command.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

import timeCounter.command.CommandName;
import timeCounter.logger.MainLogger;

public class CommandImpl implements timeCounter.command.Command
{
	@Override
	public boolean executeCommand(String commandName)
	{
		return executeCommand(commandName, null, null);
	}

	@Override
	public boolean executeCommand(String commandName, Boolean param1, Boolean param2)
	{
		boolean result = false;
		try
		{
			CommandName currentType = CommandName.valueOf(commandName.toUpperCase(Locale.ENGLISH));
			Object executor = currentType.getExecutor();
			Method method = currentType.getMethod();
			if (currentType.isBooleanReturn())
			{
				result = (boolean) (currentType.withParameters() ? method.invoke(executor, param1, param2) :
						method.invoke(executor));
			}
			else
			{
				method.invoke(executor);
			}
		}
		catch (IllegalArgumentException e)
		{
			MainLogger.getLogger().severe("Error command name: " + commandName);
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
		}
		return result;
	}
}