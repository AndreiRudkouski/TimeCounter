package main.java.init.config;

import java.io.File;

import main.java.command.Command;
import main.java.command.impl.CommandImpl;
import main.java.counter.TimeCounter;
import main.java.counter.bean.TimeAndSettingsContainer;
import main.java.counter.impl.TimeCounterImpl;
import main.java.init.annotation.Config;
import main.java.init.annotation.Instance;
import main.java.load.LoadSaveToFile;
import main.java.load.impl.LoadSaveToFileImpl;
import main.java.timer.Timer;
import main.java.timer.impl.SecondTimer;
import main.java.view.View;
import main.java.view.impl.ViewImpl;

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
	public TimeAndSettingsContainer timeAndSettingsContainer()
	{
		return new TimeAndSettingsContainer();
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
