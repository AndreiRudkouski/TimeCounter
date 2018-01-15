package main.main;

import main.counter.TimeCounter;
import main.init.Initializer;
import main.view.View;

public class Main
{
	public static void main(String[] args)
	{
		TimeCounter timeCounter = (TimeCounter) Initializer.getClassInstanceByName(TimeCounter.class.getSimpleName());
		View view = (View) Initializer.getClassInstanceByName(View.class.getSimpleName());
		timeCounter.addTimeObserver(view);
		view.addTimeObserver(timeCounter);
		view.createView();
		timeCounter.loadDataAndInitTimer();
	}
}