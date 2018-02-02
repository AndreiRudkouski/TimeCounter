package main.java.command;

import java.lang.reflect.Method;
import java.util.Arrays;

import main.java.initApp.Initializer;
import main.java.timer.counter.TimeCounter;
import main.java.viewer.manager.ViewManager;

/**
 * This interface identifies methods for commands execution
 */
public interface Command
{
	/**
	 * Executes command with name witch contains in {@link Name}
	 *
	 * @param command name of command
	 * @return result of command execution
	 */
	boolean executeCommand(Name command);

	/**
	 * Enumeration of command names
	 */
	enum Name
	{
		TIME_COUNTER_LOAD_DATA(TimeCounter.class, "loadData"),
		TIME_COUNTER_SAVE_DATA(TimeCounter.class, "saveData"),
		VIEW_IS_CHOSEN_RELAX(ViewManager.class, "isChosenRelax"),
		VIEW_IS_USER_AGREE_TO_CONNECT_SELECTED_APP(ViewManager.class, "isUserAgreeToConnectSelectedApplication"),
		TIME_COUNTER_CLOSE_APP(TimeCounter.class, "closeApplication"),
		TIME_COUNTER_IS_CHANGED_TIME_OR_SETTINGS(TimeCounter.class, "isChangedTimeOrSettings");

		private final Object executor;
		private final Method method;
		private final boolean isBooleanReturn;

		Name(Class clazz, String methodName)
		{
			executor = Initializer.instance().getClassInstanceByName(clazz.getSimpleName());
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
}
