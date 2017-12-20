package timeCounter.main;

import timeCounter.init.impl.Initializer;

public class Main
{
	public static void main(String[] args)
	{
		// Set config file and create initializer
		new Initializer("timeCounter.init.config.AppConfig");
	}
}