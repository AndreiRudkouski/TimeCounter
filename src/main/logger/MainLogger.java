package main.logger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MainLogger
{
	private static Logger logger;

	static
	{
		try
		{
			logger = Logger.getLogger("MainLogger");
			Handler handler = new FileHandler("log." + LocalDate.now() + ".txt", false);
			logger.addHandler(handler);
			SimpleFormatter formatter = new SimpleFormatter();
			handler.setFormatter(formatter);
			logger.setUseParentHandlers(false);
		}
		catch (IOException e)
		{
			Logger.getLogger("MainLogger").severe("Logger initialization error");
		}
	}

	private MainLogger()
	{
	}

	public static Logger getLogger()
	{
		return logger;
	}

	public static String getStackTrace(Exception e)
	{
		if (e != null)
		{
			return Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString)
					.reduce((s1, s2) -> s1 + "\n        " + s2).map(s -> e.toString() + "\n  Cause:" + s)
					.orElseGet(e::toString);
		}
		return "No exceptions were found";
	}
}