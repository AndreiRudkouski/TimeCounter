package timeCounter.command;

import timeCounter.counter.TimeCounter;
import timeCounter.view.View;

public enum CommandName
{
	TIME_COUNTER_LOAD_DATA(TimeCounter.class, "loadData", false, false),
	TIME_COUNTER_SAVE_DATA(TimeCounter.class, "saveData", false, false),
	VIEW_CHOSEN_RELAX(View.class, "isChosenRelax", true, false),
	VIEW_RUNNING_APPLICATION_NOTICE(View.class, "runningApplicationNotice", true, false),
	TIME_COUNTER_CLOSE(TimeCounter.class, "closeTimeCounter", true, true);

	private Class executorClass;
	private String methodName;
	private boolean isBooleanReturn;
	private boolean withParameters;

	CommandName(Class executorClass, String methodName, boolean canBooleanReturn, boolean withParameters)
	{
		this.executorClass = executorClass;
		this.methodName = methodName;
		this.isBooleanReturn = canBooleanReturn;
		this.withParameters = withParameters;
	}

	public String getMethodName()
	{
		return methodName;
	}

	public boolean isBooleanReturn()
	{
		return isBooleanReturn;
	}

	public boolean withParameters()
	{
		return withParameters;
	}

	public Class getExecutorClass()
	{
		return executorClass;
	}
}
