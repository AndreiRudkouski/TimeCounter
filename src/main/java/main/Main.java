package main.java.main;

import main.java.initApp.Initializer;
import main.java.timer.counter.TimeCounter;
import main.java.viewer.manager.ViewManager;

public class Main
{
	public static void main(String[] args)
	{
		TimeCounter timeCounter = (TimeCounter) Initializer.getClassInstanceByName(TimeCounter.class.getSimpleName());
		ViewManager view = (ViewManager) Initializer.getClassInstanceByName(ViewManager.class.getSimpleName());
		timeCounter.addTimeObserver(view);
		view.addTimeObserver(timeCounter);
		view.createView();
		timeCounter.loadDataAndInitTimer();
	}
}