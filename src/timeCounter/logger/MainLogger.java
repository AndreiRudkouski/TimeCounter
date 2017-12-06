package timeCounter.logger;

import java.io.IOException;
import java.time.LocalDate;
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
			// Configure the logger with handler and formatter
			logger = Logger.getLogger("MainLogger");
			Handler handler = new FileHandler("log." + LocalDate.now() + ".txt", false);
			logger.addHandler(handler);
			SimpleFormatter formatter = new SimpleFormatter();
			handler.setFormatter(formatter);
			logger.setUseParentHandlers(false);
		}
		catch (IOException e)
		{
			/*NOP*/
		}
	}

	private MainLogger()
	{
	}

	public static Logger getLogger()
	{
		return logger;
	}
}