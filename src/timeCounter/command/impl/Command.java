package timeCounter.command.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

import timeCounter.command.CommandType;
import timeCounter.command.ICommand;
import timeCounter.counter.ITimeCounter;
import timeCounter.init.annotation.Setter;
import timeCounter.logger.MainLogger;
import timeCounter.view.IView;

public class Command implements ICommand
{
	@Setter
	private ITimeCounter timeCounter;
	@Setter
	private IView view;

	@Override
	public void executeCommand(String commandName)
	{
		executeBooleanCommandWithParameters(commandName, null, null);
	}

	@Override
	public boolean executeBooleanCommand(String commandName)
	{
		return executeBooleanCommandWithParameters(commandName, null, null);
	}

	@Override
	public boolean executeBooleanCommandWithParameters(String commandName, Boolean saveData, Boolean closeApp)
	{
		boolean result = false;
		try
		{
			CommandType currentType = CommandType.valueOf(commandName.toUpperCase(Locale.ENGLISH));
			if (!currentType.isBooleanReturn())
			{
				Method method = timeCounter.getClass().getMethod(currentType.getMethodName());
				method.invoke(timeCounter);
			}
			else
			{
				if (currentType.withParameters())
				{
					Method method = timeCounter.getClass().getMethod(currentType.getMethodName(), Boolean.class,
							Boolean.class);
					result = (boolean) method.invoke(timeCounter, saveData, closeApp);
				}
				else
				{
					Method method = view.getClass().getMethod(currentType.getMethodName());
					result = (boolean) method.invoke(view);
				}
			}
		}
		catch (IllegalArgumentException e)
		{
			MainLogger.getLogger().severe("Error command name");
		}
		catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
		{
			MainLogger.getLogger().severe(e.toString());
		}
		return result;
	}
}
