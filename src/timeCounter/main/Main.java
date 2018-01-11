package timeCounter.main;

import timeCounter.counter.TimeCounter;
import timeCounter.init.Initializer;
import timeCounter.view.View;

public class Main
{
	public static void main(String[] args)
	{
		Initializer initializer = new Initializer("timeCounter.init.config.AppConfig");

		TimeCounter timeCounter = (TimeCounter) Initializer.getClassInstanceByName(TimeCounter.class.getSimpleName());
		View view = (View) Initializer.getClassInstanceByName(View.class.getSimpleName());
		timeCounter.addTimeObserver(view);
		view.addTimeObserver(timeCounter);
		view.createView();
		timeCounter.loadData();
	}
}