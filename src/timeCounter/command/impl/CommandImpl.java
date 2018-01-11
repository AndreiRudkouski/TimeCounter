package timeCounter.command.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

import timeCounter.command.CommandName;
import timeCounter.init.Initializer;
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
			Object executor = Initializer.getClassInstanceByName(currentType.getExecutorClass().getSimpleName());
			if (!currentType.isBooleanReturn())
			{
				Method method = executor.getClass().getMethod(currentType.getMethodName());
				method.invoke(executor);
			}
			else
			{
				if (currentType.withParameters())
				{
					Method method = executor.getClass().getMethod(currentType.getMethodName(), Boolean.class,
							Boolean.class);
					result = (boolean) method.invoke(executor, param1, param2);
				}
				else
				{
					Method method = executor.getClass().getMethod(currentType.getMethodName());
					result = (boolean) method.invoke(executor);
				}
			}
		}
		catch (IllegalArgumentException e)
		{
			MainLogger.getLogger().severe("Error command name: " + commandName);
		}
		catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
		}
		return result;
	}
}
