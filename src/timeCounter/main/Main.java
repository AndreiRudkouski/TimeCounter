package timeCounter.main;

import java.io.File;

import timeCounter.counter.ITimeCounter;
import timeCounter.counter.impl.TimeCounter;
import timeCounter.gui.IGUIWindow;
import timeCounter.gui.impl.GUIWindow;
import timeCounter.listener.impl.ApplicationListener;
import timeCounter.listener.impl.EraseCurrentTimeListener;
import timeCounter.listener.impl.EraseTodayTimeListener;
import timeCounter.listener.impl.EraseTotalTimeListener;
import timeCounter.listener.impl.LoadTimeListener;
import timeCounter.listener.impl.LocaleListener;
import timeCounter.listener.impl.SaveTimeListener;
import timeCounter.listener.impl.StartStopTimeListener;
import timeCounter.load.ILoadSaveToFile;
import timeCounter.load.impl.LoadSaveToFile;

public class Main
{
	private static final IGUIWindow WINDOW = new GUIWindow();
	private static final ILoadSaveToFile SAVER = new LoadSaveToFile();
	private static final String FILE_NAME = "set.txt";

	public static final ITimeCounter TIME_COUNTER = new TimeCounter(WINDOW, SAVER);

	public static void main(String[] args)
	{
		WINDOW.setListenersAndCreate(new StartStopTimeListener(), new LoadTimeListener(),
				new SaveTimeListener(), new EraseCurrentTimeListener(),
				new EraseTodayTimeListener(), new EraseTotalTimeListener(), new ApplicationListener(),
				new LocaleListener());
		File file = new File(FILE_NAME);
		SAVER.setFile(file);

		TIME_COUNTER.loadData();
	}
}