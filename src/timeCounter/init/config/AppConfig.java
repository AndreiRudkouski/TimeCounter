package timeCounter.init.config;

import java.io.File;

import timeCounter.controller.IController;
import timeCounter.controller.impl.Controller;
import timeCounter.counter.ITimeCounter;
import timeCounter.counter.impl.TimeCounter;
import timeCounter.init.annotation.Config;
import timeCounter.init.annotation.Instance;
import timeCounter.load.ILoadSaveToFile;
import timeCounter.load.impl.LoadSaveToFile;
import timeCounter.view.IView;
import timeCounter.view.impl.View;

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
	public IView guiWindow()
	{
		return new View();
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

	@Instance
	public IController controller()
	{
		return new Controller();
	}
}