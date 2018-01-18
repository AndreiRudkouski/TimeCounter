package main.java.initApp.config;

import java.io.File;

import main.java.command.Command;
import main.java.command.impl.CommandImpl;
import main.java.counter.TimeCounter;
import main.java.counter.container.TimeAndSettingsContainer;
import main.java.counter.container.impl.TimeAndSettingsContainerImpl;
import main.java.counter.impl.TimeCounterImpl;
import main.java.counter.timer.Timer;
import main.java.counter.timer.impl.SecondTimer;
import main.java.initApp.annotation.Config;
import main.java.initApp.annotation.Instance;
import main.java.loader.LoadSaveToFile;
import main.java.loader.impl.LoadSaveToFileImpl;
import main.java.viewer.View;
import main.java.viewer.impl.ViewImpl;

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
		return new TimeAndSettingsContainerImpl();
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
