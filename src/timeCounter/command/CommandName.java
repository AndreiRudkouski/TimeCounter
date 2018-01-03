package timeCounter.command;

public enum CommandName
{
	TIME_COUNTER_ERASE_CURRENT_DATE("timeCounter", "eraseCurrentTime", false, false),
	TIME_COUNTER_LOAD_DATA("timeCounter", "loadData", false, false),
	TIME_COUNTER_SAVE_DATA("timeCounter", "saveData", false, false),
	TIME_COUNTER_ERASE_TODAY_DATE("timeCounter", "eraseTodayTime", false, false),
	TIME_COUNTER_ERASE_TOTAL_DATE("timeCounter", "eraseTotalTime", false, false),
	VIEW_CHOSEN_RELAX("view", "isChosenRelax", true, false),
	VIEW_RUNNING_APPLICATION_NOTICE("view", "runningApplicationNotice", true, false),
	TIME_COUNTER_CLOSE("timeCounter", "closeTimeCounter", true, true);

	private String executorName;
	private String methodName;
	private boolean isBooleanReturn;
	private boolean withParameters;

	CommandName(String executorName, String methodName, boolean isBooleanReturn, boolean withParameters)
	{
		this.executorName = executorName;
		this.methodName = methodName;
		this.isBooleanReturn = isBooleanReturn;
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

	public String getExecutorName()
	{
		return executorName;
	}
}
