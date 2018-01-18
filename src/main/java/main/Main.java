package main.java.main;

import main.java.counter.TimeCounter;
import main.java.initApp.Initializer;
import main.java.viewer.View;

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