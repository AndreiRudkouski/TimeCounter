package timeCounter.init.config;

import java.io.File;

import timeCounter.command.Command;
import timeCounter.command.impl.CommandImpl;
import timeCounter.counter.TimeCounter;
import timeCounter.counter.impl.TimeCounterImpl;
import timeCounter.init.annotation.Config;
import timeCounter.init.annotation.Instance;
import timeCounter.load.LoadSaveToFile;
import timeCounter.load.impl.LoadSaveToFileImpl;
import timeCounter.timer.Timer;
import timeCounter.timer.impl.SecondTimer;
import timeCounter.view.View;
import timeCounter.view.impl.ViewImpl;

/**
 * This class contains methods to get instances of all needed classes
 */
@Config
public class AppConfig
{
	@Instance
	public TimeCounter timeCounter()
	{
		return new TimeCounterImpl();
	}

	@Instance
	public View guiWindow()
	{
		return new ViewImpl();
	}

	@Instance
	public Command command()
	{
		return new CommandImpl();
	}

	@Instance
	public LoadSaveToFile loadSaveToFile()
	{
		return new LoadSaveToFileImpl();
	}

	@Instance
	public File file()
	{
		return new File("set.txt");
	}

	@Instance
	public Timer secondTimer()
	{
		return new SecondTimer();
	}
}
