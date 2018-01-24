package main.java.timer.timer.impl;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import main.java.timer.timer.Timer;

/**
 * This class is implementation of {@link Timer} interface for executing the command every second.
 */
public class SecondsTimer implements Timer
{
	private Runnable executeCommand;
	private ScheduledExecutorService executor;

	@Override
	public void stop()
	{
		if (isRunning())
		{
			executor.shutdown();
		}
	}

	@Override
	public void start()
	{
		if (hasCommand() && !isRunning())
		{
			executor = new ScheduledThreadPoolExecutor(1);
			executor.scheduleAtFixedRate(executeCommand, 1, 1, TimeUnit.SECONDS);
		}
	}

	@Override
	public boolean isRunning()
	{
		return executor != null && !executor.isShutdown();
	}

	@Override
	public void setCommand(Runnable command)
	{
		this.executeCommand = command;
	}

	@Override
	public boolean hasCommand()
	{
		return executeCommand != null;
	}
}
