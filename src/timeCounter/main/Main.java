package timeCounter.main;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import timeCounter.init.impl.Initializer;

public class Main
{
	public static void main(String[] args)
	{
		try
		{
			// Configure the logger with handler and formatter
			Logger logger = Logger.getLogger("MainLogger");
			Handler handler = new FileHandler("log.txt", false);
			logger.addHandler(handler);
			SimpleFormatter formatter = new SimpleFormatter();
			handler.setFormatter(formatter);
			logger.setUseParentHandlers(false);
		}
		catch (IOException e)
		{
			/*NOP*/
		}

		// Set config file and create initializer
		new Initializer("AppConfig");
	}
}