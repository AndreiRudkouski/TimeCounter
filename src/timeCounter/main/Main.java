package timeCounter.main;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import timeCounter.counter.ITimeCounter;
import timeCounter.counter.impl.TimeCounter;
import timeCounter.gui.IGUIWindow;
import timeCounter.gui.impl.GUIWindow;
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
	public static final ITimeCounter TIME_COUNTER = new TimeCounter();
	private static final String FILE_NAME = "set.txt";
	private static final String LOCALE_NAME = "timeCounter.resource.locale";

	public static void main(String[] args)
	{
		IGUIWindow window = new GUIWindow();
		window.setResourceBundle(ResourceBundle.getBundle(LOCALE_NAME, Locale.getDefault()));
		window.setListenersAndCreate(new StartStopTimeListener(), new LoadTimeListener(),
				new SaveTimeListener(), new EraseCurrentTimeListener(),
				new EraseTodayTimeListener(), new EraseTotalTimeListener(), new LocaleListener());

		ILoadSaveToFile saver = new LoadSaveToFile();
		File FILE = new File(FILE_NAME);
		saver.setFile(FILE);

		TIME_COUNTER.setWindow(window);
		TIME_COUNTER.setLoadSaveToFile(saver);

		TIME_COUNTER.loadTime();
	}
}

