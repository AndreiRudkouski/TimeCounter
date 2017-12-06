package timeCounter.init.impl;

import java.io.File;

import timeCounter.counter.ITimeCounter;
import timeCounter.counter.impl.TimeCounter;
import timeCounter.gui.IGUIWindow;
import timeCounter.gui.impl.GUIWindow;
import timeCounter.init.annotation.Config;
import timeCounter.init.annotation.Instance;
import timeCounter.listener.AbstractTimeListener;
import timeCounter.listener.impl.ApplicationListener;
import timeCounter.listener.impl.EraseApplicationListener;
import timeCounter.listener.impl.EraseCurrentTimeListener;
import timeCounter.listener.impl.EraseTodayTimeListener;
import timeCounter.listener.impl.EraseTotalTimeListener;
import timeCounter.listener.impl.LoadTimeListener;
import timeCounter.listener.impl.LocaleListener;
import timeCounter.listener.impl.SaveTimeListener;
import timeCounter.listener.impl.StartStopTimeListener;
import timeCounter.load.ILoadSaveToFile;
import timeCounter.load.impl.LoadSaveToFile;

/**
 * This class contains methods to get instances of all needed classes
 */
@Config
public class AppConfig
{
	@Instance
	public ITimeCounter timeCounter()
	{
		return new TimeCounter();
	}

	@Instance
	public IGUIWindow guiWindow()
	{
		return new GUIWindow();
	}

	@Instance(name = "applicationListener")
	public AbstractTimeListener applicationListener()
	{
		return new ApplicationListener();
	}

	@Instance(name = "eraseApplicationListener")
	public AbstractTimeListener eraseApplicationListener()
	{
		return new EraseApplicationListener();
	}

	@Instance(name = "eraseCurrentTimeListener")
	public AbstractTimeListener eraseCurrentTimeListener()
	{
		return new EraseCurrentTimeListener();
	}

	@Instance(name = "eraseTodayTimeListener")
	public AbstractTimeListener eraseTodayTimeListener()
	{
		return new EraseTodayTimeListener();
	}

	@Instance(name = "eraseTotalTimeListener")
	public AbstractTimeListener eraseTotalTimeListener()
	{
		return new EraseTotalTimeListener();
	}

	@Instance(name = "loadTimeListener")
	public AbstractTimeListener loadTimeListener()
	{
		return new LoadTimeListener();
	}

	@Instance(name = "localeListener")
	public AbstractTimeListener localeListener()
	{
		return new LocaleListener();
	}

	@Instance(name = "saveTimeListener")
	public AbstractTimeListener saveTimeListener()
	{
		return new SaveTimeListener();
	}

	@Instance(name = "startStopTimeListener")
	public AbstractTimeListener startStopTimeListener()
	{
		return new StartStopTimeListener();
	}

	@Instance
	public ILoadSaveToFile loadSaveToFile()
	{
		return new LoadSaveToFile();
	}

	@Instance
	public File file()
	{
		return new File("set.txt");
	}
}
