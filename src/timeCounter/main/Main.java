package timeCounter.main;

import timeCounter.counter.ITimeCounter;
import timeCounter.init.IInitializer;
import timeCounter.init.impl.Initializer;
import timeCounter.view.IView;

public class Main
{
	public static void main(String[] args)
	{
		IInitializer initializer = new Initializer("timeCounter.init.config.AppConfig");

		ITimeCounter timeCounter = (ITimeCounter) initializer.getClassInstanceByName("ITimeCounter");
		IView view = (IView) initializer.getClassInstanceByName("IView");
		timeCounter.addTimeObserver(view);
		view.addTimeObserver(timeCounter);
		view.createView();
		timeCounter.loadData();
	}
}