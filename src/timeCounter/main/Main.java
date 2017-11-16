package timeCounter.main;

import java.io.File;

import timeCounter.counter.ITimeCounter;
import timeCounter.counter.impl.TimeCounter;
import timeCounter.gui.IGUIWindow;
import timeCounter.gui.impl.GUIWindow;
import timeCounter.listener.impl.EraseCurrentTimeListener;
import timeCounter.listener.impl.EraseTodayTimeListener;
import timeCounter.listener.impl.EraseTotalTimeListener;
import timeCounter.listener.impl.LoadTimeListener;
import timeCounter.listener.impl.SaveTimeListener;
import timeCounter.listener.impl.StartStopTimeListener;
import timeCounter.load.ILoadSaveToFile;
import timeCounter.load.impl.LoadSaveToFile;

public class Main
{
	public static final ITimeCounter TIME_COUNTER = new TimeCounter();
	private static final String FILE_NAME = "set.txt";

	public static void main(String[] args)
	{
		IGUIWindow window = new GUIWindow();
		window.setListenersAndCreate(new StartStopTimeListener(), new LoadTimeListener(),
				new SaveTimeListener(), new EraseCurrentTimeListener(),
				new EraseTodayTimeListener(), new EraseTotalTimeListener());

		ILoadSaveToFile saver = new LoadSaveToFile();
		File FILE = new File(FILE_NAME);
		saver.setFile(FILE);

		TIME_COUNTER.setWindow(window);
		TIME_COUNTER.setLoadSaveToFile(saver);

		TIME_COUNTER.loadTime();
	}
}

