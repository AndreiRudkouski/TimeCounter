package timeCounter.init.config;

import java.io.File;

import timeCounter.command.ICommand;
import timeCounter.command.impl.Command;
import timeCounter.counter.ITimeCounter;
import timeCounter.counter.impl.TimeCounter;
import timeCounter.init.annotation.Config;
import timeCounter.init.annotation.Instance;
import timeCounter.load.ILoadSaveToFile;
import timeCounter.load.impl.LoadSaveToFile;
import timeCounter.timer.ISecondTimer;
import timeCounter.timer.impl.SecondTimer;
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
	public ICommand command()
	{
		return new Command();
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
	public ISecondTimer secondTimer()
	{
		return new SecondTimer();
	}
}
