package main.java.initApp.config;

import java.io.File;

import main.java.command.Command;
import main.java.command.impl.CommandImpl;
import main.java.data.container.DataContainer;
import main.java.data.container.DataContainerHelper;
import main.java.data.container.impl.DataContainerHelperImpl;
import main.java.data.container.impl.DataContainerImpl;
import main.java.data.converter.DataConverter;
import main.java.data.converter.impl.DataConverterImpl;
import main.java.data.loadSave.LoadSaveToFile;
import main.java.data.loadSave.impl.LoadSaveToFileImpl;
import main.java.initApp.annotation.Config;
import main.java.initApp.annotation.Instance;
import main.java.timer.counter.TimeCounter;
import main.java.timer.counter.impl.TimeCounterImpl;
import main.java.timer.timer.Timer;
import main.java.timer.timer.impl.SecondsTimer;
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
	public TimeCounter getTimeCounter()
	{
		return new TimeCounterImpl();
	}

	@Instance
	public DataContainer getTimeAndSettingsContainer()
	{
		return new DataContainerImpl();
	}

	@Instance
	public DataConverter getConverter()
	{
		return new DataConverterImpl();
	}

	@Instance
	public DataContainerHelper getContainerHelper()
	{
		return new DataContainerHelperImpl();
	}

	@Instance
	public ViewManager getViewManager()
	{
		return new ViewManagerImpl();
	}

	@Instance
	public GuiView getGuiWindow()
	{
		return new GuiViewBySwing();
	}

	@Instance
	public Command getCommand()
	{
		return new CommandImpl();
	}

	@Instance
	public LoadSaveToFile getLoadSaveToFile()
	{
		return new LoadSaveToFileImpl();
	}

	@Instance
	public File getFile()
	{
		return new File("set.txt");
	}

	@Instance
	public Timer getSecondTimer()
	{
		return new SecondsTimer();
	}
}