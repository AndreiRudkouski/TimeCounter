package main.command;

import java.lang.reflect.Method;
import java.util.Arrays;

import main.counter.TimeCounter;
import main.init.Initializer;
import main.view.View;

public enum CommandName
{
	TIME_COUNTER_LOAD_DATA(TimeCounter.class, "loadDataAndInitTimer"),
	TIME_COUNTER_SAVE_DATA(TimeCounter.class, "saveData"),
	VIEW_CHOSEN_RELAX(View.class, "isChosenRelax"),
	VIEW_IS_USER_AGREE_TO_CONNECT_SELECTED_APP(View.class, "isUserAgreeToConnectSelectedApplication"),
	TIME_COUNTER_CLOSE_APP(TimeCounter.class, "closeApplication"),
	TIME_COUNTER_IS_CHANGED_TIME_OR_SETTINGS(TimeCounter.class, "isChangedTimeOrSettings");

	private Object executor;
	private Method method;
	private boolean isBooleanReturn;

	CommandName(Class clazz, String methodName)
	{
		executor = Initializer.getClassInstanceByName(clazz.getSimpleName());
		method = Arrays.stream(executor.getClass().getMethods()).filter(
				m -> m.getName().equalsIgnoreCase(methodName)).findFirst().get();
		isBooleanReturn = method.getReturnType().getSimpleName().equalsIgnoreCase(Boolean.class.getSimpleName());
	}

	public Method getMethod()
	{
		return method;
	}

	public boolean isBooleanReturn()
	{
		return isBooleanReturn;
	}

	public Object getExecutor()
	{
		return executor;
	}
}