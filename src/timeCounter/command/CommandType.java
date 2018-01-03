package timeCounter.command;

public enum CommandType
{
	ERASE_CURRENT_DATE("eraseCurrentTime", false, false),
	LOAD_DATA("loadData", false, false),
	SAVE_DATA("saveData", false, false),
	ERASE_TODAY_DATE("eraseTodayTime", false, false),
	ERASE_TOTAL_DATE("eraseTotalTime", false, false),
	CHOSEN_RELAX("isChosenRelax", true, false),
	RUNNING_APPLICATION_NOTICE("runningApplicationNotice", true, false),
	CLOSE("closeTimeCounter", true, true);

	private String methodName;
	private boolean isBooleanReturn;
	private boolean withParameters;

	CommandType(String methodName, boolean isBooleanReturn, boolean withParameters)
	{
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
}
