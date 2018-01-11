package timeCounter.command;

import java.lang.reflect.Method;
import java.util.Arrays;

import timeCounter.counter.TimeCounter;
import timeCounter.init.Initializer;
import timeCounter.view.View;

public enum CommandName
{
	TIME_COUNTER_LOAD_DATA(TimeCounter.class, "loadData"),
	TIME_COUNTER_SAVE_DATA(TimeCounter.class, "saveData"),
	VIEW_CHOSEN_RELAX(View.class, "isChosenRelax"),
	VIEW_RUNNING_APPLICATION_NOTICE(View.class, "runningApplicationNotice"),
	TIME_COUNTER_CLOSE(TimeCounter.class, "closeTimeCounter");

	private Object executor;
	private Method method;
	private boolean isBooleanReturn;
	private boolean withParameters;

	CommandName(Class clazz, String methodName)
	{
		executor = Initializer.getClassInstanceByName(clazz.getSimpleName());
		method = Arrays.stream(executor.getClass().getMethods()).filter(
				m -> m.getName().equalsIgnoreCase(methodName)).findFirst().get();
		isBooleanReturn = method.getReturnType().getSimpleName().equalsIgnoreCase(Boolean.class.getSimpleName());
		withParameters = method.getParameters().length > 0;
	}

	public Method getMethod()
	{
		return method;
	}

	public boolean isBooleanReturn()
	{
		return isBooleanReturn;
	}

	public boolean withParameters()
	{
		return withParameters;
	}

	public Object getExecutor()
	{
		return executor;
	}
}