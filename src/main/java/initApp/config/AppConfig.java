package main.java.initApp.config;

import java.io.File;

import main.java.command.Command;
import main.java.command.impl.CommandImpl;
import main.java.initApp.annotation.Config;
import main.java.initApp.annotation.Instance;
import main.java.loader.LoadSaveToFile;
import main.java.loader.impl.LoadSaveToFileImpl;
import main.java.timer.container.TimeAndSettingsContainer;
import main.java.timer.container.impl.TimeAndSettingsContainerImpl;
import main.java.timer.counter.TimeCounter;
import main.java.timer.counter.impl.TimeCounterImpl;
import main.java.timer.timer.Timer;
import main.java.timer.timer.impl.SecondTimer;
import main.java.viewer.gui.GuiView;
import main.java.viewer.gui.impl.GuiViewBySwing;
import main.java.viewer.manager.ViewManager;
import main.java.viewer.manager.impl.ViewManagerImpl;

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
	public ViewManager viewManager()
	{
		return new ViewManagerImpl();
	}

	@Instance
	public GuiView guiWindow()
	{
		return new GuiViewBySwing();
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