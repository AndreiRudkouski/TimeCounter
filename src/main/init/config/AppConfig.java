package main.init.config;

import java.io.File;

import main.command.Command;
import main.command.impl.CommandImpl;
import main.counter.TimeCounter;
import main.counter.impl.TimeCounterImpl;
import main.init.annotation.Config;
import main.init.annotation.Instance;
import main.load.LoadSaveToFile;
import main.load.impl.LoadSaveToFileImpl;
import main.timer.Timer;
import main.timer.impl.SecondTimer;
import main.view.View;
import main.view.impl.ViewImpl;

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
