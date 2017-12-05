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

	@Instance
	public AbstractTimeListener applicationListener()
	{
		return new ApplicationListener();
	}

	@Instance
	public AbstractTimeListener eraseApplicationListener()
	{
		return new EraseApplicationListener();
	}

	@Instance
	public AbstractTimeListener eraseCurrentTimeListener()
	{
		return new EraseCurrentTimeListener();
	}

	@Instance
	public AbstractTimeListener eraseTodayTimeListener()
	{
		return new EraseTodayTimeListener();
	}

	@Instance
	public AbstractTimeListener eraseTotalTimeListener()
	{
		return new EraseTotalTimeListener();
	}

	@Instance
	public AbstractTimeListener loadTimeListener()
	{
		return new LoadTimeListener();
	}

	@Instance
	public AbstractTimeListener localeListener()
	{
		return new LocaleListener();
	}

	@Instance
	public AbstractTimeListener saveTimeListener()
	{
		return new SaveTimeListener();
	}

	@Instance
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
